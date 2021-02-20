package com.aditya.project.blog.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.proto.blog.Blog;
import com.proto.blog.BlogServiceGrpc;
import com.proto.blog.CreateBlogRequest;
import com.proto.blog.CreateBlogResponse;
import com.proto.blog.ReadBlogRequest;
import com.proto.blog.ReadBlogResponse;
import com.proto.blog.UpdateBlogRequest;
import com.proto.blog.UpdateBlogResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class BlogServiceImpl extends BlogServiceGrpc.BlogServiceImplBase {

    private final MongoClient mongoClient = MongoClients.create("mongodb+srv://root:1234@cluster0-2kdyk.mongodb.net");
    private final MongoDatabase database = mongoClient.getDatabase("mydb");
    private final MongoCollection<Document> collection = database.getCollection("blogs");

    @Override
    public void createBlog(CreateBlogRequest request, StreamObserver<CreateBlogResponse> responseObserver) {
        System.out.println("Received Create Blog Request.");
        Blog blog = request.getBlog();
        Document document = new Document("author_id", blog.getAuthorId())
                .append("title", blog.getTitle())
                .append("content", blog.getContent());
        System.out.println("Inserting Blog...");
        collection.insertOne(document);
        String id = document.getObjectId("_id").toString();
        System.out.println("Inserted Blog : " + id);
        CreateBlogResponse response = CreateBlogResponse.newBuilder()
                .setBlog(blog.toBuilder()
                        .setId(id)
                        .build())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void readBlog(ReadBlogRequest request, StreamObserver<ReadBlogResponse> responseObserver) {
        System.out.println("Received Read Blog Request");
        String id = request.getId();
        System.out.println("Searching for blog by id...");
        Document result = null;
        try {
            result = collection.find(eq("_id", new ObjectId(id)))
                    .first();
        } catch (Exception e) {
            System.out.println("Exception occurred while finding blog!");
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("The blog with corresponding id not found! " + id)
                    .augmentDescription(e.getLocalizedMessage())
                    .asRuntimeException());
        }
        if (result == null) {
            System.out.println("Blog Not Found!");
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("The blog with corresponding id not found! " + id)
                    .asRuntimeException());
        } else {
            System.out.println("Blog Found. Sending Response...");
            Blog blog = documentToBlog(result);
            ReadBlogResponse response = ReadBlogResponse.newBuilder()
                    .setBlog(blog)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void updateBlog(UpdateBlogRequest request, StreamObserver<UpdateBlogResponse> responseObserver) {
        System.out.println("Received Update Blog Request");
        Blog blog = request.getBlog();
        String id = blog.getId();
        System.out.println("Searching for blog so that we can update it...");
        Document result = null;
        try {
            result = collection.find(eq("_id", new ObjectId(id)))
                    .first();
        } catch (Exception e) {
            System.out.println("Exception occurred while finding blog!");
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("The blog with corresponding id not found! " + id)
                    .augmentDescription(e.getLocalizedMessage())
                    .asRuntimeException());
        }
        if (result == null) {
            System.out.println("Blog Not Found!");
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("The blog with corresponding id not found! " + id)
                    .asRuntimeException());
        } else {
            System.out.println("Replacing Blog in Database...");
            Document replacement = new Document("author_id", blog.getAuthorId())
                    .append("title", blog.getTitle())
                    .append("content", blog.getContent())
                    .append("_id", new ObjectId(id));
            collection.replaceOne(eq("_id", result.getObjectId("_id")), replacement);
            blog = documentToBlog(replacement);
            UpdateBlogResponse response = UpdateBlogResponse.newBuilder()
                    .setBlog(blog)
                    .build();
            System.out.println("Received! Sending Response...");
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    private Blog documentToBlog(Document document) {
        return Blog.newBuilder()
                .setId(document.getObjectId("_id").toString())
                .setAuthorId(document.getString("author_id"))
                .setTitle(document.getString("title"))
                .setContent(document.getString("content"))
                .build();
    }
}

package com.aditya.project.blog.client;

import com.proto.blog.Blog;
import com.proto.blog.BlogServiceGrpc;
import com.proto.blog.CreateBlogRequest;
import com.proto.blog.CreateBlogResponse;
import com.proto.blog.DeleteBlogRequest;
import com.proto.blog.DeleteBlogResponse;
import com.proto.blog.ListBlogRequest;
import com.proto.blog.ReadBlogRequest;
import com.proto.blog.ReadBlogResponse;
import com.proto.blog.UpdateBlogRequest;
import com.proto.blog.UpdateBlogResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BlogClient {

    public static void main(String[] args) {
        System.out.println("Hello I am gRPC Client for Blogs!");
        BlogClient main = new BlogClient();
        main.run();
    }

    private void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        // Created sync Client for Blogs
        BlogServiceGrpc.BlogServiceBlockingStub blogClient = BlogServiceGrpc.newBlockingStub(channel);

        // Creating a Blog
        System.out.println("Creating blog...");
        Blog blog = Blog.newBuilder()
                .setAuthorId("Aditya")
                .setTitle("First Blog")
                .setContent("This is my new blog!")
                .build();
        CreateBlogRequest createBlogRequest = CreateBlogRequest.newBuilder()
                .setBlog(blog)
                .build();
        CreateBlogResponse createBlogResponse = blogClient.createBlog(createBlogRequest);
        System.out.println("Received Create Blog Response.");
        System.out.println(createBlogResponse.toString());

        // Reading a Blog
        System.out.println("Reading blog...");
        ReadBlogRequest readBlogRequest = ReadBlogRequest.newBuilder()
                .setId(createBlogResponse.getBlog().getId())
                .build();
        ReadBlogResponse readBlogResponse = blogClient.readBlog(readBlogRequest);
        System.out.println("Received Read Blog Response.");
        System.out.println(readBlogResponse.toString());

        // Updating a Blog
        System.out.println("Updating blog...");
        Blog newBlog = Blog.newBuilder()
                .setId(createBlogResponse.getBlog().getId())
                .setAuthorId("Aditya Kshettri")
                .setTitle("First Blog updated")
                .setContent("This is my new blog! updated")
                .build();
        UpdateBlogRequest updateBlogRequest = UpdateBlogRequest.newBuilder()
                .setBlog(newBlog)
                .build();
        UpdateBlogResponse updateBlogResponse = blogClient.updateBlog(updateBlogRequest);
        System.out.println("Updated Blog.");
        System.out.println(updateBlogResponse.toString());

        // Deleting a blog
        System.out.println("Deleting blog...");
        DeleteBlogRequest deleteBlogRequest = DeleteBlogRequest.newBuilder()
                .setId(createBlogResponse.getBlog().getId())
                .build();
        DeleteBlogResponse deleteBlogResponse = blogClient.deleteBlog(deleteBlogRequest);
        System.out.println("Deleted blog...");
        System.out.println(deleteBlogResponse.getId());

        // Listing all Blogs
        System.out.println("Listing all blogs...");
        ListBlogRequest listBlogRequest = ListBlogRequest.newBuilder()
                .build();
        blogClient.listBlog(listBlogRequest)
                .forEachRemaining(response -> System.out.println(response.getBlog().toString()));

        channel.shutdown();
    }
}

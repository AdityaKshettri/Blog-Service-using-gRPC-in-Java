package com.aditya.project.blog.client;

import com.proto.blog.Blog;
import com.proto.blog.BlogServiceGrpc;
import com.proto.blog.CreateBlogRequest;
import com.proto.blog.CreateBlogResponse;
import com.proto.blog.ReadBlogRequest;
import com.proto.blog.ReadBlogResponse;
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

        BlogServiceGrpc.BlogServiceBlockingStub blogClient = BlogServiceGrpc.newBlockingStub(channel);
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

        System.out.println("Reading blog...");
        ReadBlogRequest readBlogRequest = ReadBlogRequest.newBuilder()
                .setId(createBlogResponse.getBlog().getId())
                .build();
        ReadBlogResponse readBlogResponse = blogClient.readBlog(readBlogRequest);
        System.out.println("Received Read Blog Response.");
        System.out.println(readBlogResponse.toString());

        channel.shutdown();
    }
}

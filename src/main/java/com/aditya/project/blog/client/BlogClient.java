package com.aditya.project.blog.client;

import com.proto.blog.Blog;
import com.proto.blog.BlogServiceGrpc;
import com.proto.blog.CreateBlogRequest;
import com.proto.blog.CreateBlogResponse;
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
        createBlogCall(channel);
        channel.shutdown();
    }

    private void createBlogCall(ManagedChannel channel) {
        BlogServiceGrpc.BlogServiceBlockingStub blogClient = BlogServiceGrpc.newBlockingStub(channel);
        Blog blog = Blog.newBuilder()
                .setAuthorId("Aditya")
                .setTitle("First Blog")
                .setContent("This is my new blog!")
                .build();
        CreateBlogRequest request = CreateBlogRequest.newBuilder()
                .setBlog(blog)
                .build();
        CreateBlogResponse response = blogClient.createBlog(request);
        System.out.println("Received Create Blog Response.");
        System.out.println(response.toString());
    }
}

package com.alibaihaqi.simplesteph.grpc.blog.client;

import com.proto.blog.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BlogClient {

    public static void main(String[] args) {
        System.out.println("Hello, I'm a gRPC Blog Client");

        BlogClient main = new BlogClient();
        main.run();
    }

    private void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        System.out.println("Creating stub!");

//        doCreateBlogCall(channel);
//        doReadBlogCall(channel);
//        doUpdateBlogCall(channel);
        doDeleteBlogCall(channel);

        // do something
        System.out.println("Shutting down channel!");
        channel.shutdown();
    }

    private void doCreateBlogCall (ManagedChannel channel) {
        BlogServiceGrpc.BlogServiceBlockingStub blogClient = BlogServiceGrpc.newBlockingStub(channel);

        Blog blog = Blog.newBuilder()
                .setAuthorId("Jacky 2")
                .setTitle("New Blog 2!")
                .setContent("Hello world this is my second blog!")
                .build();

        CreateBlogResponse createResponse = blogClient.createBlog(
                CreateBlogRequest.newBuilder()
                        .setBlog(blog)
                        .build()
        );


        System.out.println("Received create blog response");
        System.out.println(createResponse.toString());
    }

    private void doReadBlogCall (ManagedChannel channel) {
        BlogServiceGrpc.BlogServiceBlockingStub blogClient = BlogServiceGrpc.newBlockingStub(channel);

        ReadBlogResponse createResponse = blogClient.readBlog(
                ReadBlogRequest.newBuilder()
                        .setBlogId("fake-id")
                        .build()
        );

        System.out.println("Received read blog response");
        System.out.println(createResponse.toString());
    }

    private void doUpdateBlogCall (ManagedChannel channel) {
        BlogServiceGrpc.BlogServiceBlockingStub blogClient = BlogServiceGrpc.newBlockingStub(channel);

        Blog blog = Blog.newBuilder()
                .setId("fake-id")
                .setAuthorId("Jacky 5")
                .setTitle("Update Blog 5!")
                .setContent("Hello world this is my fifth update blog!")
                .build();

        UpdateBlogResponse createResponse = blogClient.updateBlog(
                UpdateBlogRequest.newBuilder()
                        .setBlog(blog)
                        .build()
        );

        System.out.println("Received update blog response");
        System.out.println(createResponse.toString());
    }

    private void doDeleteBlogCall (ManagedChannel channel) {
        BlogServiceGrpc.BlogServiceBlockingStub blogClient = BlogServiceGrpc.newBlockingStub(channel);

        DeleteBlogResponse createResponse = blogClient.deleteBlog(
                DeleteBlogRequest.newBuilder()
                        .setBlogId("5f626f254ecd380f9799b5fa")
                        .build()
        );

        System.out.println("Received delete blog response");
        System.out.println("Success delete with id" + createResponse.getBlogId());
    }
}

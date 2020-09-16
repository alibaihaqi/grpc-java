package com.alibaihaqi.simplesteph.grpc.blog.client;

import com.alibaihaqi.simplesteph.grpc.calculator.client.CalculatorClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BlogClient {

    public static void main(String[] args) {
        System.out.println("Hello, I'm a gRPC Blog Client");

        BlogClient main = new BlogClient();
        main.run();
    }

    private void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()
                .build();

        System.out.println("Creating stub!");

//        doUnaryCall(channel);

        // do something
        System.out.println("Shutting down channel!");
        channel.shutdown();
    }
}

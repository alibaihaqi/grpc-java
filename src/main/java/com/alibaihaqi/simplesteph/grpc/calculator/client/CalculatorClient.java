package com.alibaihaqi.simplesteph.grpc.calculator.client;

import com.proto.calculator.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CalculatorClient {
    public static void main(String[] args) {
        System.out.println("Hello, I'm a gRPC Client");

        CalculatorClient main = new CalculatorClient();
        main.run();
    }

    private void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()
                .build();

        System.out.println("Creating stub!");

//        doUnaryCall(channel);
//        doServerStreamingCall(channel);
        doClientStreamingCall(channel);

        // do something
        System.out.println("Shutting down channel!");
        channel.shutdown();
    }

    private void doUnaryCall (ManagedChannel channel) {
        // created a calculator service client (blocking - synchronous)
        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient = CalculatorServiceGrpc.newBlockingStub(channel);

        // Unary
        // do the same for a CalculatorRequest
        SumRequest request = SumRequest.newBuilder()
                .setFirstNumber(3)
                .setSecondNumber(10)
                .build();

        // Call the RPC and get back a CalculatorResponse (protocol buffers)
        SumResponse response = calculatorClient.sum(request);

        System.out.println("response calculator: " +  response);
    }

    private void doServerStreamingCall (ManagedChannel channel) {
        // created a calculator service client (blocking - synchronous)
        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient = CalculatorServiceGrpc.newBlockingStub(channel);

        // Server Streaming
        PrimeNumberDecompositionRequest request = PrimeNumberDecompositionRequest.newBuilder()
                .setNumber(120)
                .build();

        // Call the RPC and get back a CalculatorResponse (protocol buffers)
        calculatorClient.primeNumberDecomposition(request)
                .forEachRemaining(primeNumberDecompositionResponse -> {
                    System.out.println("prime number decomposition value: " + primeNumberDecompositionResponse.getResultNumber());
                });
    }

    private void doClientStreamingCall (ManagedChannel channel) {
        // create an asynchronous client
        CalculatorServiceGrpc.CalculatorServiceStub asyncClient = CalculatorServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<ComputeAverageRequest> requestObserver = asyncClient.computeAverage(new StreamObserver<ComputeAverageResponse>() {
            @Override
            public void onNext(ComputeAverageResponse value) {
                // we get a response from the server
                System.out.println("Received a response from the server");
                System.out.println(value.getAverageNumber());
                // onNext will be called only once
            }

            @Override
            public void onError(Throwable t) {
                // we get an error from the server
            }

            @Override
            public void onCompleted() {
                // the server is done sending us data
                System.out.println("Server has completed sending us something");
                latch.countDown();
                // onCompleted will be called right after onNext()
            }
        });

        // streaming message for 10000 numbers
        System.out.println("Sending Message #1");
        for (int i = 0; i < 10000; i++) {
            requestObserver.onNext(ComputeAverageRequest.newBuilder()
                .setNumber(i)
                .build());
        }
//        System.out.println("Sending Message #1");
//        requestObserver.onNext(ComputeAverageRequest.newBuilder()
//                .setNumber(1)
//                .build());
//
//        // streaming message #2
//        System.out.println("Sending Message #2");
//        requestObserver.onNext(ComputeAverageRequest.newBuilder()
//                .setNumber(2)
//                .build());
//
//        // streaming message #3
//        System.out.println("Sending Message #3");
//        requestObserver.onNext(ComputeAverageRequest.newBuilder()
//                .setNumber(3)
//                .build());
//
//        // streaming message #4
//        System.out.println("Sending Message #4");
//        requestObserver.onNext(ComputeAverageRequest.newBuilder()
//                .setNumber(4)
//                .build());

        requestObserver.onCompleted();

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package com.alibaihaqi.simplesteph.grpc.calculator.client;

import com.proto.calculator.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CalculatorClient {

    private void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()
                .build();

        System.out.println("Creating stub!");

        doUnaryCall(channel);
        doServerStreamingCall(channel);

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

    public static void main(String[] args) {
        System.out.println("Hello, I'm a gRPC Client");

        CalculatorClient main = new CalculatorClient();
        main.run();
    }
}

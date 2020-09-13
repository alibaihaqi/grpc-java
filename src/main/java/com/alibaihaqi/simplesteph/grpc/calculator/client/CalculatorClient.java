package com.alibaihaqi.simplesteph.grpc.calculator.client;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.SumRequest;
import com.proto.calculator.SumResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CalculatorClient {

    public static void main(String[] args) {

        System.out.println("Hello, I'm a gRPC Client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()
                .build();

        System.out.println("Creating stub!");

        // created a calculator service client (blocking - synchronous)
        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient = CalculatorServiceGrpc.newBlockingStub(channel);


        // do the same for a CalculatorRequest
        SumRequest request = SumRequest.newBuilder()
                .setFirstNumber(3)
                .setSecondNumber(10)
                .build();

        // Call the RPC and get back a CalculatorResponse (protocol buffers)
        SumResponse response = calculatorClient.sum(request);

        System.out.println("response calculator: " +  response);

        // do something
        System.out.println("Shutting down channel!");
        channel.shutdown();
    }
}

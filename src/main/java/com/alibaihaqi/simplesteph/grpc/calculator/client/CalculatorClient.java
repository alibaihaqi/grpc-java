package com.alibaihaqi.simplesteph.grpc.calculator.client;

import com.proto.calculator.Calculator;
import com.proto.calculator.CalculatorRequest;
import com.proto.calculator.CalculatorResponse;
import com.proto.calculator.CalculatorServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CalculatorClient {

    public static void main(String[] args) {

        System.out.println("Hello, I'm a gRPC Client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        System.out.println("Creating stub!");

        // created a calculator service client (blocking - synchronous)
        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient = CalculatorServiceGrpc.newBlockingStub(channel);

        // created a protocol buffer calculator message
        Calculator calculator = Calculator.newBuilder()
                .setFirstNumber(3)
                .setSecondNumber(10)
                .build();

        // do the same for a CalculatorRequest
        CalculatorRequest request = CalculatorRequest.newBuilder()
                .setCalculator(calculator)
                .build();

        // Call the RPC and get back a CalculatorResponse (protocol buffers)
        CalculatorResponse response = calculatorClient.calculator(request);

        System.out.println("response calculator: " +  response);

        // do something
        System.out.println("Shutting down channel!");
        channel.shutdown();
    }
}

package com.alibaihaqi.simplesteph.grpc.calculator.server;

import com.proto.calculator.Calculator;
import com.proto.calculator.CalculatorRequest;
import com.proto.calculator.CalculatorResponse;
import com.proto.calculator.CalculatorServiceGrpc;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public void calculator(CalculatorRequest request, StreamObserver<CalculatorResponse> responseObserver) {
        System.out.println("Call Calculator Service");

        // extract the fields we need
        Calculator calculator = request.getCalculator();
        Integer first_number = calculator.getFirstNumber();
        Integer second_number = calculator.getSecondNumber();

        // Create the response
        Integer result_number = first_number + second_number;
        CalculatorResponse response = CalculatorResponse.newBuilder()
                .setResult(result_number)
                .build();

        // Send the response
        responseObserver.onNext(response);

        // Completed response
        responseObserver.onCompleted();
    }
}

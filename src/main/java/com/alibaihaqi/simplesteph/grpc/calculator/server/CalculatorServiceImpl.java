package com.alibaihaqi.simplesteph.grpc.calculator.server;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.SumRequest;
import com.proto.calculator.SumResponse;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {
        System.out.println("Call Calculator Service");

        // extract the fields we need
        Integer first_number = request.getFirstNumber();
        Integer second_number = request.getSecondNumber();

        // Create the response
        Integer result_number = first_number + second_number;
        SumResponse response = SumResponse.newBuilder()
                .setSumResult(result_number)
                .build();

        // Send the response
        responseObserver.onNext(response);

        // Completed response
        responseObserver.onCompleted();
    }
}

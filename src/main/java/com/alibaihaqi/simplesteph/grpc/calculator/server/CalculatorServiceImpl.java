package com.alibaihaqi.simplesteph.grpc.calculator.server;

import com.proto.calculator.*;
import com.proto.greet.GreetManyTimesResponse;
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

    @Override
    public void primeNumberDecomposition(PrimeNumberDecompositionRequest request, StreamObserver<PrimeNumberDecompositionResponse> responseObserver) {
        System.out.println("PrimeNumberDecomposition Calculator Service");

        Integer number = request.getNumber();
        Integer start_mod_number = 2;

        try {
            while (number > 1) {
                System.out.println("Current number: " + number);

                if (number % start_mod_number == 0) {
                    System.out.println("Current Modulus number: " + start_mod_number);


                    PrimeNumberDecompositionResponse response = PrimeNumberDecompositionResponse.newBuilder()
                            .setResultNumber(start_mod_number)
                            .build();

                    responseObserver.onNext(response);
                    Thread.sleep(1000L);
                    number = number / start_mod_number;
                } else {
                    start_mod_number = start_mod_number + 1;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            responseObserver.onCompleted();
        }
    }
}

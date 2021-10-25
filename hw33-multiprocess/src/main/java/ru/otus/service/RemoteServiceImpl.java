package ru.otus.service;


import io.grpc.stub.StreamObserver;
import ru.otus.generated.InMessage;
import ru.otus.generated.OutMessage;
import ru.otus.generated.RemoteServiceGrpc;

import java.util.stream.IntStream;

import static ru.otus.util.Utils.sleep;

public class RemoteServiceImpl extends RemoteServiceGrpc.RemoteServiceImplBase {

    @Override
    public void generate(InMessage request, StreamObserver<OutMessage> responseObserver) {
        IntStream.rangeClosed(request.getFirstValue() + 1, request.getLastValue())
                .forEach(generatedValue -> send(responseObserver, generatedValue));
        responseObserver.onCompleted();
    }

    private void send(StreamObserver<OutMessage> responseObserver, int generatedValue) {
        responseObserver.onNext(OutMessage.newBuilder().setGeneratedValue(generatedValue).build());
        sleep(2);
    }
}

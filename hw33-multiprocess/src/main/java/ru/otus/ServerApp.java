package ru.otus;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.service.RemoteServiceImpl;

public class ServerApp {

    private static final int SERVER_PORT = 8090;

    private static final Logger log = LoggerFactory.getLogger(ServerApp.class);

    public static void main(String[] args) throws Exception {

        Server server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(new RemoteServiceImpl()).build();
        server.start();
        log.info("server waiting for client connections...");
        server.awaitTermination();
    }
}

package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.service.GenerationServiceImpl;
import ru.otus.service.LogServiceImpl;

import java.util.concurrent.CountDownLatch;

public class ClientApp {

    private static final Logger log = LoggerFactory.getLogger(ClientApp.class);

    public static void main(String[] args) throws InterruptedException {
        log.info("numbers Client is starting...");

        var latch = new CountDownLatch(1);

        var generationService = new GenerationServiceImpl(latch);

        var logService = new LogServiceImpl(generationService);
        var loggerThread = new Thread(logService);
        loggerThread.setDaemon(true);
        loggerThread.start();

        generationService.start();

        latch.await();
    }
}

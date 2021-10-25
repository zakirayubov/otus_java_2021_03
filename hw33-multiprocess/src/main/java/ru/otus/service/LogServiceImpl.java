package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ru.otus.util.Utils.sleep;

public class LogServiceImpl implements LogService, Runnable {

    private final GenerationService generationService;

    private static final Logger log = LoggerFactory.getLogger(LogServiceImpl.class);

    private int currentValue = 0;
    private int lastSavedValue = 0;

    public LogServiceImpl(GenerationService generationService) {
        this.generationService = generationService;
    }

    @Override
    public void run() {
        while (true) {
            int lastGenerated = generationService.getLastGenerated();

            int extra = 0;
            if (lastGenerated != lastSavedValue) {
                extra = lastGenerated;
                lastSavedValue = lastGenerated;
            }

            currentValue = currentValue + extra + 1;

            log();
            sleep(1);
        }
    }

    @Override
    public void log() {
        log.info("currentValue:{}", currentValue);
    }
}

package ru.yandex.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SnapshotProcessor implements Runnable {
    private final AnalyzerService analyzerProcessor;

    @Override
    public void run() {
        log.info("Snapshot processor started");

    }
}

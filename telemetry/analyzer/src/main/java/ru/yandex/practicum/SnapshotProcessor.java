package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.service.AnalyzerService;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotProcessor implements Runnable {
    private final AnalyzerService analyzerProcessor;

    @Override
    public void run() {
        log.info("Snapshot processor started");

    }
}

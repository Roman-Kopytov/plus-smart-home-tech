package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.controller.HubRouterController;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyzerService {
    private final ScenarioService scenarioService;
    private final HubRouterController hubRouterController;


}

package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.AlreadyExistException;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.repository.ScenarioRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScenarioService {
    private final ScenarioRepository scenarioRepository;

    public void addScenario(ScenarioAddedEventAvro event, String hubId) {
        if (scenarioRepository.findByHubIdAndName(hubId, event.getName()).isPresent()) {
            throw new AlreadyExistException("Scenario with name exist");
        }
        Scenario scenario = new Scenario();
        scenario.setName(event.getName());
        scenario.setHubId(hubId);

        List<Condition> conditions = event.getConditions().stream()
                .map(conditionEvent -> Condition.builder()
                        .sensorId(conditionEvent.getSensorId())
                        .type(ConditionType.valueOf(conditionEvent.getType().name()))
                        .operation(ConditionOperation.valueOf(conditionEvent.getOperation().name()))
                        .value(convertToInteger(conditionEvent.getValue()))
                        .scenarios(List.of(scenario))
                        .build())
                .collect(Collectors.toList());

        List<Action> actions = event.getActions().stream()
                .map(actionEvent -> Action.builder()
                        .sensorId(actionEvent.getSensorId())
                        .type(ActionType.valueOf(actionEvent.getType().name()))
                        .value(actionEvent.getValue() != null ? actionEvent.getValue() : 0) //
                        .scenarios(List.of(scenario))
                        .build())
                .collect(Collectors.toList());

        scenario.setConditions(conditions);
        scenario.setActions(actions);

        scenarioRepository.save(scenario);
    }

    public void deleteScenario(String name, String hubId) {
        Optional<Scenario> savedScenario = scenarioRepository.findByHubIdAndName(hubId, name);
        savedScenario.orElseThrow(() -> new NotFoundException("Scenario with name not found"));
        scenarioRepository.delete(savedScenario.get());
    }

    private Integer convertToInteger(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Boolean) {
            return (Boolean) value ? 1 : 0;
        } else {
            return null;
        }
    }
}

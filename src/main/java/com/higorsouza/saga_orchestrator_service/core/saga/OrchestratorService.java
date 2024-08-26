package com.higorsouza.saga_orchestrator_service.core.saga;

import com.higorsouza.saga_orchestrator_service.core.dto.Event;
import com.higorsouza.saga_orchestrator_service.core.dto.History;
import com.higorsouza.saga_orchestrator_service.core.enums.EEventSource;
import com.higorsouza.saga_orchestrator_service.core.enums.ESagaStatus;
import com.higorsouza.saga_orchestrator_service.core.enums.ETopics;
import com.higorsouza.saga_orchestrator_service.core.producer.SagaOrchestratorProducer;
import com.higorsouza.saga_orchestrator_service.core.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.higorsouza.saga_orchestrator_service.core.enums.ETopics.NOTIFY_ENDING;

@Slf4j
@Service
@AllArgsConstructor
public class OrchestratorService {

    private final JsonUtil jsonUtil;
    private final SagaOrchestratorProducer sagaOrchestratorProducer;
    private final SagaExecutionController sagaExecutionController;

    public void startSaga(Event event) {
        event.setSource(EEventSource.ORCHESTRATOR);
        event.setStatus(ESagaStatus.SUCCESS);
        var topic = getTopic(event);
        log.info("SAGA STARTED!");
        addHistory(event, "Saga started!");
        sendToProducerWithTopic(event, topic);
    }

    public void finishSagaSuccess(Event event) {
        event.setSource(EEventSource.ORCHESTRATOR);
        event.setStatus(ESagaStatus.SUCCESS);
        log.info("SAGA FINISHED SUCCESSFULLY FOR EVENT {}!", event.getId());
        addHistory(event, "Saga finished successfully!");
        notifyFinishedSaga(event);
    }

    public void finishSagaFail(Event event) {
        event.setSource(EEventSource.ORCHESTRATOR);
        event.setStatus(ESagaStatus.FAIL);
        log.info("SAGA FINISHED WITH ERRORS FOR EVENT {}!", event.getId());
        addHistory(event, "Saga finished with errors!");
        notifyFinishedSaga(event);
    }

    public void continueSaga(Event event) {
        var topic = getTopic(event);
        event.setSource(EEventSource.ORCHESTRATOR);
        log.info("SAGA CONTINUING FOR EVENT {}", event.getId());
        sendToProducerWithTopic(event, topic);
    }

    private ETopics getTopic(Event event) {
        return sagaExecutionController.getNextTopic(event);
    }

    private void addHistory(Event event, String message){
        var history = History
                .builder()
                .source(event.getSource())
                .status(event.getStatus())
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();
        event.addToHistory(history);
    }

    private void sendToProducerWithTopic(Event event, ETopics topic){
        sagaOrchestratorProducer.sendEvent(jsonUtil.toJson(event), topic.getTopic());
    }

    private void notifyFinishedSaga(Event event) {
        sagaOrchestratorProducer.sendEvent(jsonUtil.toJson(event), NOTIFY_ENDING.getTopic());
    }


}

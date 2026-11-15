package com.example.bank.kafka;

import com.example.bank.domain.outbox.OutboxEvent;
import com.example.bank.event.TransferCompletedEvent;
import com.example.bank.event.TransferFailedEvent;
import com.example.bank.event.TransferInitiatedEvent;
import com.example.bank.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private static final Map<String, Class<?>> TOPIC_TO_CLASS = Map.of(
            "transfer-initiated", TransferInitiatedEvent.class,
            "transfer-completed", TransferCompletedEvent.class,
            "transfer-failed", TransferFailedEvent.class
    );

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishEvents() {
        List<OutboxEvent> events = outboxRepository.findTop100ByProcessedFalseOrderByCreatedAtAsc();
        if (events.isEmpty()) {
            return;
        }

        log.info("Found {} unprocessed outbox events", events.size());

        for (OutboxEvent event : events) {
            try {
                Class<?> clazz = TOPIC_TO_CLASS.get(event.getEventType());
                if (clazz == null) {
                    log.error("Unknown event type: {}. Skipping event {}", event.getEventType(), event.getId());
                    continue;
                }

                Object payload = objectMapper.readValue(event.getPayload(), clazz);
                kafkaTemplate.send(event.getEventType(), event.getAggregateId(), payload).get(5, TimeUnit.SECONDS);
                event.markProcessed();
                outboxRepository.save(event);
                log.info("Event sent: topic={}, id={}", event.getEventType(), event.getAggregateId());

            } catch (Exception e) {
                log.error("Failed to publish outbox event {}", event.getId(), e);
            }
        }
    }
}
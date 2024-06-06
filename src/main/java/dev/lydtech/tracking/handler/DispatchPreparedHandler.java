package dev.lydtech.tracking.handler;

import dev.lydtech.tracking.message.DispatchPrepared;
import dev.lydtech.tracking.service.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DispatchPreparedHandler {
    private final TrackingService trackingService;

    @KafkaListener(
            id = "orderConsumerClient",
            topics = "dispatch.tracking",
            groupId = "dispatch.tracking.consumer",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(DispatchPrepared payload){
        log.info("Received message: {}", payload);
        try {
            trackingService.updateTrackingStatus(payload);
        } catch (Exception e) {
            log.error("update Tracking Status failure: {}", payload, e);
        }

    }
}

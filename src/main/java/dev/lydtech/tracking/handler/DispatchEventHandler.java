package dev.lydtech.tracking.handler;

import dev.lydtech.tracking.message.DispatchCompleted;
import dev.lydtech.tracking.message.DispatchPrepared;
import dev.lydtech.tracking.service.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static dev.lydtech.tracking.TrackingConfiguration.DISPATCH_TRACKING_TOPIC;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(
        id = "dispatchEventConsumerClient",
        topics = DISPATCH_TRACKING_TOPIC,
        groupId = "dispatch.tracking.consumer",
        containerFactory = "kafkaListenerContainerFactory"
)
public class DispatchEventHandler {
    private final TrackingService trackingService;

    @KafkaHandler
    public void dispatchPrepared(DispatchPrepared payload) {
        log.info("dispatchPrepared - Received message: {}", payload);
        try {
            trackingService.updateTrackingStatus(payload);
        } catch (Exception e) {
            log.error("update Tracking Status failure: {}", payload, e);
        }
    }

    @KafkaHandler
    public void dispatchCompleted(DispatchCompleted payload) {
        log.info("dispatchCompleted - Received message: {}", payload);
        try {
            trackingService.updateTrackingStatus(payload);
        } catch (Exception e) {
            log.error("update Tracking Status failure: {}", payload, e);
        }
    }
}

package dev.lydtech.tracking.service;

import dev.lydtech.tracking.enun.Status;
import dev.lydtech.tracking.message.DispatchPrepared;
import dev.lydtech.tracking.message.TrackingStatusUpdated;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class TrackingService {
    public static final String TRACKING_STATUS_TOPIC = "tracking.status";
    private final KafkaTemplate<String, Object> kafkaProducer;


    public void updateTrackingStatus(DispatchPrepared dispatchPrepared) throws ExecutionException, InterruptedException {
        TrackingStatusUpdated trackingStatusUpdated = TrackingStatusUpdated.builder()
                .orderId(dispatchPrepared.getOrderId())
                .status(Status.PREPARING).build();
        
        this.kafkaProducer.send(TRACKING_STATUS_TOPIC, trackingStatusUpdated).get();

    }
}

package dev.lydtech.tracking.service;

import dev.lydtech.tracking.message.DispatchPrepared;
import dev.lydtech.tracking.message.TrackingStatusUpdated;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static dev.lydtech.tracking.TrackingConfiguration.TRACKING_STATUS_TOPIC;
import static org.mockito.Mockito.*;

class TrackingServiceTest {
    private TrackingService trackingService;
    private KafkaTemplate kafkaProducerMock;
    private CompletableFuture completableFuture;

    @BeforeEach
    void setUp() {
        kafkaProducerMock = mock(KafkaTemplate.class);
        completableFuture = CompletableFuture.completedFuture(mock(SendResult.class));
        trackingService = new TrackingService(kafkaProducerMock);
    }

    @Test
    void updateTrackingStatus_Success() throws ExecutionException, InterruptedException {
        when(kafkaProducerMock.send(anyString(), any(TrackingStatusUpdated.class))).thenReturn(completableFuture);

        DispatchPrepared dispatchPrepared = DispatchPrepared.builder().orderId(UUID.randomUUID()).build();
        trackingService.updateTrackingStatus(dispatchPrepared);
        verify(kafkaProducerMock, times(1)).send(eq(TRACKING_STATUS_TOPIC), any(TrackingStatusUpdated.class));
    }
}
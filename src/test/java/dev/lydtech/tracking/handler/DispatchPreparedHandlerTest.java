package dev.lydtech.tracking.handler;

import dev.lydtech.tracking.message.DispatchPrepared;
import dev.lydtech.tracking.service.TrackingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DispatchPreparedHandlerTest {
    private TrackingService trackingService;
    private DispatchPreparedHandler dispatchPreparedHandler;


    @BeforeEach
    void setUp() {
        trackingService = mock(TrackingService.class);
        dispatchPreparedHandler= new DispatchPreparedHandler(trackingService);
    }

    @Test
    void listen_Success() throws ExecutionException, InterruptedException {
        DispatchPrepared payload = DispatchPrepared.builder().orderId(UUID.randomUUID()).build();
        dispatchPreparedHandler.listen(payload);
        verify(trackingService, times(1)).updateTrackingStatus(any(DispatchPrepared.class));
    }

    @Test
    void listen_ThrowsException() throws ExecutionException, InterruptedException {
        DispatchPrepared payload = DispatchPrepared.builder().orderId(UUID.randomUUID()).build();
        dispatchPreparedHandler.listen(payload);
        doThrow(new RuntimeException("update tracking failed")).when(trackingService).updateTrackingStatus(payload);
    }
}
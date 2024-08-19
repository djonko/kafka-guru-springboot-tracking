package dev.lydtech.tracking.handler;

import dev.lydtech.tracking.message.DispatchCompleted;
import dev.lydtech.tracking.message.DispatchPrepared;
import dev.lydtech.tracking.service.TrackingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;

class DispatchEventHandlerTest {
    private TrackingService trackingService;
    private DispatchEventHandler dispatchEventHandler;
    private String strLocalDate;

    @BeforeEach
    void setUp() {
        trackingService = mock(TrackingService.class);
        dispatchEventHandler = new DispatchEventHandler(trackingService);
        strLocalDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @Test
    void dispatchPrepared_Success() throws ExecutionException, InterruptedException {
        DispatchPrepared payload = DispatchPrepared.builder().orderId(UUID.randomUUID()).build();
        dispatchEventHandler.dispatchPrepared(payload);
        verify(trackingService, times(1)).updateTrackingStatus(any(DispatchPrepared.class));
    }

    @Test
    void dispatchCompleted_Success() throws ExecutionException, InterruptedException {

        DispatchCompleted payload = DispatchCompleted.builder().orderId(UUID.randomUUID()).date(strLocalDate).build();
        dispatchEventHandler.dispatchCompleted(payload);
        verify(trackingService, times(1)).updateTrackingStatus(any(DispatchCompleted.class));
    }

    @Test
    void dispatchPrepared_ThrowsException() throws ExecutionException, InterruptedException {
        DispatchPrepared payload = DispatchPrepared.builder().orderId(UUID.randomUUID()).build();
        dispatchEventHandler.dispatchPrepared(payload);
        doThrow(new RuntimeException("update tracking failed")).when(trackingService).updateTrackingStatus(payload);
    }

    @Test
    void dispatchCompleted_ThrowsException() throws ExecutionException, InterruptedException {
        DispatchCompleted payload = DispatchCompleted.builder().orderId(UUID.randomUUID()).date(strLocalDate).build();
        dispatchEventHandler.dispatchCompleted(payload);
        doThrow(new RuntimeException("update tracking failed")).when(trackingService).updateTrackingStatus(payload);
    }
}
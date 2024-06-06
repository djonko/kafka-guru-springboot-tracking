package dev.lydtech.tracking.message;

import dev.lydtech.tracking.enun.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StatusUpdatedTest {
    private UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
    }

    @Test
    void validate_TrackingStatusUpdated_success(){
        TrackingStatusUpdated trackingStatusUpdated = TrackingStatusUpdated.builder()
                .orderId(uuid).status(Status.PREPARING).build();
        assertNotNull(trackingStatusUpdated);
        assertEquals(trackingStatusUpdated.getOrderId().toString(), uuid.toString());
        assertEquals(trackingStatusUpdated.getStatus(), Status.PREPARING);
    }
}
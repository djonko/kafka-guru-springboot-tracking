package dev.lydtech.tracking.message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DispatchPreparedTest {
    private UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
    }

    @Test
    void validate_DispatchPrepared_success(){
        DispatchPrepared dispatchPrepared = DispatchPrepared.builder().orderId(uuid).build();
        assertNotNull(dispatchPrepared);
        assertEquals(dispatchPrepared.getOrderId().toString(), uuid.toString());
    }
}
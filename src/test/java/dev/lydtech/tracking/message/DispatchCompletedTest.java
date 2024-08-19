package dev.lydtech.tracking.message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DispatchCompletedTest {
    private UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
    }

    @Test
    void validate_DispatchCompleted_success(){
        String strLocalDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        DispatchCompleted dispatchCompleted = DispatchCompleted.builder().orderId(uuid).date(strLocalDate).build();
        assertNotNull(dispatchCompleted);
        assertEquals(dispatchCompleted.getOrderId().toString(), uuid.toString());
        assertEquals(dispatchCompleted.getDate(), strLocalDate);
    }
}
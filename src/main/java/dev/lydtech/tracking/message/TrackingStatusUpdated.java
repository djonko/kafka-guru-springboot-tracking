package dev.lydtech.tracking.message;

import dev.lydtech.tracking.enun.Status;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class TrackingStatusUpdated {
    private UUID orderId;
    private Status status;
}

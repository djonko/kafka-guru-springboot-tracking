package dev.lydtech.tracking.message;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class DispatchPrepared {
    private UUID orderId;
}
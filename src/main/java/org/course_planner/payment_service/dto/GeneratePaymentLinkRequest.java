package org.course_planner.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneratePaymentLinkRequest {
    private Double amount;
    private final String currency = "INR";
    private Long orderId;
    private String description;
    private CustomerDetails customerDetails;
}

package org.course_planner.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneratePaymentLinkResponse {
    private Long paymentId;
    private String rpPaymentId;
    private String paymentLink;
    private Double paymentAmount;
    private String paymentStatus;
}

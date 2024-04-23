package org.course_planner.payment_service.service;

import org.course_planner.payment_service.dto.GeneratePaymentLinkRequest;
import org.course_planner.payment_service.dto.GeneratePaymentLinkResponse;

public interface PaymentService {
    GeneratePaymentLinkResponse getPaymentLink(GeneratePaymentLinkRequest request);

    void updatePaymentStatus(String paymentId, String paymentStatus);
}

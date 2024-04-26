package org.course_planner.payment_service.repository;

import org.course_planner.payment_service.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    PaymentEntity findByPaymentLinkId(String paymentLinkId);
}

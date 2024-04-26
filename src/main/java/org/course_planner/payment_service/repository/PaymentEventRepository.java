package org.course_planner.payment_service.repository;

import org.course_planner.payment_service.entity.PaymentEntity;
import org.course_planner.payment_service.entity.PaymentEventHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentEventRepository extends JpaRepository<PaymentEventHistoryEntity, Long> {
}

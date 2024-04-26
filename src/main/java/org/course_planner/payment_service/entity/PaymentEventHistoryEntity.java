package org.course_planner.payment_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment_event_history_entity")
public class PaymentEventHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "payment_event_id", updatable = false, unique = true, nullable = false)
    private Long paymentEventId;
    @Column(name = "payment_event_timestamp", updatable = false, nullable = false)
    private LocalDateTime eventTimestamp;
    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;
    @Column(name = "payment_error_reason", updatable = false)
    private String errorReason;
    @Column(name = "payment_error_source", updatable = false)
    private String errorSource;
    @Column(name = "payment_error_step", updatable = false)
    private String errorStep;
    @Column(name = "payment_method", updatable = false)
    private String method;
    @Column(name = "payment_vpa_id", updatable = false)
    private String vpa;
    @Column(name = "payment_card_network", updatable = false)
    private String cardNetwork;
    @Column(name = "payment_event", updatable = false)
    private String event;

    public PaymentEventHistoryEntity(PaymentEntity paymentEntity) {
        this.eventTimestamp = paymentEntity.getTransactionUpdateTimestamp();
        this.paymentStatus = paymentEntity.getPaymentStatus();
        this.errorReason = paymentEntity.getErrorReason();
        this.errorSource = paymentEntity.getErrorSource();
        this.errorStep = paymentEntity.getErrorStep();
        this.method = paymentEntity.getMethod();
        this.vpa = paymentEntity.getVpa();
        this.cardNetwork = paymentEntity.getCardNetwork();
        this.event = paymentEntity.getEvent();
    }
}

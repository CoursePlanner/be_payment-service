package org.course_planner.payment_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment_details_entity")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "payment_id", updatable = false, unique = true, nullable = false)
    private Long paymentId;
    @Column(name = "payment_rp_id", unique = true)
    private String rpPaymentId;
    @Column(name = "payment_rp_order_id", unique = true)
    private String rpOrderId;
    @Column(name = "payment_amount", updatable = false, nullable = false)
    private Double amount;
    @Column(name = "payment_currency", updatable = false, nullable = false)
    private String currency;
    @Column(name = "payment_order_id", updatable = false, unique = true, nullable = false)
    private Long orderId;
    @Column(name = "payment_creation_timestamp", updatable = false, nullable = false)
    private LocalDateTime transactionCreationTimestamp;
    @Column(name = "payment_update_timestamp", nullable = false)
    private LocalDateTime transactionUpdateTimestamp;
    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;
    @Column(name = "payment_short_link", updatable = false, nullable = false)
    private String paymentShortLink;
    @Column(name = "payment_link_id", updatable = false, nullable = false)
    private String paymentLinkId;
    @Column(name = "payment_error_reason")
    private String errorReason;
    @Column(name = "payment_error_source")
    private String errorSource;
    @Column(name = "payment_error_step")
    private String errorStep;
    @Column(name = "payment_method")
    private String method;
    @Column(name = "payment_vpa_id")
    private String vpa;
    @Column(name = "payment_card_network")
    private String cardNetwork;
    @Column(name = "payment_event")
    private String event;
    @OneToMany(mappedBy = "paymentEventId", fetch = FetchType.LAZY, targetEntity = PaymentEventHistoryEntity.class)
    private List<PaymentEventHistoryEntity> events = new LinkedList<>();
}

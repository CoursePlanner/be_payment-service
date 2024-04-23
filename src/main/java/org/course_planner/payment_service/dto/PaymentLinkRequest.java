package org.course_planner.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentLinkRequest {
    private Long amount;
    private final String currency = "INR";
    private final Boolean accept_partial = false;
    private Long expire_by;
    private String reference_id;
    private String description;
    private CustomerDetails customer;
    private Map<String, String> notes;
    private String callback_url;
}

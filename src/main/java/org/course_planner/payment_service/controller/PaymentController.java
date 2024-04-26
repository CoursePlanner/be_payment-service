package org.course_planner.payment_service.controller;

import org.course_planner.payment_service.dto.GeneratePaymentLinkRequest;
import org.course_planner.payment_service.dto.GeneratePaymentLinkResponse;
import org.course_planner.payment_service.service.PaymentService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @PostMapping(value = "/generatePaymentLink", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneratePaymentLinkResponse> generatePaymentLink(@RequestBody GeneratePaymentLinkRequest request) {
        return new ResponseEntity<>(paymentService.getPaymentLink(request), HttpStatus.CREATED);
    }

    @PostMapping(value = "/payment-link/webhook", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updatePaymentStatus(@RequestBody Object request) {
        LOGGER.info("updatePaymentStatus: {}", request.toString());
        paymentService.updatePaymentStatus(request);
    }
}

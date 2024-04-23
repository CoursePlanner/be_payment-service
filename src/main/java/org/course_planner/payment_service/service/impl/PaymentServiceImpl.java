package org.course_planner.payment_service.service.impl;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.course_planner.payment_service.dto.GeneratePaymentLinkRequest;
import org.course_planner.payment_service.dto.GeneratePaymentLinkResponse;
import org.course_planner.payment_service.dto.PaymentLinkRequest;
import org.course_planner.payment_service.dto.RazorPayProperties;
import org.course_planner.payment_service.entity.PaymentEntity;
import org.course_planner.payment_service.repository.PaymentRepository;
import org.course_planner.payment_service.service.PaymentService;
import org.course_planner.utils.exceptions.PaymentException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RazorpayClient razorpayClient;

    @Autowired
    private RazorPayProperties razorPayProperties;

    @Override
    public GeneratePaymentLinkResponse getPaymentLink(GeneratePaymentLinkRequest request) {
        String amountStr = request.getAmount().toString().replace(".", "");
        PaymentLinkRequest paymentLinkRequest = new PaymentLinkRequest();
        paymentLinkRequest.setAmount(Long.parseLong(amountStr));
        paymentLinkRequest.setCustomer(request.getCustomerDetails());
        paymentLinkRequest.setCallback_url(razorPayProperties.getCallBackUrl());
        paymentLinkRequest.setReference_id("");
        paymentLinkRequest.setExpire_by(Instant.now().plus(
                razorPayProperties.getPaymentLinkExpiryMinutes(), ChronoUnit.MINUTES).toEpochMilli());
        try {
            PaymentLink paymentLink = razorpayClient.paymentLink.create(new JSONObject(paymentLinkRequest));
            PaymentEntity entity = new PaymentEntity();
            entity.setPaymentStatus(paymentLink.get("status"));
            entity.setAmount(request.getAmount());
            entity.setPaymentShortLink(paymentLink.get("short_url"));
            entity.setPaymentLinkId(paymentLink.get("id"));
            entity.setCurrency(request.getCurrency());
            entity.setOrderId(request.getOrderId());
            entity.setTransactionCreationTimestamp(LocalDateTime.now());
            entity.setTransactionUpdateTimestamp(LocalDateTime.now());
            entity = paymentRepository.save(entity);
            GeneratePaymentLinkResponse response = new GeneratePaymentLinkResponse();
            response.setPaymentId(entity.getPaymentId());
            response.setPaymentLink(entity.getPaymentShortLink());
            response.setPaymentStatus(entity.getPaymentStatus());
            response.setPaymentAmount(entity.getAmount());
            response.setRpPaymentId("");
            return response;
        } catch (RazorpayException e) {
            throw new PaymentException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void updatePaymentStatus(String paymentId, String paymentStatus) {

    }
}

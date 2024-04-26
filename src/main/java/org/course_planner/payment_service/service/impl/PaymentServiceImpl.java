package org.course_planner.payment_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.annotation.PostConstruct;
import org.course_planner.payment_service.dto.GeneratePaymentLinkRequest;
import org.course_planner.payment_service.dto.GeneratePaymentLinkResponse;
import org.course_planner.payment_service.dto.PaymentLinkRequest;
import org.course_planner.payment_service.dto.RazorPayProperties;
import org.course_planner.payment_service.entity.PaymentEntity;
import org.course_planner.payment_service.entity.PaymentEventHistoryEntity;
import org.course_planner.payment_service.repository.PaymentEventRepository;
import org.course_planner.payment_service.repository.PaymentRepository;
import org.course_planner.payment_service.service.PaymentService;
import org.course_planner.utils.configs.CustomObjectMapper;
import org.course_planner.utils.exceptions.PaymentException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class PaymentServiceImpl implements PaymentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private static final String CONST_THREAD_POOL_SIZE = "cp.multi-thread-configs.payment-link-event-hook.thread-pool-size";
    private static final String CONST_THREAD_TIMEOUT_MS = "cp.multi-thread-configs.payment-link-event-hook.thread-timeout-ms";
    private static final ObjectMapper objectMapper = CustomObjectMapper.getObjectMapper();

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentEventRepository paymentEventRepository;

    @Autowired
    private RazorpayClient razorpayClient;

    @Autowired
    private RazorPayProperties razorPayProperties;

    @Autowired
    private Environment environment;

    private static ExecutorService executorService;

    @PostConstruct
    public void init() throws InterruptedException {
        executorService = Executors.newFixedThreadPool(environment.getProperty(CONST_THREAD_POOL_SIZE, Integer.class, 5));
        executorService.awaitTermination(environment.getProperty(
                CONST_THREAD_TIMEOUT_MS, Integer.class, 5000), TimeUnit.MILLISECONDS);
    }

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
            response.setPaymentStatus(entity.getPaymentStatus() != null
                    ? entity.getPaymentStatus().toLowerCase() : null);
            response.setPaymentAmount(entity.getAmount());
            response.setRpPaymentId("");
            return response;
        } catch (RazorpayException e) {
            throw new PaymentException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void updatePaymentStatus(Object request) {
        try {
            JSONObject paymentPayload = new JSONObject(objectMapper.writeValueAsString(request));
//            LOGGER.info("updatePaymentStatus: CompletableFuture: Received Payload: {}", paymentPayload);
            CompletableFuture.runAsync(() -> {
                LOGGER.info("updatePaymentStatus: CompletableFuture: Received Payload: {}", paymentPayload);
                JSONObject payloadObject = paymentPayload.optJSONObject("payload");
                String eventType = paymentPayload.optString("event", "");
                if (payloadObject != null && payloadObject.optJSONObject("payment") != null
                        && payloadObject.optJSONObject("payment").optJSONObject("entity") != null
                        && payloadObject.optJSONObject("payment_link") != null
                        && payloadObject.optJSONObject("payment_link").optJSONObject("entity") != null) {
                    JSONObject paymentEntity = payloadObject.optJSONObject("payment").getJSONObject("entity");
                    JSONObject paymentLinkEntity = payloadObject.optJSONObject("payment_link").getJSONObject("entity");
                    String paymentLinkId = paymentLinkEntity.optString("id", null);

                    if (paymentLinkId != null && !paymentLinkId.trim().isBlank()) {
                        PaymentEntity payment = paymentRepository.findByPaymentLinkId(paymentLinkId);
                        PaymentEventHistoryEntity savedEvent = paymentEventRepository.save(new PaymentEventHistoryEntity(payment));
                        payment.setRpPaymentId(paymentEntity.optString("id", ""));
                        payment.setPaymentStatus(paymentLinkEntity.optString("status",
                                paymentEntity.optString("status", payment.getPaymentStatus())));
                        payment.setCardNetwork(paymentEntity.optString("network", ""));
                        payment.setErrorReason(paymentEntity.optString("error_description", ""));
                        payment.setErrorStep(paymentEntity.optString("error_step", ""));
                        payment.setErrorSource(paymentEntity.optString("error_source", ""));
                        payment.setRpOrderId(paymentLinkEntity.optString("order_id", ""));
                        payment.setMethod(paymentEntity.optString("method", ""));
                        payment.setEvent(eventType);
                        payment.setVpa(paymentLinkEntity.optString("vpa", ""));
                        payment.getEvents().add(savedEvent);
                        payment.setTransactionUpdateTimestamp(LocalDateTime.now());
                    }
                }
            }, executorService);
        } catch (Exception ex) {
            LOGGER.error("updatePaymentStatus: ", ex);
        }
    }

    public void updatePaymentDetailsForCard(PaymentEntity paymentEntity) {
        //
    }
}

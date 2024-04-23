package org.course_planner.payment_service.dto;

import lombok.Getter;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Getter
public class RazorPayProperties {
    private static final String CONST_PROPERTY_PREFIX = "cp.razor-pay.";
    private final Environment environment;

    private final String keyId;
    private final String keySecret;
    private final Boolean enableLogging;
    private final Long paymentLinkExpiryMinutes;
    private final String callBackUrl;
    private final Boolean notifyEmail;
    private final Boolean notifySms;

    public RazorPayProperties(Environment environment) {
        this.environment = environment;
        this.keyId = getValue("key-id", String.class, "");
        this.keySecret = getValue("key-secret", String.class, "");
        this.enableLogging = getValue("enable-logging", Boolean.class, false);
        this.paymentLinkExpiryMinutes = getValue("payment-link-expiry-minutes", Long.class, 5L);
        this.callBackUrl = getValue("call-back-url", String.class, "");
        this.notifyEmail = getValue("notify-email", Boolean.class, false);
        this.notifySms = getValue("notify-sms", Boolean.class, false);
    }

    private <T> T getValue(String propertyName, Class<T> type, T defaultValue) {
        return environment.getProperty(CONST_PROPERTY_PREFIX + propertyName, type, defaultValue);
    }
}

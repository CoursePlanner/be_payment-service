package org.course_planner.payment_service.config;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.course_planner.payment_service.dto.RazorPayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RPClientConfig {
    @Autowired
    private RazorPayProperties razorPayProperties;

    @Bean
    public RazorpayClient razorpayClient() throws RazorpayException {
        return new RazorpayClient(razorPayProperties.getKeyId(), razorPayProperties.getKeySecret(),
                razorPayProperties.getEnableLogging());
    }
}

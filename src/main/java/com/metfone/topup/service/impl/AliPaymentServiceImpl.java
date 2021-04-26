package com.metfone.topup.service.impl;

import com.metfone.topup.model.alipay.AlipayPaymentResponse;
import com.metfone.topup.model.payment.PaymentRequest;
import com.metfone.topup.service.IPaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AliPaymentServiceImpl implements IPaymentService<PaymentRequest, AlipayPaymentResponse> {
    @Value("${url.service}")
    String URL_SERVICE;

    @Value("${service.payment.alipay}")
    String ALIPAY_PAYMENT_URI;

    @Override
    public AlipayPaymentResponse initPayment(PaymentRequest paymentRequest) {
        AlipayPaymentResponse alipayPaymentResponse = new AlipayPaymentResponse();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PaymentRequest> requestBody = new HttpEntity<>(paymentRequest, headers);
            ResponseEntity<AlipayPaymentResponse> response
                    = restTemplate.postForEntity(URL_SERVICE + ALIPAY_PAYMENT_URI, requestBody, AlipayPaymentResponse.class);
            return response.getBody();
        } catch (Exception ex) {
            alipayPaymentResponse.setRespcode("99");
            return alipayPaymentResponse;
        }
    }

    @Override
    public RedirectView setupPayment(AlipayPaymentResponse payment) {
        RedirectView redirectView = new RedirectView();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now) + " : Log payment " + payment.toString());
        redirectView.setUrl(payment.getRedirectUrl());
        return redirectView;
    }

}

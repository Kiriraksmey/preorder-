package com.metfone.topup.service.impl;

import com.metfone.topup.model.payment.PaymentRequest;
import com.metfone.topup.model.payment.ResponseCodeEnum;
import com.metfone.topup.model.union.UnionPaymentResponse;
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
public class UnionPaymentServiceImpl implements IPaymentService<PaymentRequest, UnionPaymentResponse> {

    @Value("${url.service}")
    String URL_SERVICE;

    @Value("${service.payment.union}")
    String UNION_PAYMENT_URI;

    @Override
    public UnionPaymentResponse initPayment(PaymentRequest paymentRequest) {
        UnionPaymentResponse unionPaymentResponse = new UnionPaymentResponse();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PaymentRequest> requestBody = new HttpEntity<>(paymentRequest, headers);
            ResponseEntity<UnionPaymentResponse> response
                    = restTemplate.postForEntity(URL_SERVICE + UNION_PAYMENT_URI, requestBody, UnionPaymentResponse.class);
            return response.getBody();
        } catch (Exception ex) {
            unionPaymentResponse.setRespcode("99");
            return unionPaymentResponse;
        }
    }

    @Override
    public RedirectView setupPayment(UnionPaymentResponse payment) {
        RedirectView redirectView = new RedirectView();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now) + " : Log payment " + payment.toString());
        redirectView.setUrl(payment.getRedirectUrl());
        return redirectView;
    }
}

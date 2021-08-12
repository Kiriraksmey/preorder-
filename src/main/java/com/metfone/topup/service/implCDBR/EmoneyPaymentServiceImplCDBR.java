package com.metfone.topup.service.implCDBR;

import com.metfone.topup.model.cybercard.PaymentRequestCyberCDBR;
import com.metfone.topup.model.emoney.EmoneyPaymentResponse;
import com.metfone.topup.model.payment.PaymentRequestEmoney;
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
public class EmoneyPaymentServiceImplCDBR implements IPaymentService<PaymentRequestCyberCDBR, EmoneyPaymentResponse> {

    @Value("${url.service}")
    String URL_SERVICE;

    @Value("${service.payment.emoney.cdbr}")
    String EMONEY_PAYMENT_URI;

    @Override
    public EmoneyPaymentResponse initPayment(PaymentRequestCyberCDBR paymentRequest) {
        EmoneyPaymentResponse eMoneyPaymentResponse = new EmoneyPaymentResponse();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PaymentRequestCyberCDBR> requestBody = new HttpEntity<>(paymentRequest, headers);
            ResponseEntity<EmoneyPaymentResponse> response
                    = restTemplate.postForEntity(URL_SERVICE + EMONEY_PAYMENT_URI, requestBody, EmoneyPaymentResponse.class);
            return response.getBody();
        } catch (Exception ex) {
            ex.printStackTrace();
            eMoneyPaymentResponse.setRespcode("99");
            return eMoneyPaymentResponse;
        }
    }

    @Override
    public RedirectView setupPayment(EmoneyPaymentResponse payment) {
        RedirectView redirectView = new RedirectView();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now) + " : Log payment " + payment.toString());
        redirectView.setUrl(payment.getRedirectUrl());
        return redirectView;
    }
}

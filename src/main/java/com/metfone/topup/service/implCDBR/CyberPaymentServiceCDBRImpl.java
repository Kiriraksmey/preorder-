package com.metfone.topup.service.implCDBR;

import com.metfone.topup.model.cybercard.CyberCardPaymentResponse;
import com.metfone.topup.model.cybercard.PaymentRequestCyber;
import com.metfone.topup.model.cybercard.PaymentRequestCyberCDBR;
import com.metfone.topup.service.IPaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class CyberPaymentServiceCDBRImpl implements IPaymentService<PaymentRequestCyberCDBR, CyberCardPaymentResponse> {

    @Value("${url.service}")
    String URL_SERVICE;

    @Value("${service.payment.cyber.cdbr}")
    String PATH_PAYMENT_CYBER;

    @Override
    public CyberCardPaymentResponse initPayment(PaymentRequestCyberCDBR paymentRequest) {
        CyberCardPaymentResponse cyberCardPaymentResponse = new CyberCardPaymentResponse();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PaymentRequestCyberCDBR> requestBody = new HttpEntity<>(paymentRequest, headers);
            System.out.println(dtf.format(now) + " : Log request metfone createTransactionABA " +
                    paymentRequest.toString());
            ResponseEntity<CyberCardPaymentResponse> response
                    = restTemplate.postForEntity(URL_SERVICE + PATH_PAYMENT_CYBER, requestBody, CyberCardPaymentResponse.class);
            return response.getBody();
        } catch (Exception ex) {
            cyberCardPaymentResponse.setRespcode("99");
            return cyberCardPaymentResponse;
        }
    }

    @Override
    public ModelMap setupPayment(CyberCardPaymentResponse payment) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now) + " : Log payment cybercard " + payment.toString());
        ModelMap model = new ModelMap();
        model.addAttribute("hash", payment.getHash());
        model.addAttribute("tran_id", payment.getTxnid());
        model.addAttribute("amount", payment.getAmt());
        model.addAttribute("firstname", payment.getFirstname());
        model.addAttribute("lastname", payment.getLastname());
        model.addAttribute("phone", payment.getPhone());
        model.addAttribute("email", payment.getEmail());
        model.addAttribute("url", payment.getUrlABA());

        return model;
    }
}

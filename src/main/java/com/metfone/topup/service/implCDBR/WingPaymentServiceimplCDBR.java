package com.metfone.topup.service.implCDBR;

import com.metfone.topup.model.Wing.GetInfoWingResponse;
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
public class WingPaymentServiceimplCDBR implements IPaymentService<PaymentRequestCyberCDBR, GetInfoWingResponse> {
    @Value("${url.service}")
    String URL_SERVICE;

    @Value("${service.payment.wing.cdbr}")
    String WING_PAYMENT_URI;

    @Override
    public GetInfoWingResponse initPayment(PaymentRequestCyberCDBR paymentRequest) {
        GetInfoWingResponse getInfoWingResponse = new GetInfoWingResponse();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PaymentRequestCyberCDBR> requestBody = new HttpEntity<>(paymentRequest, headers);
            System.out.println(dtf.format(now) + " : Log request metfone createTransactionAcleda " +
                    paymentRequest.toString());
            ResponseEntity<GetInfoWingResponse> response
                    = restTemplate.postForEntity(URL_SERVICE + WING_PAYMENT_URI, requestBody, GetInfoWingResponse.class);
            return response.getBody();
        } catch (Exception ex) {
            getInfoWingResponse.setResponseCode("99");
            return getInfoWingResponse;
        }
    }

    @Override
    public ModelMap setupPayment(GetInfoWingResponse payment) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now) + " : Log payment Wing " + payment.toString());
        ModelMap model = new ModelMap();
        model.addAttribute("sandbox", payment.getSandbox());
        model.addAttribute("amount", payment.getAmount());
        model.addAttribute("rest_api_key", payment.getRest_api_key());
        model.addAttribute("return_url", payment.getReturnUrl());
        model.addAttribute("bill_till_rbtn", payment.getBill_till_rbtn());
        model.addAttribute("bill_till_number", payment.getBill_till_number());
        model.addAttribute("urlWing", payment.getURL_WS_Wing());
        model.addAttribute("username", payment.getUsername());
        model.addAttribute("remark", payment.getRemark());
        model.addAttribute("is_inquiry", payment.getIs_inquiry());

        return model;
    }
}
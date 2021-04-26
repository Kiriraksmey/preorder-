package com.metfone.topup.service.impl;

import com.metfone.topup.model.acleda.AcledaPaymentResponse;
import com.metfone.topup.model.cybercard.CyberCardPaymentResponse;
import com.metfone.topup.model.cybercard.PaymentRequestCyber;
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
public class AcledaPaymentServiceImpl implements IPaymentService<PaymentRequestCyber, AcledaPaymentResponse> {

    @Value("${url.service}")
    String URL_SERVICE;

    @Value("${path.payment.acleda}")
    String PATH_PAYMENT_ACLEDA;

    @Override
    public AcledaPaymentResponse initPayment(PaymentRequestCyber paymentRequest) {
        AcledaPaymentResponse acledaPaymentResponse = new AcledaPaymentResponse();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PaymentRequestCyber> requestBody = new HttpEntity<>(paymentRequest, headers);
            System.out.println(dtf.format(now) + " : Log request metfone createTransactionAcleda " +
                    paymentRequest.toString());
            ResponseEntity<AcledaPaymentResponse> response
                    = restTemplate.postForEntity(URL_SERVICE + PATH_PAYMENT_ACLEDA, requestBody, AcledaPaymentResponse.class);
            return response.getBody();
        } catch (Exception ex) {
            acledaPaymentResponse.setRespcode("99");
            return acledaPaymentResponse;
        }
    }

    @Override
    public ModelMap setupPayment(AcledaPaymentResponse payment) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now) + " : Log payment Acleda " + payment.toString());
        ModelMap model = new ModelMap();
        model.addAttribute("url", payment.getUrlAcleda());
        model.addAttribute("merchantID", payment.getMerchantID());
        model.addAttribute("sessionid", payment.getSessionid());
        model.addAttribute("paymenttokenid", payment.getPaymenttokenid());
        model.addAttribute("description", payment.getDescription());
        model.addAttribute("expirytime", payment.getExpirytime());
        model.addAttribute("amount", payment.getAmt());
        model.addAttribute("quantity", payment.getQuantity());
        model.addAttribute("item", payment.getItem());
        model.addAttribute("invoiceid", payment.getInvoiceid());
        model.addAttribute("currencytype", payment.getCurrencytype());
        model.addAttribute("transactionID", payment.getTransactionID());
        model.addAttribute("successUrlToReturn", payment.getSuccessUrlToReturn());
        model.addAttribute("errorUrl", payment.getErrorUrl());

        return model;
    }
}

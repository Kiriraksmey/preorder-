package com.metfone.topup.service.implCDBR;

import com.metfone.topup.model.cybercard.PaymentRequestCyberCDBR;
import com.metfone.topup.model.payment.PaymentRequest;
import com.metfone.topup.model.wechat.WechatPaymentResponse;
import com.metfone.topup.service.IPaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.client.RestTemplate;

@Service
public class WechatPaymentServiceImplCDBR implements IPaymentService<PaymentRequestCyberCDBR, WechatPaymentResponse> {

    @Value("${url.service}")
    String URL_SERVICE;

    @Value("${service.payment.wechat.cdbr}")
    String WECHAT_PAYMENT_URI;

    @Override
    public WechatPaymentResponse initPayment(PaymentRequestCyberCDBR paymentRequest) {
        WechatPaymentResponse wechatPaymentResponse = new WechatPaymentResponse();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PaymentRequestCyberCDBR> requestBody = new HttpEntity<>(paymentRequest, headers);
            ResponseEntity<WechatPaymentResponse> response
                    = restTemplate.postForEntity(URL_SERVICE + WECHAT_PAYMENT_URI, requestBody, WechatPaymentResponse.class);
            return response.getBody();
        } catch (Exception ex) {
            wechatPaymentResponse.setRespcode("99");
            return wechatPaymentResponse;
        }
    }

    @Override
    public ModelMap setupPayment(WechatPaymentResponse payment) {
        ModelMap modelMap = new ModelMap();
        // TODO: Setup page generate QR code for scanning
        modelMap.addAttribute("isdn", payment.getIsdn());
        modelMap.addAttribute("payCode", payment.getPayCode());
        modelMap.addAttribute("nttrefid", payment.getNttrefid());
        modelMap.addAttribute("txnid", payment.getTxnid());

        return modelMap;
    }
}

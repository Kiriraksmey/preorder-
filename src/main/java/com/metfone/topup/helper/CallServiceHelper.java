package com.metfone.topup.helper;

import com.metfone.topup.model.*;
import com.metfone.topup.model.emoney.GetInfoCustomerRequest;
import com.metfone.topup.model.emoney.GetInfoCustomerResponse;
import com.metfone.topup.model.topup.GetListPaymentRequest;
import com.metfone.topup.model.topup.GetListPaymentResponse;
import com.metfone.topup.model.wechat.QRScanCheck;
import com.metfone.topup.model.wechat.WechatCheckResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CallServiceHelper {
    @Value("${url.service}")
    private String URL_SERVICE;

    @Value("${path.check_isdn}")
    private String PATH_CHECK_ISDN;

    @Value("${path.get_list_payment}")
    private String PATH_GET_LIST_PAYMENT;

    @Value("${path.payment_cyber}")
    private String PATH_PAYMENT_CYBER;

    @Value("${path.qr_scan_check}")
    private String PATH_QR_SCAN_CHECK;

    @Value("${path.call_back_ntt}")
    private String PATH_CALLBACK_NTT;

    @Value("${path.call_back_ntt_emoney}")
    private String PATH_CALLBACK_NTT_EMONEY;

    @Value("${path.call_back_aba}")
    private String PATH_CALLBACK_ABA;

    @Value("${path.call_back_acleda}")
    private String PATH_CALLBACK_ACLEDA;

    @Value("${service.getinfo.customer}")
    private String PATH_GETINFO_CUSTOMER;

    @Value("${service.callBack.wing}")
    private String PATH_CallBack_Wing;

    public CheckIsdnResponse checkIsdn(CheckIsdn checkIsdn) {
        CheckIsdnResponse result = new CheckIsdnResponse();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now) + " : Check isdn request to " + URL_SERVICE + PATH_CHECK_ISDN + " : " + checkIsdn.toString());
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CheckIsdn> requestBody = new HttpEntity<>(checkIsdn, headers);
            ResponseEntity<CheckIsdnResponse> response
                    = restTemplate.postForEntity(URL_SERVICE + PATH_CHECK_ISDN, requestBody, CheckIsdnResponse.class);
            result = response.getBody();
            return result;
        } catch (Exception ex) {
            System.out.println(dtf.format(now) + " : Exception get list payment: " + ex.getMessage());
            result.setResponseCode("01");
            result.setResponseMessage("Server internal error. Please try again later!");
            return result;
        }
    }

    public GetInfoCustomerResponse getInfoCustomer(GetInfoCustomerRequest getInfoCustomerRequest) {
        GetInfoCustomerResponse result = new GetInfoCustomerResponse();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<GetInfoCustomerRequest> requestBody = new HttpEntity<>(getInfoCustomerRequest, headers);
            ResponseEntity<GetInfoCustomerResponse> response
                    = restTemplate.postForEntity(URL_SERVICE + PATH_GETINFO_CUSTOMER, requestBody, GetInfoCustomerResponse.class);
            result = response.getBody();
            return result;
        } catch (Exception ex) {
            result.setResponseCode("01");
            result.setResponseMessage("Server internal error. Please try again later!");
            return result;
        }
    }

    public GetListPaymentResponse getListPayment(String ip) {
        GetListPaymentResponse result = new GetListPaymentResponse();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try {
            GetListPaymentRequest request = new GetListPaymentRequest();
            request.setIp(ip);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            System.out.println(dtf.format(now) + " : Get List payment request: " + request.toString());
            HttpEntity<GetListPaymentRequest> requestBody = new HttpEntity<>(request, headers);
            ResponseEntity<GetListPaymentResponse> response
                    = restTemplate.postForEntity(URL_SERVICE + PATH_GET_LIST_PAYMENT, requestBody, GetListPaymentResponse.class);
            result = response.getBody();
            return result;
        } catch (Exception ex) {
            System.out.println(dtf.format(now) + " : Exception get list payment: " + ex.getMessage());
            result.setResponseCode("01");
            result.setResponseMessage("Server internal error. Please try again later!");
            return result;
        }
    }

    public WechatCheckResponse checkWechatResponse(QRScanCheck qrScanCheck) {
        WechatCheckResponse wechatCheckResponse = new WechatCheckResponse();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        if (qrScanCheck.getCardType() == null || qrScanCheck.getCardType().isEmpty()) {
            qrScanCheck.setCardType("");
        }
        System.out.println(dtf.format(now) + " : Log payment " + qrScanCheck.toString());
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<QRScanCheck> requestBody = new HttpEntity<>(qrScanCheck, headers);
            ResponseEntity<WechatCheckResponse> response
                    = restTemplate.postForEntity(URL_SERVICE + PATH_QR_SCAN_CHECK, requestBody, WechatCheckResponse.class);
            wechatCheckResponse = response.getBody();

            if (qrScanCheck.getTxnid() == null) {
                System.out.println(dtf.format(now) + " : Log payment check TransID is null!");
            }
            System.out.println(dtf.format(now) + " : Log payment " + wechatCheckResponse.toString());
            return wechatCheckResponse;
        } catch (Exception ex) {
            System.out.println(dtf.format(now) + " : Exception check Transaction " + ex.getMessage());
            wechatCheckResponse.setRespcode("99");
            wechatCheckResponse.setRespdesc("Server internal error. Please try again later!");
            return wechatCheckResponse;
        }
    }

    public void callbackNTT(RequestCallbackNTT requestCallbackNTT) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RequestCallbackNTT> requestBody = new HttpEntity<>(requestCallbackNTT, headers);
        ResponseEntity<Object> response
                = restTemplate.postForEntity(URL_SERVICE + PATH_CALLBACK_NTT, requestBody, Object.class);
        System.out.println("Response OK");
    }

    public void callbackNTTEmoney(RequestCallbackNTT requestCallbackNTT) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RequestCallbackNTT> requestBody = new HttpEntity<>(requestCallbackNTT, headers);
        ResponseEntity<Object> response
                = restTemplate.postForEntity(URL_SERVICE + PATH_CALLBACK_NTT_EMONEY, requestBody, Object.class);
        System.out.println("Response OK");
    }


    public void callbackAba(RequestCallbackABA requestCallbackABA) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RequestCallbackNTT requestCallbackNTT = new RequestCallbackNTT();
        requestCallbackNTT.setTxnid(requestCallbackABA.getTran_id());
        requestCallbackNTT.setNttrefid(requestCallbackABA.getTran_id());
        requestCallbackNTT.setRespcode(String.valueOf(requestCallbackABA.getStatus()));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now) + " : Request callback ABA to backend: " + requestCallbackNTT.toString());
        HttpEntity<RequestCallbackNTT> requestBody = new HttpEntity<>(requestCallbackNTT, headers);
        ResponseEntity<Object> response
                = restTemplate.postForEntity(URL_SERVICE + PATH_CALLBACK_ABA, requestBody, Object.class);
        System.out.println("Response OK");
    }

    public void callbackAcleda(RequestCallbackAcleda requestCallbackAcleda) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RequestCallbackNTT requestCallbackNTT = new RequestCallbackNTT();
        requestCallbackNTT.setTxnid(requestCallbackAcleda.getTran_id());
        requestCallbackNTT.setNttrefid(requestCallbackAcleda.getTran_id());
        requestCallbackNTT.setRespcode(String.valueOf(requestCallbackAcleda.getStatus()));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now) + " : Request callback Acleda to backend: " + requestCallbackNTT.toString());
        HttpEntity<RequestCallbackNTT> requestBody = new HttpEntity<>(requestCallbackNTT, headers);
        ResponseEntity<Object> response
                = restTemplate.postForEntity(URL_SERVICE + PATH_CALLBACK_ACLEDA, requestBody, Object.class);
        System.out.println("Response OK");
    }

    public ResponseCallbackNTT callbacktWing(RequestCallbackNTT request) {
        ResponseCallbackNTT responseWing = new ResponseCallbackNTT();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println(dtf.format(now) + " : Request callback wing to backend: " + request.toString());
            HttpEntity<RequestCallbackNTT> requestBody = new HttpEntity<>(request, headers);
            ResponseEntity<ResponseCallbackNTT> response
                    = restTemplate.postForEntity(URL_SERVICE + PATH_CallBack_Wing, requestBody, ResponseCallbackNTT.class);
            responseWing = response.getBody();
            return responseWing;
        } catch (Exception ex) {
            ex.printStackTrace();
            responseWing.setResponseCode("01");
            responseWing.setResponseMessage("error.system");
        }
        return responseWing;
    }

}

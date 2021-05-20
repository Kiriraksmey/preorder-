package com.metfone.topup.controller;

import com.metfone.topup.helper.UtilHelper;
import com.metfone.topup.model.alipay.AlipayPaymentResponse;
import com.metfone.topup.model.cybercard.CyberCardPaymentResponse;
import com.metfone.topup.model.cybercard.CyberCardpaymentResponseMobile;
import com.metfone.topup.model.cybercard.PaymentRequestCyberCDBR;
import com.metfone.topup.model.emoney.EmoneyPaymentResponse;
import com.metfone.topup.model.payment.*;
import com.metfone.topup.model.union.UnionPaymentResponse;
import com.metfone.topup.model.wechat.WechatPaymentResponse;
import com.metfone.topup.service.implCDBR.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@RestController
public class PaymentCDBRController {

    @Autowired
    CyberPaymentServiceCDBRImpl cyberPaymentServiceCDBR;
    @Autowired
    AliPaymentServiceImplCDBR aliPaymentServiceImplCDBR;
    @Autowired
    EmoneyPaymentServiceImplCDBR emoneyPaymentServiceImplCDBR;
    @Autowired
    UnionPaymentServiceImplCDBR unionPaymentServiceImplCDBR;
    @Autowired
    WechatPaymentServiceImplCDBR wechatPaymentServiceImplCDBR;

    @Autowired
    MessageSource messageSource;

    @Autowired
    UtilHelper utilHelper;

    @Value("${source.path}")
    private String SOURCE_PATH;

    @Value("${url.api.mobile}")
    String URL_API_MOBILE;


    @RequestMapping(value = "/createTransMobilePayment",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CyberCardpaymentResponseMobile createTransMobilePayment(@RequestBody PaymentForMobile mobileDeposit,
                                                                   HttpServletResponse response) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        PaymentRequestCyberCDBR paymentRequest = new PaymentRequestCyberCDBR();

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmsss");
        String strDate = dateFormat.format(date);
        CyberCardpaymentResponseMobile responseJson = new CyberCardpaymentResponseMobile();
        if (mobileDeposit.getTotalAmount() != null && !mobileDeposit.getTotalAmount().isEmpty()
        ) {
            paymentRequest.setTotalAmount(Double.parseDouble(mobileDeposit.getTotalAmount()));
        } else {
            responseJson.setError_description("error.totalAmount.invalid");
            responseJson.setStatus(false);
            responseJson.setResponseCode("01");
            responseJson.setResponseMessage("error.totalAmount.invalid");
            return responseJson;
        }
        if (mobileDeposit.getPaymentAmount() != null && !mobileDeposit.getPaymentAmount().isEmpty()
        ) {
            paymentRequest.setPaymentAmount(Double.parseDouble(mobileDeposit.getPaymentAmount()));
        } else {
            responseJson.setError_description("error.payment.amount.invalid");
            responseJson.setStatus(false);
            responseJson.setResponseCode("01");
            responseJson.setResponseMessage("error.payment.amount.invalid");
            return responseJson;
        }
        if (mobileDeposit.getPhoneNumber() != null && !mobileDeposit.getPhoneNumber().isEmpty()) {
            paymentRequest.setIsdn(mobileDeposit.getPhoneNumber());
        } else {
            responseJson.setError_description("error.phoneNumber.invalid");
            responseJson.setStatus(false);
            responseJson.setResponseCode("01");
            responseJson.setResponseMessage("error.phoneNumber.invalid");
            return responseJson;
        }
        if (mobileDeposit.getFtthName() != null && !mobileDeposit.getFtthName().isEmpty()) {
            paymentRequest.setFtthName(mobileDeposit.getFtthName());
        } else {
            responseJson.setError_description("error.ftthName.invalid");
            responseJson.setStatus(false);
            responseJson.setResponseCode("01");
            responseJson.setResponseMessage("error.ftthName.invalid");
            return responseJson;
        }
        if (mobileDeposit.getPaymentMethod() != null && !mobileDeposit.getPaymentMethod().isEmpty()) {
            paymentRequest.setPaymentMethod(mobileDeposit.getPaymentMethod());
        } else {
            responseJson.setError_description("error.paymentMethod.invalid");
            responseJson.setStatus(false);
            responseJson.setResponseCode("01");
            responseJson.setResponseMessage("error.paymentMethod.invalid");
            return responseJson;
        }
        if (mobileDeposit.getFtthAccount() != null && !mobileDeposit.getFtthAccount().isEmpty()) {
            paymentRequest.setFtthAccount(mobileDeposit.getFtthAccount());
        } else {
            responseJson.setError_description("error.ftthAccount.invalid");
            responseJson.setStatus(false);
            responseJson.setResponseCode("01");
            responseJson.setResponseMessage("error.ftthAccount.invalid");
            return responseJson;
        }
        if (mobileDeposit.getAccountEmoney() != null && !mobileDeposit.getAccountEmoney().isEmpty()) {
            paymentRequest.setAccountEmoney(mobileDeposit.getAccountEmoney());
        }
        if (mobileDeposit.getFtthType() != null && !mobileDeposit.getFtthType().isEmpty()) {
            paymentRequest.setFtthType(mobileDeposit.getFtthType());
        } else {
            responseJson.setError_description("error.ftthType.invalid");
            responseJson.setStatus(false);
            responseJson.setResponseCode("01");
            responseJson.setResponseMessage("error.ftthType.invalid");
            return responseJson;
        }
        if (mobileDeposit.getContractIdInfo() != null && !mobileDeposit.getContractIdInfo().isEmpty()) {
            paymentRequest.setContractIdInfo(mobileDeposit.getContractIdInfo());
        } else {
            responseJson.setError_description("error.contractIdInfo.invalid");
            responseJson.setStatus(false);
            responseJson.setResponseCode("01");
            responseJson.setResponseMessage("error.contractIdInfo.invalid");
            return responseJson;
        }

        if (paymentRequest.getPaymentMethod() != null) {
            if (paymentRequest.getPaymentMethod().equalsIgnoreCase(PaymentTypeEnum.MASTERCARD) ||
                    paymentRequest.getPaymentMethod().equalsIgnoreCase(PaymentTypeEnum.VISA) ||
                    paymentRequest.getPaymentMethod().equalsIgnoreCase(PaymentTypeEnum.JCB) ||
                    paymentRequest.getPaymentMethod().equalsIgnoreCase(PaymentTypeEnum.ABAPAY)) {
                String cardType = "";
                String cardTypeResp = "";
                switch (paymentRequest.getPaymentMethod()) {
                    case PaymentTypeEnum.MASTERCARD:
                        cardType = CyberCardTypeEnum.MASTER_CARD.value;
                        cardTypeResp = "cards";
                        break;
                    case PaymentTypeEnum.VISA:
                        cardType = CyberCardTypeEnum.VISA.value;
                        cardTypeResp = "cards";
                        break;
                    case PaymentTypeEnum.JCB:
                        cardType = CyberCardTypeEnum.JCB.value;
                        cardTypeResp = "cards";
                        break;
                    case PaymentTypeEnum.ABAPAY:
                        cardType = CyberCardTypeEnum.ABAPAY.value;
                        cardTypeResp = "abapay";
                        break;
                }
                paymentRequest.setCardType(cardType);
                CyberCardPaymentResponse cyberCardPaymentResponse = cyberPaymentServiceCDBR.initPayment(paymentRequest);
                System.out.println(dtf.format(now) + " : Log response createTransactionABA " +
                        cyberCardPaymentResponse.toString());

                if (cyberCardPaymentResponse.getResponseCode() == null || !ResponseCodeEnum.TRANSACTION_COMPLETED.value
                        .equalsIgnoreCase(cyberCardPaymentResponse.getResponseCode())) {
                    responseJson.setError_field(ErrorFieldEnum.ISDN_FIELD);
                    responseJson.setError_description(utilHelper.convertErrorCodeToString(cyberCardPaymentResponse.getResponseMessage()));
                    responseJson.setStatus(false);
                    responseJson.setResponseCode("01");
                    responseJson.setResponseMessage(utilHelper.convertErrorCodeToString(cyberCardPaymentResponse.getResponseMessage()));
                    return responseJson;

                }
                if (cyberCardPaymentResponse.getRespcode() != null && !ResponseCodeEnum.TRANSACTION_PENDING.value
                        .equalsIgnoreCase(cyberCardPaymentResponse.getRespcode())) {
                    responseJson.setError_field(ErrorFieldEnum.PAYMENT_METHOD_FIELD);
                    responseJson.setError_description(utilHelper.convertErrorCodeToString(cyberCardPaymentResponse.getRespcode()));
                    responseJson.setStatus(false);
                    responseJson.setResponseCode("01");
                    responseJson.setResponseMessage(utilHelper.convertErrorCodeToString(cyberCardPaymentResponse.getRespcode()));
                    return responseJson;
                } else {
                    Cookie cookie = new Cookie("transIdAba", cyberCardPaymentResponse.getTxnid());
                    cookie.setPath("/" + SOURCE_PATH);
                    response.addCookie(cookie);
                    responseJson.setStatus(true);
                    responseJson.setResponseMessage(cyberCardPaymentResponse.getResponseMessage());
                    responseJson.setResponseCode(cyberCardPaymentResponse.getResponseCode());

                    try {
                        cyberCardPaymentResponse.setAmt_string(String.format("%.2f", cyberCardPaymentResponse.getAmt()));
                    } catch (Exception e) {

                    }
                    String urlForMobile = URL_API_MOBILE + "/transMobile?hash=" + utilHelper.encodeValue(cyberCardPaymentResponse.getHash()) +
                            "&tnxid=" + utilHelper.encodeValue(cyberCardPaymentResponse.getTxnid()) + "&amtString=" +
                            utilHelper.encodeValue(cyberCardPaymentResponse.getAmt_string()) + "&firstName=" +
                            utilHelper.encodeValue(cyberCardPaymentResponse.getFirstname()) + "&lastName=" +
                            utilHelper.encodeValue(cyberCardPaymentResponse.getLastname()) + "&phone=" +
                            utilHelper.encodeValue(cyberCardPaymentResponse.getPhone()) + "&email=" +
                            utilHelper.encodeValue(cyberCardPaymentResponse.getEmail()) + "&urlABA=" +
                            utilHelper.encodeValue(cyberCardPaymentResponse.getUrlABA()) + "&paymentType=" + utilHelper.encodeValue(cardTypeResp);
                    System.out.println(dtf.format(now) + " : Log urlForMobile cybercardpayment " + urlForMobile);
                    responseJson.setRedirectUrl(urlForMobile);
                    responseJson.setStatus(true);
                    return responseJson;
                }
            } else if (paymentRequest.getPaymentMethod().equalsIgnoreCase(PaymentTypeEnum.UNION)) {
                UnionPaymentResponse unionPaymentResponse = unionPaymentServiceImplCDBR.initPayment(paymentRequest);
                System.out.println("unionPaymentResponse : " + unionPaymentResponse.toString());
                if (unionPaymentResponse.getResponseCode() == null || !ResponseCodeEnum.TRANSACTION_COMPLETED.value
                        .equalsIgnoreCase(unionPaymentResponse.getResponseCode())) {
                    responseJson.setError_field(ErrorFieldEnum.ISDN_FIELD);
                    responseJson.setError_description(utilHelper.convertErrorCodeToString(unionPaymentResponse.getResponseMessage()));
                    responseJson.setStatus(false);
                    responseJson.setResponseCode("01");
                    responseJson.setResponseMessage(utilHelper.convertErrorCodeToString(unionPaymentResponse.getResponseMessage()));
                    return responseJson;
                }
                if (unionPaymentResponse.getRespcode() != null && !ResponseCodeEnum.TRANSACTION_PENDING.value
                        .equalsIgnoreCase(unionPaymentResponse.getRespcode())) {
                    responseJson.setError_field(ErrorFieldEnum.ISDN_FIELD);
                    responseJson.setError_description(utilHelper.convertErrorCodeToString(unionPaymentResponse.getRespcode()));
                    responseJson.setStatus(false);
                    responseJson.setResponseCode("01");
                    responseJson.setResponseMessage(utilHelper.convertErrorCodeToString(unionPaymentResponse.getRespcode()));
                    return responseJson;
                } else {
                    responseJson.setResponseMessage(unionPaymentResponse.getResponseMessage());
                    responseJson.setResponseCode(unionPaymentResponse.getResponseCode());
                    responseJson.setStatus(true);
                    System.out.println(dtf.format(now) + " : Log union payment " + unionPaymentResponse.toString());
                    responseJson.setRedirectUrl(unionPaymentResponse.getRedirectUrl());
                    return responseJson;
                }
            } else if (paymentRequest.getPaymentMethod().equalsIgnoreCase(PaymentTypeEnum.ALIPAY)) {

                AlipayPaymentResponse alipayPaymentResponse = aliPaymentServiceImplCDBR.initPayment(paymentRequest);
                System.out.println("alipayPaymentResponse : " + alipayPaymentResponse.toString());
                if (alipayPaymentResponse.getResponseCode() == null || !ResponseCodeEnum.TRANSACTION_COMPLETED.value
                        .equalsIgnoreCase(alipayPaymentResponse.getResponseCode())) {
                    responseJson.setError_field(ErrorFieldEnum.ISDN_FIELD);
                    responseJson.setError_description(utilHelper.convertErrorCodeToString(alipayPaymentResponse.getResponseMessage()));
                    responseJson.setStatus(false);
                    responseJson.setResponseCode("01");
                    responseJson.setResponseMessage(utilHelper.convertErrorCodeToString(alipayPaymentResponse.getResponseMessage()));
                    return responseJson;
                }
                if (alipayPaymentResponse.getRespcode() != null && !ResponseCodeEnum.TRANSACTION_PENDING.value
                        .equalsIgnoreCase(alipayPaymentResponse.getRespcode())) {
                    responseJson.setError_field(ErrorFieldEnum.PAYMENT_METHOD_FIELD);
                    responseJson.setError_description(utilHelper.convertErrorCodeToString(alipayPaymentResponse.getRespcode()));
                    responseJson.setStatus(false);
                    responseJson.setResponseCode("01");
                    responseJson.setResponseMessage(utilHelper.convertErrorCodeToString(alipayPaymentResponse.getRespcode()));
                    return responseJson;
                } else {
                    responseJson.setResponseMessage(alipayPaymentResponse.getResponseMessage());
                    responseJson.setResponseCode(alipayPaymentResponse.getResponseCode());
                    responseJson.setStatus(true);
                    System.out.println(dtf.format(now) + " : Log alipay payment " + alipayPaymentResponse.toString());
                    responseJson.setRedirectUrl(alipayPaymentResponse.getRedirectUrl());
                    return responseJson;
                }

            } else if (paymentRequest.getPaymentMethod().equalsIgnoreCase(PaymentTypeEnum.EMONEY)) {
                EmoneyPaymentResponse eMoneyPaymentResponse = emoneyPaymentServiceImplCDBR.initPayment(paymentRequest);
                if (eMoneyPaymentResponse.getResponseCode() == null || !ResponseCodeEnum.TRANSACTION_COMPLETED.value
                        .equalsIgnoreCase(eMoneyPaymentResponse.getResponseCode())) {
                    if (eMoneyPaymentResponse.getResponseMessage() != null) {
                        if (eMoneyPaymentResponse.getResponseMessage().equalsIgnoreCase("ERR_COMMON")
                                || eMoneyPaymentResponse.getResponseMessage().equalsIgnoreCase("ERR_MISSING_PARAMETERS")
                                || eMoneyPaymentResponse.getResponseMessage().equalsIgnoreCase("ERR_EMONEY_ACCOUNT_INVALID")
                                || eMoneyPaymentResponse.getResponseMessage().equalsIgnoreCase("ERR_MERCHANT_NOT_FOUND")
                                || eMoneyPaymentResponse.getResponseMessage().equalsIgnoreCase("ERR_MERCHANT_SERVICE_NOT_FOUND")
                                || eMoneyPaymentResponse.getResponseMessage().equalsIgnoreCase("ERR_HAVE_REQUIRED_PARAMETER_INVALID")
                                || eMoneyPaymentResponse.getResponseMessage().equalsIgnoreCase("ERR_TOTAL_AMOUNT_USD_KHR_NOT_MATCHES")
                                || eMoneyPaymentResponse.getResponseMessage().equalsIgnoreCase("ERR_TX_PAYMENT_TOKEN_ID_INVALID")
                                || eMoneyPaymentResponse.getResponseMessage().equalsIgnoreCase("ERR_INVOICE_NOT_FOUND")
                                || eMoneyPaymentResponse.getResponseMessage().equalsIgnoreCase("ERR_INVOICE_ALREADY_PAID_OR_CANCELLED")
                                || eMoneyPaymentResponse.getResponseMessage().equalsIgnoreCase("ERR_PHONE_NUMBER_INVALID")) {
                            responseJson.setError_field(ErrorFieldEnum.EMONEY_FIELD);
                            responseJson.setError_description(utilHelper.convertErrorCodeToString(eMoneyPaymentResponse.getResponseMessage()));
                            responseJson.setStatus(false);
                            responseJson.setResponseCode("01");
                            responseJson.setResponseMessage(utilHelper.convertErrorCodeToString(eMoneyPaymentResponse.getResponseMessage()));
                            return responseJson;
                        } else {
                            responseJson.setError_field(ErrorFieldEnum.EMONEY_FIELD);
                            responseJson.setError_description(utilHelper.convertErrorCodeToString(eMoneyPaymentResponse.getResponseMessage()));
                            responseJson.setStatus(false);
                            responseJson.setResponseCode("01");
                            responseJson.setResponseMessage(utilHelper.convertErrorCodeToString(eMoneyPaymentResponse.getResponseMessage()));
                            return responseJson;
                        }
                    } else {
                        responseJson.setError_field(ErrorFieldEnum.EMONEY_FIELD);
                        responseJson.setError_description(utilHelper.convertErrorCodeToString(eMoneyPaymentResponse.getResponseMessage()));
                        responseJson.setStatus(false);
                        responseJson.setResponseCode("01");
                        responseJson.setResponseMessage(utilHelper.convertErrorCodeToString(eMoneyPaymentResponse.getResponseMessage()));
                        return responseJson;
                    }
                }
                if (eMoneyPaymentResponse.getRespcode() != null && !ResponseCodeEnum.TRANSACTION_PENDING.value
                        .equalsIgnoreCase(eMoneyPaymentResponse.getRespcode())) {
                    responseJson.setError_field(ErrorFieldEnum.EMONEY_FIELD);
                    responseJson.setError_description(utilHelper.convertErrorCodeToString(eMoneyPaymentResponse.getRespcode()));
                    responseJson.setStatus(false);
                    responseJson.setResponseCode("01");
                    responseJson.setResponseMessage(utilHelper.convertErrorCodeToString(eMoneyPaymentResponse.getRespcode()));
                    return responseJson;
                } else {
                    System.out.println(dtf.format(now) + " : Log emoney payment " + eMoneyPaymentResponse.toString());
                    responseJson.setStatus(true);
                    responseJson.setResponseMessage(eMoneyPaymentResponse.getResponseMessage());
                    responseJson.setResponseCode(eMoneyPaymentResponse.getResponseCode());
                    responseJson.setRedirectUrl(eMoneyPaymentResponse.getRedirectUrl());
                    return responseJson;
                }
            } else if (paymentRequest.getPaymentMethod().equalsIgnoreCase(PaymentTypeEnum.WECHAT)) {

                WechatPaymentResponse wechatPaymentResponse = wechatPaymentServiceImplCDBR.initPayment(paymentRequest);
                wechatPaymentResponse.setIsdn(paymentRequest.getIsdn());
                if (wechatPaymentResponse.getResponseCode() == null || !ResponseCodeEnum.TRANSACTION_COMPLETED.value
                        .equalsIgnoreCase(wechatPaymentResponse.getResponseCode())) {
                    responseJson.setError_field(ErrorFieldEnum.PAYMENT_METHOD_FIELD);
                    responseJson.setError_description(utilHelper.convertErrorCodeToString(wechatPaymentResponse.getRespcode()));
                    responseJson.setStatus(false);
                    responseJson.setResponseCode("01");
                    responseJson.setResponseMessage(utilHelper.convertErrorCodeToString(wechatPaymentResponse.getRespcode()));
                    return responseJson;
                }
                if (wechatPaymentResponse.getRespcode() != null && !ResponseCodeEnum.TRANSACTION_PENDING.value
                        .equalsIgnoreCase(wechatPaymentResponse.getRespcode())) {
                    responseJson.setError_field(ErrorFieldEnum.PAYMENT_METHOD_FIELD);
                    responseJson.setError_description(utilHelper.convertErrorCodeToString(wechatPaymentResponse.getRespcode()));
                    responseJson.setResponseCode("01");
                    responseJson.setResponseMessage(utilHelper.convertErrorCodeToString(wechatPaymentResponse.getRespcode()));
                    responseJson.setStatus(false);
                } else {
                    String urlForMobile = URL_API_MOBILE + "/transWechatMobile?isdn=" + utilHelper.encodeValue(wechatPaymentResponse.getIsdn()) +
                            "&payCode=" + utilHelper.encodeValue(wechatPaymentResponse.getPayCode()) + "&nttrefid=" +
                            utilHelper.encodeValue(wechatPaymentResponse.getNttrefid()) + "&txnid=" +
                            utilHelper.encodeValue(wechatPaymentResponse.getTxnid());
                    System.out.println(dtf.format(now) + " : Log urlForMobile WechatPay " + urlForMobile);
                    responseJson.setResponseMessage(wechatPaymentResponse.getResponseMessage());
                    responseJson.setResponseCode(wechatPaymentResponse.getResponseCode());
                    responseJson.setRedirectUrl(urlForMobile);
                    responseJson.setStatus(true);
                }
                return responseJson;
            } else {
                responseJson.setResponseCode("01");
                responseJson.setResponseMessage(messageSource.getMessage("error.payment.type", null,
                        LocaleContextHolder.getLocaleContext().getLocale()));
                responseJson.setError_field(ErrorFieldEnum.PAYMENT_METHOD_FIELD);
                responseJson.setError_description(messageSource.getMessage("error.payment.type", null,
                        LocaleContextHolder.getLocaleContext().getLocale()));
                responseJson.setStatus(false);
                return responseJson;
            }

        } else {
            responseJson.setError_field(ErrorFieldEnum.PAYMENT_METHOD_FIELD);
            responseJson.setError_description(messageSource.getMessage("error.payment.type", null,
                    LocaleContextHolder.getLocaleContext().getLocale()));
            responseJson.setStatus(false);
            return responseJson;
        }
    }


}

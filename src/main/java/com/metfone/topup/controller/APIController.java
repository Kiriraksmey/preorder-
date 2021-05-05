package com.metfone.topup.controller;

import com.metfone.topup.helper.CallServiceHelper;
import com.metfone.topup.helper.Transformer;
import com.metfone.topup.helper.UtilHelper;
import com.metfone.topup.model.*;
import com.metfone.topup.model.MobileDeposit;
import com.metfone.topup.model.PaymentTopup;
import com.metfone.topup.model.acleda.AcledaPaymentResponse;
import com.metfone.topup.model.alipay.AlipayPaymentResponse;
import com.metfone.topup.model.cybercard.CyberCardPaymentResponse;
import com.metfone.topup.model.cybercard.CyberCardpaymentResponseMobile;
import com.metfone.topup.model.cybercard.PaymentRequestCyber;
import com.metfone.topup.model.emoney.EmoneyPaymentResponse;
import com.metfone.topup.model.payment.*;
import com.metfone.topup.model.topup.GetListPaymentResponse;
import com.metfone.topup.model.union.UnionPaymentResponse;
import com.metfone.topup.model.wechat.QRScanCheck;
import com.metfone.topup.model.wechat.WechatCheckResponse;
import com.metfone.topup.model.wechat.WechatPaymentResponse;
import com.metfone.topup.service.TopUpService;
import com.metfone.topup.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@RestController
public class APIController {

    @Autowired
    public TopUpService topUpService;

    @Autowired
    private CallServiceHelper callServiceHelper;

    @Autowired
    CyberPaymentServiceImpl cyberPaymentService;

    @Autowired
    AcledaPaymentServiceImpl acledaPaymentService;

    @Autowired
    UnionPaymentServiceImpl unionPaymentService;

    @Autowired
    EmoneyPaymentServiceImpl eMoneyPaymentService;

    @Autowired
    AliPaymentServiceImpl aliPaymentService;

    @Autowired
    WechatPaymentServiceImpl wechatPaymentService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    UtilHelper utilHelper;

    @Value("${source.path}")
    private String SOURCE_PATH;

    @Value("${url.api.mobile}")
    String URL_API_MOBILE;

    @RequestMapping(value = "/checkisdn",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CheckIsdnResponse checkIsdn(@RequestBody CheckIsdnRequest checkIsdnRequest, HttpServletResponse response) {
        CheckIsdn checkIsdn = new CheckIsdn();
        checkIsdn.setIsdn(checkIsdnRequest.getIsdn());
        checkIsdn.setAmoutTopup(checkIsdnRequest.getAmoutTopup());
        checkIsdn.setPaymentType(checkIsdnRequest.getPaymentType());
        CheckIsdnResponse result = topUpService.checkIsdn(checkIsdn);
        if ("00".equals(result.getResponseCode())) {
            Cookie cookie = new Cookie("phoneNumber", result.getIsdn());
            cookie.setPath("/" + SOURCE_PATH);
            response.addCookie(cookie);
            cookie = new Cookie("topupAmount", String.valueOf(result.getAmoutTopup()));
            cookie.setPath("/" + SOURCE_PATH);
            response.addCookie(cookie);
            cookie = new Cookie("paymentAmount", String.valueOf(result.getPaymentTopup()));
            cookie.setPath("/" + SOURCE_PATH);
            response.addCookie(cookie);
            cookie = new Cookie("paymentType", checkIsdn.getPaymentType());
            cookie.setPath("/" + SOURCE_PATH);
            response.addCookie(cookie);
            cookie = new Cookie("accountEmoney", checkIsdnRequest.getAccountEmoney());
            cookie.setPath("/" + SOURCE_PATH);
            response.addCookie(cookie);
        }
        return result;

    }

    @RequestMapping(value = "/getListPayment",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GetListPaymentResponse GetListPayment(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if ("0:0:0:0:0:0:0:1".equalsIgnoreCase(ip)) {
            ip = "127.0.0.1";
        }
        GetListPaymentResponse result = topUpService.getListPayment(ip);
        return result;

    }

    @RequestMapping(value = "/callback",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseCallbackNTT callbackNTT(@RequestBody RequestCallbackNTT requestCallbackNTT) {
        String result = topUpService.callbackNTT(requestCallbackNTT);
        ResponseCallbackNTT responseCallbackNTT = new ResponseCallbackNTT();
        responseCallbackNTT.setResponseCode("00");
        responseCallbackNTT.setResponseMessage("Success");
        return responseCallbackNTT;
    }

    @RequestMapping(value = "/callbackemoney",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseCallbackNTT callbackNTTEmoney(@RequestBody RequestCallbackNTT requestCallbackNTT) {
        String result = topUpService.callbackNTT(requestCallbackNTT);
        ResponseCallbackNTT responseCallbackNTT = new ResponseCallbackNTT();
        responseCallbackNTT.setResponseCode("00");
        responseCallbackNTT.setResponseMessage("Success");
        return responseCallbackNTT;
    }

    @RequestMapping(value = "/callbackAba",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseCallbackNTT callbackAba(@RequestBody RequestCallbackABA requestCallbackABA, HttpServletRequest request) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        System.out.println(dtf.format(now) + " : Request callback from ABA: " + requestCallbackABA.toString());
        String result = topUpService.callbackAba(requestCallbackABA);
        ResponseCallbackNTT responseCallbackNTT = new ResponseCallbackNTT();
        responseCallbackNTT.setResponseCode("00");
        responseCallbackNTT.setResponseMessage("Success");
        return responseCallbackNTT;
    }

    @RequestMapping(
            value = "/webhook",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ModelAndView processPostFormAction(NTTPaymentResponse payment) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        System.out.println(dtf.format(now) + " : NTTPaymentResponse " + payment.toString());

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("result", payment.getRespcode());
        return new ModelAndView("forward:/result");
    }

    @RequestMapping(value = "/checkQRScan",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WechatCheckResponse checkQRScan(@RequestBody QRScanCheck qrScanCheck) {
        return callServiceHelper.checkWechatResponse(qrScanCheck);
    }

    @RequestMapping(value = "/captcha",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void getCaptcha(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.setContentType("image/jpg");
        int iTotalChars = 3;
        int iHeight = 50;
        int iWidth = 90;
        Font fntStyle1 = new Font("Times New Roman", Font.BOLD, 35);
        Random randChars = new Random();
        String sImageCode = (Long.toString(Math.abs(randChars.nextLong()), 36)).substring(0, iTotalChars);
        BufferedImage biImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_INT_RGB);//
        Graphics2D g2dImage = (Graphics2D) biImage.getGraphics();
        g2dImage.setBackground(Color.WHITE);
        g2dImage.clearRect(0, 0, iWidth, iHeight);

        g2dImage.setFont(fntStyle1);
        for (int i = 0; i < iTotalChars; i++) {
            g2dImage.setColor(Color.BLACK);
            if (i % 2 == 0) {
                g2dImage.drawString(sImageCode.substring(i, i + 1), 25 * i, 30);
            } else {
                g2dImage.drawString(sImageCode.substring(i, i + 1), 25 * i, 41);
            }
        }
        HttpSession session = request.getSession();
        session.setAttribute("captcha_security", sImageCode);
        OutputStream osImage = response.getOutputStream();
        ImageIO.write(biImage, "jpeg", osImage);
        g2dImage.dispose();

    }


    @RequestMapping(value = "/createTransAba",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CyberCardPaymentResponse createTransAba(@RequestBody MobileDeposit mobileDeposit, HttpServletResponse response,
                                                   @SessionAttribute("captcha_security") @Nullable String captchaVerify) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        PaymentTopup paymentTopup = new PaymentTopup();
        CyberCardPaymentResponse responseJson = new CyberCardPaymentResponse();
        boolean valid = true;
        if (mobileDeposit.getTopupAmount() != null && !mobileDeposit.getTopupAmount().isEmpty()) {
            try {
                paymentTopup.setRefillTotal(Long.parseLong(mobileDeposit.getTopupAmount()));
            } catch (Exception e) {
                responseJson.setError_field(ErrorFieldEnum.TOPUP_AMOUNT_FIELD);
                responseJson.setError_description(messageSource.getMessage("error.topup.amount.before.submit", null,
                        LocaleContextHolder.getLocaleContext().getLocale()));
                responseJson.setStatus(false);
                return responseJson;
            }
        } else {
            responseJson.setError_field(ErrorFieldEnum.TOPUP_AMOUNT_FIELD);
            responseJson.setError_description(messageSource.getMessage("error.topup.amount.before.submit", null,
                    LocaleContextHolder.getLocaleContext().getLocale()));
            responseJson.setStatus(false);
            return responseJson;
        }

        if (mobileDeposit.getPaymentAmount() > 0) {
            paymentTopup.setRefillAmount(mobileDeposit.getPaymentAmount());
        }
        if (mobileDeposit.getPhoneNumber() != null && !mobileDeposit.getPhoneNumber().isEmpty()) {
            paymentTopup.setRefillIsdn(mobileDeposit.getPhoneNumber());
        }
        if (mobileDeposit.getPaymentMethod() != null && !mobileDeposit.getPaymentMethod().isEmpty()) {
            paymentTopup.setPaymentMethod(mobileDeposit.getPaymentMethod());
        }
        if (mobileDeposit.getAccountEmoney() != null && !mobileDeposit.getAccountEmoney().isEmpty()) {
            paymentTopup.setAccountEmoney(mobileDeposit.getAccountEmoney());
        }

        if (paymentTopup.getRefillIsdn() != null) {
            if (!paymentTopup.getRefillIsdn().startsWith("071") &&
                    !paymentTopup.getRefillIsdn().startsWith("097") &&
                    !paymentTopup.getRefillIsdn().startsWith("088") &&
                    !paymentTopup.getRefillIsdn().startsWith("068") &&
                    !paymentTopup.getRefillIsdn().startsWith("090") &&
                    !paymentTopup.getRefillIsdn().startsWith("067") &&
                    !paymentTopup.getRefillIsdn().startsWith("060") &&
                    !paymentTopup.getRefillIsdn().startsWith("031") &&
                    !paymentTopup.getRefillIsdn().startsWith("066") &&
                    !paymentTopup.getRefillIsdn().startsWith("71") &&
                    !paymentTopup.getRefillIsdn().startsWith("97") &&
                    !paymentTopup.getRefillIsdn().startsWith("88") &&
                    !paymentTopup.getRefillIsdn().startsWith("68") &&
                    !paymentTopup.getRefillIsdn().startsWith("90") &&
                    !paymentTopup.getRefillIsdn().startsWith("67") &&
                    !paymentTopup.getRefillIsdn().startsWith("60") &&
                    !paymentTopup.getRefillIsdn().startsWith("31") &&
                    !paymentTopup.getRefillIsdn().startsWith("66")) {
                responseJson.setError_field(ErrorFieldEnum.ISDN_FIELD);
                responseJson.setError_description(utilHelper.convertErrorCodeToString("error.isdn.system"));
                responseJson.setStatus(false);
                return responseJson;

            }
        }
        String verifyCaptcha = mobileDeposit.getCaptcha();
        if (captchaVerify != null && captchaVerify.equals(verifyCaptcha)) {
            valid = true;
        } else {
            valid = false;
        }
        if (!valid) {
            responseJson.setError_field(ErrorFieldEnum.CAPTCHA_FIELD);
            responseJson.setError_description(messageSource.getMessage("error.captcha.invalid", null,
                    LocaleContextHolder.getLocaleContext().getLocale()));
            responseJson.setStatus(false);
            return responseJson;
        } else {
            if (paymentTopup.getPaymentMethod() != null) {
                String cardType = "";
                switch (paymentTopup.getPaymentMethod()) {
                    case PaymentTypeEnum.MASTERCARD:
                        cardType = CyberCardTypeEnum.MASTER_CARD.value;
                        break;
                    case PaymentTypeEnum.VISA:
                        cardType = CyberCardTypeEnum.VISA.value;
                        break;
                    case PaymentTypeEnum.JCB:
                        cardType = CyberCardTypeEnum.JCB.value;
                        break;
                    case PaymentTypeEnum.ABAPAY:
                        cardType = CyberCardTypeEnum.ABAPAY.value;
                        break;
                }
                PaymentRequestCyber paymentRequestCyber = new PaymentRequestCyber();
                paymentRequestCyber.setCardType(cardType);
                paymentRequestCyber.setIsdn(paymentTopup.getRefillIsdn());
                paymentRequestCyber.setPaymentAmount(paymentTopup.getRefillAmount());
                paymentRequestCyber.setTopupAmount(paymentTopup.getRefillTotal());
                CyberCardPaymentResponse cyberCardPaymentResponse = cyberPaymentService.initPayment(paymentRequestCyber);
                System.out.println(dtf.format(now) + " : Log response createTransactionABA " +
                        cyberCardPaymentResponse.toString());
                if (cyberCardPaymentResponse.getResponseCode() == null || !ResponseCodeEnum.TRANSACTION_COMPLETED.value
                        .equalsIgnoreCase(cyberCardPaymentResponse.getResponseCode())) {
                    responseJson.setError_field(ErrorFieldEnum.ISDN_FIELD);
                    responseJson.setError_description(utilHelper.convertErrorCodeToString(cyberCardPaymentResponse.getResponseMessage()));
                    responseJson.setStatus(false);
                    return responseJson;

                }
                if (cyberCardPaymentResponse.getRespcode() != null && !ResponseCodeEnum.TRANSACTION_PENDING.value
                        .equalsIgnoreCase(cyberCardPaymentResponse.getRespcode())) {
                    responseJson.setError_field(ErrorFieldEnum.PAYMENT_METHOD_FIELD);
                    responseJson.setError_description(utilHelper.convertErrorCodeToString(cyberCardPaymentResponse.getRespcode()));
                    responseJson.setStatus(false);
                    return responseJson;
                } else {
                    Cookie cookie = new Cookie("transIdAba", cyberCardPaymentResponse.getTxnid());
                    cookie.setPath("/" + SOURCE_PATH);
                    response.addCookie(cookie);
                    try {
                        cyberCardPaymentResponse.setAmt_string(String.format("%.2f", cyberCardPaymentResponse.getAmt()));
                    } catch (Exception e) {

                    }

                    cyberCardPaymentResponse.setStatus(true);
                    return cyberCardPaymentResponse;
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

    @RequestMapping(value = "/createTransAcleda",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AcledaPaymentResponse createTransAcleda(@RequestBody MobileDeposit mobileDeposit, HttpServletResponse response,
                                                   @SessionAttribute("captcha_security") @Nullable String captchaVerify) {


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now) + " : Log mobileDeposit " +
                mobileDeposit.toString() + " captcha verify: " + captchaVerify);
        PaymentTopup paymentTopup = new PaymentTopup();
        AcledaPaymentResponse responseJson = new AcledaPaymentResponse();
        boolean valid = true;
        if (mobileDeposit.getTopupAmount() != null && !mobileDeposit.getTopupAmount().isEmpty()) {
            try {
                paymentTopup.setRefillTotal(Long.parseLong(mobileDeposit.getTopupAmount()));
            } catch (Exception e) {
                responseJson.setError_field(ErrorFieldEnum.TOPUP_AMOUNT_FIELD);
                responseJson.setError_description(messageSource.getMessage("error.topup.amount.before.submit", null,
                        LocaleContextHolder.getLocaleContext().getLocale()));
                responseJson.setStatus(false);
                return responseJson;
            }
        } else {
            responseJson.setError_field(ErrorFieldEnum.TOPUP_AMOUNT_FIELD);
            responseJson.setError_description(messageSource.getMessage("error.topup.amount.before.submit", null,
                    LocaleContextHolder.getLocaleContext().getLocale()));
            responseJson.setStatus(false);
            return responseJson;
        }

        if (mobileDeposit.getPaymentAmount() > 0) {
            paymentTopup.setRefillAmount(mobileDeposit.getPaymentAmount());
        }
        if (mobileDeposit.getPhoneNumber() != null && !mobileDeposit.getPhoneNumber().isEmpty()) {
            paymentTopup.setRefillIsdn(mobileDeposit.getPhoneNumber());
        }
        if (mobileDeposit.getPaymentMethod() != null && !mobileDeposit.getPaymentMethod().isEmpty()) {
            paymentTopup.setPaymentMethod(mobileDeposit.getPaymentMethod());
        }
        if (mobileDeposit.getAccountEmoney() != null && !mobileDeposit.getAccountEmoney().isEmpty()) {
            paymentTopup.setAccountEmoney(mobileDeposit.getAccountEmoney());
        }

        if (paymentTopup.getRefillIsdn() != null) {
            if (!paymentTopup.getRefillIsdn().startsWith("071") &&
                    !paymentTopup.getRefillIsdn().startsWith("097") &&
                    !paymentTopup.getRefillIsdn().startsWith("088") &&
                    !paymentTopup.getRefillIsdn().startsWith("068") &&
                    !paymentTopup.getRefillIsdn().startsWith("090") &&
                    !paymentTopup.getRefillIsdn().startsWith("067") &&
                    !paymentTopup.getRefillIsdn().startsWith("060") &&
                    !paymentTopup.getRefillIsdn().startsWith("031") &&
                    !paymentTopup.getRefillIsdn().startsWith("066") &&
                    !paymentTopup.getRefillIsdn().startsWith("71") &&
                    !paymentTopup.getRefillIsdn().startsWith("97") &&
                    !paymentTopup.getRefillIsdn().startsWith("88") &&
                    !paymentTopup.getRefillIsdn().startsWith("68") &&
                    !paymentTopup.getRefillIsdn().startsWith("90") &&
                    !paymentTopup.getRefillIsdn().startsWith("67") &&
                    !paymentTopup.getRefillIsdn().startsWith("60") &&
                    !paymentTopup.getRefillIsdn().startsWith("31") &&
                    !paymentTopup.getRefillIsdn().startsWith("66")) {
                responseJson.setError_field(ErrorFieldEnum.ISDN_FIELD);
                responseJson.setError_description(utilHelper.convertErrorCodeToString("error.isdn.system"));
                responseJson.setStatus(false);
                return responseJson;

            }
        }
        String verifyCaptcha = mobileDeposit.getCaptcha();
        if (captchaVerify != null && captchaVerify.equals(verifyCaptcha)) {
            valid = true;
        } else {
            valid = false;
        }
        if (!valid) {
            responseJson.setError_field(ErrorFieldEnum.CAPTCHA_FIELD);
            responseJson.setError_description(messageSource.getMessage("error.captcha.invalid", null,
                    LocaleContextHolder.getLocaleContext().getLocale()));
            responseJson.setStatus(false);
            return responseJson;
        } else {
            if (paymentTopup.getPaymentMethod() != null) {
                String cardType = CyberCardTypeEnum.ACLEDA.value;

                PaymentRequestCyber paymentRequestCyber = new PaymentRequestCyber();
                paymentRequestCyber.setCardType(cardType);
                paymentRequestCyber.setIsdn(paymentTopup.getRefillIsdn());
                paymentRequestCyber.setPaymentAmount(paymentTopup.getRefillAmount());
                paymentRequestCyber.setTopupAmount(paymentTopup.getRefillTotal());
                AcledaPaymentResponse acledaPaymentResponse = acledaPaymentService.initPayment(paymentRequestCyber);
                System.out.println(dtf.format(now) + " : Log response createTransactionAcleda " +
                        acledaPaymentResponse.toString());
                if (acledaPaymentResponse.getResponseCode() == null || !ResponseCodeEnum.TRANSACTION_COMPLETED.value
                        .equalsIgnoreCase(acledaPaymentResponse.getResponseCode())) {
                    responseJson.setError_field(ErrorFieldEnum.ISDN_FIELD);
                    responseJson.setError_description(utilHelper.convertErrorCodeToString(acledaPaymentResponse.getResponseMessage()));
                    responseJson.setStatus(false);
                    return responseJson;

                }
                if (acledaPaymentResponse.getRespcode() != null && !ResponseCodeEnum.TRANSACTION_PENDING.value
                        .equalsIgnoreCase(acledaPaymentResponse.getRespcode())) {
                    responseJson.setError_field(ErrorFieldEnum.PAYMENT_METHOD_FIELD);
                    responseJson.setError_description(utilHelper.convertErrorCodeToString(acledaPaymentResponse.getRespcode()));
                    responseJson.setStatus(false);
                    return responseJson;
                } else {
                    Cookie cookie = new Cookie("transIdAcleda", acledaPaymentResponse.getTxnid());
                    cookie.setPath("/" + SOURCE_PATH);
                    response.addCookie(cookie);
                    try {
                        acledaPaymentResponse.setAmt_string(String.format("%.2f", acledaPaymentResponse.getAmt()));
                    } catch (Exception e) {
                        System.out.println(dtf.format(now) + " : Exception format amt createAcleda transaction: " +
                                acledaPaymentResponse.toString());
                    }

                    acledaPaymentResponse.setStatus(true);
                    return acledaPaymentResponse;
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

    @RequestMapping(value = "/createTransMobile",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CyberCardpaymentResponseMobile createTransMobile(@RequestBody DepositForMobile mobileDeposit,
                                                            HttpServletResponse response) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        PaymentTopup paymentTopup = new PaymentTopup();

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmsss");
        String strDate = dateFormat.format(date);

        CyberCardpaymentResponseMobile responseJson = new CyberCardpaymentResponseMobile();
        CheckIsdn checkIsdn = new CheckIsdn();
        checkIsdn.setIsdn(mobileDeposit.getPhoneNumber());
        checkIsdn.setPaymentType(mobileDeposit.getPaymentMethod());
        if (mobileDeposit.getTopupAmount() != null && !mobileDeposit.getTopupAmount().isEmpty()) {
            try {
                checkIsdn.setAmoutTopup(Long.parseLong(mobileDeposit.getTopupAmount()));
                paymentTopup.setRefillTotal(Long.parseLong(mobileDeposit.getTopupAmount()));
            } catch (Exception e) {
                responseJson.setError_field(ErrorFieldEnum.TOPUP_AMOUNT_FIELD);
                responseJson.setError_description(messageSource.getMessage("error.topup.amount.before.submit", null,
                        LocaleContextHolder.getLocaleContext().getLocale()));
                responseJson.setStatus(false);
                return responseJson;
            }
        } else {
            responseJson.setError_field(ErrorFieldEnum.TOPUP_AMOUNT_FIELD);
            responseJson.setError_description(messageSource.getMessage("error.topup.amount.before.submit", null,
                    LocaleContextHolder.getLocaleContext().getLocale()));
            responseJson.setStatus(false);
            return responseJson;
        }
        CheckIsdnResponse checkIsdnResponse = topUpService.checkIsdn(checkIsdn);
        System.out.println("CheckIsdn response " + checkIsdnResponse.toString());

        if (checkIsdnResponse.getPaymentTopup() > 0) {
            paymentTopup.setRefillAmount(checkIsdnResponse.getPaymentTopup());
        }
        if (mobileDeposit.getPhoneNumber() != null && !mobileDeposit.getPhoneNumber().isEmpty()) {
            paymentTopup.setRefillIsdn(mobileDeposit.getPhoneNumber());
//            paymentTopup.setAccountEmoney(mobileDeposit.getPhoneNumber());
        }
        if (mobileDeposit.getPaymentMethod() != null && !mobileDeposit.getPaymentMethod().isEmpty()) {
            paymentTopup.setPaymentMethod(mobileDeposit.getPaymentMethod());
        }
        if (mobileDeposit.getAccountEmoney() != null && !mobileDeposit.getAccountEmoney().isEmpty()) {
            paymentTopup.setAccountEmoney(mobileDeposit.getAccountEmoney());
        }

        if (paymentTopup.getRefillIsdn() != null) {
            if (!paymentTopup.getRefillIsdn().startsWith("071") &&
                    !paymentTopup.getRefillIsdn().startsWith("097") &&
                    !paymentTopup.getRefillIsdn().startsWith("088") &&
                    !paymentTopup.getRefillIsdn().startsWith("068") &&
                    !paymentTopup.getRefillIsdn().startsWith("090") &&
                    !paymentTopup.getRefillIsdn().startsWith("067") &&
                    !paymentTopup.getRefillIsdn().startsWith("060") &&
                    !paymentTopup.getRefillIsdn().startsWith("031") &&
                    !paymentTopup.getRefillIsdn().startsWith("066") &&
                    !paymentTopup.getRefillIsdn().startsWith("71") &&
                    !paymentTopup.getRefillIsdn().startsWith("97") &&
                    !paymentTopup.getRefillIsdn().startsWith("88") &&
                    !paymentTopup.getRefillIsdn().startsWith("68") &&
                    !paymentTopup.getRefillIsdn().startsWith("90") &&
                    !paymentTopup.getRefillIsdn().startsWith("67") &&
                    !paymentTopup.getRefillIsdn().startsWith("60") &&
                    !paymentTopup.getRefillIsdn().startsWith("31") &&
                    !paymentTopup.getRefillIsdn().startsWith("66")) {
                responseJson.setError_field(ErrorFieldEnum.ISDN_FIELD);
                responseJson.setError_description(utilHelper.convertErrorCodeToString("error.isdn.system"));
                responseJson.setStatus(false);
                return responseJson;

            }
        }
        if (paymentTopup.getPaymentMethod() != null) {
            if (paymentTopup.getPaymentMethod().equalsIgnoreCase(PaymentTypeEnum.MASTERCARD) ||
                    paymentTopup.getPaymentMethod().equalsIgnoreCase(PaymentTypeEnum.VISA) ||
                    paymentTopup.getPaymentMethod().equalsIgnoreCase(PaymentTypeEnum.JCB) ||
                    paymentTopup.getPaymentMethod().equalsIgnoreCase(PaymentTypeEnum.ABAPAY)) {
                String cardType = "";
                String cardTypeResp = "";
                switch (paymentTopup.getPaymentMethod()) {
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
                PaymentRequestCyber paymentRequestCyber = new PaymentRequestCyber();
                paymentRequestCyber.setCardType(cardType);
                paymentRequestCyber.setIsdn(paymentTopup.getRefillIsdn());
                paymentRequestCyber.setPaymentAmount(paymentTopup.getRefillAmount());
                paymentRequestCyber.setTopupAmount(paymentTopup.getRefillTotal());
                CyberCardPaymentResponse cyberCardPaymentResponse = cyberPaymentService.initPayment(paymentRequestCyber);
                System.out.println(dtf.format(now) + " : Log response createTransactionABA " +
                        cyberCardPaymentResponse.toString());

                if (cyberCardPaymentResponse.getResponseCode() == null || !ResponseCodeEnum.TRANSACTION_COMPLETED.value
                        .equalsIgnoreCase(cyberCardPaymentResponse.getResponseCode())) {
                    responseJson.setError_field(ErrorFieldEnum.ISDN_FIELD);
                    responseJson.setError_description(utilHelper.convertErrorCodeToString(cyberCardPaymentResponse.getResponseMessage()));
                    responseJson.setStatus(false);
                    return responseJson;

                }
                if (cyberCardPaymentResponse.getRespcode() != null && !ResponseCodeEnum.TRANSACTION_PENDING.value
                        .equalsIgnoreCase(cyberCardPaymentResponse.getRespcode())) {
                    responseJson.setError_field(ErrorFieldEnum.PAYMENT_METHOD_FIELD);
                    responseJson.setError_description(utilHelper.convertErrorCodeToString(cyberCardPaymentResponse.getRespcode()));
                    responseJson.setStatus(false);
                    return responseJson;
                } else {
                    Cookie cookie = new Cookie("transIdAba", cyberCardPaymentResponse.getTxnid());
                    cookie.setPath("/" + SOURCE_PATH);
                    response.addCookie(cookie);
                    responseJson.setStatus(true);

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
            } else if (paymentTopup.getPaymentMethod().equalsIgnoreCase(PaymentTypeEnum.UNION)) {
                UnionPaymentResponse unionPaymentResponse = unionPaymentService.initPayment(Transformer.transform(paymentTopup, strDate));
                System.out.println("unionPaymentResponse : " + unionPaymentResponse.toString());
                if (unionPaymentResponse.getResponseCode() == null || !ResponseCodeEnum.TRANSACTION_COMPLETED.value
                        .equalsIgnoreCase(unionPaymentResponse.getResponseCode())) {
                    responseJson.setError_field(ErrorFieldEnum.ISDN_FIELD);
                    responseJson.setError_description(utilHelper.convertErrorCodeToString(unionPaymentResponse.getResponseMessage()));
                    responseJson.setStatus(false);
                    return responseJson;
                }
                if (unionPaymentResponse.getRespcode() != null && !ResponseCodeEnum.TRANSACTION_PENDING.value
                        .equalsIgnoreCase(unionPaymentResponse.getRespcode())) {
                    responseJson.setError_field(ErrorFieldEnum.ISDN_FIELD);
                    responseJson.setError_description(utilHelper.convertErrorCodeToString(unionPaymentResponse.getRespcode()));
                    responseJson.setStatus(false);
                    return responseJson;
                } else {
                    responseJson.setStatus(true);
                    System.out.println(dtf.format(now) + " : Log union payment " + unionPaymentResponse.toString());
                    responseJson.setRedirectUrl(unionPaymentResponse.getRedirectUrl());
                    return responseJson;
                }
            } else if (paymentTopup.getPaymentMethod().equalsIgnoreCase(PaymentTypeEnum.ALIPAY)) {

                AlipayPaymentResponse alipayPaymentResponse = aliPaymentService.initPayment(Transformer.transform(paymentTopup, strDate));
                System.out.println("alipayPaymentResponse : " + alipayPaymentResponse.toString());
                if (alipayPaymentResponse.getResponseCode() == null || !ResponseCodeEnum.TRANSACTION_COMPLETED.value
                        .equalsIgnoreCase(alipayPaymentResponse.getResponseCode())) {
                    responseJson.setError_field(ErrorFieldEnum.ISDN_FIELD);
                    responseJson.setError_description(utilHelper.convertErrorCodeToString(alipayPaymentResponse.getResponseMessage()));
                    responseJson.setStatus(false);
                    return responseJson;
                }
                if (alipayPaymentResponse.getRespcode() != null && !ResponseCodeEnum.TRANSACTION_PENDING.value
                        .equalsIgnoreCase(alipayPaymentResponse.getRespcode())) {
                    responseJson.setError_field(ErrorFieldEnum.PAYMENT_METHOD_FIELD);
                    responseJson.setError_description(utilHelper.convertErrorCodeToString(alipayPaymentResponse.getRespcode()));
                    responseJson.setStatus(false);
                    return responseJson;
                } else {
                    responseJson.setStatus(true);
                    System.out.println(dtf.format(now) + " : Log alipay payment " + alipayPaymentResponse.toString());
                    responseJson.setRedirectUrl(alipayPaymentResponse.getRedirectUrl());
                    return responseJson;
                }

            } else if (paymentTopup.getPaymentMethod().equalsIgnoreCase(PaymentTypeEnum.EMONEY)) {
                EmoneyPaymentResponse eMoneyPaymentResponse = eMoneyPaymentService.initPayment(Transformer.transformEmoney(paymentTopup, strDate));
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
                            return responseJson;
                        } else {
                            responseJson.setError_field(ErrorFieldEnum.EMONEY_FIELD);
                            responseJson.setError_description(utilHelper.convertErrorCodeToString(eMoneyPaymentResponse.getResponseMessage()));
                            responseJson.setStatus(false);
                            return responseJson;
                        }
                    } else {
                        responseJson.setError_field(ErrorFieldEnum.EMONEY_FIELD);
                        responseJson.setError_description(utilHelper.convertErrorCodeToString(eMoneyPaymentResponse.getResponseMessage()));
                        responseJson.setStatus(false);
                        return responseJson;
                    }
                }
                if (eMoneyPaymentResponse.getRespcode() != null && !ResponseCodeEnum.TRANSACTION_PENDING.value
                        .equalsIgnoreCase(eMoneyPaymentResponse.getRespcode())) {
                    responseJson.setError_field(ErrorFieldEnum.EMONEY_FIELD);
                    responseJson.setError_description(utilHelper.convertErrorCodeToString(eMoneyPaymentResponse.getRespcode()));
                    responseJson.setStatus(false);
                    return responseJson;
                } else {
                    System.out.println(dtf.format(now) + " : Log emoney payment " + eMoneyPaymentResponse.toString());
                    responseJson.setStatus(true);
                    responseJson.setRedirectUrl(eMoneyPaymentResponse.getRedirectUrl());
                    return responseJson;
                }
            } else if (paymentTopup.getPaymentMethod().equalsIgnoreCase(PaymentTypeEnum.WECHAT)) {
                WechatPaymentResponse wechatPaymentResponse = wechatPaymentService.initPayment(Transformer.transform(paymentTopup, strDate));
                wechatPaymentResponse.setIsdn(paymentTopup.getRefillIsdn());
                if (wechatPaymentResponse.getResponseCode() == null || !ResponseCodeEnum.TRANSACTION_COMPLETED.value
                        .equalsIgnoreCase(wechatPaymentResponse.getResponseCode())) {
                    responseJson.setError_field(ErrorFieldEnum.PAYMENT_METHOD_FIELD);
                    responseJson.setError_description(utilHelper.convertErrorCodeToString(wechatPaymentResponse.getRespcode()));
                    responseJson.setStatus(false);
                    return responseJson;
                }
                if (wechatPaymentResponse.getRespcode() != null && !ResponseCodeEnum.TRANSACTION_PENDING.value
                        .equalsIgnoreCase(wechatPaymentResponse.getRespcode())) {
                    responseJson.setError_field(ErrorFieldEnum.PAYMENT_METHOD_FIELD);
                    responseJson.setError_description(utilHelper.convertErrorCodeToString(wechatPaymentResponse.getRespcode()));
                    responseJson.setStatus(false);
                } else {
                    String urlForMobile = URL_API_MOBILE + "/transWechatMobile?isdn=" + utilHelper.encodeValue(wechatPaymentResponse.getIsdn()) +
                            "&payCode=" + utilHelper.encodeValue(wechatPaymentResponse.getPayCode()) + "&nttrefid=" +
                            utilHelper.encodeValue(wechatPaymentResponse.getNttrefid()) + "&txnid=" +
                            utilHelper.encodeValue(wechatPaymentResponse.getTxnid());
                    System.out.println(dtf.format(now) + " : Log urlForMobile WechatPay " + urlForMobile);
                    responseJson.setRedirectUrl(urlForMobile);
                    responseJson.setStatus(true);
                }
                return responseJson;
            } else {
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

    @RequestMapping(value = "/paymentWing",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String sendRest(HttpServletRequest request) {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Referer", request.getRequestURL().toString());
        headers.set("Referer", "http://103.27.237.84:8686/");
        HttpEntity<String> entity = new HttpEntity<String>("{\"username\":\"online.metfone\",\"rest_api_key\":\"afde53a3f27ae00637f508766463cd90e68e6d6d5ec20a202e94c52824f66aed\",\"bill_till_rbtn\":\"0\",\"bill_till_number\":\"5201\",\"sandbox\":\"1\",\"return_url\":\"url/test\",\"remark\":\"test\",\"amount\":10}", headers);
        return rest.exchange("https://stageonline.wingmoney.com/wingonlinesdk/", HttpMethod.POST, entity, String.class).getBody();
    }
}

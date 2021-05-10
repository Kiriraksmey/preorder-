package com.metfone.topup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metfone.topup.helper.CallServiceHelper;
import com.metfone.topup.helper.Transformer;
import com.metfone.topup.helper.UtilHelper;
import com.metfone.topup.model.*;
import com.metfone.topup.model.Wing.GetInfoWingResponse;
import com.metfone.topup.model.Wing.ResponseCallbackWing;
import com.metfone.topup.model.alipay.AlipayPaymentResponse;
import com.metfone.topup.model.cybercard.CyberCardPaymentResponse;
import com.metfone.topup.model.cybercard.PaymentRequestCyber;
import com.metfone.topup.model.emoney.EmoneyPaymentResponse;
import com.metfone.topup.model.emoney.GetInfoCustomerRequest;
import com.metfone.topup.model.emoney.GetInfoCustomerResponse;
import com.metfone.topup.model.emoney.ReturnDataEmoney;
import com.metfone.topup.model.payment.CyberCardTypeEnum;
import com.metfone.topup.model.payment.PaymentTypeEnum;
import com.metfone.topup.model.payment.ResponseCodeEnum;
import com.metfone.topup.model.union.UnionPaymentResponse;
import com.metfone.topup.model.wechat.WechatPaymentResponse;
import com.metfone.topup.service.TopUpService;
import com.metfone.topup.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.repository.query.Param;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@Controller
public class TopUpController {

    @Autowired
    public TopUpService topUpService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    private CallServiceHelper callServiceHelper;

    @Value("${source.path}")
    private String SOURCE_PATH;

    @Autowired
    AliPaymentServiceImpl aliPaymentService;

    @Autowired
    CyberPaymentServiceImpl cyberPaymentService;

    @Autowired
    WechatPaymentServiceImpl wechatPaymentService;

    @Autowired
    UnionPaymentServiceImpl unionPaymentService;

    @Autowired
    EmoneyPaymentServiceImpl eMoneyPaymentService;

    @Autowired
    UtilHelper utilHelper;

    @Autowired
    WingPaymentServiceimpl wingPaymentService;

    @GetMapping("/")
    public String greeting(Model model, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("mobileDeposit", new MobileDeposit());
        handleLogOutResponse(request, response);
        return "index";
    }

    @GetMapping("/topup")
    public ModelAndView doTopUp(@CookieValue(value = "phoneNumber") @Nullable String phoneNumber,
                                @CookieValue(value = "topupAmount") @Nullable String topupAmount,
                                @CookieValue(value = "paymentAmount") @Nullable String paymentAmount,
                                @CookieValue(value = "paymentType") @Nullable String paymentType,
                                @CookieValue(value = "accountEmoney") @Nullable String accountEmoney,
                                ModelMap model) {
        if (phoneNumber == null || phoneNumber.isEmpty() ||
                topupAmount == null || topupAmount.isEmpty() ||
                paymentAmount == null || paymentAmount.isEmpty()) {
            model.addAttribute("mobileDeposit", new MobileDeposit());
            return new ModelAndView("redirect:/", model);
        }

        MobileDeposit mobileDeposit = new MobileDeposit();
        if (Long.parseLong(topupAmount) > 0) {
            mobileDeposit.setTopupAmount(topupAmount);
        }
        if (Float.parseFloat(paymentAmount) > 0) {
            mobileDeposit.setPaymentAmount(Float.parseFloat(paymentAmount));
        }
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            mobileDeposit.setPhoneNumber(phoneNumber);
        }
        if (paymentType != null && !paymentType.isEmpty()) {
            mobileDeposit.setPaymentMethod(paymentType);
        }
        if (accountEmoney != null && !accountEmoney.isEmpty()) {
            mobileDeposit.setAccountEmoney(accountEmoney);
        }

        //todo

        model.addAttribute("mobileDeposit", mobileDeposit);
        return new ModelAndView("index", model);
    }

    @PostMapping("/topup")
    public ModelAndView doTopUp(@ModelAttribute MobileDeposit mobileDeposit, ModelMap model,
                                @SessionAttribute("captcha_security") @Nullable String captchaVerify,
                                RedirectAttributes redirectAttributes, HttpServletResponse response) {
        PaymentTopup paymentTopup = new PaymentTopup();
        boolean valid = true;
        if (mobileDeposit.getTopupAmount() != null && !mobileDeposit.getTopupAmount().isEmpty()) {
            try {
                paymentTopup.setRefillTotal(Long.parseLong(mobileDeposit.getTopupAmount()));
            } catch (Exception e) {
                model.addAttribute("mobileDeposit", mobileDeposit);
                model.addAttribute("errorTopupAmount",
                        messageSource.getMessage("error.topup.amount.before.submit", null,
                                LocaleContextHolder.getLocaleContext().getLocale()));
                return new ModelAndView("index", model);
            }
        } else {
            model.addAttribute("mobileDeposit", mobileDeposit);
            model.addAttribute("errorTopupAmount",
                    messageSource.getMessage("error.topup.amount.before.submit", null,
                            LocaleContextHolder.getLocaleContext().getLocale()));
            return new ModelAndView("index", model);
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
                mobileDeposit.setCaptcha("");
                model.addAttribute("mobileDeposit", mobileDeposit);
                model.addAttribute("errorPhoneNumber",
                        utilHelper.convertErrorCodeToString("error.isdn.system"));
                return new ModelAndView("index", model);
            }
        }

        String verifyCaptcha = mobileDeposit.getCaptcha();
        if (captchaVerify != null && captchaVerify.equals(verifyCaptcha)) {
            valid = true;
        } else {
            valid = false;
        }
        if (!valid) {
            mobileDeposit.setCaptcha("");
            model.addAttribute("mobileDeposit", mobileDeposit);
            model.addAttribute("errorCaptchaUpdate",
                    messageSource.getMessage("error.captcha.invalid", null,
                            LocaleContextHolder.getLocaleContext().getLocale()));
            return new ModelAndView("index", model);
        } else {
            if (paymentTopup.getPaymentMethod() != null) {
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmsss");
                String strDate = dateFormat.format(date);
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
                switch (paymentTopup.getPaymentMethod()) {
                    case PaymentTypeEnum.MASTERCARD:
                    case PaymentTypeEnum.VISA:
                    case PaymentTypeEnum.JCB:
                    case PaymentTypeEnum.ABAPAY:
                        PaymentRequestCyber paymentRequestCyber = new PaymentRequestCyber();
                        paymentRequestCyber.setCardType(cardType);
                        paymentRequestCyber.setIsdn(paymentTopup.getRefillIsdn());
                        paymentRequestCyber.setPaymentAmount(paymentTopup.getRefillAmount());
                        paymentRequestCyber.setTopupAmount(paymentTopup.getRefillTotal());
                        CyberCardPaymentResponse cyberCardPaymentResponse = cyberPaymentService.initPayment(paymentRequestCyber);
                        if (cyberCardPaymentResponse.getResponseCode() == null || !ResponseCodeEnum.TRANSACTION_COMPLETED.value
                                .equalsIgnoreCase(cyberCardPaymentResponse.getResponseCode())) {
                            mobileDeposit.setCaptcha("");
                            model.addAttribute("mobileDeposit", mobileDeposit);
                            model.addAttribute("errorPhoneNumber",
                                    utilHelper.convertErrorCodeToString(cyberCardPaymentResponse.getResponseMessage()));
                            return new ModelAndView("index", model);
                        }
                        if (cyberCardPaymentResponse.getRespcode() != null && !ResponseCodeEnum.TRANSACTION_PENDING.value
                                .equalsIgnoreCase(cyberCardPaymentResponse.getRespcode())) {
                            mobileDeposit.setCaptcha("");
                            model.addAttribute("mobileDeposit", mobileDeposit);
                            model.addAttribute("errorPaymentType",
                                    utilHelper.convertErrorCodeToString(cyberCardPaymentResponse.getRespcode()));
                            return new ModelAndView("index", model);
                        } else {
                            Cookie cookie = new Cookie("transIdAba", cyberCardPaymentResponse.getTxnid());
                            cookie.setPath("/" + SOURCE_PATH);
                            response.addCookie(cookie);
                            return new ModelAndView("initPaymentABA", cyberPaymentService.setupPayment(cyberCardPaymentResponse));
                        }
                    case PaymentTypeEnum.ALIPAY:
                        AlipayPaymentResponse alipayPaymentResponse = aliPaymentService.initPayment(Transformer.transform(paymentTopup, strDate));
                        if (alipayPaymentResponse.getResponseCode() == null || !ResponseCodeEnum.TRANSACTION_COMPLETED.value
                                .equalsIgnoreCase(alipayPaymentResponse.getResponseCode())) {
                            mobileDeposit.setCaptcha("");
                            model.addAttribute("mobileDeposit", mobileDeposit);
                            model.addAttribute("errorPhoneNumber",
                                    utilHelper.convertErrorCodeToString(alipayPaymentResponse.getResponseMessage()));
                            return new ModelAndView("index", model);
                        }
                        if (alipayPaymentResponse.getRespcode() != null && !ResponseCodeEnum.TRANSACTION_PENDING.value
                                .equalsIgnoreCase(alipayPaymentResponse.getRespcode())) {
                            mobileDeposit.setCaptcha("");
                            model.addAttribute("mobileDeposit", mobileDeposit);
                            model.addAttribute("errorPaymentType",
                                    utilHelper.convertErrorCodeToString(alipayPaymentResponse.getRespcode()));
                            return new ModelAndView("index", model);
                        } else {
                            return new ModelAndView(aliPaymentService.setupPayment(alipayPaymentResponse));
                        }
                    case PaymentTypeEnum.UNION:
                        UnionPaymentResponse unionPaymentResponse = unionPaymentService.initPayment(Transformer.transform(paymentTopup, strDate));
                        if (unionPaymentResponse.getResponseCode() == null || !ResponseCodeEnum.TRANSACTION_COMPLETED.value
                                .equalsIgnoreCase(unionPaymentResponse.getResponseCode())) {
                            mobileDeposit.setCaptcha("");
                            model.addAttribute("mobileDeposit", mobileDeposit);
                            model.addAttribute("errorPhoneNumber",
                                    utilHelper.convertErrorCodeToString(unionPaymentResponse.getResponseMessage()));
                            return new ModelAndView("index", model);
                        }
                        if (unionPaymentResponse.getRespcode() != null && !ResponseCodeEnum.TRANSACTION_PENDING.value
                                .equalsIgnoreCase(unionPaymentResponse.getRespcode())) {
                            mobileDeposit.setCaptcha("");
                            model.addAttribute("mobileDeposit", mobileDeposit);
                            model.addAttribute("errorPaymentType",
                                    utilHelper.convertErrorCodeToString(unionPaymentResponse.getRespcode()));
                            return new ModelAndView("index", model);
                        } else {
                            return new ModelAndView(unionPaymentService.setupPayment(unionPaymentResponse));
                        }

                    case PaymentTypeEnum.EMONEY:
                        EmoneyPaymentResponse eMoneyPaymentResponse = eMoneyPaymentService.initPayment(Transformer.transformEmoney(paymentTopup, strDate));
                        if (eMoneyPaymentResponse.getResponseCode() == null || !ResponseCodeEnum.TRANSACTION_COMPLETED.value
                                .equalsIgnoreCase(eMoneyPaymentResponse.getResponseCode())) {
                            mobileDeposit.setCaptcha("");
                            model.addAttribute("mobileDeposit", mobileDeposit);
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
                                    model.addAttribute("errorAccountEmoney",
                                            utilHelper.convertErrorCodeToString(eMoneyPaymentResponse.getResponseMessage()));
                                } else {
                                    model.addAttribute("errorPhoneNumber",
                                            utilHelper.convertErrorCodeToString(eMoneyPaymentResponse.getResponseMessage()));
                                }
                            } else {
                                model.addAttribute("errorPhoneNumber",
                                        utilHelper.convertErrorCodeToString(eMoneyPaymentResponse.getResponseMessage()));
                            }

                            return new ModelAndView("index", model);
                        }
                        if (eMoneyPaymentResponse.getRespcode() != null && !ResponseCodeEnum.TRANSACTION_PENDING.value
                                .equalsIgnoreCase(eMoneyPaymentResponse.getRespcode())) {
                            mobileDeposit.setCaptcha("");
                            model.addAttribute("mobileDeposit", mobileDeposit);
                            model.addAttribute("errorAccountEmoney",
                                    utilHelper.convertErrorCodeToString(eMoneyPaymentResponse.getRespcode()));
                            return new ModelAndView("index", model);
                        } else {
                            return new ModelAndView(eMoneyPaymentService.setupPayment(eMoneyPaymentResponse));
                        }

                    case PaymentTypeEnum.WECHAT:
                        WechatPaymentResponse wechatPaymentResponse = wechatPaymentService.initPayment(Transformer.transform(paymentTopup, strDate));
                        wechatPaymentResponse.setIsdn(paymentTopup.getRefillIsdn());
                        if (wechatPaymentResponse.getResponseCode() == null || !ResponseCodeEnum.TRANSACTION_COMPLETED.value
                                .equalsIgnoreCase(wechatPaymentResponse.getResponseCode())) {
                            mobileDeposit.setCaptcha("");
                            model.addAttribute("mobileDeposit", mobileDeposit);
                            model.addAttribute("errorPhoneNumber",
                                    utilHelper.convertErrorCodeToString(wechatPaymentResponse.getResponseMessage()));
                            return new ModelAndView("index", model);
                        }
                        if (wechatPaymentResponse.getRespcode() != null && !ResponseCodeEnum.TRANSACTION_PENDING.value
                                .equalsIgnoreCase(wechatPaymentResponse.getRespcode())) {
                            mobileDeposit.setCaptcha("");
                            model.addAttribute("mobileDeposit", mobileDeposit);
                            model.addAttribute("errorPaymentType",
                                    utilHelper.convertErrorCodeToString(wechatPaymentResponse.getRespcode()));
                            return new ModelAndView("index", model);
                        } else {
                            return new ModelAndView("wechat_payment", wechatPaymentService.setupPayment(wechatPaymentResponse));
                        }

                    case PaymentTypeEnum.WING:
                        GetInfoWingResponse getInfoWingResponse = wingPaymentService.initPayment(Transformer.transformEmoney(paymentTopup, strDate));
                        if (getInfoWingResponse.getResponseCode() == null || !ResponseCodeEnum.TRANSACTION_COMPLETED.value
                                .equalsIgnoreCase(getInfoWingResponse.getResponseCode())) {
                            mobileDeposit.setCaptcha("");
                            model.addAttribute("mobileDeposit", mobileDeposit);
                            model.addAttribute("errorPhoneNumber",
                                    utilHelper.convertErrorCodeToString(getInfoWingResponse.getResponseMessage()));
                            return new ModelAndView("index", model);
                        } else {
                            Cookie cookie = new Cookie("invoiceID", getInfoWingResponse.getRemark());
                            cookie.setPath("/" + SOURCE_PATH);
                            response.addCookie(cookie);
                            cookie = new Cookie("requetsID", getInfoWingResponse.getRequetsID());
                            cookie.setPath("/" + SOURCE_PATH);
                            response.addCookie(cookie);
                            return new ModelAndView("initPaymentWing", wingPaymentService.setupPayment(getInfoWingResponse));
                        }
                    default:
                        model.addAttribute("mobileDeposit", new MobileDeposit());
                        return new ModelAndView("index", model);
                }
            } else {
                mobileDeposit.setCaptcha("");
                model.addAttribute("mobileDeposit", mobileDeposit);
                model.addAttribute("errorPaymentType",
                        messageSource.getMessage("error.payment.type", null,
                                LocaleContextHolder.getLocaleContext().getLocale()));
                return new ModelAndView("index", model);
            }
        }
    }

    private void handleLogOutResponse(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (!"transIdAba".equalsIgnoreCase(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setValue(null);
                    cookie.setPath("/" + SOURCE_PATH);
                    response.addCookie(cookie);
                }
            }
        }

    }

    @GetMapping("/webhookemoney")
    @ResponseStatus(HttpStatus.OK)
    public String webhookEmoney(@RequestParam(value = "data") String data, ModelMap modelMap) throws ParseException {

        ReturnDataEmoney returnDataEmoney;
        try {
            String dataReturnJson = utilHelper.decryptCallbackData(data);
            ObjectMapper mapper = new ObjectMapper();
            returnDataEmoney = mapper.readValue(dataReturnJson, ReturnDataEmoney.class);
            System.out.println("NTTPaymentResponse " + returnDataEmoney.toString());
        } catch (Exception ex) {
            returnDataEmoney = new ReturnDataEmoney();
        }

        if (returnDataEmoney.getRefId() != null && returnDataEmoney.getTransDetailId() > 0) {
            GetInfoCustomerRequest getInfoCustomerRequest = new GetInfoCustomerRequest();
            getInfoCustomerRequest.setTxnid(returnDataEmoney.getRefId());
            getInfoCustomerRequest.setNttrefid(returnDataEmoney.getTransDetailId());
            GetInfoCustomerResponse payment = callServiceHelper.getInfoCustomer(getInfoCustomerRequest);

            modelMap.addAttribute("invoiceID", payment.getNttrefid());
            modelMap.addAttribute("payee", payment.getUdf2());
            modelMap.addAttribute("subtotal", payment.getUdf1());
            modelMap.addAttribute("subscriber", payment.getUdf3());
            modelMap.addAttribute("total", Double.valueOf(payment.getAmt()) / 100);
            modelMap.addAttribute("paymentDes", payment.getRespdesc());
            modelMap.addAttribute("curr", returnDataEmoney.getPaidCurrencyCode());

            final SimpleDateFormat DATE_TIME_FORMAT_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final SimpleDateFormat DATE_TIME_FORMAT_2 = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss a");
            Date tnxDate = DATE_TIME_FORMAT_1.parse(payment.getUdf4());

            modelMap.addAttribute("txnDate", DATE_TIME_FORMAT_2.format(tnxDate));

            if (returnDataEmoney.getStatus() != 3) {
                return "payment_fail";
            } else {
                return "payment_success";
            }
        }
        return null;
    }

    @GetMapping("/showCallBackABA")
    public String showCallBackABA(
            HttpServletRequest request,
            HttpServletResponse response) {
        return "showcallbackABA";
    }

    @GetMapping("/showCallBackAcleda")
    public ModelAndView showCallBackAcleda(
            @RequestParam(value = "_arNo", required = false) String arNo,
            @RequestParam(value = "_transactionid", required = false) String transId,
            @RequestParam(value = "_paymentresult", required = false) String paymentResult,
            @RequestParam(value = "_paymenttokenid", required = false) String paymentTokenId,
            @RequestParam(value = "_resultCode", required = false) String resultCode,
            HttpServletRequest request, HttpServletResponse response) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now) + " : paymentTokenId: " + paymentTokenId + " _resultCode: " + resultCode);
        RequestCallbackAcleda requestCallbackAcleda = new RequestCallbackAcleda();
        try {
            requestCallbackAcleda.setStatus(Integer.valueOf(resultCode));
        } catch (Exception e) {
            requestCallbackAcleda.setStatus(9999);
        }

        requestCallbackAcleda.setTran_id(paymentTokenId);

        System.out.println(dtf.format(now) + " : Request callback from Acleda: " + requestCallbackAcleda.toString());
        try {
            topUpService.callbackAcleda(requestCallbackAcleda);
        } catch (Exception e) {
            System.out.println(dtf.format(now) + " : Exception when callback Acleda: " + e.getMessage());
        }
        ModelMap model = new ModelMap();
        model.addAttribute("txId", paymentTokenId);
        return new ModelAndView("showcallbackAcleda", model);
    }

    @RequestMapping(value = "/transMobile",
            method = RequestMethod.GET)
    public String transMobile(Model model,
                              @RequestParam String hash, @RequestParam String tnxid,
                              @RequestParam String amtString, @RequestParam String firstName,
                              @RequestParam String lastName, @RequestParam String phone,
                              @RequestParam String email, @RequestParam String paymentType,
                              @RequestParam String urlABA) {
        model.addAttribute("hash", hash);
        model.addAttribute("tnxid", tnxid);
        model.addAttribute("amtString", amtString);
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        model.addAttribute("phone", phone);
        model.addAttribute("email", email);
        model.addAttribute("paymentType", paymentType);
        model.addAttribute("urlABA", urlABA);
        return "initPaymentMobile";
    }

    @RequestMapping(value = "/transWechatMobile",
            method = RequestMethod.GET)
    public String transWechatMobile(Model model,
                                    @RequestParam String isdn, @RequestParam String payCode,
                                    @RequestParam String nttrefid, @RequestParam String txnid) {
        model.addAttribute("isdn", isdn);
        model.addAttribute("payCode", payCode);
        model.addAttribute("nttrefid", nttrefid);
        model.addAttribute("txnid", txnid);
        return "wechat_payment";
    }


    @RequestMapping(value = "/resultWing",
            method = RequestMethod.GET)
    public String resultWing(@Param("status") @Nullable String status,
                             @CookieValue(value = "phoneNumber") @Nullable String phoneNumber,
                             @CookieValue(value = "topupAmount") @Nullable String topupAmount,
                             @CookieValue(value = "paymentAmount") @Nullable String paymentAmount,
                             @CookieValue(value = "paymentType") @Nullable String paymentType,
                             @CookieValue(value = "invoiceID") @Nullable String invoiceID,
                             @CookieValue(value = "requetsID") @Nullable String requetsID,
                             ModelMap model, HttpServletResponse httpServletResponse) {
        if (status.equals("success")) {
            RequestCallbackNTT requets = new RequestCallbackNTT();
            requets.setNttrefid(invoiceID);
            requets.setTxnid(requetsID);
            requets.setAmt(paymentAmount);
            ResponseCallbackWing response = callServiceHelper.callbacktWing(requets);
            if (response.getResponseCode().equals("00")) {
                Cookie cookie = new Cookie("payee", response.getCustomerName());
                cookie.setPath(SOURCE_PATH);
                httpServletResponse.addCookie(cookie);
                return "redirect:/payment_success";
            }
        }
        return "redirect:/payment_fail";

    }

    @RequestMapping(value = "/payment_success",
            method = RequestMethod.GET)
    public String resultWingSucces(
            @CookieValue(value = "phoneNumber") @Nullable String phoneNumber,
            @CookieValue(value = "topupAmount") @Nullable String topupAmount,
            @CookieValue(value = "paymentAmount") @Nullable String paymentAmount,
            @CookieValue(value = "invoiceID") @Nullable String invoiceID,
            @CookieValue(value = "payee") @Nullable String payee,
            ModelMap model) {
        Date date = new Date();
        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss a");
        model.addAttribute("txnDate", format.format(date));
        model.addAttribute("subscriber", phoneNumber);
        model.addAttribute("total", Double.valueOf(paymentAmount));
        model.addAttribute("subtotal", Double.valueOf(topupAmount));
        model.addAttribute("invoiceID", invoiceID);
        model.addAttribute("curr", "USD");
        if (payee != null) {
            model.addAttribute("payee", payee);
        }

        return "payment_success";
    }

    @RequestMapping(value = "/payment_fail",
            method = RequestMethod.GET)
    public String paymentFail(@Param("status") @Nullable String status,
                              @CookieValue(value = "invoiceID") @Nullable String invoiceID,
                              @CookieValue(value = "topupAmount") @Nullable String topupAmount,
                              @CookieValue(value = "paymentAmount") @Nullable String paymentAmount,
                              ModelMap model) {
        model.addAttribute("total", Double.valueOf(paymentAmount));
        model.addAttribute("subtotal", Double.valueOf(topupAmount));
        model.addAttribute("invoiceID", invoiceID);
        model.addAttribute("curr", "USD");

        return "payment_fail";
    }
}

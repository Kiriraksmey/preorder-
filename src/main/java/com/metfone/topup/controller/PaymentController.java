package com.metfone.topup.controller;

import com.metfone.topup.helper.UtilHelper;
import com.metfone.topup.model.MobileDeposit;
import com.metfone.topup.model.cybercard.CyberCardPaymentResponse;
import com.metfone.topup.model.cybercard.PaymentRequestCyber;
import com.metfone.topup.model.payment.CyberCardTypeEnum;
import com.metfone.topup.model.payment.NTTPaymentResponse;
import com.metfone.topup.model.payment.PaymentTypeEnum;
import com.metfone.topup.model.payment.ResponseCodeEnum;
import com.metfone.topup.model.union.UnionPaymentResponse;
import com.metfone.topup.helper.Transformer;
import com.metfone.topup.model.PaymentTopup;
import com.metfone.topup.model.alipay.AlipayPaymentResponse;
import com.metfone.topup.model.wechat.WechatPaymentResponse;
import com.metfone.topup.service.impl.AliPaymentServiceImpl;
import com.metfone.topup.service.impl.CyberPaymentServiceImpl;
import com.metfone.topup.service.impl.UnionPaymentServiceImpl;
import com.metfone.topup.service.impl.WechatPaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@Controller
public class PaymentController {

    @Autowired
    AliPaymentServiceImpl aliPaymentService;

    @Autowired
    CyberPaymentServiceImpl cyberPaymentService;

    @Autowired
    WechatPaymentServiceImpl wechatPaymentService;

    @Autowired
    UnionPaymentServiceImpl unionPaymentService;

    @Autowired
    UtilHelper utilHelper;

    @Autowired
    MessageSource messageSource;

    @Value("${source.path}")
    private String SOURCE_PATH;

    @PostMapping("/initpayment")
    public ModelAndView initpayment(@ModelAttribute PaymentTopup paymentTopup, ModelMap model,
                                    HttpServletResponse response, RedirectAttributes redirectAttributes) {
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
            }
            switch (paymentTopup.getPaymentMethod()) {
                case PaymentTypeEnum.MASTERCARD:
                case PaymentTypeEnum.VISA:
                case PaymentTypeEnum.JCB:
                    PaymentRequestCyber paymentRequestCyber = new PaymentRequestCyber();
                    paymentRequestCyber.setCardType(cardType);
                    paymentRequestCyber.setIsdn(paymentTopup.getRefillIsdn());
                    paymentRequestCyber.setPaymentAmount(paymentTopup.getRefillAmount());
                    paymentRequestCyber.setTopupAmount(paymentTopup.getRefillTotal());
                    CyberCardPaymentResponse cyberCardPaymentResponse = cyberPaymentService.initPayment(paymentRequestCyber);
                    if (cyberCardPaymentResponse.getResponseCode() == null || !ResponseCodeEnum.TRANSACTION_COMPLETED.value
                            .equalsIgnoreCase(cyberCardPaymentResponse.getResponseCode())) {
                        model.addAttribute("paymentTopup", paymentTopup);
                        model.addAttribute("errorPaymentType",
                                utilHelper.convertErrorCodeToString(cyberCardPaymentResponse.getRespcode()));
                        return new ModelAndView("payment", model);
                    }
                    if (cyberCardPaymentResponse.getRespcode() != null && !ResponseCodeEnum.TRANSACTION_COMPLETED.value
                            .equalsIgnoreCase(cyberCardPaymentResponse.getRespcode())) {
                        model.addAttribute("paymentTopup", paymentTopup);
                        model.addAttribute("errorPaymentType",
                                utilHelper.convertErrorCodeToString(cyberCardPaymentResponse.getRespcode()));
                        return new ModelAndView("payment", model);
                    } else {
                        System.out.println("Log payment ABA " + cyberCardPaymentResponse.toString());
                        model.addAttribute("hash", cyberCardPaymentResponse.getHash());
                        model.addAttribute("tran_id", cyberCardPaymentResponse.getTxnid());
                        model.addAttribute("amount", cyberCardPaymentResponse.getAmt());
                        model.addAttribute("firstname", cyberCardPaymentResponse.getFirstname());
                        model.addAttribute("lastname", cyberCardPaymentResponse.getLastname());
                        model.addAttribute("phone", cyberCardPaymentResponse.getPhone());
                        model.addAttribute("email", cyberCardPaymentResponse.getEmail());
                        model.addAttribute("url", cyberCardPaymentResponse.getUrlABA());

                        return new ModelAndView("payment", model);
                    }
                case PaymentTypeEnum.ALIPAY:
                    AlipayPaymentResponse alipayPaymentResponse = aliPaymentService.initPayment(Transformer.transform(paymentTopup, strDate));
                    if (alipayPaymentResponse.getResponseCode() == null || !ResponseCodeEnum.TRANSACTION_COMPLETED.value
                            .equalsIgnoreCase(alipayPaymentResponse.getResponseCode())) {
                        model.addAttribute("paymentTopup", paymentTopup);
                        model.addAttribute("errorPaymentType",
                                utilHelper.convertErrorCodeToString(alipayPaymentResponse.getRespcode()));
                        return new ModelAndView("payment", model);
                    }
                    if (alipayPaymentResponse.getRespcode() != null && !ResponseCodeEnum.TRANSACTION_PENDING.value
                            .equalsIgnoreCase(alipayPaymentResponse.getRespcode())) {
                        model.addAttribute("paymentTopup", paymentTopup);
                        model.addAttribute("errorPaymentType",
                                utilHelper.convertErrorCodeToString(alipayPaymentResponse.getRespcode()));
                        return new ModelAndView("payment", model);
                    } else {
                        return new ModelAndView(aliPaymentService.setupPayment(alipayPaymentResponse));
                    }
                case PaymentTypeEnum.UNION:
                    UnionPaymentResponse unionPaymentResponse = unionPaymentService.initPayment(Transformer.transform(paymentTopup, strDate));
                    if (unionPaymentResponse.getResponseCode() == null || !ResponseCodeEnum.TRANSACTION_COMPLETED.value
                            .equalsIgnoreCase(unionPaymentResponse.getResponseCode())) {
                        model.addAttribute("paymentTopup", paymentTopup);
                        model.addAttribute("errorPaymentType",
                                utilHelper.convertErrorCodeToString(unionPaymentResponse.getRespcode()));
                        return new ModelAndView("payment", model);
                    }
                    if (unionPaymentResponse.getRespcode() != null && !ResponseCodeEnum.TRANSACTION_PENDING.value
                            .equalsIgnoreCase(unionPaymentResponse.getRespcode())) {
                        model.addAttribute("paymentTopup", paymentTopup);
                        model.addAttribute("errorPaymentType",
                                utilHelper.convertErrorCodeToString(unionPaymentResponse.getRespcode()));
                        return new ModelAndView("payment", model);
                    } else {
                        return new ModelAndView(unionPaymentService.setupPayment(unionPaymentResponse));
                    }

                case PaymentTypeEnum.WECHAT:
                    WechatPaymentResponse wechatPaymentResponse = wechatPaymentService.initPayment(Transformer.transform(paymentTopup, strDate));
                    wechatPaymentResponse.setIsdn(paymentTopup.getRefillIsdn());
                    if (wechatPaymentResponse.getResponseCode() == null || !ResponseCodeEnum.TRANSACTION_COMPLETED.value
                            .equalsIgnoreCase(wechatPaymentResponse.getResponseCode())) {
                        model.addAttribute("paymentTopup", paymentTopup);
                        model.addAttribute("errorPaymentType",
                                utilHelper.convertErrorCodeToString(wechatPaymentResponse.getRespcode()));
                        return new ModelAndView("payment", model);
                    }
                    if (wechatPaymentResponse.getRespcode() != null && !ResponseCodeEnum.TRANSACTION_PENDING.value
                            .equalsIgnoreCase(wechatPaymentResponse.getRespcode())) {
                        model.addAttribute("paymentTopup", paymentTopup);
                        model.addAttribute("errorPaymentType",
                                utilHelper.convertErrorCodeToString(wechatPaymentResponse.getRespcode()));
                        return new ModelAndView("payment", model);
                    } else {
                        return new ModelAndView("wechat_payment", wechatPaymentService.setupPayment(wechatPaymentResponse));
                    }

                default:
                    model.addAttribute("mobileDeposit", new MobileDeposit());
                    return new ModelAndView("redirect:/", model);
            }
        } else {
            redirectAttributes.addFlashAttribute("paymentTopup", paymentTopup);
            redirectAttributes.addFlashAttribute("errorPaymentType", messageSource.getMessage("error.payment.type", null,
                    LocaleContextHolder.getLocaleContext().getLocale()));

            return new ModelAndView("redirect:/topup");
        }
    }

    @RequestMapping(
            value = "/result",
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String topupResult(NTTPaymentResponse payment, ModelMap modelMap) throws ParseException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        if (payment.getRespdesc() == null) {
            payment.setRespdesc("error.isdn.system");
        }

        if ("error.isdn.system".equalsIgnoreCase(payment.getRespdesc()) || "error.payment.timeout".equalsIgnoreCase(payment.getRespdesc())) {
            payment.setRespdesc(utilHelper.convertErrorCodeToString(payment.getRespdesc()));
        }

        modelMap.addAttribute("invoiceID", payment.getNttrefid());
        modelMap.addAttribute("payee", payment.getUdf2());
        modelMap.addAttribute("subtotal", payment.getUdf1());
        modelMap.addAttribute("subscriber", payment.getUdf3());
        try {
            modelMap.addAttribute("total", Double.valueOf(payment.getAmt()) / 100);
        } catch (Exception e) {
            System.out.println(dtf.format(now) + " : Exception parse Total value: " + payment.getAmt() + ", invoice: " + payment.getNttrefid());
            modelMap.addAttribute("total", 0);
        }

        modelMap.addAttribute("paymentDes", payment.getRespdesc());
        modelMap.addAttribute("curr", payment.getTxncurr());

        final SimpleDateFormat DATE_TIME_FORMAT_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final SimpleDateFormat DATE_TIME_FORMAT_2 = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss a");
        try {
            Date tnxDate = DATE_TIME_FORMAT_1.parse(payment.getUdf4());
            modelMap.addAttribute("txnDate", DATE_TIME_FORMAT_2.format(tnxDate));
        } catch (Exception e) {

            System.out.println(dtf.format(now) + " : Exception parse txnDate value: " + payment.getAmt() + ", invoice: " + payment.getNttrefid());
            modelMap.addAttribute("txnDate", "");
        }
        if (payment.getRespcode().equals(ResponseCodeEnum.TRANSACTION_COMPLETED.value)) {
            return "payment_success";
        } else {
            return "payment_fail";
        }

    }

    @RequestMapping(
            value = "/print-receipt",
            method = RequestMethod.GET
    )
    public String print(@RequestParam(name = "date") String date,
                        @RequestParam(name = "orderId") String orderId,
                        @RequestParam(name = "subtotal") String subtotal,
                        @RequestParam(name = "total") String total,
                        Model model) {
        model.addAttribute("date", date);
        model.addAttribute("orderId", orderId);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("total", total);
        return "receipt";
    }

    @RequestMapping(
            value = "/print-receiptWing",
            method = RequestMethod.GET
    )
    public String printWing(@RequestParam(name = "date") String date,
                            @RequestParam(name = "orderId") String orderId,
                            @RequestParam(name = "subtotal") String subtotal,
                            @RequestParam(name = "total") String total,
                            @RequestParam(name = "wingAccountNo") String wingAccountNo,
                            @RequestParam(name = "wingAccountName") String wingAccountName,
                            Model model) {
        model.addAttribute("date", date);
        model.addAttribute("wingAccountNo", wingAccountNo);
        model.addAttribute("wingAccountName", wingAccountName);
        model.addAttribute("orderId", orderId);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("total", total);
        return "receipt_wing";
    }

}

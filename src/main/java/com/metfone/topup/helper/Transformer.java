package com.metfone.topup.helper;

import com.metfone.topup.model.PaymentTopup;
import com.metfone.topup.model.payment.PaymentRequest;
import com.metfone.topup.model.payment.PaymentRequestEmoney;

import java.util.Optional;

public final class Transformer {

    public static PaymentRequest transform(PaymentTopup paymentTopup, String invoiceId) {
        PaymentRequest paymentRequest = Optional.ofNullable(paymentTopup)
                .map(map -> new PaymentRequest().builder()
                        .isdn(paymentTopup.getRefillIsdn())
                        .paymentAmount(paymentTopup.getRefillAmount())
                        .topupAmount(paymentTopup.getRefillTotal())
                        .build()).orElse(null);
        paymentRequest.setInvoiceID(invoiceId);
        return paymentRequest;
    }

    public static PaymentRequestEmoney transformEmoney(PaymentTopup paymentTopup, String invoiceId) {
        PaymentRequestEmoney paymentRequest = Optional.ofNullable(paymentTopup)
                .map(map -> new PaymentRequestEmoney().builder()
                        .isdn(paymentTopup.getRefillIsdn())
                        .paymentAmount(paymentTopup.getRefillAmount())
                        .topupAmount(paymentTopup.getRefillTotal())
                        .accountEmoney(paymentTopup.getAccountEmoney())
                        .build()).orElse(null);
        paymentRequest.setInvoiceID(invoiceId);
        return paymentRequest;
    }

}

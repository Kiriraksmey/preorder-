package com.metfone.topup.model.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestEmoney {
    private String isdn;
    private float topupAmount;
    private float paymentAmount;
    private String invoiceID;
    private String accountEmoney;
}

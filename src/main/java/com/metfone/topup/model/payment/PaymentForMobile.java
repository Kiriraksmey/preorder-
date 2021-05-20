package com.metfone.topup.model.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentForMobile implements Serializable {
    private String phoneNumber;
    private String totalAmount;
    private String paymentMethod;
    private String accountEmoney;
    private String ftthAccount;
    private String paymentAmount;
    private String ftthType;
    private String ftthName;
    private String contractIdInfo;
}
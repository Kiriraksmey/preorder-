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
public class CyberPaymentTransaction implements Serializable {

    private String orderId;
    private String isdn;
    private float paymentAmount;
    private long topupAmount;
    private String cardNumber;
    private String cardHolderName;
    private String expirationDate;
    private String ccvNumber;
    private String paymentType;

}

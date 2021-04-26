package com.metfone.topup.model.cybercard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestCyber implements Serializable {

    private String isdn;
    private long topupAmount;
    private float paymentAmount;
    private String cardType;

}

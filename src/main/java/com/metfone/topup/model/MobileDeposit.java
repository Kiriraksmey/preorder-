package com.metfone.topup.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MobileDeposit implements Serializable {
    private String phoneNumber;
    private String topupAmount;
    private float paymentAmount;
    private String paymentMethod;
    private boolean termAgreed;
    private String captcha;
    private String accountEmoney;
}

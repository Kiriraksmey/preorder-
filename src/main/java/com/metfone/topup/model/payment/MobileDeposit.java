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
public class MobileDeposit implements Serializable {
    private String phoneNumber;
    private long topupAmount;
    private float paymentAmount;
    private boolean termAgreed;
}

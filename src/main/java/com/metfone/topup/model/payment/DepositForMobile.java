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
public class DepositForMobile implements Serializable {
    private String phoneNumber;
    private String topupAmount;
    private String paymentMethod;
    private String accountEmoney;
}

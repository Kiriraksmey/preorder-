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
public class PaymentTopup implements Serializable {
    private float refillAmount;
    private long refillTotal;
    private String refillIsdn;
    private String paymentMethod;
    private String accountEmoney;
}

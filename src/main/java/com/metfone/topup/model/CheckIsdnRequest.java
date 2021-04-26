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
public class CheckIsdnRequest implements Serializable {
    private String isdn;
    private long amoutTopup;
    private String paymentType;
    private String accountEmoney;
}

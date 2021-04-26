package com.metfone.topup.model.emoney;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnDataEmoney {

    private long transDetailId;
    private int status;
    private String refId;
    private long paidTid;
    private double paidFee;
    private double paidAmount;
    private String paidCurrencyCode;
    private double paidTotalAmount;

}

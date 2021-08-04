package com.metfone.topup.model.Wing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCallbackWing {
    private String responseCode;
    private String responseMessage;
    private String customerName;
    private String currencytype;
    private String wingAccountNo;
    private String transactionIdWing;
    private String billerName;

}

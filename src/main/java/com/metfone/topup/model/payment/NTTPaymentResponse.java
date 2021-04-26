package com.metfone.topup.model.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public  class NTTPaymentResponse {
    private String login;
    private String respcode;
    private String respdesc;
    private String txncurr;
    private String amt;
    private String txnid;
    private String proc_code;
    private String nttrefid;
    private String signature;
    private String udf1;
    private String udf2;
    private String udf3;
    private String udf4;
    private String udf5;
    private String date;


}

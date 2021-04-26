package com.metfone.topup.model.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WechatPaymentResponse {
    private String responseCode;
    private String responseMessage;
    private float amt;
    private String txncurr;
    private String txnid;
    private String nttrefid;
    private String respcode;
    private String respdesc;
    @JsonProperty("proc_code")
    private String procCode;
    @JsonProperty("pay_code")
    private String payCode;
    private String isdn;
}

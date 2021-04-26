package com.metfone.topup.model.cybercard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CyberCardPaymentResponse {

    private String responseCode;
    private String responseMessage;
    private String respcode;
    private String respdesc;
    @JsonProperty("redirect_url")
    private String redirectUrl;
    private double amt;
    private String txncurr;
    private String nttrefid;
    @JsonProperty("pay_code")
    private String payCode;
    private String txnid;
    private String hash;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String urlABA;
    private String error_field;
    private String error_description;
    private boolean status;
    private String amt_string;
}

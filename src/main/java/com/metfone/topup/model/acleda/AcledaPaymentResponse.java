package com.metfone.topup.model.acleda;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcledaPaymentResponse {
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
    private String urlAcleda;
    private String merchantID;
    private String sessionid;
    private String paymenttokenid;
    private String description;
    private String expirytime;
    private String quantity;
    private String item;
    private String invoiceid;
    private String transactionID;
    private String currencytype;
    private String successUrlToReturn;
    private String errorUrl;
}

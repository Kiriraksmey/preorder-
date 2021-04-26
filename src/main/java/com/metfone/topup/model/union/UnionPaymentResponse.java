package com.metfone.topup.model.union;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnionPaymentResponse {
    private String responseCode;
    private String responseMessage;
    private double amt;
    private String txncurr;
    private String nttrefid;
    private String respcode;
    private String respdesc;
    @JsonProperty("redirect_url")
    private String redirectUrl;
}

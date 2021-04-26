package com.metfone.topup.model.cybercard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CyberCardpaymentResponseMobile {
    private String responseCode;
    private String responseMessage;
    private String respcode;
    private String respdesc;
    @JsonProperty("redirect_url")
    private String redirectUrl;
    private String error_field;
    private String error_description;
    private boolean status;
}

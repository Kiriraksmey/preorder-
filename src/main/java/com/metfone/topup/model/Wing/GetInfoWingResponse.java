package com.metfone.topup.model.Wing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetInfoWingResponse {
    private String responseCode;
    private String responseMessage;
    private String sandbox;
    private String username;
    private String rest_api_key;
    private String bill_till_rbtn;
    private String bill_till_number;
    private String is_inquiry;
    private String remark;
    private String returnUrl;
    private String URL_WS_Wing;
    private float amount;
    private String requetsID;
}

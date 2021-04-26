package com.metfone.topup.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCallbackNTT {

    private String amt;
    private String txncurr;
    private String txnid;
    private String nttrefid;
    private String respcode;
    private String respdesc;
    private String proc_code;
    private String date;
    private String signature;
    private String callbackUrl;
    private String login;
    private String channelType;
    private String paymentMethod;
    private String ru;
    private String captureId;
    private String manualCapture;
    private String manualCaptureStatus;
    private String auth_code;
    private String udf1;
    private String udf2;
    private String udf3;
    private String udf4;

}

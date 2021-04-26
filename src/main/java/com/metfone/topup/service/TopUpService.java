package com.metfone.topup.service;

import com.metfone.topup.helper.CallServiceHelper;
import com.metfone.topup.model.*;
import com.metfone.topup.model.topup.GetListPaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TopUpService {

    @Autowired
    private CallServiceHelper callServiceHelper;

    public CheckIsdnResponse checkIsdn(CheckIsdn checkIsdn){
        CheckIsdnResponse response = new CheckIsdnResponse();
        String regexMobile = "[0-9]+";
        Pattern p = Pattern.compile(regexMobile);

        response.setResponseCode("01");
        if(checkIsdn.getIsdn() == null || checkIsdn.getIsdn().isEmpty()){
            return response;
        }
        Matcher m = p.matcher(checkIsdn.getIsdn());
        if(checkIsdn.getIsdn().length() > 16 || !m.find() || !m.group().equals(checkIsdn.getIsdn())){
            return response;
        }
        response = callServiceHelper.checkIsdn(checkIsdn);
        return response;
    }

    public GetListPaymentResponse getListPayment(String ip) {
        GetListPaymentResponse response = new GetListPaymentResponse();
        response = callServiceHelper.getListPayment(ip);
        return response;
    }

    public String callbackNTT(RequestCallbackNTT requestCallbackNTT) {
        String result = "";
        callServiceHelper.callbackNTT(requestCallbackNTT);
        return result;
    }

    public String callbackNTTEmoney(RequestCallbackNTT requestCallbackNTT) {
        String result = "";
        callServiceHelper.callbackNTTEmoney(requestCallbackNTT);
        return result;
    }

    public String callbackAba(RequestCallbackABA requestCallbackABA) {
        String result = "";
        callServiceHelper.callbackAba(requestCallbackABA);
        return result;
    }

    public String callbackAcleda(RequestCallbackAcleda requestCallbackAcleda) {
        String result = "";
        callServiceHelper.callbackAcleda(requestCallbackAcleda);
        return result;
    }

}

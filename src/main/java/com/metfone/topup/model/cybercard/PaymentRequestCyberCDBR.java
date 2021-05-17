package com.metfone.topup.model.cybercard;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestCyberCDBR implements Serializable {
    private String isdn;
    private Double topupAmount;
    private String cardType;
    private String ftthAccount;
    private String idBill;
    private Double paymentAmount;
    private String channelType;// alipay
    private String accountEmoney;
    private String paymentMethod;
    private String ftthType;
    private String ftthName;


}

package com.metfone.topup.model.wechat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QRScanCheck {
    private String isdn;
    private String nttrefid;
    private String txnid;
    private String cardType;
}

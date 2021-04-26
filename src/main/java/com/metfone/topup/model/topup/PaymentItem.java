package com.metfone.topup.model.topup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentItem {
    private int paramId;
    private String paramType;
    private String paramCode;
    private String paramName;
    private String paramValue;
    private int status;
}

package com.metfone.topup.model.topup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetListPaymentResponse {
    private String responseCode;
    private String responseMessage;
    private List<PaymentItem> lst;
}

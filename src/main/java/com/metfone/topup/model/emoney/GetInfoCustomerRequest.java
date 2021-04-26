package com.metfone.topup.model.emoney;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetInfoCustomerRequest {
    private String txnid;
    private long nttrefid;
}

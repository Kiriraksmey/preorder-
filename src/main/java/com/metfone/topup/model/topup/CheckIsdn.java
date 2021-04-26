package com.metfone.topup.model.topup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckIsdn implements Serializable {

    private String isdn;
    private long amoutTopup;
    private String paymentType;

}

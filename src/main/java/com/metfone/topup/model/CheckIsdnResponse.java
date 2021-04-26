package com.metfone.topup.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckIsdnResponse implements Serializable {

    private String responseCode;
    private String responseMessage;
    private String sub;
    private String isdn;
    private long amoutTopup;
    private float paymentTopup;

}

package com.metfone.topup.model.payment;

public enum  ResponseCodeEnum {
    TRANSACTION_COMPLETED("00"),
    TRANSACTION_FAILED("01"),
    TRANSACTION_PENDING("02");
    public final String value;

    private ResponseCodeEnum(String value) {
        this.value = value;
    }

}

package com.metfone.topup.model.payment;

public enum CyberCardTypeEnum {

    VISA("Visa"),
    MASTER_CARD("Master"),
    JCB("JCB"),
    ABAPAY("AbaPay"),
    ACLEDA("Acleda");
    public final String value;

    private CyberCardTypeEnum(String value) {
        this.value = value;
    }

}

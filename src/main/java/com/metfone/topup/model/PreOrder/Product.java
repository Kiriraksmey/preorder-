package com.metfone.topup.model.PreOrder;

import java.util.Date;

public class Product {
    private Long categoryId;
    private Long productId;
    private Long modelId;
    private Long custId;
    private String customerName;
    private String isdn;
    private String customerAdress;
    private Long orderCode;
    private Long deposit;
    private String pickUpLocation;
    private String status;
    private Date createDate;
    private String createUser;
    private Date modifyDate;
    private String modifyUser;
    private Long verificationCode;
    private Long capacityId;
    private Long colorId;
    private String nationalId;
    private String province;
    private Long subCategoryId;
    private String productName;
    private String otp;
    private String trackingCode;

    public Product() {

    }

    public Product(Long categoryId, Long productId, Long modelId, Long custId, String customerName, String isdn, String customerAdress, Long orderCode, Long deposit, String pickUpLocation, String status, Date createDate, String createUser, Date modifyDate, String modifyUser, Long verificationCode, Long capacityId, Long colorId, String nationalId, String province, Long subCategoryId, String productName, String otp, String trackingCode) {
        this.categoryId = categoryId;
        this.productId = productId;
        this.modelId = modelId;
        this.custId = custId;
        this.customerName = customerName;
        this.isdn = isdn;
        this.customerAdress = customerAdress;
        this.orderCode = orderCode;
        this.deposit = deposit;
        this.pickUpLocation = pickUpLocation;
        this.status = status;
        this.createDate = createDate;
        this.createUser = createUser;
        this.modifyDate = modifyDate;
        this.modifyUser = modifyUser;
        this.verificationCode = verificationCode;
        this.capacityId = capacityId;
        this.colorId = colorId;
        this.nationalId = nationalId;
        this.province = province;
        this.subCategoryId = subCategoryId;
        this.productName = productName;
        this.otp = otp;
        this.trackingCode = trackingCode;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String getCustomerAdress() {
        return customerAdress;
    }

    public void setCustomerAdress(String customerAdress) {
        this.customerAdress = customerAdress;
    }

    public Long getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Long orderCode) {
        this.orderCode = orderCode;
    }

    public Long getDeposit() {
        return deposit;
    }

    public void setDeposit(Long deposit) {
        this.deposit = deposit;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(String pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public Long getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(Long verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Long getCapacityId() {
        return capacityId;
    }

    public void setCapacityId(Long capacityId) {
        this.capacityId = capacityId;
    }

    public Long getColorId() {
        return colorId;
    }

    public void setColorId(Long colorId) {
        this.colorId = colorId;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }
}

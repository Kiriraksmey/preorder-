package com.metfone.topup.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Service
public interface IPaymentService<T, K> {
    K initPayment(T request);
    Object setupPayment(K payment);
}

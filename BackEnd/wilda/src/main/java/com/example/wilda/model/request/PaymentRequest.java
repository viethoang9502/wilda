package com.example.wilda.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private String amount;
    private String orderInfo;
}
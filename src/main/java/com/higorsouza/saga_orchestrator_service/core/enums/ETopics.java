package com.higorsouza.saga_orchestrator_service.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ETopics {

    START_SAGA("start-saga"),
    BASE_ORCHESTRATOR("orchestrator"),
    FINISH_SUCCESSO("finish-success"),
    FINISH_FAIL("finish-fail"),
    PRODUCT_VALIDATION_SUCCESS("product-validation-success"),
    PRODUCT_VALIDATION_FAIL("product-validation-fail"),
    PAYMENT_SUCCESS("payment-success"),
    PAYMENT_FAIL("payment-fail"),
    INVENTORY_SUCCESS("inventory-success"),
    INVENTORY_FAIL("inventory-fail"),
    NOTIFY_ENDING("notify-ending");

    private String topic;
}

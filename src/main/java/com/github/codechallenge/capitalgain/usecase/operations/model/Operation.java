package com.github.codechallenge.capitalgain.usecase.operations.model;

import java.math.BigDecimal;

public record Operation(String operation, BigDecimal unitCost, long quantity) {
    public BigDecimal total() {
        return unitCost.multiply(BigDecimal.valueOf(quantity));
    }
}
package com.github.codechallenge.capitalgain.usecase.operations.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter @Setter
@Accessors(fluent = true)
@AllArgsConstructor
public class Account {
    private BigDecimal averagePrice, prejudice;
    private long quantity;

    public boolean hasPrejudice() {
        return prejudice.signum() < 0;
    }

    public BigDecimal total() {
        return averagePrice.multiply(BigDecimal.valueOf(quantity));
    }
}
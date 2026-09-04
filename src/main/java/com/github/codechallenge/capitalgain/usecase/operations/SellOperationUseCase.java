package com.github.codechallenge.capitalgain.usecase.operations;

import com.github.codechallenge.capitalgain.usecase.operations.model.Account;
import com.github.codechallenge.capitalgain.usecase.operations.model.Operation;
import com.github.codechallenge.capitalgain.usecase.operations.model.Tax;

import java.math.BigDecimal;
import java.util.function.BiFunction;

import static java.math.BigDecimal.ZERO;

public class SellOperationUseCase implements BiFunction<Account, Operation, Tax> {
    private static final BigDecimal OPERATION_MINIMUM = BigDecimal.valueOf(20000);
    private static final BigDecimal TAX_PERCENTAGE = BigDecimal.valueOf(0.2);

    @Override
    public Tax apply(final Account account, final Operation operation) {
        if(!"sell".equals(operation.operation())) return null;

        // Check if there was a profit in the current operation.
        final var gain = operation.total().subtract(account.averagePrice().multiply(BigDecimal.valueOf(operation.quantity())));

        /*
         * Auxiliary flags, respectively:
         * - {@code hasLoss}: indicates a loss occurred in the current operation
         * - {@code isTaxable}: true if the operation meets the minimum threshold for applying taxes
         */
        final var difference = gain.add(account.prejudice());
        final boolean hasLoss = gain.signum() < 0, isTaxable = operation.total().compareTo(OPERATION_MINIMUM) > 0;

        // It only accumulates or modifies the loss if a loss occurred in the current transaction, or if there was already an accumulation of losses and the current transaction was taxed.
        account.prejudice(!isTaxable && !hasLoss ? account.prejudice() : difference.signum() > 0 ? ZERO : difference);
        account.quantity(account.quantity() - operation.quantity());

        return !isTaxable || account.hasPrejudice() ? new Tax(ZERO) : new Tax(difference.multiply(TAX_PERCENTAGE));
    }
}
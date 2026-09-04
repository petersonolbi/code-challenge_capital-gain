package br.com.nubank.capitalgain.usecase.operations;

import br.com.nubank.capitalgain.usecase.operations.model.Account;
import br.com.nubank.capitalgain.usecase.operations.model.Operation;
import br.com.nubank.capitalgain.usecase.operations.model.Tax;

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
         * - {@code previousLoss}: indicates a loss occurred in a previous operation
         * - {@code hasLoss}: indicates a loss occurred in the current operation
         * - {@code isTaxable}: true if the operation meets the minimum threshold for applying taxes
         */
        final boolean previousLoss = account.hasPrejudice(), hasLoss = gain.signum() < 0, isTaxable = operation.total().compareTo(OPERATION_MINIMUM) > 0;
        final var difference = gain.add(account.prejudice());

        // Only accumulates the loss, IF there was a loss in the current operation OR there was already an accumulation of losses and the current operation was taxed.
        account.prejudice(hasLoss || (previousLoss && isTaxable) ? gain.compareTo(account.prejudice().negate()) > 0 ? ZERO : account.prejudice().add(gain) : account.prejudice());
        account.quantity(account.quantity() - operation.quantity());

        return !isTaxable || account.hasPrejudice() || hasLoss ? new Tax(ZERO) : new Tax((previousLoss ? difference : gain).multiply(TAX_PERCENTAGE));
    }
}
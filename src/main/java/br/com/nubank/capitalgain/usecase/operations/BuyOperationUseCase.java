package br.com.nubank.capitalgain.usecase.operations;

import br.com.nubank.capitalgain.usecase.operations.model.Account;
import br.com.nubank.capitalgain.usecase.operations.model.Operation;
import br.com.nubank.capitalgain.usecase.operations.model.Tax;

import java.math.BigDecimal;
import java.util.function.BiFunction;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;

public class BuyOperationUseCase implements BiFunction<Account, Operation, Tax> {
    @Override
    public Tax apply(final Account account, final Operation operation) {
        if(!"buy".equals(operation.operation())) return null;

        // Adjusts the average price according to recent purchase operations.
        account.averagePrice(calculateAveragePrice(account, operation));
        account.quantity(account.quantity() + operation.quantity());

        return new Tax(ZERO);
    }

    private BigDecimal calculateAveragePrice(final Account account, final Operation operation) {
        return account.total().add(operation.total()).divide(BigDecimal.valueOf(account.quantity() + operation.quantity()), HALF_UP);
    }
}

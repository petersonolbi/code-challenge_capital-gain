package br.com.nubank.capitalgain.usecase.operations;

import br.com.nubank.capitalgain.usecase.operations.model.Account;
import br.com.nubank.capitalgain.usecase.operations.model.Operation;
import br.com.nubank.capitalgain.usecase.operations.model.Tax;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.function.BiFunction;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BuyOperationUseCaseTest {
    private final BiFunction<Account, Operation, Tax> useCase = new BuyOperationUseCase();

    @Test
    void givenApply_whenBuy_thenReturnTax() {
        // Given:
        final var account = new Account(BigDecimal.valueOf(1000), TEN, 10L);

        // When:
        final var tax = useCase.apply(account, new Operation("buy", BigDecimal.valueOf(120), 5));

        // Then:
        assertEquals(15L, account.quantity());
        assertEquals(BigDecimal.valueOf(707), account.averagePrice());
        assertEquals(ZERO, tax.tax());
    }

    @Test
    void givenApply_whenSell_thenDoesNothing() {
        // When:
        assertNull(useCase.apply(new Account(ZERO, ZERO, 0L), new Operation("sell", TEN, 5)));
    }
}

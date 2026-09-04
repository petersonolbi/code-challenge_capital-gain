package br.com.nubank.capitalgain.usecase.operations;

import br.com.nubank.capitalgain.usecase.operations.model.Account;
import br.com.nubank.capitalgain.usecase.operations.model.Operation;
import br.com.nubank.capitalgain.usecase.operations.model.Tax;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.function.BiFunction;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SellOperationUseCaseTest {
    private final BiFunction<Account, Operation, Tax> useCase = new SellOperationUseCase();

    @Test
    void givenApply_whenSell_thenDoesNothing() {
        // When:
        assertNull(useCase.apply(new Account(ZERO, ZERO, 10L), new Operation("buy", BigDecimal.valueOf(100), 5L)));
    }

    @Test
    @DisplayName("When 'total ≤ OPERATION_MINIMUM' apply 'Tax = 0' and update 'Account'")
    void givenApply_whenBellowMinimum_thenZeroTax() {
        // Given:
        final var account = new Account(BigDecimal.valueOf(50), ZERO, 10L);

        // When: total = 50 * 100 = 5_000 < 20_000
        final var tax = useCase.apply(account, new Operation("sell", BigDecimal.valueOf(40), 5L));

        // Then:
        assertEquals(ZERO, tax.tax());
        assertEquals(5L, account.quantity());
        assertEquals(BigDecimal.valueOf(-50), account.prejudice());
    }

    @Test
    @DisplayName("When 'Operation' with 'gain < 0' apply 'Tax = 0' and update prejudice")
    void givenApply_whenSellWithLoss_thenZeroTaxAndAccumulatePrejudice() {
        // Given:
        final var account = new Account(BigDecimal.valueOf(100), ZERO, 10L);

        // When: total = 50 * 10 = 500, price = 10, gain = -500
        final var tax = useCase.apply(account, new Operation("sell", BigDecimal.valueOf(50), 10L));

        // Then:
        assertEquals(ZERO, tax.tax());
        assertEquals(BigDecimal.valueOf(-500), account.prejudice());
        assertEquals(0L, account.quantity());
    }

    @Test
    @DisplayName("When gain apply 'Tax' of 20%")
    void givenApply_whenSellAndGainWithoutPrejudice_thenTaxApplied() {
        // Given:
        final var account = new Account(TEN, ZERO, 100L);

        // When: total = 300 * 100 = 30000, gain = 29000, taxes = gain * 0.2 = 5800
        final var tax = useCase.apply(account, new Operation("sell", BigDecimal.valueOf(300), 100L));

        // Then:
        assertEquals(BigDecimal.valueOf(5800.0), tax.tax());
        assertEquals(0L, account.quantity());
        assertEquals(ZERO, account.prejudice());
    }

    @Test
    @DisplayName("When there is a previous loss, only the surplus is taxed")
    void givenApply_whenSellWithPreviousLoss_thenPartialTax() {
        // Given:
        final var account = new Account(BigDecimal.valueOf(50), BigDecimal.valueOf(-5000), 200L);

        // When: total = 200 * 200 = 40000, gain = 30000, taxes = (gain - 5000) * 0.2 = 5000.0
        final var tax = useCase.apply(account, new Operation("sell", BigDecimal.valueOf(200), 200L));

        // Then:
        assertEquals(BigDecimal.valueOf(5000.0), tax.tax());
        assertEquals(ZERO, account.prejudice());
        assertEquals(0L, account.quantity());
    }
}

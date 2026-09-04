package com.github.codechallenge.capitalgain.adapter.operations.cmd;

import com.github.codechallenge.capitalgain.usecase.operations.model.Account;
import com.github.codechallenge.capitalgain.usecase.operations.model.Operation;
import com.github.codechallenge.capitalgain.usecase.operations.model.Tax;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.math.BigDecimal.ZERO;

@RequiredArgsConstructor
public class OperationListener {
    private final List<BiFunction<Account, Operation, Tax>> operationsType;
    private final Gson gson;

    // Application ports:
    private final Supplier<String> input;
    private final Consumer<String> output;

    public void onOperation() {
        // Listens to the 'input' until it finds an empty line.
        for(String json = input.get(); !json.isBlank(); json = input.get()) {
            final Collection<Operation> operations = gson.fromJson(json, new TypeToken<Collection<Operation>>() {}.getType());

            final var account = new Account(ZERO, ZERO, 0L);
            final var tax = operations.stream().flatMap(operation -> operationsType.stream().map(ot -> ot.apply(account, operation)).filter(Objects::nonNull)).toList();

            output.accept(gson.toJson(tax));
        }
    }
}
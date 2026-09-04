package br.com.nubank.capitalgain;

import br.com.nubank.capitalgain.adapter.operations.cmd.OperationListener;
import br.com.nubank.capitalgain.usecase.operations.BuyOperationUseCase;
import br.com.nubank.capitalgain.usecase.operations.SellOperationUseCase;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_DASHES;
import static java.math.RoundingMode.HALF_UP;

public class Main {
    public static void main(String[] args) {
        try(final var scanner = new Scanner(System.in)) {
            new OperationListener (
                List.of(new BuyOperationUseCase(), new SellOperationUseCase()),
                new GsonBuilder().registerTypeAdapter(BigDecimal.class, (JsonSerializer<BigDecimal>) (src, type, context) -> new JsonPrimitive(src.setScale(2, HALF_UP))).setFieldNamingPolicy(LOWER_CASE_WITH_DASHES).create(),
                scanner::nextLine, System.out::println
            ).onOperation();
        }
    }
}
package com.github.codechallenge.capitalgain.adapter.operations.cmd;

import com.github.codechallenge.capitalgain.usecase.operations.BuyOperationUseCase;
import com.github.codechallenge.capitalgain.usecase.operations.SellOperationUseCase;
import com.github.codechallenge.capitalgain.usecase.operations.model.Account;
import com.github.codechallenge.capitalgain.usecase.operations.model.Operation;
import com.github.codechallenge.capitalgain.usecase.operations.model.Tax;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_DASHES;
import static java.math.RoundingMode.HALF_UP;
import static java.nio.file.Files.readAllLines;
import static java.util.Objects.requireNonNull;
import static org.mockito.Mockito.*;

/**
 * The purpose of this class is to serve as an integrated test for all scenarios described in the challenge document "Case #N".
 * To add a new scenario, use the 'operations' and 'taxes' files: insert the scenario into the 'operations' file and specify the expected result in the 'taxes' file.
 */
class OperationListenerTest {
    private static final List<BiFunction<Account, Operation, Tax>> OPERATIONS = List.of(new BuyOperationUseCase(), new SellOperationUseCase());
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(BigDecimal.class, (JsonSerializer<BigDecimal>) (src, type, context) -> new JsonPrimitive(src.setScale(2, HALF_UP))).setFieldNamingPolicy(LOWER_CASE_WITH_DASHES).create();

    @MethodSource("provideOperations")
    @ParameterizedTest(name = "Case #{index}: operation = {1}, expectedTax = {0}")
    void givenApply_whenSellOperation_thenCalculateTax(String expectedTax, String operation) {
        executeOperations(expectedTax, operation);
    }

    private void executeOperations(final String tax, final String operation) {
        System.setOut(mock(PrintStream.class));
        final Scanner scanner = when(mock(Scanner.class).nextLine()).thenReturn(operation, "").getMock();
        new OperationListener(OPERATIONS, GSON, scanner::nextLine, System.out::println).onOperation();
        verify(System.out).println(tax);
    }

    private static Stream<Arguments> provideOperations() throws IOException, URISyntaxException {
        final List<String> operations = read("OperationListenerTest/operations"), taxes = read("OperationListenerTest/taxes");
        return IntStream.range(0, operations.size()).mapToObj(i -> Arguments.of(taxes.get(i), operations.get(i)));
    }

    private static List<String> read(final String file) throws IOException, URISyntaxException {
        return readAllLines(Path.of(requireNonNull(OperationListenerTest.class.getClassLoader().getResource(file)).toURI()));
    }
}
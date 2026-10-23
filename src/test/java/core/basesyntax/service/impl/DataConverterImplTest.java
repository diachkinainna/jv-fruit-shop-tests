package core.basesyntax.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.model.FruitTransaction;
import core.basesyntax.service.DataConverter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataConverterImplTest {
    private List<String> transactions;
    private List<FruitTransaction> expected;
    private final DataConverter dataConverter = new DataConverterImpl();

    @BeforeEach
    void setUp() {
        transactions = new ArrayList<>();
        transactions.add("type,fruit,quantity");
        transactions.add("b,banana,20");
        transactions.add("s,banana,100");
        transactions.add("p,banana,13");
        transactions.add("r,apple,10");

        expected = new ArrayList<>();
        FruitTransaction firstFruitTransaction = new FruitTransaction();
        firstFruitTransaction.setOperation(FruitTransaction.Operation.fromCode("b"));
        firstFruitTransaction.setFruitName("banana");
        firstFruitTransaction.setQuantity(20);
        FruitTransaction secondFruitTransaction = new FruitTransaction();
        secondFruitTransaction.setOperation(FruitTransaction.Operation.fromCode("s"));
        secondFruitTransaction.setFruitName("banana");
        secondFruitTransaction.setQuantity(100);
        FruitTransaction thirdFruitTransaction = new FruitTransaction();
        thirdFruitTransaction.setOperation(FruitTransaction.Operation.fromCode("p"));
        thirdFruitTransaction.setFruitName("banana");
        thirdFruitTransaction.setQuantity(13);
        FruitTransaction fourthFruitTransaction = new FruitTransaction();
        fourthFruitTransaction.setOperation(FruitTransaction.Operation.fromCode("r"));
        fourthFruitTransaction.setFruitName("apple");
        fourthFruitTransaction.setQuantity(10);
        expected.add(firstFruitTransaction);
        expected.add(secondFruitTransaction);
        expected.add(thirdFruitTransaction);
        expected.add(fourthFruitTransaction);
    }

    @Test
    void convertToTransaction_validTransactions_ok() {
        List<FruitTransaction> actual = dataConverter.convertToTransactions(transactions);
        assertEquals(expected, actual);
    }

    @Test
    void convertToTransaction_emptyListTransactions_ok() {
        List<FruitTransaction> actual = dataConverter.convertToTransactions(List.of());
        assertEquals(List.of(), actual);
    }

    @Test
    void convertToTransaction_withZeroQuantity_ok() {
        transactions.add("r,apple,0");
        FruitTransaction fifthFruitTransaction = new FruitTransaction();
        fifthFruitTransaction.setOperation(FruitTransaction.Operation.fromCode("r"));
        fifthFruitTransaction.setFruitName("apple");
        fifthFruitTransaction.setQuantity(0);
        expected.add(fifthFruitTransaction);
        List<FruitTransaction> actual = dataConverter.convertToTransactions(transactions);
        assertEquals(expected, actual);
    }

    @Test
    void convertToTransaction_validTransactionsWithWhitespace_ok() {
        transactions.add(" r  , apple  ,   10 ");
        FruitTransaction fifthFruitTransaction = new FruitTransaction();
        fifthFruitTransaction.setOperation(FruitTransaction.Operation.fromCode("r"));
        fifthFruitTransaction.setFruitName("apple");
        fifthFruitTransaction.setQuantity(10);
        expected.add(fifthFruitTransaction);
        List<FruitTransaction> actual = dataConverter.convertToTransactions(transactions);
        assertEquals(expected, actual);
    }

    @Test
    void convertToTransaction_invalidOperation_notOk() {
        transactions.add("a,banana,13");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> dataConverter.convertToTransactions(transactions));
        assertEquals("Unknown code a", exception.getMessage());
    }

    @Test
    void convertToTransaction_invalidTransaction_notOk() {
        transactions.add("b,13");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> dataConverter.convertToTransactions(transactions));
        assertEquals("Invalid input data", exception.getMessage());
    }

    @Test
    void convertToTransaction_invalidFormatQuantity_notOk() {
        transactions.add("b,banana,one");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> dataConverter.convertToTransactions(transactions));
        assertEquals("Invalid number format", exception.getMessage());
    }

    @Test
    void convertToTransaction_negativeQuantity_notOk() {
        transactions.add("b,banana,-13");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> dataConverter.convertToTransactions(transactions));
        assertEquals("Quantity must be greater than or equal to zero",
                exception.getMessage());
    }
}

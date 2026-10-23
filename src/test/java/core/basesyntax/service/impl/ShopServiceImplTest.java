package core.basesyntax.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.db.FruitStorage;
import core.basesyntax.model.Fruit;
import core.basesyntax.model.FruitTransaction;
import core.basesyntax.service.ShopService;
import core.basesyntax.strategy.BalanceOperation;
import core.basesyntax.strategy.OperationHandler;
import core.basesyntax.strategy.OperationStrategy;
import core.basesyntax.strategy.OperationStrategyImpl;
import core.basesyntax.strategy.PurchaseOperation;
import core.basesyntax.strategy.ReturnOperation;
import core.basesyntax.strategy.SupplyOperation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShopServiceImplTest {
    private static final int TRANSACTION_BANANA_BALANCE = 107;
    private static Map<FruitTransaction.Operation, OperationHandler> operationHandlers;
    private List<FruitTransaction> fruitTransactions;
    private FruitStorage fruitStorage;
    private ShopService shopService;

    @BeforeAll
    static void beforeAll() {
        operationHandlers = new HashMap<>();
        operationHandlers.put(FruitTransaction.Operation.BALANCE, new BalanceOperation());
        operationHandlers.put(FruitTransaction.Operation.PURCHASE, new PurchaseOperation());
        operationHandlers.put(FruitTransaction.Operation.RETURN, new ReturnOperation());
        operationHandlers.put(FruitTransaction.Operation.SUPPLY, new SupplyOperation());
    }

    @BeforeEach
    void setUp() {
        fruitStorage = new FruitStorage();
        OperationStrategy operationStrategy = new OperationStrategyImpl(operationHandlers);
        shopService = new ShopServiceImpl(operationStrategy, fruitStorage);

        fruitTransactions = new ArrayList<>();
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
        fruitTransactions.add(firstFruitTransaction);
        fruitTransactions.add(secondFruitTransaction);
        fruitTransactions.add(thirdFruitTransaction);
        fruitTransactions.add(fourthFruitTransaction);
    }

    @Test
    void process_validDataAndEmptyStorage_ok() {
        shopService.process(fruitTransactions);
        Fruit expected = new Fruit();
        expected.setName("banana");
        expected.setQuantity(TRANSACTION_BANANA_BALANCE);

        assertEquals(expected, fruitStorage.get("banana").orElseThrow());
        assertEquals(2, fruitStorage.getFruits().size());
    }

    @Test
    void process_validDataAndFruitExist_ok() {
        Fruit banana = new Fruit();
        banana.setName("banana");
        banana.setQuantity(100);
        fruitStorage.add(banana);

        shopService.process(fruitTransactions);
        Fruit expected = new Fruit();
        expected.setName("banana");
        expected.setQuantity(100 + TRANSACTION_BANANA_BALANCE);

        assertEquals(expected, fruitStorage.get("banana").orElseThrow());
        assertEquals(2, fruitStorage.getFruits().size());
    }

    @Test
    void process_emptyDataAndEmptyStorage_ok() {
        shopService.process(new ArrayList<>());
        assertEquals(0, fruitStorage.getFruits().size());
    }

    @Test
    void process_negativeBalance_notOk() {
        FruitTransaction newFruitTransaction = new FruitTransaction();
        newFruitTransaction.setOperation(FruitTransaction.Operation.fromCode("p"));
        newFruitTransaction.setFruitName("banana");
        newFruitTransaction.setQuantity(200);
        fruitTransactions.add(newFruitTransaction);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> shopService.process(fruitTransactions));
        assertEquals("Balance cannot be less than zero", exception.getMessage());
    }
}

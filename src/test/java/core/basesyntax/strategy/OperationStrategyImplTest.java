package core.basesyntax.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.model.FruitTransaction;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class OperationStrategyImplTest {
    private static OperationStrategy operationStrategy;

    @BeforeAll
    static void beforeAll() {
        Map<FruitTransaction.Operation, OperationHandler> operationHandlers = new HashMap<>();
        operationHandlers.put(FruitTransaction.Operation.BALANCE, new BalanceOperation());
        operationHandlers.put(FruitTransaction.Operation.PURCHASE, new PurchaseOperation());
        operationHandlers.put(FruitTransaction.Operation.RETURN, new ReturnOperation());
        operationHandlers.put(FruitTransaction.Operation.SUPPLY, new SupplyOperation());
        operationStrategy = new OperationStrategyImpl(operationHandlers);
    }

    @Test
    void getOperationHandler_validOperationBalance_ok() {
        assertEquals(BalanceOperation.class, operationStrategy
                .getOperationHandler(FruitTransaction.Operation.BALANCE)
                .getClass());
    }

    @Test
    void getOperationHandler_validOperationPurchase_ok() {
        assertEquals(PurchaseOperation.class, operationStrategy
                .getOperationHandler(FruitTransaction.Operation.PURCHASE)
                .getClass());
    }

    @Test
    void getOperationHandler_validOperationReturn_ok() {
        assertEquals(ReturnOperation.class, operationStrategy
                .getOperationHandler(FruitTransaction.Operation.RETURN)
                .getClass());
    }

    @Test
    void getOperationHandler_validOperationSupply_ok() {
        assertEquals(SupplyOperation.class, operationStrategy
                .getOperationHandler(FruitTransaction.Operation.SUPPLY)
                .getClass());
    }

    @Test
    void getOperationHandler_nullOperation_ok() {
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                operationStrategy.getOperationHandler(null));
        assertEquals("Operation cannot be null", exception.getMessage());
    }
}

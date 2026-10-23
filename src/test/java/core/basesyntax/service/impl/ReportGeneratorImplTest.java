package core.basesyntax.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import core.basesyntax.db.FruitStorage;
import core.basesyntax.model.Fruit;
import core.basesyntax.service.ReportGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportGeneratorImplTest {
    private FruitStorage fruitStorage;
    private ReportGenerator reportGenerator;

    @BeforeEach
    void setUp() {
        fruitStorage = new FruitStorage();
        reportGenerator = new ReportGeneratorImpl(fruitStorage);
    }

    @Test
    void createReport_validData_ok() {
        Fruit banana = new Fruit();
        banana.setName("banana");
        banana.setQuantity(152);
        Fruit apple = new Fruit();
        apple.setName("apple");
        apple.setQuantity(90);
        fruitStorage.add(banana);
        fruitStorage.add(apple);

        String expected = "fruit,quantity" + System.lineSeparator()
                + "banana,152" + System.lineSeparator() + "apple,90";

        assertEquals(expected, reportGenerator.createReport());
    }

    @Test
    void createReport_emptyStorage_ok() {
        String expected = "fruit,quantity";
        assertEquals(expected, reportGenerator.createReport());
    }
}

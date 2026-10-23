package core.basesyntax.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.service.FileWriterService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class CsvFileWriterServiceTest {
    private static final String TEST_FINAL_REPORT_PATH = "src/main/resources/testFinalReport.csv";
    private final FileWriterService fileWriterService = new CsvFileWriterService();

    @Test
    void writeToFile_validDataAndPath() throws IOException {
        String data = "fruit,quantity" + System.lineSeparator()
                + "banana,152" + System.lineSeparator() + "apple,90";
        Path path = Path.of(TEST_FINAL_REPORT_PATH);
        try {
            fileWriterService.writeToFile(data, TEST_FINAL_REPORT_PATH);
            assertTrue(Files.exists(path));
            assertEquals(data, Files.readString(path));
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void writeToFile_nullData() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> fileWriterService.writeToFile(null, TEST_FINAL_REPORT_PATH));
        assertEquals("File path and data cannot be null", exception.getMessage());
    }

    @Test
    void writeToFile_nullPath() {
        String data = "fruit,quantity" + System.lineSeparator()
                + "banana,152" + System.lineSeparator() + "apple,90";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> fileWriterService.writeToFile(data, null));
        assertEquals("File path and data cannot be null", exception.getMessage());
    }
}

package core.basesyntax.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.service.FileReaderService;
import java.util.List;
import org.junit.jupiter.api.Test;

class CsvFileReaderServiceTest {
    private static final String VALID_PATH_TO_FILE = "src/main/resources/testReportToRead.csv";
    private static final int EXPECTED_LIST_SIZE = 9;
    private static final String INVALID_PATH_TO_FILE = "src/main/resources/invalidPath.csv";
    private final FileReaderService fileReaderService = new CsvFileReaderService();

    @Test
    void readFromFile_validPath_ok() {
        List<String> actual = fileReaderService.readFromFile(VALID_PATH_TO_FILE);
        assertNotNull(actual);
        assertEquals(EXPECTED_LIST_SIZE, actual.size());
    }

    @Test
    void readFromFile_invalidPath_notOk() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> fileReaderService.readFromFile(INVALID_PATH_TO_FILE));
        assertEquals("Can't read data from the file " + INVALID_PATH_TO_FILE,
                exception.getMessage());
    }

    @Test
    void readFromFile_nullPath_ok() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> fileReaderService.readFromFile(null));
        assertEquals("File path cannot be null", exception.getMessage());
    }
}

package ua.com.foxminded.orderlist;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileDataReaderTest {

    @Test
    void receiveSortedFilesShouldReturnGivenStringsWhenItGetsStartLogFile() {
        Map<String, String> actual = FileDataReader.receiveSortedFiles(TestingData.START_TEST, TestingData.LOG_FILE_TRIM_INDEX);
        Map<String, String> expected = new HashMap<>();
        expected.put("SVF", "2018-05-24_12:02:58.917");
        expected.put("FAM", "2018-05-24_12:13:04.512");
        expected.put("NHR", "2018-05-24_12:02:49.914");
        assertEquals(expected, actual);
    }

    @Test
    void receiveSortedFilesShouldShouldReturnEmptyResultWhenItGetsEmptyFile() {
        Map<String, String> actual = FileDataReader.receiveSortedFiles(TestingData.EMPTY_FILE, TestingData.ABBREVIATION_TRIM_INDEX);
        Map<String, String> expected = new HashMap<>();
        assertEquals(expected, actual);
    }

    @Test
    void receiveSortedFilesShouldThrowIllegalArgumentExceptionWhenItGetsWrongFilePath() {
        assertThrows(java.lang.IllegalArgumentException.class, () ->
                FileDataReader.receiveSortedFiles(TestingData.WRONG_FILE_PATH, TestingData.LOG_FILE_TRIM_INDEX));
    }
}

package ua.com.foxminded.orderlist;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DriversSorterTest {

    private static final String START_LOG = "start.log";
    private static final String END_LOG = "end.log";
    private static final String ABBREVIATIONS = "abbreviations.txt";
    private static final String WRONG_FORMAT_START_LOG = "start_wrong_time_format.log";
    private static final String ABBREVIATION_TEST = "abbreviations_test.txt";
    private static final String END_TEST = "end_test.log";
    private static final String UNMATCHING_ABBREVIATION = "abbreviation_test_unmatching_abbr.txt";

    @Test
    void createSortedReportShouldReturnGivenColumnWhenItGetsLogFiles() {
        DriversSorter driversSorter = new DriversSorter();
        String actual = driversSorter.createSortedReport(START_LOG, END_LOG, ABBREVIATIONS);
        String expected = "1.  Sebastian Vettel   | FERRARI                      |1:04.415" + "\n"
                + "2.  Daniel Ricciardo   | RED BULL RACING TAG HEUER    |1:12.013" + "\n"
                + "3.  Valtteri Bottas    | MERCEDES                     |1:12.434" + "\n"
                + "4.  Lewis Hamilton     | MERCEDES                     |1:12.460" + "\n"
                + "5.  Stoffel Vandoorne  | MCLAREN RENAULT              |1:12.463" + "\n"
                + "6.  Kimi Raikkonen     | FERRARI                      |1:12.639" + "\n"
                + "7.  Fernando Alonso    | MCLAREN RENAULT              |1:12.657" + "\n"
                + "8.  Sergey Sirotkin    | WILLIAMS MERCEDES            |1:12.706" + "\n"
                + "9.  Charles Leclerc    | SAUBER FERRARI               |1:12.829" + "\n"
                + "10. Sergio Perez       | FORCE INDIA MERCEDES         |1:12.848" + "\n"
                + "11. Romain Grosjean    | HAAS FERRARI                 |1:12.930" + "\n"
                + "12. Pierre Gasly       | SCUDERIA TORO ROSSO HONDA    |1:12.941" + "\n"
                + "13. Carlos Sainz       | RENAULT                      |1:12.950" + "\n"
                + "14. Esteban Ocon       | FORCE INDIA MERCEDES         |1:13.028" + "\n"
                + "15. Nico Hulkenberg    | RENAULT                      |1:13.065" + "\n"
                + "----------------------------------------------------------------" + "\n"
                + "16. Brendon Hartley    | SCUDERIA TORO ROSSO HONDA    |1:13.179" + "\n"
                + "17. Marcus Ericsson    | SAUBER FERRARI               |1:13.265" + "\n"
                + "18. Lance Stroll       | WILLIAMS MERCEDES            |1:13.323" + "\n"
                + "19. Kevin Magnussen    | HAAS FERRARI                 |1:13.393" + "\n";
        assertEquals(expected, actual);
    }

    @Test
    void createSortedReportShouldThrowIllegalArgumentExceptionWhenItGetsWrongFilePath() {
        assertThrows(java.lang.IllegalArgumentException.class, () -> {
            DriversSorter driversSorter = new DriversSorter();
            driversSorter.createSortedReport(START_LOG, END_LOG, TestingData.WRONG_FILE_PATH);
        });
    }

    @Test()
    void createSortedReportShouldThrowIllegalArgumentExceptionWhenItGetsWrongTimeFormat() {
        assertThrows(java.lang.IllegalArgumentException.class, () -> {
            DriversSorter driversSorter = new DriversSorter();
            driversSorter.createSortedReport(WRONG_FORMAT_START_LOG, END_TEST, ABBREVIATION_TEST);
        });
    }

    @Test
    void createSortedReportShouldReturnEmptyResultWhenItGetsEmptyAbbreviationFile() {
        DriversSorter driversSorter = new DriversSorter();
        String actual = driversSorter.createSortedReport(TestingData.START_TEST, END_TEST, TestingData.EMPTY_FILE);
        String expected = "";
        assertEquals(expected, actual);
    }

    @Test
    void createSortedReportShouldThrowIllegalArgumentExceptionWhenItGetsUnMatchingAbbreviations() {
        assertThrows(IllegalArgumentException.class, () -> {
            DriversSorter driversSorter = new DriversSorter();
            driversSorter.createSortedReport(TestingData.START_TEST, END_TEST, UNMATCHING_ABBREVIATION);
        });
    }
}

package ua.com.foxminded.orderlist;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DriversSorter {

    public String createSortedReport(String startLogPath, String endLogPath, String abbreviationsPath) {
        Map<String, String> startTime = FileDataReader.receiveSortedFiles(startLogPath,
                Constants.LOG_FILE_TRIM_INDEX);
        Map<String, String> endTime = FileDataReader.receiveSortedFiles(endLogPath,
                Constants.LOG_FILE_TRIM_INDEX);
        Map<String, String> abbreviations = FileDataReader.receiveSortedFiles(abbreviationsPath,
                Constants.ABBREVIATION_TRIM_INDEX);
        Map<String, String> racingTime = countRacingTime(startTime, endTime);
        Map<String, String> formattedAbbreviations = formatAbbreviations(abbreviations);
        Map<String, Long> unsortedDrivers = assignTimeToDrivers(racingTime, formattedAbbreviations);
        Map<String, Long> sortedDrivers = sortByTime(unsortedDrivers);
        return createReport(sortedDrivers);
    }

    private String createReport(Map<String, Long> sortedDrivers) {
        final int topRacersIndex = 16;
        StringBuilder sortedReport = new StringBuilder();
        int counter = 1;
        for (Map.Entry<String, Long> entry : sortedDrivers.entrySet()) {
            if (counter == topRacersIndex) {
                sortedReport.append("----------------------------------------------------------------").append("\n");
            }
            String key = entry.getKey();
            Long value = entry.getValue();
            createRecord(key, value, counter, sortedReport);
            counter++;
        }
        return sortedReport.toString();
    }

    private void createRecord(String key, long value, int counter, StringBuilder sortedReport) {
        final int lineLength = 48;
        String positionNumber = addPositionNumber(counter);
        String additionalSpaces = addSpaces(key.length(), lineLength);
        String timeInMillis = convertMillisToTimeFormat(value);
        sortedReport.append(positionNumber).append(key)
                .append(additionalSpaces).append("|")
                .append(timeInMillis).append("\n");
    }

    private String addPositionNumber(int number) {
        return (number < 10) ? number + ".  " : number + ". ";
    }

    private Map<String, String> countRacingTime(Map<String, String> startTime, Map<String, String> endTime) {
        Map<String, String> racingTime = new HashMap<>();
        for (Map.Entry<String, String> entry : startTime.entrySet()) {
            long startTimePoint = parseTime(entry.getValue()).toInstant(ZoneOffset.UTC).toEpochMilli();
            long endTimePoint = parseTime(endTime.get(entry.getKey())).toInstant(ZoneOffset.UTC).toEpochMilli();
            if (startTimePoint == 0 || endTimePoint == 0) {
                throw new IllegalArgumentException("The program received null startTimePoint or endTimePoint data");
            }
            long timeOfRacing = endTimePoint - startTimePoint;
            racingTime.put(entry.getKey(), Long.toString(timeOfRacing));
        }
        return racingTime;
    }

    private LocalDateTime parseTime(String currentLine) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss.SSS");
            return LocalDateTime.parse(currentLine, formatter);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Time parsing error in line: " + currentLine, ex);
        }
    }

    private Map<String, String> formatAbbreviations(Map<String, String> abbreviations) {
        return abbreviations.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> createAutoLine(e.getValue().trim().split("_"))));
    }

    private String createAutoLine(String[] separatedRacersInfo) {
        return separatedRacersInfo[0]
                + addSpaces(separatedRacersInfo[0].length(), 17)
                + ("| ")
                + separatedRacersInfo[1];
    }

    private String addSpaces(int stringLength, int maxLength) {
        final int spaces = 2;
        if (stringLength < maxLength) {
            return String.join("", Collections.nCopies(maxLength + spaces - stringLength, " "));
        } else {
            return String.join("", Collections.nCopies(spaces, " "));
        }
    }

    private Map<String, Long> assignTimeToDrivers(Map<String, String> racingTime,
                                                  Map<String, String> formattedAbbreviations) {
        return formattedAbbreviations.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, e -> Long.valueOf(racingTime.get(e.getKey()))));
    }

    private Map<String, Long> sortByTime(Map<String, Long> unsortedMap) {
        return unsortedMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    private String convertMillisToTimeFormat(Long millis) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes);
        long milliseconds = millis - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis));
        return String.format("%d:%02d.%03d", minutes, seconds, milliseconds);
    }
}

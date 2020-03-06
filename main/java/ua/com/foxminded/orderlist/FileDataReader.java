package ua.com.foxminded.orderlist;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

class FileDataReader {

    public static Map<String, String> receiveSortedFiles(String filePath, int trimIndex) {
        FileDataReader fileDataReader = new FileDataReader();
        String[] input = fileDataReader.sortDataFromFile(filePath);
        return Arrays.stream(input).collect(toMap(s -> s.trim().substring(0, 3), s -> s.trim().substring(trimIndex)));
    }

    private String[] sortDataFromFile(String filePath) {
        URL fileURL = null;
        try {
            fileURL = getClass().getClassLoader().getResource(filePath);
            if (fileURL == null) {
                throw new IllegalArgumentException("URI of the file" + filePath + "is empty");
            }
            URI fileUri = fileURL.toURI();
            File file = Paths.get(fileUri).toFile();
            try (Stream<String> sortedDataStream = Files.lines(Paths.get(file.getAbsolutePath()))) {
                return sortedDataStream.sorted().toArray(String[]::new);
            }
        } catch (IOException ex) {
            throw new IllegalArgumentException("Can't read the file: " + filePath, ex);
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Can't covert URL:" + fileURL + "to valid URI", ex);
        }
    }
}

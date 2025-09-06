import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

public class CsvParser {
    final Path parentPath;
    String separator = ",";
    File csvFile;

    public CsvParser(Path parentPath) {
        this.parentPath = parentPath;
    }

    public CsvParser(Path parentPath, String separator) {
        this(parentPath);
        this.separator = separator;
    }

    public void searchForCsv() {
        for (File file : parentPath.toAbsolutePath().toFile().listFiles()) {
            if (file.getName().endsWith(".csv")) {
                csvFile = file;
                assert csvFile.exists();
                assert !csvFile.isDirectory();
                return;
            }
        }
        throw new RuntimeException("Cannot find any CSV files in " + parentPath.toAbsolutePath());
    }

    public LinkedHashMap<String, List<String>> readCsv() {
        List<String> lines;
        try {
            lines = Files.readAllLines(csvFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assert lines.size() >= 2;

        String[] headers = lines.removeFirst().split(Pattern.quote(separator));


        LinkedHashMap<String, List<String>> columns = new LinkedHashMap<>();

        for (String line : lines) {
            String[] points = cleanBogusCommas(line).split(",");
            for (int i = 0; i < points.length; i++) {
                String point = points[i]; // point represents 1 datapoint e.g. the BPM for a specific night
                columns.computeIfAbsent(headers[i], _ -> new ArrayList<>()).add(point);
            }
        }
        return columns;

    }

    // remove nasty commas inside strings
    private String cleanBogusCommas(String s) {
        boolean insideQuotes = false;
        StringBuilder res = new StringBuilder(256);
        for (char c : s.toCharArray()) {
            if (c == ',' && insideQuotes) continue;
            if (c == '\"') insideQuotes = !insideQuotes;
            res.append(c);

        }
        return res.toString();
    }
}

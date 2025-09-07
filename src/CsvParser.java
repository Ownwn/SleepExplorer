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

    private List<String> headers = null;
    private List<String> lines = null;

    private List<String> headers() {
        assert (lines == null) == (headers == null); // yes this is on purpose

        if (headers != null) {
            return headers;
        }
        lines();
        assert headers != null;
        return headers;
    }

    private List<String> lines() {
        if (lines != null) {
            return lines;
        }

        try {
            lines = new ArrayList<>(Files.readAllLines(csvFile.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assert lines.size() >= 2;

        headers = Arrays.asList(lines.removeFirst().split(Pattern.quote(separator)));
        return lines;
    }

    public LinkedHashMap<String, List<String>> readCsvColumns() {
        LinkedHashMap<String, List<String>> columns = new LinkedHashMap<>();

        for (String line : lines()) {
            String[] points = cleanBogusCommas(line).split(",");
            for (int i = 0; i < points.length; i++) {
                String point = points[i]; // point represents 1 datapoint e.g. the BPM for a specific night
                columns.computeIfAbsent(headers().get(i), _ -> new ArrayList<>()).add(point);
            }
        }
        return columns;

    }

    public List<Map<String, String>> readCsvRows() {
        List<Map<String, String>> rows = new ArrayList<>();

        for (String line : lines()) {
            String[] points = cleanBogusCommas(line).split(",");

            Map<String, String> row = new LinkedHashMap<>();
            for (int i = 0; i < headers().size(); i++) {
                if (i >= points.length) {
                    row.put(headers().get(i), null);
                } else {
                    row.put(headers().get(i), points[i]);
                }

            }
            rows.add(row);
        }
        return rows;
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

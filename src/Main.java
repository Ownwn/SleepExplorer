import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Main {
    public static final Path csvPath = Path.of("");
    public static final String separator = ",";

    public static void main(String[] args) {
        CsvParser parser = new CsvParser(csvPath, separator);
        parser.searchForCsv();
        Map<String, List<String>> columns = parser.readCsv();

    }


}
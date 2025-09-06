import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class Main {
    public static final Path csvPath = Path.of("");
    public static final String separator = ",";

    public static void main(String[] args) {
        CsvParser parser = new CsvParser(csvPath, separator);
        parser.searchForCsv();
        List<Day> days = parseDays(parser.readCsv());

    }

    public static List<Day> parseDays(LinkedHashMap<String, List<String>> columns) {
        // todo
        return null;
    }


    record Day(Times times, Durations durations, Vitals vitals) { // contains data for a day
    }

    record Times(Date fromDate, Date toDate, LocalDateTime bedTime, LocalDateTime wakeTime) {
    }

    record Durations(Duration sleepTime, Duration remTime, Duration deepTime, Duration inBedTime,
                     Duration awakeDuringNightTime, Duration fellAsleepIn) {
    }


    record Vitals(double efficiency, double sleepBPM, double dayBPM, double wakingBPM, double hrv, double sleepHRV,
                  double sleepSpO2, double respRate) {
    }


}
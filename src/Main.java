import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static final Path csvPath = Path.of("");
    public static final String separator = ",";

    public static void main(String[] args) {
        CsvParser parser = new CsvParser(csvPath, separator);
        parser.searchForCsv();

        List<Day> days = parseDays(parser.readCsvRows());
        System.out.println(days.stream().map(Day::toString).collect(Collectors.joining("\n")));

    }

    public static List<Day> parseDays(List<Map<String, String>> rows) {

        assert rows.stream().map(Map::size).distinct().count() == 1L;

        List<Day> days = new ArrayList<>();
        for (var row : rows) {
            Map<Data, String> data = Arrays.stream(Data.values())
                    .collect(HashMap::new, (map, val) -> map.put(val, row.get(val.key)), (_, _) -> {
                        throw new Error();
                    });

            Map<Data, String> isos = new HashMap<>();
            isos.put(Data.FROMDATE, data.remove(Data.FROMDATE));
            isos.put(Data.TODATE, data.remove(Data.TODATE));
            isos.put(Data.BEDTIME, data.remove(Data.BEDTIME));
            isos.put(Data.WAKETIME, data.remove(Data.WAKETIME));

            days.add(new Day(isos, data));
        }

        return days;
    }

    enum Data {
        FROMDATE("fromDate"),
        TODATE("toDate"),
        BEDTIME("bedtime"),
        WAKETIME("waketime"),
        INBEDTIME("inBed"),
        AWAKETIME("awake"),
        FELLASLEEPTIME("fellAsleepIn"),
        ASLEEPTIME("asleep"),
        EFFICIENCY("efficiency"),
        REMTIME("REM"),
        DEEPTIME("deep"),
        SLEEPBPM("sleepBPM"),
        DAYBPM("dayBPM"),
        WAKINGBPM("wakingBPM"),
        AWAKEHRV("hrv"),
        SLEEPHRV("sleepHRV"),
        SPO2("SpO2Avg"),
        RESPRATE("respAvg");

        final String key;

        Data(String key) {
            this.key = key;
        }
    }

    record Day(Map<Data, String> isoTimes, Map<Data, String> numericalValues) {
        @Override
        public String toString() {
            return "Evening of " + isoTimes.get(Data.FROMDATE) + ", efficiency: " + numericalValues.get(Data.EFFICIENCY);
        }
    }
}
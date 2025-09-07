import javax.xml.crypto.Data;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Main {
    public static final Path csvPath = Path.of("");
    public static final String separator = ",";

    public static void main(String[] args) {
        CsvParser parser = new CsvParser(csvPath, separator);
        parser.searchForCsv();

        List<Map<Data, String>> days = parseDays(parser.readCsvRows());
        System.out.println(days.stream().map(Map::toString).collect(Collectors.joining("\n")));

    }

    public static List<Map<Data, String>> parseDays(List<Map<String, String>> rows) {

        assert rows.stream().map(Map::size).distinct().count() == 1L;

        List<Map<Data, String>> days = new ArrayList<>();
        for (var row : rows) {


            Map<Data, String> day = Arrays.stream(Data.values())
                    .collect(HashMap::new, (map, val) -> Objects.requireNonNull(map.put(val, row.get(val.key))), (_, _) -> {throw new Error();});
            days.add(day);
        }

        return days;
    }

//    public static Day buildDay(Map<String, String> row) {
//        Times times = Times.fromData(row);
//        Durations durations = Durations.fromData(row);
//        Vitals vitals = Vitals.fromData(row);
//
//        return new Day(times, durations, vitals);
//
//    }



    static BiFunction<Data, Map<String, List<String>>, String> grabber = (data, columns) -> columns.get(data.key).removeFirst();

    record Day(Times times, Durations durations, Vitals vitals) { // contains data for a day

    }


    record Times(Date fromDate, Date toDate, LocalDateTime bedTime, LocalDateTime wakeTime) {
        static Times fromString(String fromDate, String toDate, String bedTime, String wakeTime) {
            return null; // todo
        }
        static Times fromData(Map<String, List<String>> columns) {
            return Times.fromString(
                    grabber.apply(Data.FROMDATE, columns),
                    grabber.apply(Data.TODATE, columns),
                    grabber.apply(Data.BEDTIME, columns),
                    grabber.apply(Data.WAKETIME, columns)
            );
        }
    }

    record Durations(Duration sleepTime, Duration remTime, Duration deepTime, Duration inBedTime,
                     Duration awakeDuringNightTime, Duration fellAsleepIn) {
        static Durations fromString(String sleepTime, String remTime, String deepTime, String inBedTime, String awakeDuringNightTime, String fellAsleepIn) {
            return null; // todo
        }

        static Durations fromData(Map<String, List<String>> columns) {
            return Durations.fromString(
                    grabber.apply(Data.ASLEEPTIME, columns),
                    grabber.apply(Data.REMTIME, columns),
                    grabber.apply(Data.DEEPTIME, columns),
                    grabber.apply(Data.INBEDTIME, columns),
                    grabber.apply(Data.AWAKETIME, columns),
                    grabber.apply(Data.FELLASLEEPTIME, columns)
            );
        }
    }


    record Vitals(double efficiency, double sleepBPM, double dayBPM, double wakingBPM, double hrv, double sleepHRV,
                  double sleepSpO2, double respRate) {
        static Vitals fromString(String efficiency, String sleepBPM, String dayBPM, String wakingBPM, String hrv, String sleepHRV, String sleepSpO2, String respRate) {
            return null; // todo
        }

        static Vitals fromData(Map<String, List<String>> columns) {
            return Vitals.fromString(
                    grabber.apply(Data.EFFICIENCY, columns),
                    grabber.apply(Data.SLEEPBPM, columns),
                    grabber.apply(Data.DAYBPM, columns),
                    grabber.apply(Data.WAKINGBPM, columns),
                    grabber.apply(Data.AWAKEHRV, columns),
                    grabber.apply(Data.SLEEPHRV, columns),
                    grabber.apply(Data.SPO2, columns),
                    grabber.apply(Data.RESPRATE, columns)
            );
        }
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


}
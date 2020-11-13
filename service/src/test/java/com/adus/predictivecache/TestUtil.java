package com.adus.predictivecache;

import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestUtil {
    public static final String USER_101 = "User101";
    public static final String PAGE_1 = "page-1";
    public static final String PAGE_2 = "page-2";
    public static final String PAGE_3 = "page-3";
    public static final String TUESDAY_11_HRS_10_MINS_AM = "2020-04-21 11:10:56";
    public static final String TUESDAY_11_HRS_15_MINS_AM = "2020-04-21 11:15:56";

    public static final LocalDateTime TUESDAY_11_HRS_10_MINS_AM_PARSED = LocalDateTime.parse(TUESDAY_11_HRS_10_MINS_AM, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    public static final LocalDateTime TUESDAY_04_HRS_20_MINS_AM_PARSED = LocalDateTime.parse("2020-04-21 04:20:56", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    public static <K, V> Map<K, V> toMap(List<Pair<K, V>> entries) {
        Map<K, V> map = new HashMap<>();
        entries.forEach(e -> map.put(e.getFirst(), e.getSecond()));
        return map;
    }

    public static Map<String, Map> createDOWHourRangeIndependentPatternTrend() {
        return toMap(
                Arrays.asList(
                        Pair.of("hourlyTrend",
                                toMap(Arrays.asList(Pair.of("hourRange_9", 8), Pair.of("hourRange_12", 5)))
                        ),
                        Pair.of("dowTrend",
                                toMap(Arrays.asList(Pair.of("dow_1", 7), Pair.of("dow_2", 6)))
                        )
                )
        );
    }

    public static Map<String, Map> createDOWHourRangeDependentPatternTrend() {
        return toMap(Arrays.asList(
                Pair.of("dow_1", toMap(Arrays.asList(Pair.of("hourRange_9", 4), Pair.of("hourRange_12", 3)))),
                Pair.of("dow_2", toMap(Arrays.asList(Pair.of("hourRange_9", 4), Pair.of("hourRange_12", 2))))
                )
        );
    }
}

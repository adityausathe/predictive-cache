package com.adus.predictivecache.framework.services.matchers;

import com.adus.predictivecache.framework.config.PredCacheProperties;
import com.adus.predictivecache.framework.dao.PatternTrend;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class IndependentTrendMatcher implements TrendMatcher {
    private static final String DOW_TREND = "dowTrend";
    private static final String HOURLY_TREND = "hourlyTrend";
    private final PredCacheProperties predCacheProperties;

    public IndependentTrendMatcher(PredCacheProperties predCacheProperties) {
        this.predCacheProperties = predCacheProperties;
    }

    @Override
    public boolean matches(String hourRangeId, String dowId, PatternTrend trend) {
        boolean hourlyTrendMatched = isHourlyTrendMatched(hourRangeId, trend);
        boolean dowTrendMatched = isDOWTrendMatched(dowId, trend);
        return hourlyTrendMatched && dowTrendMatched;
    }

    @SuppressWarnings("unchecked")
    private boolean isDOWTrendMatched(String dowId, PatternTrend patternTrend) {
        return isTrendMatched(dowId, patternTrend.getTrend().get(DOW_TREND));
    }

    @SuppressWarnings("unchecked")
    private boolean isHourlyTrendMatched(String hourRangeId, PatternTrend patternTrend) {
        return isTrendMatched(hourRangeId, patternTrend.getTrend().get(HOURLY_TREND));
    }

    private boolean isTrendMatched(String trendId, Map<String, Integer> trend) {
        List<Map.Entry<String, Integer>> reverseSortedTrend
                = trend.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.<Integer>naturalOrder().reversed()))
                .collect(Collectors.toList());

        for (int i = 0; i < reverseSortedTrend.size() * predCacheProperties.getTrendPercentile(); i++) {
            if (trendId.equals(reverseSortedTrend.get(i).getKey())) {
                return true;
            }
        }
        return false;
    }

}

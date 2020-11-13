package com.adus.predictivecache.framework.services.matchers;

import com.adus.predictivecache.framework.config.PredCacheProperties;
import com.adus.predictivecache.framework.dao.PatternTrend;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DependentTrendMatcher implements TrendMatcher {

    private final PredCacheProperties predCacheProperties;

    public DependentTrendMatcher(PredCacheProperties predCacheProperties) {
        this.predCacheProperties = predCacheProperties;
    }

    @Override
    public boolean matches(String hourRangeId, String dowId, PatternTrend trend) {
        @SuppressWarnings("unchecked")
        Map<String, Integer> hourlyTrend = trend.getTrend().get(dowId);
        if (hourlyTrend == null) {
            return false;
        }
        List<Map.Entry<String, Integer>> reverseSortedTrend
                = hourlyTrend.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.<Integer>naturalOrder().reversed()))
                .collect(Collectors.toList());

        for (int i = 0; i < reverseSortedTrend.size() * predCacheProperties.getTrendPercentile(); i++) {
            if (hourRangeId.equals(reverseSortedTrend.get(i).getKey())) {
                return true;
            }
        }
        return false;
    }
}

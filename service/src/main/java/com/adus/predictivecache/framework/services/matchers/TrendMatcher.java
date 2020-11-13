package com.adus.predictivecache.framework.services.matchers;

import com.adus.predictivecache.framework.dao.PatternTrend;

public interface TrendMatcher {
    boolean matches(String hourRangeId, String dowId, PatternTrend trend);
}

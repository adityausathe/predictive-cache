package com.adus.predictivecache.framework.services.matchers;

import com.adus.predictivecache.framework.config.PredCacheProperties;
import com.adus.predictivecache.framework.dao.PatternTrend;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static com.adus.predictivecache.TestUtil.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DependentTrendMatcherTest {

    @Mock
    private PredCacheProperties predCacheProperties;
    @InjectMocks
    private DependentTrendMatcher dependentTrendMatcher;
    private PatternTrend dowHourRangeDependentPatternTrend;

    @BeforeEach
    void setUp() {
        dowHourRangeDependentPatternTrend = getDowHourRangeDependentPatternTrend();
    }

    @Test
    void matches_100Percentile() {
        // do not filter out any trend slots
        when(predCacheProperties.getTrendPercentile()).thenReturn(1.0f);
        assertTrue(dependentTrendMatcher.matches("hourRange_9", "dow_1", dowHourRangeDependentPatternTrend));
    }

    @Test
    void doesNotMatch_100Percentile() {
        // do not filter out any trend slots
        when(predCacheProperties.getTrendPercentile()).thenReturn(1.0f);
        assertFalse(dependentTrendMatcher.matches("hourRange_3", "dow_1", dowHourRangeDependentPatternTrend));
    }

    @Test
    void matches_50Percentile() {
        // filter out half of the trend slots
        when(predCacheProperties.getTrendPercentile()).thenReturn(0.5f);
        assertTrue(dependentTrendMatcher.matches("hourRange_9", "dow_1", dowHourRangeDependentPatternTrend));
    }

    @Test
    void doesNotMatch_50Percentile() {
        // filter out half of the trend slots
        when(predCacheProperties.getTrendPercentile()).thenReturn(0.5f);
        assertFalse(dependentTrendMatcher.matches("hourRange_12", "dow_1", dowHourRangeDependentPatternTrend));
    }

    private PatternTrend getDowHourRangeDependentPatternTrend() {
        PatternTrend patternTrend = new PatternTrend();
        patternTrend.setSequence(Arrays.asList(PAGE_1, PAGE_2));
        patternTrend.setTrend(createDOWHourRangeDependentPatternTrend());
        return patternTrend;
    }
}
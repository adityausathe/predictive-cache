package com.adus.predictivecache.framework.services;

import com.adus.predictivecache.framework.dao.PatternTrend;
import com.adus.predictivecache.framework.dao.TrendRepository;
import com.adus.predictivecache.framework.dao.UserTrend;
import com.adus.predictivecache.framework.services.matchers.MatchersFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static com.adus.predictivecache.TestUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatternPredictionServiceTest {

    @Mock
    private TrendRepository trendRepository;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private MatchersFactory matchersFactory;
    @InjectMocks
    private PatternPredictionService patternPredictionService;
    private PatternTrend patternTrend;

    @Test
    void predictNextItemInSequence_TrendMatchFound() {
        setupTrend();
        when(matchersFactory.get().matches("hourRange_9", "dow_2", patternTrend)).thenReturn(true);
        Optional<String> nextResource = patternPredictionService.predictNextItemInSequence(USER_101, Collections.singletonList(PAGE_1), TUESDAY_11_HRS_10_MINS_AM_PARSED);

        assertEquals(Optional.of(PAGE_2), nextResource);
    }

    @Test
    void predictNextItemInSequence_TrendMatchNotFound() {
        setupTrend();
        when(matchersFactory.get().matches("hourRange_3", "dow_2", patternTrend)).thenReturn(false);
        Optional<String> nextResource = patternPredictionService.predictNextItemInSequence(USER_101, Collections.singletonList(PAGE_1), TUESDAY_04_HRS_20_MINS_AM_PARSED);

        assertFalse(nextResource.isPresent());
    }

    @Test
    void predictNextItemInSequence_PrefixSequenceTooLong() {
        setupTrend();
        Optional<String> nextResource = patternPredictionService.predictNextItemInSequence(USER_101, Arrays.asList(PAGE_1, PAGE_2), TUESDAY_11_HRS_10_MINS_AM_PARSED);

        assertFalse(nextResource.isPresent());
    }

    private void setupTrend() {
        UserTrend userTrend = new UserTrend();
        userTrend.setUserId(USER_101);
        userTrend.setPatternTrends(Collections.singletonList(getPatternTrend()));
        when(trendRepository.findByUserId(USER_101)).thenReturn(userTrend);
    }

    private PatternTrend getPatternTrend() {
        patternTrend = new PatternTrend();
        patternTrend.setSequence(Arrays.asList(PAGE_1, PAGE_2));
        patternTrend.setTrend(createDOWHourRangeIndependentPatternTrend());
        return patternTrend;
    }
}
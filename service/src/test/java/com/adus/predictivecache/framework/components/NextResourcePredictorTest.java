package com.adus.predictivecache.framework.components;

import com.adus.predictivecache.framework.components.cache.OngoingSequenceCache;
import com.adus.predictivecache.framework.components.cache.ResourceResultCache;
import com.adus.predictivecache.framework.config.RequestTimeStampProvider;
import com.adus.predictivecache.framework.services.PatternPredictionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static com.adus.predictivecache.TestUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NextResourcePredictorTest {

    private static final LocalDateTime ANY_TIMESTAMP = LocalDateTime.now();
    @Mock
    private PatternPredictionService patternPredictionService;
    @Mock
    private RequestTimeStampProvider requestTimeStampProvider;
    @Mock
    private ResourceResultCache resourceResultCache;

    private NextResourcePredictor nextResourcePredictor;
    private OngoingSequenceCache ongoingSequenceCache;

    @BeforeEach
    void setUp() {
        ongoingSequenceCache = new OngoingSequenceCache();
        ongoingSequenceCache.remove(USER_101);
        nextResourcePredictor = new NextResourcePredictor(patternPredictionService, requestTimeStampProvider,
                ongoingSequenceCache, resourceResultCache);
        when(requestTimeStampProvider.getRequestTimeStamp()).thenReturn(ANY_TIMESTAMP);
    }

    @AfterEach
    void tearDown() {
        ongoingSequenceCache.remove(USER_101);
    }

    @Test
    void predict_WhenTrendIs_Page1_Page2() {
        // assuming underlying trend is "page-1" -> "page-2"
        canPredictWhileServicePage1Resource();
        cannotPredictWhileServingPage2Resource();
    }

    @Test
    void predict_WhenTrendsAre_Page1_Page2_AND_Page2_Page3() {
        // assuming underlying trends are-
        //         1. "page-1" -> "page-2"
        //         2. "page-2" -> "page-3"
        canPredictWhileServicePage1Resource();
        canPredictWhileServingPage2Resource();
    }

    private void canPredictWhileServicePage1Resource() {
        Optional<String> expectedNextResource = Optional.of(PAGE_2);
        when(patternPredictionService.predictNextItemInSequence(USER_101, Collections.singletonList(PAGE_1), ANY_TIMESTAMP))
                .thenReturn(expectedNextResource);

        Optional<String> nextResource = nextResourcePredictor.predict(USER_101, PAGE_1);
        assertEquals(expectedNextResource, nextResource);

        assertEquals(Collections.singletonList(PAGE_1), ongoingSequenceCache.get(USER_101));
        verifyNoInteractions(resourceResultCache);
    }

    private void canPredictWhileServingPage2Resource() {
        Optional<String> expectedNextResource = Optional.of(PAGE_3);
        when(patternPredictionService.predictNextItemInSequence(USER_101, Arrays.asList(PAGE_1, PAGE_2), ANY_TIMESTAMP))
                .thenReturn(Optional.empty());
        when(patternPredictionService.predictNextItemInSequence(USER_101, Collections.singletonList(PAGE_2), ANY_TIMESTAMP))
                .thenReturn(expectedNextResource);

        Optional<String> nextResource = nextResourcePredictor.predict(USER_101, PAGE_2);
        assertEquals(expectedNextResource, nextResource);

        assertEquals(Collections.singletonList(PAGE_2), ongoingSequenceCache.get(USER_101));
        verifyNoInteractions(resourceResultCache);
    }

    private void cannotPredictWhileServingPage2Resource() {
        when(patternPredictionService.predictNextItemInSequence(USER_101, Arrays.asList(PAGE_1, PAGE_2), ANY_TIMESTAMP))
                .thenReturn(Optional.empty());
        when(patternPredictionService.predictNextItemInSequence(USER_101, Collections.singletonList(PAGE_2), ANY_TIMESTAMP))
                .thenReturn(Optional.empty());

        Optional<String> nextResource = nextResourcePredictor.predict(USER_101, PAGE_2);
        assertEquals(Optional.empty(), nextResource);

        assertNull(ongoingSequenceCache.get(USER_101));
        verify(resourceResultCache).remove(USER_101);
    }
}
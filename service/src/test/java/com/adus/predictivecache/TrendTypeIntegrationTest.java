package com.adus.predictivecache;

import com.adus.predictivecache.framework.dao.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static com.adus.predictivecache.TestUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

abstract class TrendTypeIntegrationTest {

    private static final String API_PAGE_1 = "/api/page-1?userId={userId}&original_timestamp={timestamp}";
    private static final String API_PAGE_2 = "/api/page-2?userId={userId}&original_timestamp={timestamp}";

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AccessRecordRepository accessRecordRepository;
    @Autowired
    private TrendRepository trendRepository;

    @BeforeEach
    void setUp() {
        cleanupRepositories();
        createTrend();
        restTemplate.postForEntity("/predictive-cache/metrics/reset", null, Void.class);
    }

    private void createTrend() {
        UserTrend userTrend = new UserTrend();
        userTrend.setUserId(TestUtil.USER_101);
        PatternTrend patternTrend = new PatternTrend();
        patternTrend.setSequence(Arrays.asList("page-1", "page-2"));
        patternTrend.setTrend(createPatternTrend());
        userTrend.setPatternTrends(Collections.singletonList(patternTrend));

        trendRepository.save(userTrend);
    }

    protected abstract Map<String, Map> createPatternTrend();

    private void cleanupRepositories() {
        accessRecordRepository.deleteAll();
        trendRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        cleanupRepositories();
    }


    @Test
    void predicted_CachedResultReturned_BecauseAccessSequenceMatched() {
        ResponseEntity<String> page1Response = restTemplate.getForEntity(API_PAGE_1, String.class, TestUtil.USER_101, TestUtil.TUESDAY_11_HRS_10_MINS_AM);
        assertEquals(HttpStatus.OK, page1Response.getStatusCode());

        ResponseEntity<String> page2Response = restTemplate.getForEntity(API_PAGE_2, String.class, TestUtil.USER_101, TestUtil.TUESDAY_11_HRS_15_MINS_AM);
        assertEquals(HttpStatus.OK, page2Response.getStatusCode());

        assertMetric("/predictive-cache/metrics/requests", 2);
        assertMetric("/predictive-cache/metrics/cached-result-computations", 1);
        assertMetric("/predictive-cache/metrics/cache-hits", 1);

        assertAccessRecordsPopulated(PAGE_1, PAGE_2);
    }

    @Test
    void predicted_CachedResultNotReturned_BecauseAccessSequenceSemiMatched() {
        ResponseEntity<String> page1Response = restTemplate.getForEntity(API_PAGE_1, String.class, TestUtil.USER_101, TestUtil.TUESDAY_11_HRS_10_MINS_AM);
        assertEquals(HttpStatus.OK, page1Response.getStatusCode());

        ResponseEntity<String> page2Response = restTemplate.getForEntity(API_PAGE_1, String.class, TestUtil.USER_101, TestUtil.TUESDAY_11_HRS_15_MINS_AM);
        assertEquals(HttpStatus.OK, page2Response.getStatusCode());

        assertMetric("/predictive-cache/metrics/requests", 2);
        assertMetric("/predictive-cache/metrics/cached-result-computations", 2);
        assertMetric("/predictive-cache/metrics/cache-hits", 0);

        assertAccessRecordsPopulated(PAGE_1, PAGE_1);
    }

    @Test
    void notPredicted_CachedResultNotReturned_BecauseAccessSequenceNotMatched() {
        ResponseEntity<String> page1Response = restTemplate.getForEntity(API_PAGE_2, String.class, TestUtil.USER_101, TestUtil.TUESDAY_11_HRS_10_MINS_AM);
        assertEquals(HttpStatus.OK, page1Response.getStatusCode());

        ResponseEntity<String> page2Response = restTemplate.getForEntity(API_PAGE_2, String.class, TestUtil.USER_101, TestUtil.TUESDAY_11_HRS_15_MINS_AM);
        assertEquals(HttpStatus.OK, page2Response.getStatusCode());

        assertMetric("/predictive-cache/metrics/requests", 2);
        assertMetric("/predictive-cache/metrics/cached-result-computations", 0);
        assertMetric("/predictive-cache/metrics/cache-hits", 0);

        assertAccessRecordsPopulated(PAGE_2, PAGE_2);
    }

    private void assertMetric(String metricEndpoint, int expectedCount) {
        ResponseEntity<String> response = restTemplate.getForEntity(metricEndpoint, String.class);
        if (expectedCount > 0) {
            assertEquals(TestUtil.USER_101 + "," + expectedCount, response.getBody());
        } else {
            assertNull(response.getBody());
        }
    }

    private void assertAccessRecordsPopulated(String firstPage, String secondPage) {
        List<AccessRecord> accessRecords = accessRecordRepository.findAll();
        assertEquals(2, accessRecords.size());
        accessRecords.sort(Comparator.comparing(AccessRecord::getResourceId));
        assertEquals(USER_101, accessRecords.get(0).getUserId());
        assertEquals(firstPage, accessRecords.get(0).getResourceId());
        assertEquals(TUESDAY_11_HRS_10_MINS_AM, accessRecords.get(0).getAccessTime());

        assertEquals(USER_101, accessRecords.get(1).getUserId());
        assertEquals(secondPage, accessRecords.get(1).getResourceId());
        assertEquals(TUESDAY_11_HRS_15_MINS_AM, accessRecords.get(1).getAccessTime());
    }

}

package com.adus.predictivecache;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest(
        properties = {
                "com.adus.predictivecache.trend-type=Dependent",
                "com.adus.predictivecache.mongoDbUri=mongodb://localhost:27017/test-pred-cache-frame-db",
                "server.port=56788"
        },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class DependentTrendTypeIntegrationTest extends TrendTypeIntegrationTest {

    @Override
    protected Map<String, Map> createPatternTrend() {
        return TestUtil.createDOWHourRangeDependentPatternTrend();
    }

}

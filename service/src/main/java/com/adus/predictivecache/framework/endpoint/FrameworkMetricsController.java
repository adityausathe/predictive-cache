package com.adus.predictivecache.framework.endpoint;

import com.adus.predictivecache.framework.metrics.MetricRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/predictive-cache/metrics")
@RestController
public class FrameworkMetricsController {

    @GetMapping("/requests")
    public String getRequests() {
        return MetricRegistry.getRequestReport();
    }

    @GetMapping("/cached-result-computations")
    public String getCachedResultComputations() {
        return MetricRegistry.getCachedResultComputationReport();
    }

    @GetMapping("/cache-hits")
    public String getCacheHits() {
        return MetricRegistry.getHitReport();
    }

    @PostMapping("/reset")
    public void reset() {
        MetricRegistry.clear();
    }
}

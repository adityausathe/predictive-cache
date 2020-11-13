package com.adus.predictivecache.framework.endpoint;

import com.adus.predictivecache.framework.services.TrendCalibrationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/predictive-cache/calibration")
public class TrendCalibrationController {
    private final TrendCalibrationService trendCalibrationService;

    public TrendCalibrationController(TrendCalibrationService trendCalibrationService) {
        this.trendCalibrationService = trendCalibrationService;
    }

    @PostMapping("/")
    public void invokeCalibration() {
        trendCalibrationService.calibrate();
    }
}

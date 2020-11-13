package com.adus.predictivecache.framework.services;

import com.adus.predictivecache.framework.config.PredCacheProperties;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class TrendCalibrationService {

    private final PredCacheProperties predCacheProperties;

    public TrendCalibrationService(PredCacheProperties predCacheProperties) {
        this.predCacheProperties = predCacheProperties;
    }

    public void calibrate() {
        File analyticsFolder = new File(predCacheProperties.getAnalyticsFolder());
        File analyticsScript = new File(analyticsFolder, "main.py");
        if (!analyticsScript.exists()) {
            throw new IllegalStateException("Analytics scripts not found");
        }
        try {
            ProcessBuilder pb = new ProcessBuilder("python", analyticsScript.getAbsolutePath(), "Dependent", "100", "0.5");
            pb.directory(analyticsFolder);
            Process process = pb.start();
            int returnStatus = process.waitFor();
            System.out.println(returnStatus);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

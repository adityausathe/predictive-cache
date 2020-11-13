package com.adus.predictivecache.framework.config;

import java.time.LocalDateTime;

public interface RequestTimeStampProvider {
    LocalDateTime getRequestTimeStamp();
}

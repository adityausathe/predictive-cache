package com.adus.predictivecache.framework.services;

import com.adus.predictivecache.framework.config.RequestTimeStampProvider;
import com.adus.predictivecache.framework.dao.AccessRecord;
import com.adus.predictivecache.framework.dao.AccessRecordRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class AccessRecordService {

    private final AccessRecordRepository accessRecordRepository;
    private final RequestTimeStampProvider requestTimeStampProvider;

    public AccessRecordService(AccessRecordRepository accessRecordRepository, RequestTimeStampProvider requestTimeStampProvider) {
        this.accessRecordRepository = accessRecordRepository;
        this.requestTimeStampProvider = requestTimeStampProvider;
    }

    public void recordAccess(String userId, String resourceId) {
        AccessRecord accessRecord = new AccessRecord();
        accessRecord.setUserId(userId);
        accessRecord.setResourceId(resourceId);
        accessRecord.setAccessTime(requestTimeStampProvider.getRequestTimeStamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        accessRecordRepository.save(accessRecord);
    }

}

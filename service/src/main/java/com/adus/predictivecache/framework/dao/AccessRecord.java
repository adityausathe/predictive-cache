package com.adus.predictivecache.framework.dao;

import com.mongodb.lang.NonNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class AccessRecord {
    @Id
    private String id;
    @NonNull
    private String userId;
    @NonNull
    private String resourceId;
    @NonNull
    private String accessTime;
}

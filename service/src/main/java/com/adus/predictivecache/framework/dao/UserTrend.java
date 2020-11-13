package com.adus.predictivecache.framework.dao;

import com.mongodb.lang.NonNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document
public class UserTrend {
    @Id
    private String id;
    @NonNull
    private String userId;
    @NonNull
    private List<PatternTrend> patternTrends;
}

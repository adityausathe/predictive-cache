package com.adus.predictivecache.framework.dao;

import com.mongodb.lang.NonNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Document
public class PatternTrend {
    @NonNull
    private List<String> sequence;
    @NonNull
    private Map<String, Map> trend;
}

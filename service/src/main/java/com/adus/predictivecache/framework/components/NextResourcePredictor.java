package com.adus.predictivecache.framework.components;

import com.adus.predictivecache.framework.components.cache.OngoingSequenceCache;
import com.adus.predictivecache.framework.components.cache.ResourceResultCache;
import com.adus.predictivecache.framework.config.RequestTimeStampProvider;
import com.adus.predictivecache.framework.services.PatternPredictionService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class NextResourcePredictor {
    private final PatternPredictionService patternPredictionService;
    private final RequestTimeStampProvider requestTimeStampProvider;
    private final OngoingSequenceCache ongoingSequenceCache;
    private final ResourceResultCache resourceResultCache;

    public NextResourcePredictor(PatternPredictionService patternPredictionService,
                                 RequestTimeStampProvider requestTimeStampProvider,
                                 OngoingSequenceCache ongoingSequenceCache,
                                 ResourceResultCache resourceResultCache) {
        this.patternPredictionService = patternPredictionService;
        this.requestTimeStampProvider = requestTimeStampProvider;
        this.ongoingSequenceCache = ongoingSequenceCache;
        this.resourceResultCache = resourceResultCache;
    }

    public Optional<String> predict(String userId, String currentResourceId) {
        List<String> executedSequenceTillNow = getSequenceUntilNowIncludingCurrent(userId, currentResourceId);

        // generate all possible prefix sequences
        Stream<List<String>> possibleSequencesTillNow = IntStream.range(0, executedSequenceTillNow.size())
                .mapToObj(startIdx -> executedSequenceTillNow.subList(startIdx, executedSequenceTillNow.size()));

        LocalDateTime requestTimeStamp = requestTimeStampProvider.getRequestTimeStamp();
        // check for each possible sequences till now
        // preference for largest sequence first
        Optional<Pair<List<String>, Optional<String>>> nextSequenceItem = possibleSequencesTillNow
                .map(sequenceTillNow -> Pair.of(sequenceTillNow, patternPredictionService.predictNextItemInSequence(userId, sequenceTillNow, requestTimeStamp)))
                .filter(p -> p.getSecond().isPresent())
                .findFirst();

        if (nextSequenceItem.isPresent()) {
            ongoingSequenceCache.put(userId, nextSequenceItem.get().getFirst());
        } else {
            ongoingSequenceCache.remove(userId);
            resourceResultCache.remove(userId);
        }
        return nextSequenceItem.flatMap(Pair::getSecond);
    }

    private List<String> getSequenceUntilNowIncludingCurrent(String userId, String currentResourceId) {
        List<String> sequenceTillNow = ongoingSequenceCache.get(userId);
        if (CollectionUtils.isEmpty(sequenceTillNow)) {
            sequenceTillNow = new ArrayList<>();
        }
        sequenceTillNow.add(currentResourceId);
        return sequenceTillNow;
    }
}

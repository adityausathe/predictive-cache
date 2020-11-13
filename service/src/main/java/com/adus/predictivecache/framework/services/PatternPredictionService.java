package com.adus.predictivecache.framework.services;

import com.adus.predictivecache.framework.dao.PatternTrend;
import com.adus.predictivecache.framework.dao.TrendRepository;
import com.adus.predictivecache.framework.dao.UserTrend;
import com.adus.predictivecache.framework.services.matchers.MatchersFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatternPredictionService {
    private static final String HOUR_RANGE = "hourRange_";
    private static final String DOW = "dow_";
    private final TrendRepository trendRepository;
    private final MatchersFactory matchersFactory;

    public PatternPredictionService(TrendRepository trendRepository,
                                    MatchersFactory matchersFactory) {
        this.trendRepository = trendRepository;
        this.matchersFactory = matchersFactory;
    }

    public Optional<String> predictNextItemInSequence(String userId, List<String> prefixSequence, LocalDateTime requestTimeStamp) {
        UserTrend trend = trendRepository.findByUserId(userId);

        if (trend == null) {
            return Optional.empty();
        }
        List<PatternTrend> matchingPatternTrends = trend.getPatternTrends().stream()
                .filter(pTrend -> matchSequences(prefixSequence, pTrend.getSequence()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(matchingPatternTrends)) {
            return Optional.empty();
        }

        Optional<List<String>> matchedSequence = applyTimeSeriesTrendFilter(matchingPatternTrends, requestTimeStamp);
        if (matchedSequence.isPresent()) {
            String nextInTheSequence = matchedSequence.get().get(prefixSequence.size());
            return Optional.of(nextInTheSequence);
        }
        return Optional.empty();
    }

    private Optional<List<String>> applyTimeSeriesTrendFilter(List<PatternTrend> matchingPatternTrends, LocalDateTime requestTimeStamp) {
        String hourRangeId = getHourRangeId(requestTimeStamp.getHour());
        String dowId = getDOWId(requestTimeStamp.getDayOfWeek());

        // preference for longer sequences, in case of a tie
        matchingPatternTrends.sort(Comparator.<PatternTrend, Integer>comparing(patternTrend -> patternTrend.getSequence().size()).reversed());

        Optional<PatternTrend> matchedTrend = matchingPatternTrends
                .stream()
                .filter(patternTrend -> matchersFactory.get().matches(hourRangeId, dowId, patternTrend))
                .findFirst();
        if (matchedTrend.isPresent()) {
            return matchedTrend.map(PatternTrend::getSequence);
        }
        return Optional.empty();
    }

    private String getHourRangeId(int hour) {
        return HOUR_RANGE + (((int) (hour / 3)) * 3);
    }

    private String getDOWId(DayOfWeek dayOfWeek) {
        return DOW + dayOfWeek.getValue();
    }

    private boolean matchSequences(List<String> prefixSequence, List<String> sequence) {
        if (prefixSequence.size() >= sequence.size()) {
            return false;
        }
        for (int i = 0; i < prefixSequence.size(); i++) {
            if (!prefixSequence.get(i).equals(sequence.get(i)))
                return false;
        }
        return true;
    }
}

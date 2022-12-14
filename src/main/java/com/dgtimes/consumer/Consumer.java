package com.dgtimes.consumer;
import java.time.LocalDateTime;
import java.util.*;

import com.dgtimes.consumer.async.AsyncQueue;
import com.dgtimes.consumer.news.News;
import com.dgtimes.consumer.searchLog.SearchLog;
import com.dgtimes.consumer.searchRanking.SearchRanking;
import com.dgtimes.consumer.news.NewsRepository;
import com.dgtimes.consumer.searchLog.SearchLogRepository;
import com.dgtimes.consumer.searchRanking.SearchRankingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class Consumer<E> {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    private final NewsRepository newsRepository;
    private final SearchRankingRepository searchRankingRepository;
    private final SearchLogRepository searchLogRepository;

    private AsyncQueue<News> queue = new AsyncQueue<>();

    private ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "TEST_QUEUE")
    public void RabbitmqHandler(String message) throws IOException {
        News newsDto = objectMapper.readValue(message, News.class);
        queue.add(newsDto);
    }

    @Scheduled(cron = "* * * * * *")
    @Async
    public void saveNews() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<News> newsDtoList = queue.pollList();
        newsRepository.saveAll(newsDtoList);

        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        logger.info("Current Thread : {} - {} - {}???", Thread.currentThread().getName(), totalTimeMillis, newsDtoList.size());
    }

    @Transactional
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.HOURS)
    @Async
    public SearchRanking createSearchRanking(){

        HashMap<String, Integer> check = new HashMap<>();
        List<SearchLog> searchLogList = searchLogRepository.findAllById();

        if (searchLogList == null){
            SearchRanking ranking = searchRankingRepository.findTopByOrderByDateDesc();
            searchRankingRepository.save(ranking);
            return ranking;
        } // ?????? ?????? ????????? ????????? ?????? ????????? ?????? ?????? ????????? ??????

        for (SearchLog log : searchLogList){
            check.put(log.getKeyword(), check.getOrDefault(log.getKeyword(), 1) +1);
        }

        List<Map.Entry<String, Integer>> entryList = new LinkedList<>(check.entrySet());

        entryList.sort((((o1, o2) -> check.get(o2.getKey())- check.get(o1.getKey()))));

        List<String> keywordList = new ArrayList<>();

        if(searchLogList.size() == 0) {
            logger.info("WARING : ?????? ?????? ????????? ????????????.");
            return null;
        }

        LocalDateTime date = searchLogList.get(searchLogList.size()-1).getTimestamp();

        for (Map.Entry<String, Integer> entry : entryList){
            keywordList.add(entry.getKey());
            if (keywordList.size() >= 10){
                break;
            }
        }

        logger.info("????????? ????????? = {}", keywordList);

        SearchRanking ranking = new SearchRanking(date, keywordList);
        searchRankingRepository.save(ranking);

        return ranking;
    }

}

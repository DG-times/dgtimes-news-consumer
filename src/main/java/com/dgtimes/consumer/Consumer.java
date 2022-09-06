package com.dgtimes.consumer;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class Consumer<E> {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    private final RelatedKeywordService relatedKeywordService;

    private AsyncQueue<NewsDto> queue = new AsyncQueue<>();

    private ObjectMapper objectMapper = new ObjectMapper();


    @RabbitListener(queues = "TEST_QUEUE")
    public void handler(String message) throws IOException {
        NewsDto newsDto = objectMapper.readValue(message, NewsDto.class);
        queue.add(newsDto);
    }

    @Scheduled(cron = "* * * * * *")
    @Async
    public void saveNews() throws InterruptedException{
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<NewsDto> newsDtoList = queue.pollList();
        newsDtoList.stream().forEach(x -> readJSON(x.getKeywords()));

        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        logger.info("Current Thread : {} - {} - {}개", Thread.currentThread().getName(), totalTimeMillis, newsDtoList.size());
    }

    public void setRelatedKeyword(List<String> keywords) {
        for (int a = 0; a < keywords.size()-1; a++) {
            String pickKeyword = keywords.get(a);
            for (int b = a+1; b < keywords.size(); b++) {
                relatedKeywordService.searchRelatedValue(pickKeyword,keywords.get(b));
            }
        }
    }

    // ["정치", "트럼프", "힐러리"]
    public void readJSON(List<String> keyword_list) {
        keyword_list.stream().forEach(key -> relatedKeywordService.searchValue(key));
        setRelatedKeyword(keyword_list);
    }

    /*
    public void saveNews() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<News> newsDtoList = queue.pollList();
        testRepository.saveAll(newsDtoList);

        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        logger.info("Current Thread : {} - {} - {}개", Thread.currentThread().getName(), totalTimeMillis, newsDtoList.size());
    }
     */

}

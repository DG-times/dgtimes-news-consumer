package com.dgtimes.consumer;
import java.util.*;

import com.dgtimes.consumer.async.AsyncQueue;
import com.dgtimes.consumer.model.News;
import com.dgtimes.consumer.repository.NewsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class Consumer<E> {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    private final NewsRepository newsRepository;

    private AsyncQueue<News> queue = new AsyncQueue<>();

    private ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "TEST_QUEUE")
    public void handler(String message) throws IOException {
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
        logger.info("Current Thread : {} - {} - {}개", Thread.currentThread().getName(), totalTimeMillis, newsDtoList.size());
    }

}

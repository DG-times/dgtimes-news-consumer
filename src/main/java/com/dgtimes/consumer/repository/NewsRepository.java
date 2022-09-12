package com.dgtimes.consumer.repository;

import com.dgtimes.consumer.model.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
}


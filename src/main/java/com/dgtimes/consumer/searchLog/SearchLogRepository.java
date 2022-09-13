package com.dgtimes.consumer.searchLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchLogRepository extends JpaRepository<SearchLog, String> {
    @Query(value = "select * " +
            "from search_log " +
            "where timestamp >= date_add" +
            "(now(), INTERVAL -1 HOUR)", nativeQuery = true)
    List<SearchLog> findAllById();
}

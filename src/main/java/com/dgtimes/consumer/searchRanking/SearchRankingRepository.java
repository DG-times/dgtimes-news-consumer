package com.dgtimes.consumer.searchRanking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchRankingRepository extends JpaRepository<SearchRanking, Long> {
    List<SearchRanking> findTop2ByOrderByDateDesc();
    SearchRanking findTopByOrderByDateDesc();

}


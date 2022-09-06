package com.dgtimes.consumer;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RelatedKeywordRepository extends Neo4jRepository<Keyword, Long> {
    //    카워드 검색
    @Query("match (k:Keyword) WHERE k.value CONTAINS $value RETURN k limit 1")
    Keyword findByValue(@Param("value") String value);
    //    키워드 등록
    @Query("create (k:Keyword {value:$value1})")
    void queryByInsertValue(@Param("value1") String value1);

    // 등록된 키워드에 가중치 적용
    @Query("match (k:Keyword {value:$value1})\n" + "match (relatedK:Keyword {value:$value2})\n" +
            "create (k)-[r:related {weight:$weight}]->(relatedK)")
    void queryByRelated(@Param("value1") String value1,
                        @Param("value2") String value2,
                        @Param("weight") Integer weight);

    // 키워드, 연관키워드 관계가 있는지 조회
    @Query("match (k:Keyword {value:$value1})-[r:related]->(k2:Keyword {value:$value2}) return r.weight limit 1")
    Integer findByRelatedValue(@Param("value1") String value1,
                               @Param("value2") String value2);

    // 등록된 관계가 있을경우, 해당 관계의 값을 업데이트
    @Query("match (k:Keyword {value:$value1})-[r:related {weight:$weight}]-(k2:Keyword {value:$value2}) \n" +
            "set r.weight = $setWeight\n" +
            "return r")
    void queryBySetRelatedValue(@Param("value1") String value1,
                                @Param("weight") Integer weight,
                                @Param("value2") String value2,
                                @Param("setWeight") Integer setWeight);


    // 키워드 검색시 연관키워드와 가중치 결과 출력
    @Query("match (k:Keyword {value:$value1})-[r]-(k2:Keyword) where (r.weight) is not null \n" +
            "with k2,r.weight as weight\n" +
            "return k2{.value, weight}")
    List<Keyword> findByAllRelatedKeyword(@Param("value1") String value1);
}

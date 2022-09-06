package com.dgtimes.consumer;

import lombok.Getter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
@Getter
public class Keyword {
    @Id
    @GeneratedValue
    private Long id;

    private String value;

    private Integer weight;

    private Object relatedValue;

    // 없으면 안됨
    private Keyword() {
        // Empty constructor required as of Neo4j API 2.0.5
    };

    @Override
    public String toString() {
        return "Keyword{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", weight='" + weight + '\'' +
                ", relatedValue=" + relatedValue +
                '}';
    }
//    뉴스에서 가져온 연관키워드
//    검색에서 가져온 연관키워드

//    @Relationship(type = "related")
//    public Set<Keyword> related;
//
//    public void worksWith(Keyword keyword) {
//        if (related == null) {
//            related = new HashSet<>();
//        }
//        System.out.println("keyword = " + keyword);
//        related.add(keyword);
//    }
}

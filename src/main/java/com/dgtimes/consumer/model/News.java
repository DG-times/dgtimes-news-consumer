package com.dgtimes.consumer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "news")
@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "test-sequence-generator")
    @GenericGenerator(
            name = "test-sequence-generator",
            strategy = "sequence",
            parameters = {
                    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = SequenceStyleGenerator.DEF_SEQUENCE_NAME),
                    @Parameter(name = SequenceStyleGenerator.INITIAL_PARAM, value = "1"),
                    @Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1000"),
                    @Parameter(name = AvailableSettings.PREFERRED_POOLED_OPTIMIZER, value = "pooled-lo")
            }
    )
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String writer;
    private String publisher;
    @Column(columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date publishedDate;
    private long category;
    private String thumbnailUrl;
    @Column(length = 1000)
    private String mainUrl;
}
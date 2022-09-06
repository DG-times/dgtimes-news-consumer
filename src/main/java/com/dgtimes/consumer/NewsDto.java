package com.dgtimes.consumer;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsDto {
    private String title;
    private String content;
    private String writer;
    private String publisher;
    private int category;
    private String tag;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp publishedDate;
    private String thumbnailUrl;
    private String mainUrl;

    private List<String> keywords;


    @Override
    public String toString() {
        return "NewsDto{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", writer='" + writer + '\'' +
                ", publisher='" + publisher + '\'' +
                ", category=" + category +
                ", tag='" + tag + '\'' +
                ", publishedDate=" + publishedDate +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", mainUrl='" + mainUrl + '\'' +
                ", keywords=" + keywords +
                '}';
    }
}

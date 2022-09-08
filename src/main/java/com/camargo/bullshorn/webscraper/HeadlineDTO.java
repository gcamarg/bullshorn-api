package com.camargo.bullshorn.webscraper;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Document("headlines")
public class HeadlineDTO {

    @Id
    private String id;
    private String url;
    @TextIndexed
    private String headline;
    @CreationTimestamp
    private Date createdAt;

    public HeadlineDTO(String url, String headline, Date createdAt) {
        this.url = url;
        this.headline = headline;
        this.createdAt = createdAt;
    }

    public HeadlineDTO() {
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

package com.site.bemystory.domain;

import org.apache.catalina.LifecycleState;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class BookForm {
    private String subject;
    private List<String> contents = new ArrayList<>();
    private List<String> img_urls = new ArrayList<>();
    private String story_type;
    private LocalDate date;


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    public List<String> getImg_urls() {
        return img_urls;
    }

    public void setImg_urls(List<String> img_urls) {
        this.img_urls = img_urls;
    }

    public String getStory_type() {
        return story_type;
    }

    public void setStory_type(String story_type) {
        this.story_type = story_type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}

package com.raymond210129.nctucmc.dataStructure;

import java.util.Date;

public class Notification {
    private String content;
    private String link;
    private int option;
    private String rank;
    private String title;
    private String topic;
    private long startTime;
    private String user;

    public Notification(){}
    public Notification(String title, String content, String topic, String rank, String link, int option, String user)
    {
        this.title = title;
        this.content = content;
        this.topic = topic;
        this.rank = rank;
        this.link = link;
        this.option = option;
        this.user = user;
        this.startTime = new Date().getTime();
    }

    public String getContent()
    {
        return content;
    }

    public String getLink()
    {
        return link;
    }

    public int getOption()
    {
        return option;
    }

    public String getRank()
    {
        return rank;
    }

    public String getTitle()
    {
        return title;
    }

    public String getTopic()
    {
        return topic;
    }

    public String getUser()
    {
        return user;
    }

    public long getTime()
    {
        return startTime;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public void setOption(int option)
    {
        this.option = option;
    }

    public void setRank(String rank)
    {
        this.rank = rank;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public void setTime(long startTime)
    {
        this.startTime = startTime;
    }
}

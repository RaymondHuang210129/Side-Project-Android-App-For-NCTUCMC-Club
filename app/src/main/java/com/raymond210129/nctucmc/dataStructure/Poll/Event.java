package com.raymond210129.nctucmc.dataStructure.Poll;

public class Event {
    private String content;
    private Long createTime;
    private String formKey;
    private String title;
    private String type;
    private long deadline;
    private String group;
    private String user;

    public Event() {
    }

    public Event(String content, Long createTime, String formKey, String title, String type, long deadline, String group, String user) {
        this.content = content;
        this.createTime = createTime;
        this.formKey = formKey;
        this.title = title;
        this.type = type;
        this.deadline = deadline;
        this.group = group;
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}

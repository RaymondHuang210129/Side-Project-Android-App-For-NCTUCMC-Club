package com.raymond210129.nctucmc.dataStructure.Poll;

public class Form {
    private String content;
    private String memberKey;

    public Form() {
    }

    public Form(String content, String memberKey) {
        this.content = content;
        this.memberKey = memberKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMemberKey() {
        return memberKey;
    }

    public void setMemberKey(String memberKey) {
        this.memberKey = memberKey;
    }
}

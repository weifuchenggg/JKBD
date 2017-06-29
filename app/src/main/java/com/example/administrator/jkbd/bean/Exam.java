package com.example.administrator.jkbd.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/29.
 */

public class Exam implements Serializable{


    /**
     * id : 19
     * question : 这个标志是何含义？
     * answer : 1
     * item1 : 休息区
     * item2 : 服务区
     * item3 : 停车场
     * item4 : 观景台
     * explains : 这个是休息区，服务区是有加油，通车，吃饭的组合标志，停车场就是一个P，图中没有加油站和吃饭的，所以它只是一个休息区。
     * url : http://images.juheapi.com/jztk/c1c2subject1/19.jpg
     */

    private int id;
    private String question;
    private String answer;
    private String item1;
    private String item2;
    private String item3;
    private String item4;
    private String explains;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getItem1() {
        return item1;
    }

    public void setItem1(String item1) {
        this.item1 = item1;
    }

    public String getItem2() {
        return item2;
    }

    public void setItem2(String item2) {
        this.item2 = item2;
    }

    public String getItem3() {
        return item3;
    }

    public void setItem3(String item3) {
        this.item3 = item3;
    }

    public String getItem4() {
        return item4;
    }

    public void setItem4(String item4) {
        this.item4 = item4;
    }

    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

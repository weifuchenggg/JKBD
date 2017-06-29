package com.example.administrator.jkbd.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */

public class Result implements Serializable{

    /**
     * error_code : 0
     * reason : ok
     * result : [{"id":9,"question":"这个标志是何含义？","answer":"2","item1":"距有人看守铁路道口50米","item2":"距无人看守铁路道口100米","item3":"距有人看守铁路道口100米","item4":"距无人看守铁路道口50米","explains":"一道红线是50米，二道100米。","url":"http://images.juheapi.com/jztk/c1c2subject1/9.jpg"},{"id":10,"question":"这个标志是何含义？","answer":"2","item1":"分向行驶车道","item2":"掉头和左转合用车道","item3":"禁止左转和掉头车道","item4":"直行和左转合用车道","explains":"左转和掉头合并在一个标志里，你应该能看到的。","url":"http://images.juheapi.com/jztk/c1c2subject1/10.jpg"},{"id":33,"question":"这个标志是何含义？",question":"将转向灯开关向下拉，右转向灯亮。","answer":"2","item1":"正确","item2":"错误","item3":"","item4":"","explains":"转向灯操作：上右下左。","url":"http://images.juheapi.com/jztk/c1c2subject1/1209.jpg"}]
     */

    private int error_code;
    private String reason;
    private List<Exam> result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<Exam> getResult() {
        return result;
    }

    public void setResult(List<Exam> result) {
        this.result = result;
    }

    public static class ResultBean {
    }


}

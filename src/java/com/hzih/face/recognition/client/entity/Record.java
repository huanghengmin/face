package com.hzih.face.recognition.client.entity;

import java.util.List;

/**
 * Created by Administrator on 15-8-2.
 */
public class Record {
    private String taskId;
    private String code;
    private String msg;
    private List<Result> results;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}

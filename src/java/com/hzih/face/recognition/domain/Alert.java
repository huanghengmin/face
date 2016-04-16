package com.hzih.face.recognition.domain;

import java.util.Date;

/**
 * Created by Administrator on 15-9-17.
 */
public class Alert {
    private long id;
    private String taskId;
    private String terminalId;
    private String featureId;
    private int score;//相似值
    private String ipcFilePath;//前端IPC捕捉图像
    private String personFilePath;//比对库中图像
    private String personInfo;
    private Date alertTime;
    private String alertLevel;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getIpcFilePath() {
        return ipcFilePath;
    }

    public void setIpcFilePath(String ipcFilePath) {
        this.ipcFilePath = ipcFilePath;
    }

    public String getPersonFilePath() {
        return personFilePath;
    }

    public void setPersonFilePath(String personFilePath) {
        this.personFilePath = personFilePath;
    }

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public String getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(String personInfo) {
        this.personInfo = personInfo;
    }

    public Date getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(Date alertTime) {
        this.alertTime = alertTime;
    }

    public String getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(String alertLevel) {
        this.alertLevel = alertLevel;
    }
}

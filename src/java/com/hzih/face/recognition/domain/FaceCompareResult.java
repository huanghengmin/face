package com.hzih.face.recognition.domain;

import java.util.Date;

/**
 * Created by Administrator on 15-9-15.
 */
public class FaceCompareResult {

    private long id;
    private String taskId;
    private String deviceId;
    private String featureId;
    private int score;
    private Date dateTime;
    private String compareResult;
    private String badFriend;//团伙标记 有则,1_taskId;无则,0
    private String imagePath;

    private double xLeft;
    private double yLeft;
    private double xRight;
    private double yRight;
    private double xMouth;
    private double yMouth;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getCompareResult() {
        return compareResult;
    }

    public void setCompareResult(String compareResult) {
        this.compareResult = compareResult;
    }

    public String getBadFriend() {
        return badFriend;
    }

    public void setBadFriend(String badFriend) {
        this.badFriend = badFriend;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public double getxLeft() {
        return xLeft;
    }

    public void setxLeft(double xLeft) {
        this.xLeft = xLeft;
    }

    public double getyLeft() {
        return yLeft;
    }

    public void setyLeft(double yLeft) {
        this.yLeft = yLeft;
    }

    public double getxRight() {
        return xRight;
    }

    public void setxRight(double xRight) {
        this.xRight = xRight;
    }

    public double getyRight() {
        return yRight;
    }

    public void setyRight(double yRight) {
        this.yRight = yRight;
    }

    public double getxMouth() {
        return xMouth;
    }

    public void setxMouth(double xMouth) {
        this.xMouth = xMouth;
    }

    public double getyMouth() {
        return yMouth;
    }

    public void setyMouth(double yMouth) {
        this.yMouth = yMouth;
    }
}

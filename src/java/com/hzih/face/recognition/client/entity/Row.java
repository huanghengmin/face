package com.hzih.face.recognition.client.entity;

import java.util.List;

/**
 * Created by Administrator on 15-8-2.
 */
public class Row {
    private int index;
    private String featureId;
    private int score;
    private List<Field> fields;
    private double xLeft;
    private double yLeft;
    private double xRight;
    private double yRight;
    private double xMouth;
    private double yMouth;
    private double[] position = {xLeft,yLeft,xRight,yRight,xMouth,yMouth};
    private String imageBase64;
    private String logicDbId;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
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

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getLogicDbId() {
        return logicDbId;
    }

    public void setLogicDbId(String logicDbId) {
        this.logicDbId = logicDbId;
    }
}

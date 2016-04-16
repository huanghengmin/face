package com.hzih.face.recognition.domain;


import com.hzih.face.recognition.entity.SipXml;
import com.hzih.face.recognition.utils.Configuration;

/**
 * Created by Administrator on 15-8-3.
 */
public class FaceInfoCompRequest extends SipXml {

    private String taskId;
    private String photoName;
    private String filePath;
    private String featureId;
    private String score;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public FaceInfoCompRequest xmlToBean(byte[] buff) {
        Configuration config = new Configuration(buff);
        FaceInfoCompRequest faceInfoCompRequest = config.getFaceInfoCompRequest();
        return faceInfoCompRequest;
    }
}

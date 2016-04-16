package com.hzih.face.recognition.client.entity;

import java.util.List;

/**
 * Created by Administrator on 15-8-2.
 */
public class Condition {

    private String taskId;
    private String taskPriority;
    private String position;
    private String image;
    private int candidateNum;
    private int threshold;

    private List<LogIcDbId> logIcDbIds;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(String taskPriority) {
        this.taskPriority = taskPriority;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getCandidateNum() {
        return candidateNum;
    }

    public void setCandidateNum(int candidateNum) {
        this.candidateNum = candidateNum;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<LogIcDbId> getLogIcDbIds() {
        return logIcDbIds;
    }

    public void setLogIcDbIds(List<LogIcDbId> logIcDbIds) {
        this.logIcDbIds = logIcDbIds;
    }

    @Override
    public String toString() {
        String result =
                "<CONDITION TASKID=\""+taskId+"\" TASKPRIORITY=\""+taskPriority+"\">\r\n" +
                "<IMAGE OPERATION=\"=\" POSITION=\""+position+"\">"+image+"</IMAGE>\r\n" +
                "<CANDIDATENUM OPERATION=\"=\">"+candidateNum+"</CANDIDATENUM>\r\n" +
                "<THRESHOLD OPERATION=\"=\">"+threshold+"</THRESHOLD>\r\n";
        for(LogIcDbId logIcDbId : logIcDbIds) {
            result += logIcDbId.toString();
        }
        return result + "</CONDITION>\r\n";
    }
}

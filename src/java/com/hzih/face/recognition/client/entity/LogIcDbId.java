package com.hzih.face.recognition.client.entity;

/**
 * Created by Administrator on 15-8-2.
 */
public class LogIcDbId {

    private int index;
    private String gender;
    private String birthDayF;
    private String birthDayL;
    private String nation;
    private String reg;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDayF() {
        return birthDayF;
    }

    public void setBirthDayF(String birthDayF) {
        this.birthDayF = birthDayF;
    }

    public String getBirthDayL() {
        return birthDayL;
    }

    public void setBirthDayL(String birthDayL) {
        this.birthDayL = birthDayL;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    @Override
    public String toString() {
        return "<LOGICDBID INDEX=\""+index+"\">\r\n" +
                "<GENDER OPERATION=\"=\">"+gender+"</GENDER>\r\n" +
                "<BIRTHDAY OPERATION=\">=\">"+birthDayF+"</BIRTHDAY>\r\n" +
                "<BIRTHDAY OPERATION=\"<=\">"+birthDayL+"</BIRTHDAY>\r\n" +
                "<NATION OPERATION=\"=\">"+nation+"</NATION>\r\n" +
                "<REG OPERATION=\"=\">"+reg+"</REG>\r\n" +
                "</LOGICDBID>\r\n";
    }
}

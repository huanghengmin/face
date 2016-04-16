package com.hzih.face.recognition.client.entity;

import java.util.List;

/**
 * Created by Administrator on 15-8-2.
 */
public class Conditions {

    private List<Condition> conditions;

    private int image;
    private int position;
    private List<String> featureIds;


    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setImage(boolean isImage) {
        int i = 0;
        if(isImage){
            i = 1;
        }
        setImage(i);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setPosition(boolean isPosition) {
        int i = 0;
        if(isPosition){
            i = 1;
        }
        setPosition(i);
    }

    public List<String> getFeatureIds() {
        return featureIds;
    }

    public void setFeatureIds(List<String> featureIds) {
        this.featureIds = featureIds;
    }

    public String toStringFars1ToN() {
        String result = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" +
                "<CONDITIONS>\r\n";
        for (Condition condition : conditions) {
            result += condition.toString();
        }
        return result + "</CONDITIONS>";
    }

    public String toStringGetPersonInfo() {
        String result = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" +
                "<CONDITIONS>\r\n";
        result += "<IMAGE>"+image+"</IMAGE>\r\n" +
                "<POSITION>"+position+"</POSITION>\r\n"+
                "<FEATUREIDS>\r\n";
        for(String featureId : featureIds){
            result += "<FEATUREID>"+featureId+"</FEATUREID>\r\n";

        }
        return result +"</FEATUREIDS>\r\n" +
                "</CONDITIONS>";
    }
}

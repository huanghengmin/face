package com.hzih.face.recognition.client.entity;

import java.util.List;

/**
 * Created by Administrator on 15-8-1.
 */
public class XmlString {
    public static String makeLicense(String username,String password) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" +
                "<LICENSE>\r\n" +
                "<USERNAME>"+username+"</USERNAME>\r\n" +
                "<PASSWORD>"+password+"</PASSWORD>\r\n" +
                "</LICENSE>";
    }


    public static String makeInformation(
            String taskId,String taskPriority,
            String position,int candidateNum,
            int threshold,int gender,
            String birthDayF,String birthDayL,
            String nation,String reg,String image) {

        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" +
                "<CONDITIONS>\r\n" +
                "<CONDITION TASKID=\""+taskId+"\" TASKPRIORITY=\""+taskPriority+"\">\r\n" +
                "<IMAGE OPERATION=\"=\" POSITION=\""+position+"\">"+image+"</IMAGE>\r\n" +
                "<CANDIDATENUM OPERATION=\"=\">"+candidateNum+"</CANDIDATENUM>\r\n" +
                "<THRESHOLD OPERATION=\"=\">"+threshold+"</THRESHOLD>\r\n" +
                "<LOGICDBID INDEX=\"1\">\r\n" +
                "<GENDER OPERATION=\"=\">"+gender+"</GENDER>\r\n" +
                "<BIRTHDAY OPERATION=\">=\">"+birthDayF+"</BIRTHDAY>\r\n" +
                "<BIRTHDAY OPERATION=\"<=\">"+birthDayL+"</BIRTHDAY>\r\n" +
                "<NATION OPERATION=\"=\">"+nation+"</NATION>\r\n" +
                "<REG OPERATION=\"=\"></REG>\r\n" +
                "</LOGICDBID>\r\n" +
                "</CONDITION>\r\n"+
                "</CONDITIONS>";
    }


    public static String makeFars1TONResultInformation(String jobId, int start, int end) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" +
                "<CONDITION>\r\n" +
                "<JOBID>"+jobId+"</JOBID>\r\n" +
                "<START>"+start+"</START>\r\n" +
                "<END>"+end+"</END>\r\n" +
                "</CONDITION>";
    }

    public static String makeFarsGetPersonInfo(List<String> featureIds) {
        String xml =  "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" +
                "<CONDITIONS>\r\n" +
                "<IMAGE>"+1+"</IMAGE>\r\n" +
                "<POSITION>"+1+"</POSITION>\r\n" +
                "<FEATUREIDS>\r\n";
                for(String featureId : featureIds){
                    xml += "<FEATUREID>"+featureId+"</FEATUREID>\r\n";

                }
        return xml +"</FEATUREIDS>\r\n" +
                "</CONDITIONS>";
    }

    public static String makeFarsGetLogicDbInfos() {
        return "";
    }

    public static String makeFarsAddPerson(String imageBase64) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" +
                "<PERSON_INFO>\r\n" +
                "<LOGIC_DB_ID>01</LOGIC_DB_ID>\r\n" +
                "<SRC_PERSON_ID>R20150803200221xxx</SRC_PERSON_ID>\r\n" +
                "<NAME>张三</NAME>\r\n" +
                "<IDCARD>33030303030303030</IDCARD>\r\n" +
                "<BIRTHDAY>1984-10-10 00:00:00</BIRTHDAY>\r\n" +
                "<GANDER>1</GANDER>\r\n" +
                "<NATION>01</NATION>\r\n" +
                "<REG>230828</REG>\r\n" +
                "<CUSTOM_FIELD>\r\n" +
                "<FIELD>\r\n" +
                "<FIELD_NO>11</FIELD_NO>\r\n" +
                "<FIELD_VALUE>浙江</FIELD_VALUE>\r\n" +
                "</FIELD>\r\n" +
                "</CUSTOM_FIELD>\r\n" +
                "<IMAGES COUNT=\"1\">\r\n" +
                "<IMAGE>\r\n" +
                "<POSITION>100,120,100,180,80,90</POSITION>\r\n" +
                "<IMG>"+imageBase64+"</IMG>\r\n" +
                "<SRC_IMAGE_ID>I20150803200221xxx</SRC_IMAGE_ID>\r\n" +
                "</IMAGE>\r\n" +
                "</IMAGES>\r\n" +
                "</PERSON_INFO>";
    }

    public static String makeFarsAddImage(String personId,String imageBase64) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" +
                "<DATA>\r\n" +
                "<PERSON_ID>"+personId+"</PERSON_ID>\r\n" +
                "<IMAGES  COUNT=\"1\">\r\n" +
                "<IMAGE>\r\n" +
                "<POSITION>100,120,100,180,80,90</POSITION>\r\n" +
                "<IMG>"+imageBase64+"</IMG>\r\n" +
                "<SRC_IMAGE_ID>I2015052613XXX</SRC_IMAGE_ID>\r\n" +
                "</IMAGE>\r\n" +
                "</IMAGES>\r\n" +
                "</DATA>";
    }

    public static String makeFarsModifyFacePoint() {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" +
                "<IMAGE_INFO>\r\n" +
                "<IMAGE_IDS  COUNT=\"2\">\r\n" +
                "<IMAGE_ID  POSITION=\"100,120,100,180,80,90\">997</IMAGE_ID>\r\n" +
                "<IMAGE_ID  POSITION=\"100,120,100,180,80,90\">999</IMAGE_ID>\r\n" +
                "</IMAGE_IDS>\r\n" +
                "</IMAGE_INFO>";
    }

    public static String makeFarsDeleteImage() {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" +
                "<IMAGE_info>\r\n" +
                "<image_IDs  count=\"2\">\r\n" +
                "<IMAGE_id >997</ IMAGE _id>\r\n" +
                "<IMAGE_id>999</ IMAGE _id>\r\n" +
                "</ image_IDs >\r\n" +
                "</ IMAGE _info>";
    }


    public static String makeFarsModifyPerson() {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" +
                "<PERSON_INFO>\r\n" +
                "<PERSON_ID>01</ PERSON_ID>\r\n" +
                "<name>张三</name>\r\n" +
                "<idcard>230828198906245134</idcard>\r\n" +
                "<birthday>1989-06-24 00:00:00</birthday>\r\n" +
                "<gander>1</gander>\r\n" +
                "<nation>01</NATION>\r\n" +
                "<REG>230828</REG>\r\n" +
                "<BIRTHYEAR>1989</BIRTHYEAR>\r\n" +
                "<CUSTOM_FIELD>\r\n" +
                "<FIELD>\r\n" +
                "<FIELD_NO>11</FIELD_NO>\r\n" +
                "<FIELD_VALUE>浙江<FIELD_VALUE>\r\n" +
                "</FIELD>\r\n" +
                "<FIELD>\r\n" +
                "<FIELD_NO>50</FIELD_NO>\r\n" +
                "<FIELD_VALUE>测试</FIELD_VALUE>\r\n" +
                "</FIELD>\r\n" +
                "</CUSTOM_FIELD>\r\n" +
                "</person_info>";
    }

    public static String makeFarsDeletePerson() {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" +
                "<PERSON_INFO>\r\n" +
                "<PERSON_IDS count=\"2\">\r\n" +
                "<PERSON_ID>997</PERSON_ID>\r\n" +
                "<PERSON_ID>999</PERSON_ID>\r\n" +
                "</PERSON_IDS>\r\n" +
                "</PERSON _INFO>";
    }
}

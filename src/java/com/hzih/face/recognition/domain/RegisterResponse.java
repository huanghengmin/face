package com.hzih.face.recognition.domain;

import com.hzih.face.recognition.entity.SipXml;

/**
 * Created by Administrator on 15-7-24.
 */
public class RegisterResponse extends SipXml {
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String toString(){
        return "<?xml version=\"1.0\"?>\r\n\r\n" +
                "<Response>\r\n" +
                "<DeviceType>" + deviceType + "</DeviceType>\r\n" +
                "<CmdType>" + cmdType + "</CmdType>\r\n" +
                "<DeviceID>" + deviceId + "</DeviceID>\r\n" +
                "<Result>" + result + "</Result>\r\n" +
                "</Response>";

    }
}

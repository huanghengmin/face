package com.hzih.face.recognition.domain;

import com.hzih.face.recognition.entity.SipType;
import com.hzih.face.recognition.entity.SipXml;

/**
 * Created by Administrator on 15-8-3.
 */
public class FaceInfoCompResponse extends SipXml {

    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "<?xml version=\"1.0\"?>\r\n\r\n" +
                "<Response>\r\n" +
                "<DeviceType>" + deviceType + "</DeviceType>\r\n" +
                "<DeviceID>" + deviceId + "</DeviceID>\r\n" +
                "<CmdType>" + SipType.FaceInfoComp + "</CmdType>\r\n" +
                "<Result>" + result + "</Result>\r\n" +
                "</Response>";
    }

}

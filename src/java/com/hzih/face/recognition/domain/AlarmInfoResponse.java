package com.hzih.face.recognition.domain;

import com.hzih.face.recognition.entity.SipType;
import com.hzih.face.recognition.entity.SipXml;

/**
 * Created by Administrator on 15-7-27.
 */
public class AlarmInfoResponse extends SipXml {

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
                "<CmdType>" + SipType.AlarmInfo + "</CmdType>\r\n" +
                "<DeviceID>" + deviceId + "</DeviceID>\r\n" +
                "<Result>" + result + "</Result>\r\n" +
                "</Response>";
    }
}

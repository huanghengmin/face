package com.hzih.face.recognition.domain;

import com.hzih.face.recognition.entity.SipType;
import com.hzih.face.recognition.entity.SipXml;

/**
 * Created by Administrator on 15-7-24.
 */
public class CatchPictureRequest extends SipXml {

    public String toString(){
        return "<?xml version=\"1.0\"?>\r\n\r\n" +
                "<Query>\r\n" +
                "<DeviceType>" + deviceType + "</DeviceType>\r\n" +
                "<CmdType>" + SipType.CatchPicture + "</CmdType>\r\n" +
                "<DeviceID>" + deviceId + "</DeviceID>\r\n" +
                "</Query>";

    }
}

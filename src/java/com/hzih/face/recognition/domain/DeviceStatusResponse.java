package com.hzih.face.recognition.domain;

import com.hzih.face.recognition.entity.SipXml;
import com.hzih.face.recognition.utils.Configuration;

/**
 * Created by Administrator on 15-7-24.
 */
public class DeviceStatusResponse extends SipXml {
    private String result;
    private String deviceStatus;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public DeviceStatusResponse xmlToBean(byte[] buff){
        Configuration config = new Configuration(buff);
        DeviceStatusResponse deviceStatus = config.getDeviceStatusResponse();
        return deviceStatus;
    }
}

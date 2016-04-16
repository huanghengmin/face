package com.hzih.face.recognition.domain;


import com.hzih.face.recognition.entity.SipXml;
import com.hzih.face.recognition.utils.Configuration;

/**
 * Created by Administrator on 15-7-27.
 */
public class AlarmInfoRequest extends SipXml {
    private String gpsStatus;
    private String nfcStatus;
    private String camStatus;

    public String getGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(String gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    public String getNfcStatus() {
        return nfcStatus;
    }

    public void setNfcStatus(String nfcStatus) {
        this.nfcStatus = nfcStatus;
    }

    public String getCamStatus() {
        return camStatus;
    }

    public void setCamStatus(String camStatus) {
        this.camStatus = camStatus;
    }

    public AlarmInfoRequest xmlToBean(byte[] buff) {
        Configuration config = new Configuration(buff);
        AlarmInfoRequest alarmInfo = config.getAlarmInfoRequest();
        return alarmInfo;
    }
}

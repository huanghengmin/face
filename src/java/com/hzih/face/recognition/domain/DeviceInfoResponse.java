package com.hzih.face.recognition.domain;


import com.hzih.face.recognition.entity.SipXml;
import com.hzih.face.recognition.utils.Configuration;

/**
 * Created by Administrator on 15-7-24.
 */
public class DeviceInfoResponse extends SipXml {
    private String result;
    private String cpuUse;
    private String cpuTemperature;
    private String memSituation;
    private String comparisonState;
    private String ipcNetState;
    private String ipcImageState;
    private String lastTime;


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCpuUse() {
        return cpuUse;
    }

    public void setCpuUse(String cpuUse) {
        this.cpuUse = cpuUse;
    }

    public String getCpuTemperature() {
        return cpuTemperature;
    }

    public void setCpuTemperature(String cpuTemperature) {
        this.cpuTemperature = cpuTemperature;
    }

    public String getMemSituation() {
        return memSituation;
    }

    public void setMemSituation(String memSituation) {
        this.memSituation = memSituation;
    }

    public String getComparisonState() {
        return comparisonState;
    }

    public void setComparisonState(String comparisonState) {
        this.comparisonState = comparisonState;
    }

    public String getIpcNetState() {
        return ipcNetState;
    }

    public void setIpcNetState(String ipcNetState) {
        this.ipcNetState = ipcNetState;
    }

    public String getIpcImageState() {
        return ipcImageState;
    }

    public void setIpcImageState(String ipcImageState) {
        this.ipcImageState = ipcImageState;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public DeviceInfoResponse xmlToBean(byte[] buff){
        Configuration config = new Configuration(buff);
        DeviceInfoResponse deviceInfo = config.getDeviceInfoResponse();
        return deviceInfo;
    }
}

package com.hzih.face.recognition.service;

import com.hzih.face.recognition.domain.Device;
import com.hzih.face.recognition.domain.RegisterRequest;

import java.util.Date;

/**
 * Created by Administrator on 15-10-26.
 */
public interface DeviceService {
    String queryDevice(int start, int limit, String deviceId, Date startDate, Date endDate,String name, String ip, String mac) throws Exception;

    String insertDevice(Device device) throws Exception;

    String deleteDevice(String terminalId) throws Exception;

    String updateDevice(Device device) throws Exception;

    String deviceIpcImage(String terminalId, String ip, int port) throws Exception;

    void updateRegister(RegisterRequest register) throws Exception;
}

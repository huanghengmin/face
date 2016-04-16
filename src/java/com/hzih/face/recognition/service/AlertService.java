package com.hzih.face.recognition.service;

/**
 * Created by Administrator on 15-10-26.
 */
public interface AlertService {
    String queryAlert(int start, int limit, String featureId, String taskId,String terminalId, String personInfo, String startDateStr, String endDateStr) throws Exception;
}

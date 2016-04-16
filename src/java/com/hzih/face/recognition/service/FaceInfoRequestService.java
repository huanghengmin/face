package com.hzih.face.recognition.service;


import com.hzih.face.recognition.domain.*;

import java.util.Date;

/**
 * Created by Administrator on 15-7-30.
 */
public interface FaceInfoRequestService extends AlertService, FeatureService, DeviceService, FaceCompareResultService {

    void updateFaceInfoRequest(String taskId, String filePath) throws Exception;

    public void insert(FaceInfoRequest faceInfoRequest) throws Exception;

    public String queryFaceInfo(int start, int limit, String terminalId, Date startDate, Date endDate, String taskId, String latitude, String longitude) throws Exception;

    public FaceInfoRequest queryFaceInfo(String taskId) throws Exception;

}

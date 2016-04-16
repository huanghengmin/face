package com.hzih.face.recognition.service;

import com.hzih.face.recognition.domain.FaceCompareResult;
import com.hzih.face.recognition.domain.FaceInfoCompRequest;

/**
 * Created by Administrator on 15-10-26.
 */
public interface FaceCompareResultService {


    void insertFaceInfoCompRequest(FaceInfoCompRequest faceInfoCompRequest) throws Exception;

    String queryFaceCompareInfo(String taskId, String filePath, String deviceId, int threshold) throws Exception;

    String queryFaceCompareResult(int start, int limit, String terminalId, String badFriend, String startDateStr, String endDateStr, String latitude, String longitude) throws Exception;

    String insertFaceCompareResult(FaceCompareResult faceCompareResult) throws Exception;

    String queryFaceCompareInfoDetail(String taskId, String filePath, String deviceId, String badFriend) throws Exception;

    String updateFaceCompareResult(FaceCompareResult faceCompareResult) throws Exception;
}

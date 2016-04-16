package com.hzih.face.recognition.dao;

import cn.collin.commons.dao.BaseDao;
import com.hzih.face.recognition.domain.FaceInfoCompRequest;

/**
 * Created by Administrator on 15-7-30.
 */
public interface FaceInfoCompRequestDao extends BaseDao{

    public FaceInfoCompRequest findByTaskIdAndFilePath(String taskId, String filePath) throws Exception;

    public void updateByeTaskIdAndFilePath(FaceInfoCompRequest faceInfoCompRequest) throws Exception;
}

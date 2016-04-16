package com.hzih.face.recognition.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.face.recognition.domain.FaceCompareResult;

import java.util.List;

/**
 * Created by Administrator on 15-7-30.
 */
public interface FaceCompareResultDao extends BaseDao{
    public PageResult listByParams(int start, int limit, String terminalId, String taskId,
                                   String startDateStr, String endDateStr,
                                   String latitude, String longitude) throws Exception;


    public List<FaceCompareResult> findByTaskId(String taskId) throws Exception;

    public FaceCompareResult findByTaskIdAndBadFriend(String taskId, String badFriend) throws Exception;
}

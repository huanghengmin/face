package com.hzih.face.recognition.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.face.recognition.domain.FaceInfoRequest;

import java.util.Date;

/**
 * Created by Administrator on 15-7-30.
 */
public interface FaceInfoRequestDao extends BaseDao{
    public PageResult listByParams(int start, int limit, String terminalId,
                                   Date startDate, Date endDate,
                                   String taskId, String latitude, String longitude) throws Exception;

    public FaceInfoRequest findByTaskId(String taskId) throws Exception;

    public FaceInfoRequest findByTaskIdAndBadFriend(String taskId, String badFriend) throws Exception;

}

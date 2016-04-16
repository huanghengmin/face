package com.hzih.face.recognition.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;

public interface AlertDao extends BaseDao {

    public PageResult listPageResult(int pageIndex, int limit, String featureId,
                                     String startDateStr, String endDateStr,
                                     String personInfo, String taskId, String terminalId) throws Exception;


}

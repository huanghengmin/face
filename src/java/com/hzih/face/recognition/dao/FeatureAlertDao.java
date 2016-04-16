package com.hzih.face.recognition.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;

public interface FeatureAlertDao extends BaseDao {

    public PageResult listPageResult(int pageIndex, int limit,
                                     String featureId, String action, String alertLevel) throws Exception;


}

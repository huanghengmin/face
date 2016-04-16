package com.hzih.face.recognition.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;

import java.util.Date;

public interface DeviceDao extends BaseDao {

    public PageResult listPageResult(int pageIndex, int limit, String deviceId,
                                     Date startDate, Date endDate,
                                     String deviceName, String ip, String mac) throws Exception;


}

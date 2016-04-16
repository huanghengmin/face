package com.hzih.face.recognition.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;

public interface CompareDBDao extends BaseDao {

    public PageResult listPageResult(int pageIndex, int limit, String name,
                                     String type, String depart) throws Exception;


}

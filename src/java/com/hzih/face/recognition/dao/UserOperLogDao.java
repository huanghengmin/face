package com.hzih.face.recognition.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;

import java.util.Date;

public interface UserOperLogDao extends BaseDao {
	/**
	 * 分页,按条件查询
	 * 
	 */
	public PageResult listLogsByParams(int pageIndex, int pageLength,
									   Date startDate, Date endDate, String logLevel, String userName)throws Exception;

}

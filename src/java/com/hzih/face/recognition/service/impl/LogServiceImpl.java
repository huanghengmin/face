package com.hzih.face.recognition.service.impl;

import cn.collin.commons.domain.PageResult;
import com.hzih.face.recognition.dao.UserOperLogDao;
import com.hzih.face.recognition.domain.UserOperLog;
import com.hzih.face.recognition.service.LogService;
import com.hzih.face.recognition.domain.UserOperLog;
import com.hzih.face.recognition.service.LogService;
import org.apache.log4j.Logger;

import java.util.Date;

public class LogServiceImpl implements LogService {
	private final static Logger logger = Logger.getLogger(LogServiceImpl.class);
	private UserOperLogDao userOperLogDao;



	public void setUserOperLogDao(UserOperLogDao userOperLogDao) {
		this.userOperLogDao = userOperLogDao;
	}



	public PageResult listUserOperLogByPage(int pageIndex, int pageLength,
			Date startDate, Date endDate, String logLevel, String userName) {
		logger.debug("startDate:" + startDate + " endDate:" + endDate);
		PageResult ps = null;
		try {
			ps = this.userOperLogDao.listLogsByParams(pageIndex,
                    pageLength, startDate, endDate, logLevel, userName);
		} catch (Exception e) {
			logger.error("查找用户日志失败",e);
			return null;
		}
		return ps;
	}


	public void newLog(String level, String userName, String auditModule,String auditInfo) {
		UserOperLog entry = new UserOperLog();
		entry.setAuditInfo(auditInfo);
		entry.setAuditModule(auditModule);
		entry.setLevel(level);
		entry.setUserName(userName);
		entry.setLogTime(new Date());
        try{
            userOperLogDao.create(entry);
        } catch (Exception e){
            logger.error("新增用户日志失败",e);
        }
	}
}

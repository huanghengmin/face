package com.hzih.face.recognition.service.impl;

import cn.collin.commons.domain.PageResult;
import com.hzih.face.recognition.dao.UserOperLogDao;
import com.hzih.face.recognition.domain.UserOperLog;
import com.hzih.face.recognition.service.AuditService;
import com.hzih.face.recognition.utils.DateUtils;
import com.hzih.face.recognition.utils.FileUtil;
import com.hzih.face.recognition.utils.StringContext;
import com.hzih.face.recognition.utils.StringUtils;
import com.hzih.face.recognition.service.AuditService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-19
 * Time: 上午10:04
 * To change this template use File | Settings | File Templates.
 */
public class AuditServiceImpl implements AuditService {
    private UserOperLogDao userOperLogDao;

    /**
     * 分页读取用户日志--并以json形式返回
     */
    public String selectUserAudit(int pageIndex, int limit, Date startDate, Date endDate,
                                  String logLevel, String userName) throws Exception {
        PageResult pageResult = userOperLogDao.listLogsByParams(pageIndex,limit,startDate, endDate, logLevel, userName);
        List<UserOperLog> userOperLogs = pageResult.getResults();
        int total = pageResult.getAllResultsAmount();
        String json = "{success:true,total:"+ total + ",rows:[";
        for (UserOperLog u : userOperLogs) {
            json +="{id:'"+u.getId()+"',userName:'"+u.getUserName()+"',level:'"+u.getLevel()+
                    "',auditModule:'"+u.getAuditModule()+"',auditInfo:'"+u.getAuditInfo()+
                    "',logTime:'"+ DateUtils.formatDate(u.getLogTime(), "yy-MM-dd HH:mm:ss")+"'},";
        }
        json += "]}";
        return json;
    }

    public UserOperLogDao getUserOperLogDao() {
        return userOperLogDao;
    }

    public void setUserOperLogDao(UserOperLogDao userOperLogDao) {
        this.userOperLogDao = userOperLogDao;
    }

}

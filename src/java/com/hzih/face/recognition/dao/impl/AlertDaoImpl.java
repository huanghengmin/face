package com.hzih.face.recognition.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.face.recognition.dao.AlertDao;
import com.hzih.face.recognition.domain.Alert;
import com.hzih.face.recognition.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 15-8-18.
 */
public class AlertDaoImpl extends MyDaoSupport implements AlertDao {
    @Override
    public PageResult listPageResult(int pageIndex, int pageLength, String featureId,
                                     String startDate, String endDate,
                                     String personInfo, String taskId, String terminalId) throws Exception {
        StringBuffer sb = new StringBuffer(" from Alert s where 1=1");
        List params = new ArrayList(6);// 手动指定容量，避免多次扩容
        if(StringUtils.isNotBlank(startDate)){
            startDate = startDate.replaceAll("%20"," ");
            sb.append(" and alertTime >= to_date(?,'yyyy-mm-dd hh24:mi:ss')");
            params.add(startDate);
        }
        if(StringUtils.isNotBlank(endDate)){
            endDate = endDate.replaceAll("%20"," ");
            sb.append(" and registerTime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')");
            params.add(endDate);
        }

        if (StringUtils.isNotBlank(featureId)) {
            sb.append(" and featureId = ?");
            params.add(featureId);
        }
        if (StringUtils.isNotBlank(taskId)) {
            sb.append(" and taskId = ?");
            params.add(taskId);
        }
        if (StringUtils.isNotBlank(terminalId)) {
            sb.append(" and terminalId = ?");
            params.add(terminalId);
        }

        if (StringUtils.isNotBlank(personInfo)) {
            sb.append(" and personInfo like ?");
            params.add("%"+personInfo+"%");
        }
        sb.append(" order by id desc");
        String countString = "select count(*) " + sb.toString();
        String queryString = sb.toString();

        PageResult ps = this.findByPage(queryString, countString, params
                .toArray(), pageIndex, pageLength);
//        logger.debug(ps == null ? "ps=null" : "ps.results.size:"
//                + ps.getResults().size());
        return ps;
    }

    @Override
    public void setEntityClass() {
        this.entityClass = Alert.class;
    }
}

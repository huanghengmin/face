package com.hzih.face.recognition.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.face.recognition.dao.FaceCompareResultDao;
import com.hzih.face.recognition.domain.FaceCompareResult;
import com.hzih.face.recognition.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 15-7-30.
 */
public class FaceCompareResultDaoImpl extends MyDaoSupport implements FaceCompareResultDao {
    @Override
    public void setEntityClass() {
        this.entityClass = FaceCompareResult.class;
    }

    @Override
    public PageResult listByParams(int pageIndex, int pageLength, String terminalId,String taskId,
                                   String startDate, String endDate,
                                    String latitude, String longitude) throws Exception {
        StringBuffer sb = new StringBuffer(" from FaceCompareResult s where 1=1");
        List params = new ArrayList(7);// 手动指定容量，避免多次扩容
        if(StringUtils.isNotBlank(startDate)){
            startDate = startDate.replaceAll("%20"," ");
            sb.append(" and dateTime >= to_date(?,'yyyy-mm-dd hh24:mi:ss')");
            params.add(startDate);
        }
        if(StringUtils.isNotBlank(endDate)){
            endDate = endDate.replaceAll("%20"," ");
            sb.append(" and dateTime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')");
            //                    Date startDate = DateUtils.parse(startDateStr, DateUtils.format_1);
//                    Date endDate = DateUtils.parse(endDateStr, DateUtils.format_1);
            params.add(endDate);
        }

        if (StringUtils.isNotBlank(terminalId)) {
            sb.append(" and deviceId = ?");
            params.add(terminalId);
        }

        if (StringUtils.isNotBlank(latitude)) {
            sb.append(" and latitude = ?");
            params.add(longitude);
        }

        if (StringUtils.isNotBlank(longitude)) {
            sb.append(" and longitude = ?");
            params.add(longitude);
        }

        if (StringUtils.isNotBlank(taskId)) {
            sb.append(" and taskId = ?");
            params.add(taskId);
        }
        sb.append(" order by id desc");
        String countString = "select count(*) " + sb.toString();
        String queryString = sb.toString();

        PageResult ps = this.findByPage(queryString, countString, params
                .toArray(), pageIndex, pageLength);
        return ps;
    }

    @Override
    public List<FaceCompareResult> findByTaskId(String taskId) throws Exception {
        String hql = new String("from FaceCompareResult where taskId=? ");
        List list = getHibernateTemplate().find(hql,new String[] { taskId });
        if (list != null && list.size() > 0) {
            return list;
        } else {
            return null;
        }
    }

    @Override
    public FaceCompareResult findByTaskIdAndBadFriend(String taskId, String badFriend) throws Exception {
        String hql = new String("from FaceCompareResult where taskId=? and badFriend=?");
        List list = getHibernateTemplate().find(hql,new String[] { taskId ,badFriend});
        if (list != null && list.size() > 0) {
            return (FaceCompareResult) list.get(0);
        } else {
            return null;
        }
    }


}

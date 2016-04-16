package com.hzih.face.recognition.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.face.recognition.dao.FaceInfoRequestDao;
import com.hzih.face.recognition.domain.FaceInfoRequest;
import com.hzih.face.recognition.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 15-7-30.
 */
public class FaceInfoRequestDaoImpl extends MyDaoSupport implements FaceInfoRequestDao {
    @Override
    public void setEntityClass() {
        this.entityClass = FaceInfoRequest.class;
    }

    @Override
    public PageResult listByParams(int pageIndex, int pageLength, String terminalId, Date startDate, Date endDate,
                                   String taskId, String latitude, String longitude) throws Exception {
        StringBuffer sb = new StringBuffer(" from FaceInfoRequest s where 1=1");
        List params = new ArrayList(7);// 手动指定容量，避免多次扩容

        if(startDate!=null){
            sb.append(" and date_format(dateTime,'%Y-%m-%d')>= date_format(?,'%Y-%m-%d')");
            params.add(startDate);
        }
        if(endDate!=null){
            sb.append(" and date_format(dateTime,'%Y-%m-%d')<= date_format(?,'%Y-%m-%d')");
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
//        logger.debug(ps == null ? "ps=null" : "ps.results.size:"
//                + ps.getResults().size());
        return ps;
    }

    @Override
    public FaceInfoRequest findByTaskId(String taskId) throws Exception {
        String hql = new String("from FaceInfoRequest where taskId=? ");
        List list = getHibernateTemplate().find(hql,new String[] { taskId });
        if (list != null && list.size() > 0) {
            return (FaceInfoRequest) list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public FaceInfoRequest findByTaskIdAndBadFriend(String taskId, String badFriend) throws Exception {
        String hql = new String("from FaceInfoRequest where taskId=? and badFriend=?");
        List list = getHibernateTemplate().find(hql,new String[] { taskId,badFriend });
        if (list != null && list.size() > 0) {
            return (FaceInfoRequest) list.get(0);
        } else {
            return null;
        }
    }
}

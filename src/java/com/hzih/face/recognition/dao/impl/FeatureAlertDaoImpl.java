package com.hzih.face.recognition.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.face.recognition.dao.FeatureAlertDao;
import com.hzih.face.recognition.domain.FeatureAlert;
import com.hzih.face.recognition.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 15-8-18.
 */
public class FeatureAlertDaoImpl extends MyDaoSupport implements FeatureAlertDao {
    @Override
    public PageResult listPageResult(int pageIndex, int limit,
                                     String featureId, String action, String alertLevel) throws Exception {
        StringBuffer sb = new StringBuffer(" from FeatureAlert s where 1=1");
        List params = new ArrayList(7);// 手动指定容量，避免多次扩容


        if (StringUtils.isNotBlank(featureId)) {
            sb.append(" and id = ?");
            params.add(featureId);
        }

        if (StringUtils.isNotBlank(alertLevel)) {
            sb.append(" and alertLevel = ?");
            params.add(alertLevel);
        }

        sb.append(" order by id desc");
        String countString = "select count(*) " + sb.toString();
        String queryString = sb.toString();

        PageResult ps = this.findByPage(queryString, countString, params
                .toArray(), pageIndex, limit);
//        logger.debug(ps == null ? "ps=null" : "ps.results.size:"
//                + ps.getResults().size());
        return ps;
    }

    @Override
    public void setEntityClass() {
        this.entityClass = FeatureAlert.class;
    }
}

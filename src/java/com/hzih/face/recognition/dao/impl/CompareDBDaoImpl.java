package com.hzih.face.recognition.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.face.recognition.dao.CompareDBDao;
import com.hzih.face.recognition.domain.CompareDB;
import com.hzih.face.recognition.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 15-8-18.
 */
public class CompareDBDaoImpl extends MyDaoSupport implements CompareDBDao {
    @Override
    public PageResult listPageResult(int pageIndex, int pageLength,String name, String type, String depart) throws Exception {
        StringBuffer sb = new StringBuffer(" from CompareDB s where 1=1");
        List params = new ArrayList(6);// 手动指定容量，避免多次扩容
        if (StringUtils.isNotBlank(name)) {
            sb.append(" and name like ?");
            params.add("%"+name+"%");
        }
        if (StringUtils.isNotBlank(type)) {
            sb.append(" and type like ?");
            params.add("%"+type+"%");
        }
        if (StringUtils.isNotBlank(depart)) {
            sb.append(" and depart like ?");
            params.add("%"+depart+"%");
        }

        sb.append(" order by id desc");
        String countString = "select count(*) " + sb.toString();
        String queryString = sb.toString();

        PageResult ps = this.findByPage(queryString, countString, params.toArray(), pageIndex, pageLength);
        return ps;
    }

    @Override
    public void setEntityClass() {
        this.entityClass = CompareDB.class;
    }
}

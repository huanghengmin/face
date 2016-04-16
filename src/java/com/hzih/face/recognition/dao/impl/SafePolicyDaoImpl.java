package com.hzih.face.recognition.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import com.hzih.face.recognition.dao.SafePolicyDao;
import com.hzih.face.recognition.domain.SafePolicy;

import java.util.List;

public class SafePolicyDaoImpl extends MyDaoSupport implements SafePolicyDao {

	@Override
	public void setEntityClass() {
		this.entityClass = SafePolicy.class;
	}

	public SafePolicy getData() {
		String hql = new String("from SafePolicy");
		List list = getHibernateTemplate().find(hql);
		if(list.size()>0){
			return (SafePolicy)list.get(0);
		}else{
			SafePolicy entry = new SafePolicy();
			entry.setErrorLimit(3);
			entry.setPasswordLength(8);
			entry.setRemoteDisabled(true);
			entry.setTimeout(300);
			getHibernateTemplate().save(entry);
			return entry;
		}
	}

}

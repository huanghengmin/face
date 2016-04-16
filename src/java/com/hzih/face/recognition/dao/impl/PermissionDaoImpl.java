package com.hzih.face.recognition.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import com.hzih.face.recognition.dao.PermissionDao;
import com.hzih.face.recognition.domain.Permission;

public class PermissionDaoImpl extends MyDaoSupport implements PermissionDao {

	@Override
	public void setEntityClass() {
		this.entityClass = Permission.class;
	}

}

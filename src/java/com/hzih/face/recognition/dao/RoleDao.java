package com.hzih.face.recognition.dao;

import cn.collin.commons.dao.BaseDao;
import com.hzih.face.recognition.domain.Role;

public interface RoleDao extends BaseDao {

    public Role findByName(String name) throws Exception;
}

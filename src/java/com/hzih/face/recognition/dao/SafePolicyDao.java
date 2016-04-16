package com.hzih.face.recognition.dao;

import cn.collin.commons.dao.BaseDao;
import com.hzih.face.recognition.domain.SafePolicy;
import com.hzih.face.recognition.domain.SafePolicy;

public interface SafePolicyDao extends BaseDao{

	SafePolicy getData();

}

package com.hzih.face.recognition.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.face.recognition.dao.DeviceDao;
import com.hzih.face.recognition.domain.Device;
import com.hzih.face.recognition.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 15-8-18.
 */
public class DeviceDaoImpl extends MyDaoSupport implements DeviceDao {
    @Override
    public PageResult listPageResult(int pageIndex, int pageLength, String deviceId,
                                     Date startDate, Date endDate,
                                     String deviceName, String ip, String mac) throws Exception {
        StringBuffer sb = new StringBuffer(" from Device s where 1=1");
        List params = new ArrayList(6);

        if(startDate!=null){
            sb.append(" and date_format(registerTime,'%Y-%m-%d')>= date_format(?,'%Y-%m-%d')");
            params.add(startDate);
        }
        if(endDate!=null){
            sb.append(" and date_format(registerTime,'%Y-%m-%d')<= date_format(?,'%Y-%m-%d')");
            params.add(endDate);
        }

        if (StringUtils.isNotBlank(deviceId)) {
            sb.append(" and deviceId = ?");
            params.add(deviceId);
        }

        if (StringUtils.isNotBlank(ip)) {
            sb.append(" and ip = ?");
            params.add(ip);
        }

        if (StringUtils.isNotBlank(mac)) {
            sb.append(" and mac = ?");
            params.add(mac);
        }

        if (StringUtils.isNotBlank(deviceName)) {
            sb.append(" and fileName like ?");
            params.add("%"+deviceName+"%");
        }
        sb.append(" order by deviceId desc");
        String countString = "select count(*) " + sb.toString();
        String queryString = sb.toString();

        PageResult ps = this.findByPage(queryString, countString, params
                .toArray(), pageIndex, pageLength);
        return ps;
    }

    @Override
    public void setEntityClass() {
        this.entityClass = Device.class;
    }
}

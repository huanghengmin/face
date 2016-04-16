package com.hzih.face.recognition.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import com.hzih.face.recognition.dao.FaceInfoCompRequestDao;
import com.hzih.face.recognition.domain.FaceInfoCompRequest;

import java.util.List;

/**
 * Created by Administrator on 15-7-30.
 */
public class FaceInfoCompRequestDaoImpl extends MyDaoSupport implements FaceInfoCompRequestDao {
    @Override
    public void setEntityClass() {
        this.entityClass = FaceInfoCompRequest.class;
    }


    @Override
    public FaceInfoCompRequest findByTaskIdAndFilePath(String taskId, String filePath) throws Exception {
        String hql = new String("from FaceInfoCompRequest where taskId=? and filePath = ?");
        List list = getHibernateTemplate().find(hql,new String[] { taskId,filePath });
        if (list != null && list.size() > 0) {
            return (FaceInfoCompRequest) list.get(0);
        } else {
            return null;
        }

    }

    @Override
    public void updateByeTaskIdAndFilePath(FaceInfoCompRequest faceInfoCompRequest) throws Exception {
        String hql = new String("update FaceInfoCompRequest set featureId = ?,score = ? where taskId=? and filePath = ?");
        int result = getHibernateTemplate().bulkUpdate(hql,new String[] {
                faceInfoCompRequest.getFeatureId(),faceInfoCompRequest.getScore(),
                faceInfoCompRequest.getTaskId(),faceInfoCompRequest.getFilePath() });
    }
}

package com.hzih.face.recognition.service;

import com.hzih.face.recognition.domain.FeatureAlert;

/**
 * Created by Administrator on 15-10-26.
 */
public interface FeatureService {
    String insertFeature(FeatureAlert featureAlert) throws Exception;

    String updateFeature(FeatureAlert featureAlert) throws Exception;

    String deleteFeature(String featureId) throws Exception;

    String queryFeature(int start, int limit, String featureId, String action, String alertLevel) throws Exception;
}

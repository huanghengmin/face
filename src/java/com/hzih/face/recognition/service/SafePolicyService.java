package com.hzih.face.recognition.service;

import com.hzih.face.recognition.domain.SafePolicy;
import com.hzih.face.recognition.domain.SafePolicy;

public interface SafePolicyService {

	SafePolicy getData();

    public String select() throws Exception;

    public String update(SafePolicy safePolicy) throws Exception;

    public String selectPasswordRules() throws Exception;
}

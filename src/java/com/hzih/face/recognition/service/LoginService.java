package com.hzih.face.recognition.service;

import com.hzih.face.recognition.domain.Account;

public interface LoginService {

	Account getAccountByNameAndPwd(String name, String pwd) ;

    Account getAccountByName(String name) ;
}

package com.hzih.face.recognition.web;

import com.hzih.face.recognition.domain.SafePolicy;
import com.hzih.face.recognition.domain.SafePolicy;

import java.util.HashMap;
import java.util.Map;

/**
 * 站点上下文对象
 * 
 * @author collin.code@gmail.com
 * 
 */
public class SiteContext {
	/**
	 * web context根目录所在的真实文件路径
	 */
	public String contextRealPath = null;
	public SafePolicy safePolicy;
	private static SiteContext instance;
	public static Map loginErrorList = new HashMap();//用于保存错误登录的用户名和密码记录，以IP地址为key
	static {
		instance = new SiteContext();
	}

	public static SiteContext getInstance() {
		return instance;
	}
}
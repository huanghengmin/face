package com.hzih.face.recognition.tcp;

import com.hzih.face.recognition.utils.StringContext;
import org.apache.log4j.Logger;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * Created by Administrator on 15-6-17.
 */
public class ServiceUtils {
    private final static Logger logger = Logger.getLogger(ServiceUtils.class);
    //设备ID号
    public String deviceId;
    public String faceServer;

    private ServiceUtils(){

    }

    public static ServiceUtils getService() {
        Properties pros = new Properties();
        try {
            FileInputStream ins = new FileInputStream(StringContext.config_properties);
            pros.load(ins);
            ServiceUtils service = new ServiceUtils();
            service.deviceId = pros.getProperty("deviceId");
            service.faceServer = pros.getProperty("faceServer");
            return service;
        } catch (IOException e) {
            logger.error("加载配置文件 config.properties 错误！", e);
            return null;
        }
    }
}

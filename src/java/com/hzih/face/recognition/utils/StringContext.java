package com.hzih.face.recognition.utils;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-7
 * Time: 上午10:56
 * To change this template use File | Settings | File Templates.
 */
public class StringContext {
    public final static String systemPath = System.getProperty("face.home");
    public final static String INTERFACE = "/etc/network/interfaces";//linux下IP信息存储文件
    public final static String IFSTATE = "/etc/network/run/ifstate"; //linux下DNS信息
    public final static String localLogPath = systemPath + "/logs";   //日志文件目录
    public final static String webPath = systemPath+"/tomcat/webapps"; //war服务文件存储目录
    public final static String tempPath = systemPath+"/tomcat/temp"; //缓存目录
    public static final String SECURITY_CONFIG = StringContext.systemPath +"/tomcat/conf/server.xml";
    /**
     * 日志服务器配置文件
     */
    public static final String syslog_xml =StringContext.systemPath + "/config/syslog.xml";

    public static final String mysql_bak_sql = StringContext.systemPath + "/mysqlbak.sql";

    public static final String config_properties  =  StringContext.systemPath + "/config/config.properties";

}

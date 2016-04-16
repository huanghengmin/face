package com.hzih.face.recognition.service.impl;

import cn.collin.commons.domain.PageResult;
import com.hzih.face.recognition.client.WsdlClient;
import com.hzih.face.recognition.client.entity.*;
import com.hzih.face.recognition.dao.*;
import com.hzih.face.recognition.domain.*;
import com.hzih.face.recognition.tcp.TcpShortClient;
import com.hzih.face.recognition.service.FaceInfoRequestService;
import com.hzih.face.recognition.utils.DateUtils;
import com.hzih.face.recognition.utils.FileUtil;
import com.hzih.face.recognition.utils.StringContext;
import com.hzih.face.recognition.utils.StringUtils;
import com.hzih.face.recognition.utils.mina.MessageInfo;
import com.inetec.common.util.OSInfo;
import org.apache.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.*;

/**
 * Created by Administrator on 15-7-30.
 */
public class FaceInfoRequestServiceImpl implements FaceInfoRequestService {
    private static Logger logger = Logger.getLogger(FaceInfoRequestServiceImpl.class);

    private FaceInfoCompRequestDao faceInfoCompRequestDao;
    private FaceInfoRequestDao faceInfoRequestDao;
    private DeviceDao deviceDao;
    private FaceCompareResultDao faceCompareResultDao;
    private FeatureAlertDao featureAlertDao;
    private AlertDao alertDao;

    public void setAlertDao(AlertDao alertDao) {
        this.alertDao = alertDao;
    }

    public void setFeatureAlertDao(FeatureAlertDao featureAlertDao) {
        this.featureAlertDao = featureAlertDao;
    }

    public void setFaceInfoCompRequestDao(FaceInfoCompRequestDao faceInfoCompRequestDao) {
        this.faceInfoCompRequestDao = faceInfoCompRequestDao;
    }

    public void setFaceInfoRequestDao(FaceInfoRequestDao faceInfoRequestDao) {
        this.faceInfoRequestDao = faceInfoRequestDao;
    }

    public void setDeviceDao(DeviceDao deviceDao) {
        this.deviceDao = deviceDao;
    }

    public void setFaceCompareResultDao(FaceCompareResultDao faceCompareResultDao) {
        this.faceCompareResultDao = faceCompareResultDao;
    }

    @Override
    public void insert(FaceInfoRequest faceInfoRequest) throws Exception {
        faceInfoRequestDao.create(faceInfoRequest);//后续报警业务处理可以分一个线程处理
        if(featureAlertDao.findAll().size()>0) {
            String[] heads = faceInfoRequest.getFilePathHead().split(";");
            String taskId = faceInfoRequest.getTaskId();
            String deviceId = faceInfoRequest.getDeviceId();
            for (String filePath : heads) {
                if(StringUtils.isBlank(filePath)){
                    continue;
                }

                if(OSInfo.getOSInfo().isWin()){
                    filePath = filePath.replace("/","\\");
                }
                FaceInfoCompRequest faceInfoCompRequest = faceInfoCompRequestDao.findByTaskIdAndFilePath(taskId,filePath);
                String featureIds = "";
                if(faceInfoCompRequest != null) {
                    featureIds = faceInfoCompRequest.getFeatureId();
                }
                if(faceInfoCompRequest == null || featureIds == null){

                    List<Row> rowList = getFars1TON(taskId,filePath,500);
                    String scores = "";
                    for(Row row : rowList) {
                        if(featureIds == null){
                            featureIds = "";
                        }
                        featureIds += row.getFeatureId() + ";";
                        scores += row.getScore()+";";
                    }
                    boolean isUpdate = true;
                    if(faceInfoCompRequest == null){
                        faceInfoCompRequest = new FaceInfoCompRequest();
                        isUpdate = false;
                    }
                    faceInfoCompRequest.setDeviceId(deviceId);
                    faceInfoCompRequest.setTaskId(taskId);
                    faceInfoCompRequest.setFilePath(filePath);
                    faceInfoCompRequest.setFeatureId(featureIds);
                    faceInfoCompRequest.setScore(scores);
                    if(isUpdate){
                        faceInfoCompRequestDao.updateByeTaskIdAndFilePath(faceInfoCompRequest);
                    } else {
                        faceInfoCompRequestDao.create(faceInfoCompRequest);
                    }
                }
                if(featureIds!=null){

                    Map<String,String> scoreMap = new HashMap<String,String>();
                    Map<String,FeatureAlert> alertMap = new HashMap<String,FeatureAlert>();
                    String[] fs = featureIds.split(";");
                    String[] scores = faceInfoCompRequest.getScore().split(";");
                    String _featureIds = "";
                    for (int i =0;i<fs.length;i++) {
                        String featureId = fs[i];
                        if(featureId.length()==0){
                            continue;
                        }
                        FeatureAlert featureAlert = (FeatureAlert) featureAlertDao.getById(featureId);
                        if(featureAlert!=null) {
                            _featureIds = featureId + ";";
                            scoreMap.put(featureId,scores[i]);
                            alertMap.put(featureId,featureAlert);
                        }
                    }
                    if(_featureIds.length()>0){
                        _featureIds = _featureIds.substring(0,_featureIds.lastIndexOf(";"));
                        List<Row> list = getPersonInfo(_featureIds);
                        String personInfoStr = "";
                        for (Row personInfo : list) {
                            List<Field> fields = personInfo.getFields();
                            int fieldSize = fields.size() * 2;
                            String imagePath = toFile(personInfo.getImageBase64(),filePath,personInfo.getFeatureId());
                            FeatureAlert featureAlert = alertMap.get(personInfo.getFeatureId());
                            Alert alert = new Alert();
                            alert.setTaskId(taskId);
                            alert.setAlertLevel(featureAlert.getAlertLevel());
                            alert.setTerminalId(deviceId);
                            alert.setIpcFilePath(filePath);
                            alert.setPersonFilePath(imagePath);
                            alert.setAlertTime(new Date());
                            alert.setFeatureId(personInfo.getFeatureId());
                            alert.setScore(Integer.parseInt(scoreMap.get(personInfo.getFeatureId())));
                            int i = 0;
                            for (Field field : fields) {
                                i++;
                                if(i<fieldSize){
                                    personInfoStr += field.getValue()+":" + field.getCodeValue()+"|";
                                } else {
                                    personInfoStr += field.getValue()+":" + field.getCodeValue();
                                }
                            }
                            alert.setPersonInfo(personInfoStr);
                            alertDao.create(alert);
                        }
                    }
                }
            }
        }



    }

    @Override
    public String queryFaceInfo(int start, int limit, String terminalId, Date startDate, Date endDate, String taskId, String latitude, String longitude) throws Exception {
        PageResult pageResult = faceInfoRequestDao.listByParams(start/limit+1,limit,terminalId,startDate,endDate,taskId,latitude,longitude);
        List<FaceInfoRequest> list = pageResult.getResults();
        int total = pageResult.getAllResultsAmount();
        String json = "{success:true,total:"+ total + ",rows:[";
        for (FaceInfoRequest f : list) {
            Device device = (Device) deviceDao.getById(Device.class,f.getDeviceId());
            json +="{terminalId:'"+ f.getDeviceId()+"',logTime:'"+ DateUtils.formatDate(f.getDateTime(), DateUtils.format)+
                    "',place:'"+(device==null?"未知":device.getPlace())+
                    "',latitude:'"+f.getLatitude()+"',longitude:'"+f.getLongitude()+
                    "',filePathA:'"+f.getFilePathA()+"',filePathH:'"+f.getFilePathH()+
                    "',filePathHead:'"+f.getFilePathHead()+"',taskId:'"+f.getTaskId()+"'},";
        }
        json += "]}";
        return json;
    }

    @Override
    public String queryDevice(int start, int limit, String deviceId, Date startDate, Date endDate,
                              String name, String ip, String mac) throws Exception {
        PageResult pageResult = deviceDao.listPageResult(start/limit+1,limit,deviceId,startDate,endDate,name,ip,mac);
        List<Device> list = pageResult.getResults();
        int total = pageResult.getAllResultsAmount();
        String json = "{success:true,total:"+ total + ",rows:[";
        for (Device device : list) {
            DeviceInfoResponse d = getDeviceInfo(device.getIp(),device.getPort(),device.getDeviceId());
            json +="{terminalId:'"+ device.getDeviceId()+"',registerTime:'"+DateUtils.formatDate(device.getRegisterTime(),DateUtils.format)+
                    "',name:'"+device.getDeviceName()+"',ipPort:'"+device.getIp()+":"+device.getPort()+
                    "',province:'"+device.getProvince()+"',city:'"+device.getCity()+
                    "',organization:'"+device.getOrganization()+"',institution:'"+device.getInstitution()+
                    "',place:'"+device.getPlace()+"',address:'"+device.getAddress()+
                    "',manager:'"+device.getManager()+"',mobile:'"+device.getMobile()+
                    "',telephone:'"+device.getTelephone()+
                    "',latitude:'"+device.getLatitude()+"',longitude:'"+device.getLongitude()+
                    "',terminalNetStatus:'"+(d==null?"0":"1")+
                    "',terminalCpuUse:'"+(d==null?"--/--":d.getCpuUse()+"%")+"',terminalCpuTemperature:'"+(d==null?"--/--":d.getCpuTemperature()+"℃")+
                    "',terminalMem:'"+(d==null?"--/--":d.getMemSituation())+
                    "',ipcNetStatus:'"+(d==null?"--/--":d.getIpcNetState())+"',ipcComparisonStatus:'"+(d==null?"--/--":d.getComparisonState())+
                    "',ipcPictureStatus:'"+(d==null?"--/--":d.getIpcImageState())+"',lastTime:'"+(d==null?"--/--":d.getLastTime())+"'},";
        }
        json += "]}";
        return json;
    }


    private DeviceInfoResponse getDeviceInfo(String ip,int port,String deviceId){
        NioSocketConnector connector = TcpShortClient.getInstance().getConnector();
        DeviceInfoResponse deviceInfo = null;
        ConnectFuture connFuture = null;
        try{
            connFuture = connector.connect(new InetSocketAddress(ip,port));
            connFuture.awaitUninterruptibly();
            if  (connFuture.isDone()) {
                if  (!connFuture.isConnected()) {  //若在指定时间内没连接成功，则抛出异常
                    logger.info("fail to connect "+ip+":"+port);
                    throw   new  Exception();
                }
            }
            IoSession session = connFuture.getSession();
            MessageInfo messageInfo = new MessageInfo();
            DeviceInfoRequest deviceInfoRequest = new DeviceInfoRequest();
            deviceInfoRequest.setDeviceType("0x02");
            deviceInfoRequest.setDeviceId(deviceId);
            byte[] body = deviceInfoRequest.toString().getBytes();

            messageInfo.setVersion(MessageInfo.Version);
            messageInfo.setBodyLen(body.length);
            messageInfo.setReserved(new byte[21]);
            messageInfo.setBody(body);
            if(session!=null){
                session.write(messageInfo);
            }
            session.getCloseFuture().awaitUninterruptibly();
            String bodyResponse = (String) session.getAttribute("result");
            deviceInfo = new DeviceInfoResponse();
            deviceInfo = deviceInfo.xmlToBean(bodyResponse.getBytes());
        } catch (Exception e){
            logger.error(e.getMessage(), e);
        } finally {
            connFuture.cancel();
        }
        return deviceInfo;
    }


    @Override
    public void insertFaceInfoCompRequest(FaceInfoCompRequest faceInfoCompRequest) throws Exception {
        faceInfoCompRequestDao.create(faceInfoCompRequest);
    }

    @Override
    public FaceInfoRequest queryFaceInfo(String taskId) throws Exception {
        return faceInfoRequestDao.findByTaskId(taskId);
    }

    @Override
    public String queryFaceCompareInfo(String taskId, String filePath, String deviceId, int threshold) throws Exception {
        if(OSInfo.getOSInfo().isWin()){
            filePath = filePath.replace("/","\\");
        }
        FaceInfoCompRequest faceInfoCompRequest = faceInfoCompRequestDao.findByTaskIdAndFilePath(taskId,filePath);
        String featureIds = "";
        if(faceInfoCompRequest != null) {
            featureIds = faceInfoCompRequest.getFeatureId();
        }
        if(faceInfoCompRequest == null || featureIds == null){

            List<Row> rowList = getFars1TON(taskId,filePath,threshold);
            String scores = "";
            for(Row row : rowList) {
                if(featureIds == null){
                    featureIds = "";
                }
                featureIds += row.getFeatureId() + ";";
                scores += row.getScore()+";";
            }
            boolean isUpdate = true;
            if(faceInfoCompRequest == null){
                faceInfoCompRequest = new FaceInfoCompRequest();
                isUpdate = false;
            }
            faceInfoCompRequest.setDeviceId(deviceId);
            faceInfoCompRequest.setTaskId(taskId);
            faceInfoCompRequest.setFilePath(filePath);
            faceInfoCompRequest.setFeatureId(featureIds);
            faceInfoCompRequest.setScore(scores);
            if(isUpdate){
                faceInfoCompRequestDao.updateByeTaskIdAndFilePath(faceInfoCompRequest);
            } else {
                faceInfoCompRequestDao.create(faceInfoCompRequest);
            }
        }
        if(featureIds==null){
            return "{success:true,total:0,rows:[]}";
        }
        Map<String,String> scoreMap = new HashMap<String,String>();
        String[] fs = featureIds.split(";");
        String[] scores = faceInfoCompRequest.getScore().split(";");
        for (int i =0;i<fs.length;i++) {
            String featureId = fs[i];
            if(featureId.length()==0){
                continue;
            }
            scoreMap.put(featureId,scores[i]);
        }

        List<Row> list = getPersonInfo(featureIds);
        int total = list.size();
        String json = "{success:true,total:"+ total + ",rows:[";
        for (Row personInfo : list) {
            List<Field> fields = personInfo.getFields();
            int fieldSize = fields.size() * 2;
            String imagePath = toFile(personInfo.getImageBase64(),filePath,personInfo.getFeatureId());
            json +="{featureId:'"+ personInfo.getFeatureId()+"',score:'"+scoreMap.get(personInfo.getFeatureId())+
                    "',xLeft:'"+personInfo.getxLeft()+"',yLeft:'"+personInfo.getyLeft()+
                    "',xRight:'"+personInfo.getxRight()+"',yRight:'"+personInfo.getyRight()+
                    "',xMouth:'"+personInfo.getxMouth()+"',yMouth:'"+personInfo.getyMouth()+
                    "',image:'"+imagePath+"',fieldSize:'"+fieldSize+"',";
            int i = 0;
            for (Field field : fields) {
                i++;
                if(i<fieldSize){
                    json += field.getName() + ":'"+field.getValue()+"'," + field.getCodeName() + ":'"+field.getCodeValue()+"',";
                } else {
                    json += field.getName() + ":'"+field.getValue()+"'," + field.getCodeName() + ":'"+field.getCodeValue()+"'";
                }
            }

            json += "},";
        }
        json += "]}";
        return json;
    }

    @Override
    public String queryFaceCompareInfoDetail(String taskId, String filePath, String deviceId, String badFriend) throws Exception {
        FaceCompareResult f = faceCompareResultDao.findByTaskIdAndBadFriend(taskId,badFriend);
        int total = 0;
        String row = "";
        if(f!=null) {
            total = 1;
            String[] fields = f.getCompareResult().split("\\|");
            int fieldSize = fields.length - 1;
            row ="{featureId:'"+ f.getFeatureId()+"',score:'"+f.getScore()+
                    "',xLeft:'"+f.getxLeft()+"',yLeft:'"+f.getyLeft()+
                    "',xRight:'"+f.getxRight()+"',yRight:'"+f.getyRight()+
                    "',xMouth:'"+f.getxMouth()+"',yMouth:'"+f.getyMouth()+
                    "',image:'"+f.getImagePath()+"',fieldSize:'"+fieldSize+"',";

            for ( int i=0;i< fields.length;i++) {
                String field = fields[i];
                if(field.length()>0 ) {
                    if(i<fieldSize){
                        row += i + ":'"+field+"',";
                    } else {
                        row += i + ":'"+field+"'";
                    }
                }
            }
            row += "}";
        }
        String json = "{success:true,total:"+ total + ",rows:["+row+"]}";
        return json;
    }

    @Override
    public String insertDevice(Device device) throws Exception {
        Device old = (Device) deviceDao.getById(device.getDeviceId());
        if(old != null){
            return "终端已经存在,请换一个ID";
        }
        deviceDao.create(device);
        return "注册成功";
    }

    @Override
    public String deleteDevice(String terminalId) throws Exception {
        deviceDao.delete(terminalId);
        return "ok";
    }

    @Override
    public String updateDevice(Device device) throws Exception {
        Device old = (Device) deviceDao.getById(device.getDeviceId());
        if(old != null){
            old.setDeviceName(device.getDeviceName());
            old.setIp(device.getIp());
            old.setPort(device.getPort());
            old.setMac(device.getMac());
            old.setProvince(device.getProvince());
            old.setCity(device.getCity());
            old.setOrganization(device.getOrganization());
            old.setInstitution(device.getInstitution());
            old.setPlace(device.getPlace());
            old.setLatitude(device.getLatitude());
            old.setLongitude(device.getLongitude());
            old.setAddress(device.getAddress());
            old.setManager(device.getManager());
            old.setTelephone(device.getTelephone());
            old.setMobile(device.getMobile());
            deviceDao.update(old);
            return "修改成功";
        } else {
            device.setRegisterTime(new Date());
            deviceDao.create(device);
            return "重新注册成功";
        }

    }

    @Override
    public String deviceIpcImage(String terminalId, String ip, int port) throws Exception {
        try{
            return getPicture(ip,port,terminalId);
        } catch (Exception e){

        }
        return StringContext.systemPath + "/others/0013050_600_375.jpg";
    }

    @Override
    public void updateRegister(RegisterRequest register) throws Exception {
        Device old = (Device) deviceDao.getById(register.getDeviceId());
        if(old!=null) {
            old.setIp(register.getIp());
            old.setPort(register.getPort());
            old.setProvince(register.getProvince());
            old.setCity(register.getCity());
            old.setOrganization(register.getOrganization());
            old.setInstitution(register.getInstitution());
            old.setPlace(register.getPlace());
            old.setLatitude(register.getLatitude());
            old.setLongitude(register.getLongitude());
            old.setAddress(register.getAddress());
            old.setManager(register.getManager());
            old.setTelephone(register.getTelephone());
            old.setMobile(register.getMobile());
            old.setRegisterTime(new Date());
            deviceDao.update(old);
        } else {
            old = new Device();
            old.setDeviceId(register.getDeviceId());
            old.setMac("00:00:00:00:00:00");
            old.setDeviceName(register.getDeviceId());
            old.setIp(register.getIp());
            old.setPort(register.getPort());
            old.setProvince(register.getProvince());
            old.setCity(register.getCity());
            old.setOrganization(register.getOrganization());
            old.setInstitution(register.getInstitution());
            old.setPlace(register.getPlace());
            old.setLatitude(register.getLatitude());
            old.setLongitude(register.getLongitude());
            old.setAddress(register.getAddress());
            old.setManager(register.getManager());
            old.setTelephone(register.getTelephone());
            old.setMobile(register.getMobile());
            old.setRegisterTime(new Date());
            deviceDao.create(old);
        }

    }

    @Override
    public String queryFaceCompareResult(int start, int limit, String terminalId,String taskId,
                                       String startDateStr, String endDateStr, String latitude, String longitude) throws Exception {

        PageResult pageResult = faceCompareResultDao.listByParams(start/limit+1,limit,terminalId,taskId,startDateStr,endDateStr,latitude,longitude);
        List<FaceCompareResult> list = pageResult.getResults();
        int total = pageResult.getAllResultsAmount();
        String json = "{success:true,total:"+ total + ",rows:[";
        for (FaceCompareResult f : list) {
            Device device = (Device) deviceDao.getById(Device.class,f.getDeviceId());
            json +="{terminalId:'"+ f.getDeviceId()+"',logTime:'"+DateUtils.formatDate(f.getDateTime(),DateUtils.format)+

                    "',place:'"+(device==null?"未知":device.getPlace())+
                    "',latitude:'"+(device==null?"未知":device.getLatitude())+
                    "',longitude:'"+(device==null?"未知":device.getLongitude())+
                    "',compareResult:'"+f.getCompareResult()+"',badFriend:'"+f.getBadFriend()+
                    "',taskId:'"+f.getTaskId()+"',score:'"+f.getScore()+
                    "',featureId:'"+f.getFeatureId()+"',imagePath:'"+f.getImagePath()+"'},";
        }
        json += "]}";
        return json;
    }

    @Override
    public String insertFaceCompareResult(FaceCompareResult faceCompareResult) throws Exception {
        FaceInfoRequest faceInfoRequest = faceInfoRequestDao.findByTaskId(faceCompareResult.getTaskId());
        faceCompareResult.setDateTime(faceInfoRequest.getDateTime());
        faceCompareResultDao.create(faceCompareResult);
        return "入库成功";
    }

    @Override
    public String updateFaceCompareResult(FaceCompareResult f) throws Exception {
        FaceInfoRequest old =
                faceInfoRequestDao.findByTaskIdAndBadFriend(f.getTaskId(), f.getBadFriend());
        old.setDateTime(f.getDateTime());
        faceCompareResultDao.update(old);
        return "入库成功";
    }

    @Override
    public String insertFeature(FeatureAlert featureAlert) throws Exception {
        FeatureAlert old = (FeatureAlert) featureAlertDao.getById(featureAlert.getId());
        if(old!=null) {
            old.setAlertLevel(featureAlert.getAlertLevel());
            old.setTargetDB(featureAlert.getTargetDB());
            old.setAction(featureAlert.getAction());
            featureAlertDao.update(old);
            return "已存在,修改成功";
        } else {
            featureAlertDao.create(featureAlert);
            return "新增成功";
        }
    }

    @Override
    public String updateFeature(FeatureAlert featureAlert) throws Exception {
        FeatureAlert old = (FeatureAlert) featureAlertDao.getById(featureAlert.getId());
        if(old!=null) {
            old.setAlertLevel(featureAlert.getAlertLevel());
            old.setTargetDB(featureAlert.getTargetDB());
            old.setAction(featureAlert.getAction());
            featureAlertDao.update(old);
            return "修改成功";
        } else {
            featureAlertDao.create(featureAlert);
            return "不存在,新增成功";
        }
    }

    @Override
    public String deleteFeature(String featureId) throws Exception {
        featureAlertDao.delete(FeatureAlert.class,featureId);
        return "ok";
    }

    @Override
    public String queryFeature(int start, int limit, String featureId, String action, String alertLevel) throws Exception {
        PageResult pr = featureAlertDao.listPageResult(start/limit+1,limit,featureId,action,alertLevel);
        int total = pr.getAllResultsAmount();
        List<FeatureAlert> list = pr.getResults();
        String json = "{success:true,total:"+ total + ",rows:[";
        for (FeatureAlert f : list) {
            json += "{featureId:'"+f.getId()+"',action:'"+f.getAction()+
                    "',targetDB:'"+f.getTargetDB()+"',alertLevel:'"+f.getAlertLevel()+"'},";
        }
        json += "]}";
        return json;
    }

    @Override
    public String queryAlert(int start, int limit, String featureId, String taskId, String terminalId, String personInfo,
                             String startDateStr, String endDateStr) throws Exception {
        PageResult pr = alertDao.listPageResult(start/limit+1,limit,featureId,startDateStr,endDateStr,personInfo,taskId,terminalId);
        int total = pr.getAllResultsAmount();
        List<Alert> list = pr.getResults();
        String json = "{success:true,total:"+ total + ",rows:[";
        for (Alert a : list) {
            Device device = (Device) deviceDao.getById(Device.class,a.getTerminalId());
            json += "{terminalId:'"+a.getTerminalId()+"',taskId:'"+a.getTaskId()+
                    "',alertTime:'"+DateUtils.formatDate(a.getAlertTime(),DateUtils.format)+
                    "',place:'"+(device==null?"未知":device.getPlace())+
                    "',latitude:'"+(device==null?"未知":device.getLatitude())+
                    "',longitude:'"+(device==null?"未知":device.getLongitude())+
                    "',featureId:'"+a.getFeatureId()+
                    "',score:'"+a.getScore()+"',personInfo:'"+a.getPersonInfo()+
                    "',ipcFilePath:'"+a.getIpcFilePath()+"',personFilePath:'"+a.getPersonFilePath()+
                    "',alertLevel:'"+a.getAlertLevel()+"'},";
        }
        json += "]}";
        return json;
    }

    @Override
    public void updateFaceInfoRequest(String taskId, String filePath) throws Exception {
        FaceInfoRequest faceInfoRequest = faceInfoRequestDao.findByTaskId(taskId);
        if(faceInfoRequest!=null) {
            String fileHead = faceInfoRequest.getFilePathHead();
            fileHead = fileHead.replaceAll("\\\\","/");
            String _filePath = filePath.replaceAll("\\\\", "/") + "|1";
            fileHead = fileHead.replaceAll(filePath,_filePath);
            faceInfoRequest.setFilePathHead(fileHead);
            faceInfoRequestDao.update(faceInfoRequest);
        }
    }

    private String getPicture(String ip,int port,String deviceId){
        NioSocketConnector connector = TcpShortClient.getInstance().getConnector();
        ConnectFuture connFuture = null;
        try{
            connFuture = connector.connect(new InetSocketAddress(ip,port));
            connFuture.awaitUninterruptibly();
            if  (connFuture.isDone()) {
                if  (!connFuture.isConnected()) {  //若在指定时间内没连接成功，则抛出异常
                    logger.info("fail to connect "+ip+":"+port);
                    throw   new  Exception();
                }
            }
            IoSession session = connFuture.getSession();
            MessageInfo messageInfo = new MessageInfo();
            CatchPictureRequest catchPicture = new CatchPictureRequest();
            catchPicture.setDeviceType("0x02");
            catchPicture.setDeviceId(deviceId);
            byte[] body = catchPicture.toString().getBytes();

            messageInfo.setVersion(MessageInfo.Version);
            messageInfo.setBodyLen(body.length);
            messageInfo.setReserved(new byte[21]);
            messageInfo.setBody(body);
            if(session!=null){
                session.write(messageInfo);
            }
            session.getCloseFuture().awaitUninterruptibly();
            String bodyResponse = (String) session.getAttribute("result");
            CatchPictureResponse response = new CatchPictureResponse();
            response = response.xmlToBean(bodyResponse.getBytes());
            String filePath =  "/temp/temp_"+deviceId+".jpg";
            FileUtil.decodeBase64TOFile(response.getCatchPic(), filePath);
            return filePath;
        } catch (Exception e){
            logger.error(e.getMessage(), e);
        } finally {
            connFuture.cancel();
        }
        return "error.jsp";
    }

    private String toFile(String base64, String filePath, String featureId) throws IOException {
        File file = new File(filePath);
        String imageFilePath = file.getParent() + "/" + featureId + "_" + file.getName();
        FileUtil.decodeBase64TOFile(base64, imageFilePath);
        return imageFilePath;
    }

    private List<Row> getFars1TON(String taskId, String filePath, int threshold) throws IOException {
        String license = XmlString.makeLicense("admin", "0");
        Conditions conditions = new Conditions();
        List<Condition> conditionList = new ArrayList<Condition>();
        Condition condition = new Condition();
        condition.setTaskId(taskId);
        condition.setTaskPriority("0");
        condition.setPosition("0,0,0,0,0,0");
        condition.setImage(FileUtil.encodeBase64FileTOString(filePath));
        condition.setCandidateNum(1);
        condition.setThreshold(threshold);
        List<LogIcDbId> logIcDbIdList = new ArrayList<LogIcDbId>();
        LogIcDbId logIcDbId = new LogIcDbId();
        logIcDbId.setIndex(1);
        logIcDbId.setGender("");
        logIcDbId.setBirthDayF("");
        logIcDbId.setBirthDayL("");
        logIcDbId.setNation("");
        logIcDbId.setReg("");
        logIcDbIdList.add(logIcDbId);
        condition.setLogIcDbIds(logIcDbIdList);
        conditionList.add(condition);
        conditions.setConditions(conditionList);
        String information = conditions.toStringFars1ToN();
        Data data = WsdlClient.getFars1ToN(license, information);
        List<Row> rowList = new ArrayList<Row>();
        List<Record> records = data.getRecords();
        for (Record record : records) {
            List<Result> resultList = record.getResults();
            for (Result result : resultList) {
                List<Row> rowList1 = result.getRows();
                for (Row row : rowList1) {
                    rowList.add(row);
                }
            }
        }
        return rowList;
    }

    private List<Row> getPersonInfo(String featureIdStr) throws UnsupportedEncodingException {
        String license = XmlString.makeLicense("admin","0");
        Conditions conditions = new Conditions();
        List<String> featureIds = new ArrayList<String>();
        String[] fs = featureIdStr.split(";");
        for (String featureId :fs) {
            if(featureId.length()==0){
                continue;
            }
            featureIds.add(featureId);
        }
        conditions.setFeatureIds(featureIds);
        conditions.setImage(true);
        conditions.setPosition(true);
        String information = conditions.toStringGetPersonInfo();
        Data data = WsdlClient.getFarsGETPERSONINFO(license,information);

        List<Row> rows = data.getResult().getRows();
        return rows;
    }

    private Row getPersonInfo_old(String featureId) throws UnsupportedEncodingException {

        List<Field> fieldList = new ArrayList<Field>();
        Field field = new Field();
        field.setName("姓名");
        field.setValue("张**");
        field.setCodeName("xxx");
        field.setCodeValue("yyy");
        fieldList.add(field);
        field = new Field();
        field.setName("姓别");
        field.setValue("女");
        field.setCodeName("GENDER");
        field.setCodeValue("2");
        fieldList.add(field);
        Row row = new Row();
        row.setFeatureId(featureId);
        row.setFields(fieldList);
        row.setxLeft(100.00);
        row.setyLeft(100.00);
        row.setxRight(100.00);
        row.setyRight(100.00);
        row.setxMouth(100.00);
        row.setyMouth(100.00);
        row.setImageBase64("D:/app/center/others/testPhoto.jpg");
        return row;
    }

}

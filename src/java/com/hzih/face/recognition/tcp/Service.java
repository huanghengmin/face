package com.hzih.face.recognition.tcp;

import com.hzih.face.recognition.service.AlertService;
import com.hzih.face.recognition.service.FaceInfoRequestService;
import com.hzih.face.recognition.utils.ImageUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.Properties;


/**
 * Created by Administrator on 15-6-17.
 */
public class Service extends HttpServlet {
    private final static Logger logger = Logger.getLogger(Service.class);

    public AlertService getFaceInfoRequestService() {
        return faceInfoRequestService;
    }

    public void setFaceInfoRequestService(FaceInfoRequestService faceInfoRequestService) {
        this.faceInfoRequestService = faceInfoRequestService;
    }

    private FaceInfoRequestService faceInfoRequestService;

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)  {
        PrintWriter writer = null;
        try{
            String c = request.getParameter("c");
            response.setContentType("text/html;charset=GBK");
            if(c != null ){
                request.getSession().setAttribute("name","device");
                /*if("login".equals(c)){
                    String name = request.getParameter("name");
                    if("admin".equals(name)||"device".equals(name)||"all".equals(name)){
                        request.getSession().setAttribute("name",name);
                        request.getRequestDispatcher("/main.jsp").forward(request, response);
                    }

                } else*/ if("faceInfo".equals(c)) {
                    writer = response.getWriter();
                    String start = request.getParameter("start");
                    String limit = request.getParameter("limit");
                    String terminalId = request.getParameter("terminalId");
                    String taskId = request.getParameter("taskId");
                    String startDateStr = request.getParameter("startDate");
                    String endDateStr = request.getParameter("endDate");
                    String latitude = request.getParameter("latitude");
                    String longitude = request.getParameter("longitude");

//                    String json = faceInfoRequestService.queryFaceInfo(Integer.parseInt(start), Integer.parseInt(limit),
//                            terminalId, startDateStr, endDateStr, taskId, latitude, longitude);
//                    if(json!=null) {
//                        json = StringUtils.trim(json);
//                        writer.println(json);
//                    }
                }/* else if("faceCompareResult".equals(c)) {
                    writer = response.getWriter();
                    String start = request.getParameter("start");
                    String limit = request.getParameter("limit");
                    String terminalId = request.getParameter("terminalId");
                    String taskId = request.getParameter("taskId");
                    String startDateStr = request.getParameter("startDate");
                    String endDateStr = request.getParameter("endDate");
                    String latitude = request.getParameter("latitude");
                    String longitude = request.getParameter("longitude");

                    String json = faceInfoRequestService.queryFaceCompareResult(Integer.parseInt(start), Integer.parseInt(limit),
                            terminalId, taskId, startDateStr, endDateStr, latitude, longitude);
                    if(json!=null) {
                        json = StringUtils.trim(json);
                        writer.println(json);
                    }
                } else if("faceCompareResultInsert".equals(c)) {
                    writer = response.getWriter();
                    String terminalId = request.getParameter("terminalId");
                    String featureId = request.getParameter("featureId");
                    String badFriend = request.getParameter("badFriend");
                    String taskId = request.getParameter("taskId");
                    String filePath = request.getParameter("filePath");
                    String imagePath = request.getParameter("imagePath");
                    imagePath = URLDecoder.decode(imagePath, "utf-8");
                    int score = Integer.parseInt(request.getParameter("score"));
                    terminalId = URLDecoder.decode(terminalId, "utf-8");
                    String compareResult = request.getParameter("compareResult");
                    compareResult = URLDecoder.decode(compareResult, "utf-8");

                    FaceCompareResult faceCompareResult = new FaceCompareResult();
                    faceCompareResult.setDeviceId(terminalId);
                    faceCompareResult.setBadFriend(badFriend);
                    faceCompareResult.setTaskId(taskId);
                    faceCompareResult.setFeatureId(featureId);
                    faceCompareResult.setCompareResult(compareResult);
                    faceCompareResult.setScore(score);
                    faceCompareResult.setImagePath(imagePath);

                    String json = faceInfoRequestService.insertFaceCompareResult(faceCompareResult);
                    faceInfoRequestService.updateFaceInfoRequest(taskId,filePath);
                    if(json!=null) {
                        json = StringUtils.trim(json);
                        writer.println(json);
                    }
                } else if("faceCompareResultUpdate".equals(c)) {
                    writer = response.getWriter();
                    String terminalId = request.getParameter("terminalId");
                    String featureId = request.getParameter("featureId");
                    String badFriend = request.getParameter("badFriend");
                    String taskId = request.getParameter("taskId");
                    String filePath = request.getParameter("filePath");
                    String imagePath = request.getParameter("imagePath");
                    imagePath = URLDecoder.decode(imagePath, "utf-8");
                    int score = Integer.parseInt(request.getParameter("score"));
                    terminalId = URLDecoder.decode(terminalId, "utf-8");
                    String compareResult = request.getParameter("compareResult");
                    compareResult = URLDecoder.decode(compareResult, "utf-8");

                    FaceCompareResult faceCompareResult = new FaceCompareResult();
                    faceCompareResult.setDeviceId(terminalId);
                    faceCompareResult.setBadFriend(badFriend);
                    faceCompareResult.setTaskId(taskId);
                    faceCompareResult.setFeatureId(featureId);
                    faceCompareResult.setCompareResult(compareResult);
                    faceCompareResult.setScore(score);
                    faceCompareResult.setImagePath(imagePath);

                    String json = faceInfoRequestService.updateFaceCompareResult(faceCompareResult);
                    faceInfoRequestService.updateFaceInfoRequest(taskId,filePath);
                    if(json!=null) {
                        json = StringUtils.trim(json);
                        writer.println(json);
                    }
                }*//* else if("device".equals(c)) {
                    writer = response.getWriter();
                    String start = request.getParameter("start");
                    String limit = request.getParameter("limit");
                    String terminalId = request.getParameter("terminalId");
                    String fileName = request.getParameter("fileName");
                    String startDateStr = request.getParameter("startDate");
                    String endDateStr = request.getParameter("endDate");
                    String ip = request.getParameter("ip");
                    String mac = request.getParameter("mac");
//                    String json = faceInfoRequestService.queryDevice(Integer.parseInt(start), Integer.parseInt(limit),
//                            terminalId, startDateStr, endDateStr, fileName, ip, mac);
//                    if(json!=null) {
//                        json = StringUtils.trim(json);
//                        writer.println(json);
//                    }
                } else if("deviceRegister".equals(c)) {
                    writer = response.getWriter();
                    String terminalId = request.getParameter("terminalId");
                    terminalId = URLDecoder.decode(terminalId, "utf-8");
                    String name = request.getParameter("name");
                    name = URLDecoder.decode(name, "utf-8");
                    String ipPort = request.getParameter("ipPort");
                    String mac = request.getParameter("mac");
                    mac = "00:00:00:00:00:00";
                    Device device = new Device();
                    device.setDeviceId(terminalId);
                    device.setDeviceName(name);
                    device.setIp(ipPort.split(":")[0]);
                    device.setPort(Integer.parseInt(ipPort.split(":")[1]));
                    device.setMac(mac);
                    device.setRegisterTime(new Date());
                    device.setLatitude(00.00);
                    device.setLongitude(00.00);

                    String json = faceInfoRequestService.insertDevice(device);
                    if(json!=null) {
                        json = StringUtils.trim(json);
                        writer.println(json);
                    }
                } else if("deviceUpdate".equals(c)) {
                    writer = response.getWriter();
                    String terminalId = request.getParameter("terminalId");
                    terminalId = URLDecoder.decode(terminalId, "utf-8");
                    String ipPort = request.getParameter("ipPort");
                    String name = request.getParameter("name");
                    name = URLDecoder.decode(name, "utf-8");
                    String province = request.getParameter("province");
                    province = URLDecoder.decode(province, "utf-8");
                    String city = request.getParameter("city");
                    city = URLDecoder.decode(city, "utf-8");
                    String organization = request.getParameter("organization");
                    organization = URLDecoder.decode(organization, "utf-8");
                    String institutions = request.getParameter("institutions");
                    institutions = URLDecoder.decode(institutions, "utf-8");
                    String place = request.getParameter("place");
                    place = URLDecoder.decode(place, "utf-8");
                    String address = request.getParameter("address");
                    address = URLDecoder.decode(address, "utf-8");
                    String manager = request.getParameter("manager");
                    manager = URLDecoder.decode(manager, "utf-8");
                    String mobile = request.getParameter("mobile");
                    String telephone = request.getParameter("telephone");
                    double latitude = Double.parseDouble(request.getParameter("latitude"));
                    double longitude = Double.parseDouble(request.getParameter("longitude"));
                    String mac = request.getParameter("mac");
                    mac = "00:00:00:00:00:00";
                    Device device = new Device();
                    device.setDeviceId(terminalId);
                    device.setDeviceName(name);
                    device.setIp(ipPort.split(":")[0]);
                    device.setPort(Integer.parseInt(ipPort.split(":")[1]));
                    device.setMac(mac);
                    device.setRegisterTime(new Date());
                    device.setProvince(province);
                    device.setCity(city);
                    device.setOrganization(organization);
                    device.setInstitution(institutions);
                    device.setPlace(place);
                    device.setAddress(address);
                    device.setManager(manager);
                    device.setMobile(mobile);
                    device.setTelephone(telephone);
                    device.setLatitude(latitude);
                    device.setLongitude(longitude);

                    String json = faceInfoRequestService.updateDevice(device);
                    if(json!=null) {
                        json = StringUtils.trim(json);
                        writer.println(json);
                    }
                } else if("deviceDelete".equals(c)) {
                    writer = response.getWriter();
                    String terminalId = request.getParameter("terminalId");
                    terminalId = URLDecoder.decode(terminalId, "utf-8");
                    String json = faceInfoRequestService.deleteDevice(terminalId);
                    if(json!=null) {
                        json = StringUtils.trim(json);
                        writer.println(json);
                    }
                } else if("deviceIpcImage".equals(c)) {
                    writer = response.getWriter();
                    String terminalId = request.getParameter("terminalId");
                    terminalId = URLDecoder.decode(terminalId, "utf-8");
                    String ipPort = request.getParameter("ipPort");
                    String ip = ipPort.split(":")[0];
                    int port = Integer.parseInt(ipPort.split(":")[1]);
                    String json = faceInfoRequestService.deviceIpcImage(terminalId, ip, port);
                    if(json!=null) {
                        json = StringUtils.trim(json);
                        writer.println(json);
                    }
                }*/ /*else if("queryFeature".equals(c)) {
                    writer = response.getWriter();
                    String featureId = request.getParameter("featureId");
                    featureId = URLDecoder.decode(featureId, "utf-8");
                    String action = request.getParameter("action");
                    action = URLDecoder.decode(action, "utf-8");
                    String alertLevel = request.getParameter("alertLevel");
                    alertLevel = URLDecoder.decode(alertLevel, "utf-8");
                    String start = request.getParameter("start");
                    String limit = request.getParameter("limit");
                    String json = faceInfoRequestService.queryFeature(Integer.parseInt(start), Integer.parseInt(limit),
                            featureId, action, alertLevel);
                    if(json!=null) {
                        json = StringUtils.trim(json);
                        writer.println(json);
                    }
                }*/ /*else if("queryAlert".equals(c)) {
                    writer = response.getWriter();
                    String featureId = request.getParameter("featureId");
                    if(StringUtils.isNotBlank(featureId)){
                        featureId = URLDecoder.decode(featureId, "utf-8");
                    }
                    String terminalId = request.getParameter("terminalId");
                    if(StringUtils.isNotBlank(terminalId)){
                        terminalId = URLDecoder.decode(terminalId, "utf-8");
                    }
                    String personInfo = request.getParameter("personInfo");
                    if(StringUtils.isNotBlank(personInfo)){
                        personInfo = URLDecoder.decode(personInfo, "utf-8");
                    }

                    String taskId = request.getParameter("taskId");
                    String start = request.getParameter("start");
                    String limit = request.getParameter("limit");
                    String startDateStr = request.getParameter("startDate");
                    String endDateStr = request.getParameter("endDate");

                    String json = faceInfoRequestService.queryAlert(Integer.parseInt(start), Integer.parseInt(limit),
                            featureId, taskId, terminalId, personInfo, startDateStr, endDateStr);
                    if(json!=null) {
                        json = StringUtils.trim(json);
                        writer.println(json);
                    }
                }*/ /*else if("insertFeature".equals(c)) {
                    writer = response.getWriter();
                    String featureId = request.getParameter("featureId");
                    featureId = URLDecoder.decode(featureId, "utf-8");
                    String action = request.getParameter("action");
                    action = URLDecoder.decode(action, "utf-8");
                    String alertLevel = request.getParameter("alertLevel");
                    alertLevel = URLDecoder.decode(alertLevel, "utf-8");
                    String targetDB = request.getParameter("targetDB");
                    targetDB = URLDecoder.decode(targetDB, "utf-8");
                    FeatureAlert featureAlert = new FeatureAlert();
                    featureAlert.setId(featureId);
                    featureAlert.setAction(action);
                    featureAlert.setTargetDB(targetDB);
                    featureAlert.setAlertLevel(alertLevel);

                    String json = faceInfoRequestService.insertFeature(featureAlert);
                    if(json!=null) {
                        json = StringUtils.trim(json);
                        writer.println(json);
                    }
                } else if("updateFeature".equals(c)) {
                    writer = response.getWriter();
                    String featureId = request.getParameter("featureId");
                    featureId = URLDecoder.decode(featureId, "utf-8");
                    String action = request.getParameter("action");
                    action = URLDecoder.decode(action, "utf-8");
                    String alertLevel = request.getParameter("alertLevel");
                    alertLevel = URLDecoder.decode(alertLevel, "utf-8");
                    String targetDB = request.getParameter("targetDB");
                    targetDB = URLDecoder.decode(targetDB, "utf-8");
                    FeatureAlert featureAlert = new FeatureAlert();
                    featureAlert.setId(featureId);
                    featureAlert.setAction(action);
                    featureAlert.setTargetDB(targetDB);
                    featureAlert.setAlertLevel(alertLevel);

                    String json = faceInfoRequestService.updateFeature(featureAlert);
                    if(json!=null) {
                        json = StringUtils.trim(json);
                        writer.println(json);
                    }
                } else if("deleteFeature".equals(c)) {
                    writer = response.getWriter();
                    String featureId = request.getParameter("featureId");
                    featureId = URLDecoder.decode(featureId, "utf-8");
                    String json = faceInfoRequestService.deleteFeature(featureId);
                    if(json!=null) {
                        json = StringUtils.trim(json);
                        writer.println(json);
                    }
                } */else if("faceInfoDetailCompare".equals(c)){
                    writer = response.getWriter();
                    String filePath = request.getParameter("filePath");
                    String taskId = request.getParameter("taskId");
                    String deviceId = request.getParameter("terminalId");
                    String threasholdStr = request.getParameter("threshold");
                    int threshold;
                    try{
                        threshold = Integer.parseInt(threasholdStr);
                    } catch (Exception e){
                        threshold = 500;
                    }
                    String json = faceInfoRequestService.queryFaceCompareInfo(taskId, filePath, deviceId, threshold);
                    if(json!=null) {
                        json = StringUtils.trim(json);
                        writer.println(json);
                    }
                } else if("faceInfoDetail".equals(c)){
                    writer = response.getWriter();
                    String filePath = request.getParameter("filePath");
                    String taskId = request.getParameter("taskId");
                    String deviceId = request.getParameter("terminalId");
                    String badFriend = request.getParameter("badFriend");
                    String threasholdStr = request.getParameter("threshold");
                    int threshold;
                    try{
                        threshold = Integer.parseInt(threasholdStr);
                    } catch (Exception e){
                        threshold = 500;
                    }
                    String json = faceInfoRequestService.queryFaceCompareInfoDetail(taskId, filePath, deviceId, badFriend);
                    if(json!=null) {
                        json = StringUtils.trim(json);
                        writer.println(json);
                    }
                } else if("baseInfo".equals(c)){
                    writer = response.getWriter();
                    String json = "";
                    String type = request.getParameter("type");
                    if("read".equalsIgnoreCase(type)){
                        Properties pros = new Properties();
                        String deviceId = null;
                        try {
                            pros.load(getClass().getResourceAsStream("/config.properties"));
                            deviceId = pros.getProperty("deviceId");
//                            Service.sipServerIP = pros.getProperty("sipServerIP");
//                            Service.faceServer = pros.getProperty("faceServer");
//                            Service.sipServerPort = Integer.parseInt(pros.getProperty("sipServerPort"));
                        } catch (IOException e) {
                            logger.error("加载配置文件config.properties错误",e);
                        }
                        json = "{success:true,deviceId:'"+deviceId+"'}";
                    } else if("update".equalsIgnoreCase(type)) {

                        String deviceId = request.getParameter("deviceId");
                        json = "{success:true,msg:'修改成功'}";
                    }
                    if(json!=null) {
                        json = StringUtils.trim(json);
                        writer.println(json);
                    }

                } /*else if("show_photo".equals(c)){
                    ServletOutputStream output = null;
                    InputStream in = null;
                    try {
                        String path = request.getParameter("filePath");
                        response.reset();
                        output = response.getOutputStream();
                        in = new FileInputStream(path);
                        byte tmp[] = new byte[256];
                        int i=0;
                        while ((i = in.read(tmp)) != -1) {
                            output.write(tmp, 0, i);
                        }
                        output.flush(); //强制清出缓冲区
                    }catch (FileNotFoundException e){
                        logger.error(e.getMessage());
                    } catch (Exception e) {
                        logger.error("",e);
                    }finally{
                        try {
                            if(output!=null){
                                output.close();
                            }
                            if(in!=null){
                                in.close();
                            }
                        } catch (IOException e) {
                        }
                    }
                } else if("show_photo_small".equals(c)){
                    ServletOutputStream output = null;
                    InputStream in = null;
                    try {
                        String path = request.getParameter("filePath");
                        int width = Integer.parseInt(request.getParameter("width"));
                        int height = Integer.parseInt(request.getParameter("height"));
                        File file = new File(path);
                        String _path = file.getParent() + "/" +  ImageUtil.DEFAULT_THUMB_PREVFIX + width + "_" + height +"_" + file.getName();
                        File _file = new File(_path);
                        if(!_file.exists()){
                            ImageUtil.makeThumbFile(path,width,height,false);
                        }
                        response.reset();
                        output = response.getOutputStream();
                        in = new FileInputStream(_path);
                        byte tmp[] = new byte[256];
                        int i=0;
                        while ((i = in.read(tmp)) != -1) {
                            output.write(tmp, 0, i);
                        }
                        output.flush(); //强制清出缓冲区
                    }catch (FileNotFoundException e){
                        logger.error(e.getMessage());
                    } catch (Exception e) {
                        logger.error("",e);
                    }finally{
                        try {
                            if(output!=null){
                                output.close();
                            }
                            if(in!=null){
                                in.close();
                            }
                        } catch (IOException e) {
                        }
                    }
                }*/

            }
        } catch (Exception e) {
            logger.error("错误汇集",e);
            try {
//                userOperLogService.saveUserOperLog(new Date(),user,"错误汇集","ERROR",e.getMessage());
            } catch (Exception e1) {
                logger.error("错误汇集入库失败",e1);
            }
            if(writer!=null){
                writer.println("{success:false,msg:'"+e.getMessage()+"'}");
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)  {
        doGet(request,response);
    }


    @Override
    public void init() throws ServletException {
        super.init();
        runServer("0.0.0.0",5000,faceInfoRequestService);
    }

    private void runServer(String ip,int port,FaceInfoRequestService faceInfoRequestService){
        TcpServer tcpServer = new TcpServer();
        InetSocketAddress address = new InetSocketAddress(ip,port);
        tcpServer.init(address,faceInfoRequestService);
        new Thread(tcpServer).start();
    }
}

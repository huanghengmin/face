package com.hzih.face.recognition.web.action.recognition;

import cn.collin.commons.utils.DateUtils;
import com.hzih.face.recognition.domain.Device;
import com.hzih.face.recognition.service.AlertService;
import com.hzih.face.recognition.service.FaceInfoRequestService;
import com.hzih.face.recognition.service.LogService;
import com.hzih.face.recognition.web.SessionUtils;
import com.hzih.face.recognition.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Date;

/**
 * Created by Administrator on 15-10-22.
 */
public class DeviceAction extends ActionSupport {
    private FaceInfoRequestService faceInfoRequestService;
    private int start;
    private int limit;
    private Logger logger = Logger.getLogger(DeviceAction.class);
    private LogService logService;

    public String find() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String terminalId = request.getParameter("terminalId");
        String fileName = request.getParameter("fileName");
        String ip = request.getParameter("ip");
        String mac = request.getParameter("mac");
        String json = "{'success':true,'total':0,'rows':[]}";
        try {
            Date startDate = com.hzih.face.recognition.utils.StringUtils.isBlank(startDateStr) ? null : DateUtils.parse(startDateStr, "yyyy-MM-dd");
            Date endDate = com.hzih.face.recognition.utils.StringUtils.isBlank(endDateStr) ? null : DateUtils.parse(endDateStr, "yyyy-MM-dd");
            json = faceInfoRequestService.queryDevice(start, limit, terminalId, startDate, endDate, fileName, ip, mac);
        } catch (Exception e) {
            logger.error("用户日志审计", e);
            logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "用户日志审计", "用户读取用户日志审计信息失败 ");
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String register() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String terminalId = request.getParameter("terminalId");
        terminalId = URLDecoder.decode(terminalId, "utf-8");
        String name = request.getParameter("name");
        name = URLDecoder.decode(name, "utf-8");
        String ipPort = request.getParameter("ipPort");
        String mac = request.getParameter("mac");
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
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String update() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
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
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String remove() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String terminalId = request.getParameter("terminalId");
        terminalId = URLDecoder.decode(terminalId, "utf-8");
        String json = faceInfoRequestService.deleteDevice(terminalId);
        if(json!=null) {
            json = StringUtils.trim(json);
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String ipcImage() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String terminalId = request.getParameter("terminalId");
        terminalId = URLDecoder.decode(terminalId, "utf-8");
        String ipPort = request.getParameter("ipPort");
        String ip = ipPort.split(":")[0];
        int port = Integer.parseInt(ipPort.split(":")[1]);
        String json = faceInfoRequestService.deviceIpcImage(terminalId, ip, port);
        if(json!=null) {
            json = StringUtils.trim(json);
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }


    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public FaceInfoRequestService getFaceInfoRequestService() {
        return faceInfoRequestService;
    }

    public void setFaceInfoRequestService(FaceInfoRequestService faceInfoRequestService) {
        this.faceInfoRequestService = faceInfoRequestService;
    }
}
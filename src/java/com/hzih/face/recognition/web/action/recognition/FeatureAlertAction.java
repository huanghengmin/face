package com.hzih.face.recognition.web.action.recognition;

import com.hzih.face.recognition.domain.FeatureAlert;
import com.hzih.face.recognition.service.AlertService;
import com.hzih.face.recognition.service.FaceInfoRequestService;
import com.hzih.face.recognition.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

/**
 * Created by Administrator on 15-10-23.
 */
public class FeatureAlertAction extends ActionSupport {

    private FaceInfoRequestService faceInfoRequestService;

    public FaceInfoRequestService getFaceInfoRequestService() {
        return faceInfoRequestService;
    }

    public void setFaceInfoRequestService(FaceInfoRequestService faceInfoRequestService) {
        this.faceInfoRequestService = faceInfoRequestService;
    }

    public String find() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String featureId = request.getParameter("featureId");
        if(featureId!=null)
            featureId = URLDecoder.decode(featureId, "utf-8");
        String action = request.getParameter("action");
        if(action!=null)
            action = URLDecoder.decode(action, "utf-8");
        String alertLevel = request.getParameter("alertLevel");
        if(alertLevel!=null)
            alertLevel = URLDecoder.decode(alertLevel, "utf-8");
        String start = request.getParameter("start");
        String limit = request.getParameter("limit");
        String json = faceInfoRequestService.queryFeature(Integer.parseInt(start), Integer.parseInt(limit),featureId, action, alertLevel);
        if(json!=null) {
            json = StringUtils.trim(json);
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String insert() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
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
        json = StringUtils.trim(json);
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String update() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
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
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String delete() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String featureId = request.getParameter("featureId");
        featureId = URLDecoder.decode(featureId, "utf-8");
        String json = faceInfoRequestService.deleteFeature(featureId);
        if(json!=null) {
            json = StringUtils.trim(json);
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }
}

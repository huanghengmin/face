package com.hzih.face.recognition.web.action.recognition;

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
public class AlertAction extends ActionSupport {
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

        String json = faceInfoRequestService.queryAlert(Integer.parseInt(start), Integer.parseInt(limit), featureId, taskId, terminalId, personInfo, startDateStr, endDateStr);
        if(json!=null) {
            json = StringUtils.trim(json);
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }
}

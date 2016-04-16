package com.hzih.face.recognition.web.action.recognition;

import com.hzih.face.recognition.domain.FaceCompareResult;
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
public class FaceCompareResultAction extends ActionSupport {
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
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String insert() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
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
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }
}

package com.hzih.face.recognition.web.action.recognition;

import cn.collin.commons.utils.DateUtils;
import com.hzih.face.recognition.service.AlertService;
import com.hzih.face.recognition.service.FaceInfoRequestService;
import com.hzih.face.recognition.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by Administrator on 15-10-23.
 */
public class FaceAction extends ActionSupport {
    private int start;
    private int limit;

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
        String terminalId = request.getParameter("terminalId");
        String taskId = request.getParameter("taskId");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String latitude = request.getParameter("latitude");
        String longitude = request.getParameter("longitude");
        Date startDate = com.hzih.face.recognition.utils.StringUtils.isBlank(startDateStr) ? null : DateUtils
                .parse(startDateStr, "yyyy-MM-dd");
        Date endDate = com.hzih.face.recognition.utils.StringUtils.isBlank(endDateStr) ? null : DateUtils
                .parse(endDateStr, "yyyy-MM-dd");
        String json = faceInfoRequestService.queryFaceInfo(start, limit, terminalId, startDate, endDate, taskId, latitude, longitude);
        if(json!=null) {
            json = StringUtils.trim(json);
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }
}

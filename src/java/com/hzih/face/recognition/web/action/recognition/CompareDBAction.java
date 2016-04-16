package com.hzih.face.recognition.web.action.recognition;

import cn.collin.commons.domain.PageResult;
import com.hzih.face.recognition.dao.CompareDBDao;
import com.hzih.face.recognition.domain.CompareDB;
import com.hzih.face.recognition.domain.Device;
import com.hzih.face.recognition.domain.DeviceInfoResponse;
import com.hzih.face.recognition.service.FaceInfoRequestService;
import com.hzih.face.recognition.utils.DateUtils;
import com.hzih.face.recognition.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 15-10-23.
 */
public class CompareDBAction extends ActionSupport {
    private CompareDBDao compareDBDao;
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

    public CompareDBDao getCompareDBDao() {
        return compareDBDao;
    }

    public void setCompareDBDao(CompareDBDao compareDBDao) {
        this.compareDBDao = compareDBDao;
    }

    public String find() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String depart = request.getParameter("depart");
        PageResult listPageResult = compareDBDao.listPageResult(start / limit + 1, limit, name, type, depart);
        List<CompareDB> list = listPageResult.getResults();
        int total = listPageResult.getAllResultsAmount();
        String json = "{success:true,total:" + total + ",rows:[";
        for (CompareDB db : list) {
            json += "{id:'" + db.getId() + "',name:'" + db.getName() +
                    "',type:'" + db.getType() + "',depart:'" + db.getDepart() +
                    "',describe:'" + db.getDescribe() +
                    "'},";
        }
        json += "]}";
        actionBase.actionEnd(response, json, result);
        return null;
    }
}

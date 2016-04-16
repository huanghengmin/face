package com.hzih.face.recognition.web.action.recognition;

import com.hzih.face.recognition.utils.ImageUtil;
import com.hzih.face.recognition.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by Administrator on 15-11-17.
 */
public class ImageAction extends ActionSupport {
    private Logger logger = Logger.getLogger(ImageAction.class);

    public String show_photo_small() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
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
            output.flush();
        }catch (FileNotFoundException e){
            logger.error(e.getMessage(),e);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
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
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String show_photo() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
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
            output.flush();
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
        actionBase.actionEnd(response, json, result);
        return null;
    }

}

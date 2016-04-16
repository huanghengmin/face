package com.hzih.face.recognition.utils;

import com.hzih.face.recognition.client.entity.*;
import com.hzih.face.recognition.domain.*;
import com.hzih.face.recognition.entity.SipXml;
import com.inetec.common.exception.Ex;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Configuration {
    static Logger logger = Logger.getLogger(Configuration.class);

    private Document document;
    public String confPath;

    public Configuration(Document doc) {
        this.document = doc;
    }

    public Configuration(byte[] buff) {
        SAXReader saxReader = new SAXReader();
        try {
            InputStream is = byteToString(buff);
            document = saxReader.read(is);
        } catch (DocumentException e) {
            logger.info(e.getMessage(),e);
        }
    }

    public FaceInfoRequest getFaceInfoRequest() throws ParseException {
        Element base = (Element)document.selectSingleNode("/Query");
        if(base!=null){
            FaceInfoRequest faceInfo = new FaceInfoRequest();
            String deviceType = base.element("DeviceType").getText().trim();
            String deviceId = base.element("DeviceID").getText().trim();
            String taskId = base.element("TaskID").getText().trim();
            String cmdType = base.element("CmdType").getText().trim();
            String latitude = base.element("Latitude").getText().trim();
            String longitude = base.element("Longitude").getText().trim();
            String dateTime = base.element("DateTime").getText().trim();
            String faceNum = base.element("FaceNum").getText().trim();
            String compStatus = base.element("CompStatus").getText().trim();
            String compressFormat = base.element("CompressFormat").getText().trim();
            String fileName = base.element("FileName").getText().trim();
            String fileSize = base.element("FileSize").getText().trim();
            faceInfo.setDeviceType(deviceType);
            faceInfo.setDeviceId(deviceId);
            faceInfo.setTaskId(taskId);
            faceInfo.setCmdType(cmdType);
            faceInfo.setLatitude(Double.parseDouble(latitude));
            faceInfo.setLongitude(Double.parseDouble(longitude));
            faceInfo.setDateTime(DateUtils.parse(dateTime,DateUtils.format));
            faceInfo.setFaceNum(Integer.parseInt(faceNum.substring(2),16));
            faceInfo.setCompStatus(compStatus);
            faceInfo.setCompressFormat(compressFormat);
            faceInfo.setFileName(fileName);
            faceInfo.setFileSize(Long.parseLong(fileSize.substring(2),16));
            return faceInfo;
        }
        return null;
    }

    public CatchPictureResponse getCatchPictureResponseResponse() {
        Element base = (Element)document.selectSingleNode("/Response");
        if(base!=null){
            CatchPictureResponse catchPicture = new CatchPictureResponse();
            catchPicture.setDeviceId(base.element("DeviceID").getText().trim());
            catchPicture.setDeviceType(base.element("DeviceType").getText().trim());
            catchPicture.setCmdType(base.element("CmdType").getText().trim());
            try{
                catchPicture.setCatchPic(base.element("CatchPic").getText().trim());
            } catch (Exception e){
            }
            return catchPicture;
        }
        return null;
    }

    public AlarmInfoRequest getAlarmInfoRequest(){
        Element base = (Element)document.selectSingleNode("/Notify");
        if(base!=null){
            AlarmInfoRequest alarmInfo = new AlarmInfoRequest();
            String deviceType = base.element("DeviceType").getText().trim();
            String deviceId = base.element("DeviceID").getText().trim();
            String cmdType = base.element("CmdType").getText().trim();
            String gpsStatus;
            try{
                gpsStatus = base.element("GPSStatus").getText().trim();
            } catch (Exception e){
                gpsStatus = SipXml.ResultFailure;
            }
            String nfcStatus;
            try{
                nfcStatus = base.element("NFCStatus").getText().trim();
            } catch (Exception e){
                nfcStatus = SipXml.ResultFailure;
            }
            String camStatus;
            try{
                camStatus = base.element("CAMStatus").getText().trim();
            } catch (Exception e){
                camStatus = SipXml.ResultFailure;
            }
            alarmInfo.setDeviceType(deviceType);
            alarmInfo.setDeviceId(deviceId);
            alarmInfo.setCmdType(cmdType);
            alarmInfo.setGpsStatus(gpsStatus);
            alarmInfo.setNfcStatus(nfcStatus);
            alarmInfo.setCamStatus(camStatus);
            return alarmInfo;
        }
        return null;
    }

    public FaceInfoCompRequest getFaceInfoCompRequest() {
        Element base = (Element)document.selectSingleNode("/Notify");
        if(base!=null){
            FaceInfoCompRequest faceInfoCompRequest = new FaceInfoCompRequest();
            String deviceType = base.element("DeviceType").getText().trim();
            String deviceId = base.element("DeviceID").getText().trim();
            String featureId = base.element("FeatureID").getText().trim();
            String taskId = base.element("TaskID").getText().trim();
            String cmdType = base.element("CmdType").getText().trim();
            String score = base.element("Score").getText().trim();
            String photoName = base.element("PhotoName").getText().trim();

            faceInfoCompRequest.setDeviceId(deviceId);
            faceInfoCompRequest.setCmdType(cmdType);
            faceInfoCompRequest.setDeviceType(deviceType);
            faceInfoCompRequest.setTaskId(taskId);
            faceInfoCompRequest.setScore(score);
            faceInfoCompRequest.setPhotoName(photoName);
            faceInfoCompRequest.setFeatureId(featureId);
            return faceInfoCompRequest;
        }
        return null;
    }

    public Data get1ToNData() {
        Element dataE = (Element)document.selectSingleNode("/DATA");
        if(dataE!=null) {
            Data data = new Data();
            List<Record> recordList = new ArrayList<Record>();
            List<Result> resultList = new ArrayList<Result>();
            List<Row> rowList = new ArrayList<Row>();
            String code = dataE.attribute("CODE").getText();
            String msg = dataE.attribute("MSG")==null?"调用失败":dataE.attribute("MSG").getText();
            data.setCode(code);
            data.setMsg(msg);
            if("0000".equalsIgnoreCase(code)){
                List<Element> recordEList =  document.selectNodes("/DATA/RECORD");
                for (Element recordE : recordEList) {
                    Record record = new Record();
                    record.setTaskId(recordE.attribute("TASKID").getText());
                    record.setCode(recordE.attribute("CODE").getText());
                    record.setMsg(recordE.attribute("MSG").getText());
                    List<Element> resultEList =  recordE.selectNodes("RESULT");
                    if(recordEList == null) {
                        continue;
                    }
                    for (Element resultE : resultEList) {
                        Result result = new Result();
                        int counter = Integer.parseInt(resultE.attribute("COUNTER")==null?"0":resultE.attribute("COUNTER").getText());
                        result.setCounter(counter);
                        result.setJobId(resultE.attribute("JOBID").getText());
                        List<Element> rowEList = resultE.selectNodes("ROW");
                        if(rowEList == null){
                            continue;
                        }
                        for (Element rowE :rowEList){
                            Row row = new Row();
                            row.setIndex(Integer.parseInt(rowE.attribute("INDEX").getText()));
                            row.setFeatureId(rowE.element("FEATUREID").getTextTrim());
                            row.setScore(Integer.parseInt(rowE.element("SCORE").getTextTrim()));
                            row.setLogicDbId(rowE.element("LOGICDBID").getTextTrim());
                            rowList.add(row);
                        }
                        result.setRows(rowList);
                        resultList.add(result);
                    }
                    record.setResults(resultList);
                    recordList.add(record);
                }
                data.setRecords(recordList);
                return data;
            }
            data.setRecords(recordList);
            return data;
        }
        return null;
    }

    public Data getPersonInfoData(){
        Element dataE = (Element)document.selectSingleNode("/DATA");
        if(dataE!=null) {
            Data data = new Data();
            Result result = new Result();
            List<Row> list = new ArrayList<Row>();
            String code = dataE.attribute("CODE").getText();
            String msg = dataE.attribute("MSG").getText();
            data.setCode(code);
            data.setMsg(msg);
            if("0000".equalsIgnoreCase(code)){
                Element resultE = (Element) document.selectSingleNode("/DATA/RESULT[@COUNTER="+1+"]");
                int counter = Integer.parseInt(resultE==null?"0":resultE.attribute("COUNTER").getText());
                result.setCounter(counter);
                for (int i = 1; i <= counter;i++){
                    Row row = new Row();
                    Element rowE = (Element) document.selectSingleNode("/DATA/RESULT[@COUNTER="+counter+"]/ROW[@INDEX="+i+"]");
                    row.setFeatureId(rowE.element("FEATUREID").getTextTrim());
                    row.setImageBase64(rowE.element("IMAGE").getTextTrim());
                    Element position = (Element) rowE.element("POSITION");
                    row.setxLeft(Double.parseDouble(position.element("XLEFT").getTextTrim()));
                    row.setyLeft(Double.parseDouble(position.element("YLEFT").getTextTrim()));
                    row.setxRight(Double.parseDouble(position.element("XRIGHT").getTextTrim()));
                    row.setyRight(Double.parseDouble(position.element("YRIGHT").getTextTrim()));
                    row.setxMouth(Double.parseDouble(position.element("XMOUTH").getTextTrim()));
                    row.setyMouth(Double.parseDouble(position.element("YMOUTH").getTextTrim()));
                    List<Element> fieldsE = rowE.selectNodes("FIELD");
                    List<Field> fieldList = new ArrayList<Field>();
                    for (Element fieldE : fieldsE){
                        Field f = new Field();
                        f.setName(fieldE.element("FIELD_NAME").getTextTrim());
                        f.setValue(fieldE.element("FIELD_VALUE").getTextTrim());
                        f.setCodeName(fieldE.element("FIELD_CODENAME") == null ? "--":fieldE.element("FIELD_CODENAME").getTextTrim());
                        f.setCodeValue(fieldE.element("FIELD_CODEVALUE") == null ? "--":fieldE.element("FIELD_CODEVALUE").getTextTrim());
                        fieldList.add(f);
                    }
                    row.setFields(fieldList);
                    list.add(row);
                }
                result.setRows(list);
                data.setResult(result);
                return data;
            }
            result.setRows(list);
            data.setResult(result);
            return data;
        }
        return null;
    }

    public DeviceStatusResponse getDeviceStatusResponse() {
        Element base = (Element)document.selectSingleNode("/Response");
        if(base!=null){
            DeviceStatusResponse deviceStatus = new DeviceStatusResponse();
            String deviceType = base.element("DeviceType").getText().trim();
            String deviceId = base.element("DeviceID").getText().trim();
            String cmdType = base.element("CmdType").getText().trim();
            String dStatus = base.element("DeviceStatus").getText().trim();
            String result = base.element("Result").getText().trim();
            deviceStatus.setDeviceType(deviceType);
            deviceStatus.setDeviceId(deviceId);
            deviceStatus.setCmdType(cmdType);
            deviceStatus.setDeviceStatus(dStatus);
            deviceStatus.setResult(result);
            return deviceStatus;
        }
        return null;
    }

    public DeviceInfoResponse getDeviceInfoResponse() {
        Element base = (Element)document.selectSingleNode("/Response");
        if(base!=null){
            DeviceInfoResponse deviceInfo = new DeviceInfoResponse();
            deviceInfo.setDeviceId(base.element("DeviceID").getText().trim());
            deviceInfo.setDeviceType(base.element("DeviceType").getText().trim());
            deviceInfo.setCmdType(base.element("CmdType").getText().trim());
            deviceInfo.setCpuUse(base.element("CPUUsage").getText().trim());
            deviceInfo.setCpuTemperature(base.element("CPUTemperature").getText().trim());
            deviceInfo.setMemSituation(base.element("MemorySituation").getText().trim());
            deviceInfo.setComparisonState(base.element("ComparisonState").getText().trim());
            deviceInfo.setIpcNetState(base.element("IPCNetworkState").getText().trim());
            try {
                deviceInfo.setIpcImageState(base.element("IPCImageState").getText().trim());
            } catch (Exception e){
                deviceInfo.setIpcImageState("0");
            }
            deviceInfo.setLastTime(base.element("FaceDetectTime").getText().trim());
            try {
                deviceInfo.setResult(base.element("Result").getText().trim());
            }catch (Exception e){
                deviceInfo.setResult("没有该标签");
            }
            return deviceInfo;
        }
        return null;
    }

    public RegisterRequest getRegisterRequest() {
        Element base = (Element)document.selectSingleNode("/Register");
        if(base!=null){
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setDeviceId(base.element("DeviceID").getText().trim());
            registerRequest.setDeviceType(base.element("DeviceType").getText().trim());
            registerRequest.setCmdType(base.element("CmdType").getText().trim());
            try {
                registerRequest.setProvince(new String(Base64.decodeBase64((base.element("Province").getTextTrim().getBytes())),"gbk"));
                registerRequest.setCity(new String(Base64.decodeBase64((base.element("City").getTextTrim().getBytes())), "gbk"));
                registerRequest.setOrganization(new String(Base64.decodeBase64((base.element("Organization").getTextTrim().getBytes())), "gbk"));
                registerRequest.setInstitution(new String(Base64.decodeBase64((base.element("Institution").getTextTrim().getBytes())), "gbk"));
                registerRequest.setPlace(new String(Base64.decodeBase64((base.element("Place").getTextTrim().getBytes())), "gbk"));
                registerRequest.setAddress(new String(Base64.decodeBase64((base.element("Address").getTextTrim().getBytes())), "gbk"));
                String latitude = base.element("Latitude").getText().trim();
                String longitude = base.element("Longitude").getText().trim();
                registerRequest.setLatitude(Double.parseDouble(latitude));
                registerRequest.setLongitude(Double.parseDouble(longitude));
                registerRequest.setIp(base.element("IP").getTextTrim());
                registerRequest.setPort(Integer.parseInt(base.element("Port").getTextTrim()));
                registerRequest.setManager(new String(Base64.decodeBase64((base.element("Manager").getTextTrim().getBytes())), "gbk"));
                registerRequest.setMobile(base.element("Mobile").getTextTrim());
                registerRequest.setTelephone(base.element("Telephone").getTextTrim());


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return registerRequest;
        }
        return null;
    }

    private InputStream byteToString(byte[] buff) {
        return new ByteArrayInputStream(buff);
    }

    public boolean getPrivated() {
        String value = "1";
        Element channel = (Element)document.selectSingleNode("/configuration/system/ichange/stp/channel[@value='"+value+"']");
        return Boolean.valueOf(channel.element("privated").getText());
    }

    public Configuration(String path) throws Ex {
        this.confPath = path;
        SAXReader saxReader = new SAXReader();
        try {
            document = saxReader.read(path);
        } catch (DocumentException e) {
            e.printStackTrace();
            logger.info(e.getMessage(),e);
        }
    }

    public Configuration(InputStream is, String path) throws Ex {
        this.confPath = path;
        SAXReader saxReader = new SAXReader();
        try {
            document = saxReader.read(is);
        } catch (DocumentException e) {
            logger.info(e.getMessage(),e);
        }
    }

    public String editConnectorIp(String ip, String port) {
        try{
            Element connector = (Element) document.selectSingleNode("/Server/Service/Connector[@port=" + port + "]");
            if(connector != null){
                connector.attribute("address").setText(ip);
                String result = save();
                if(result.equals("保存成功")){
                    if(port.equals(""+8443)){
                        return "更新管理服务接口设定IP地址成功";
                    }else if(port.equals(""+8000)){
                        return "更新集控采集数据接口设定IP地址成功";
                    }else{
                        return "更新成功,端口是"+port;
                    }
                }else{
                    return result;
                }
            }
        } catch (Exception e){
            logger.info(e.getMessage(),e);
        }
        return "更新出错";
    }

    public String getConnectorIp(String port) {
        String ip = "";
        try{
           Element connector = (Element) document.selectSingleNode("/Server/Service/Connector[@port=" + port + "]");
            if(connector != null){
                ip = connector.attribute("address").getText();
            }
        } catch (Exception e){
            logger.info(e.getMessage(),e);
        }
        return ip;
    }

    public List<String> getAllowIPs(){
        List<String> allowIps = new ArrayList<String>();
        try{
            Element valve = (Element) document.selectSingleNode("/Server/Service/Engine/Valve");
            if(valve!=null){
                String ip = valve.attribute("allow").getText();
                String[] ips = ip.split("\\|");
                if(ips.length>1){
                    for (int i = 0; i < ips.length; i ++){
                        allowIps.add(ips[i]);
                    }
                }else{
                    allowIps.add(ip);
                }
            }
        } catch (Exception e){
            logger.info(e.getMessage(),e);
        }
        return allowIps;
    }

    public String editAllowIp(String ip) {
        try{
            Element value = (Element) document.selectSingleNode("/Server/Service/Engine/Valve");
            if(value!=null){
                ip = value.attribute("allow").getText() + ip;
                value.attribute("allow").setText(ip);
                String result = save();
                if(result.equals("保存成功")){
                        return "更新管理客户机地址成功";
                }else{
                    return result;
                }
            }
        } catch (Exception e){
            logger.info(e.getMessage(),e);
        }
        return "更新出错";
    }
    public String deleteAllowIp(String ip) {
        try{
            Element value = (Element) document.selectSingleNode("/Server/Service/Engine/Valve");
            if(value!=null){
                value.attribute("allow").setText(ip);
                String result = save();
                if(result.equals("保存成功")){
                        return "删除管理客户机地址成功";
                }else{
                    return result;
                }
            }
        } catch (Exception e){
            logger.info(e.getMessage(),e);
        }
        return "删除出错";
    }

    public String save() {
        String result = null;
        XMLWriter output = null;
        try {
            File file = new File(confPath);
            FileInputStream fin = new FileInputStream(file);
            byte[] bytes = new byte[fin.available()];
            while (fin.read(bytes) < 0) fin.close();
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            output = new XMLWriter(new FileOutputStream(file),format);
            if(document != null){
                output.write(document);
                return result = "保存成功";
            }else{
                result = "dom4j处理出错";
            }
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(),e);
            result = e.getMessage();
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            result = e.getMessage();
        } finally {
            try {
                if (output != null)
                    output.close();
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
                result = e.getMessage();
            }
        }
        return "保存失败,"+result;
    }


}
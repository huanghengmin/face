package com.hzih.face.recognition.tcp;

import JACE.ASX.MessageBlock;
import JACE.ASX.MessageQueue;
import com.hzih.face.recognition.client.WsdlClient;
import com.hzih.face.recognition.client.entity.*;
import com.hzih.face.recognition.domain.CompareInfo;
import com.hzih.face.recognition.domain.FaceInfoCompRequest;
import com.hzih.face.recognition.domain.FaceInfoRequest;
import com.hzih.face.recognition.service.FaceInfoRequestService;
import com.hzih.face.recognition.utils.FileUtil;
import org.apache.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-29
 * Time: 下午4:59
 */
public class CompareService extends Thread {
    private static final Logger logger = Logger.getLogger(CompareService.class);
    private MessageQueue queue;
    private boolean run = false;
    private FaceInfoRequestService faceInfoRequestService;

    public void init(FaceInfoRequestService faceInfoRequestService) {
        this.faceInfoRequestService = faceInfoRequestService;
    }

    public boolean haveMessages() {
        if (queue == null) {
            queue = new MessageQueue();
            return false;
        }
        return (!queue.isEmpty());
    }

    public void offer(CompareInfo compareInfo) {
        try {
            queue.enqueueTail(new MessageBlock(compareInfo));
        } catch (InterruptedException e) {
        }
    }

    public void offer(FaceInfoCompRequest faceInfoCompRequest) {
        try {
            queue.enqueueTail(new MessageBlock(faceInfoCompRequest));
        } catch (InterruptedException e) {
        }
    }

    public void run() {
        this.run = true;
        while (run) {
            if (haveMessages()) {
                FileOutputStream os = null;
                try {
                    MessageBlock messageBlock = queue.dequeueHead();
                    Object obj = messageBlock.obj();
                    CompareInfo compareInfo;
                    if (obj instanceof CompareInfo) {
                        compareInfo = (CompareInfo) obj;

                        List<Row> rowList = getFars1TON(compareInfo.getTaskId(), compareInfo.getFilePath(), 10);
                        String featureIds = "";
                        String scores = "";
                        for (Row row : rowList) {
                            if (featureIds == null) {
                                featureIds = "";
                            }
                            featureIds += row.getFeatureId() + ";";
                            scores += row.getScore() + ";";
                        }

                        compareInfo.setScore(scores);
                        compareInfo.setFeatureId(featureIds);
                        FaceInfoCompRequest faceInfoCompRequest = new FaceInfoCompRequest();
                        faceInfoCompRequest.setTaskId(compareInfo.getTaskId());
                        faceInfoCompRequest.setFilePath(compareInfo.getFilePath());
                        faceInfoCompRequest.setDeviceId(compareInfo.getTerminalId());
                        faceInfoCompRequest.setScore(scores);
                        faceInfoCompRequest.setFeatureId(featureIds);

                        if (faceInfoRequestService != null) {
                            faceInfoRequestService.insertFaceInfoCompRequest(faceInfoCompRequest);
                        }

                    } else if (obj instanceof FaceInfoCompRequest) {
                        FaceInfoCompRequest faceInfoCompRequest = (FaceInfoCompRequest) obj;
                        if (faceInfoRequestService != null) {
                            FaceInfoRequest faceInfoRequest = faceInfoRequestService.queryFaceInfo(faceInfoCompRequest.getTaskId());
                            if (faceInfoRequest != null) {
                                faceInfoCompRequest.setDeviceId(faceInfoRequest.getDeviceId());
                                faceInfoCompRequest.setFeatureId(faceInfoCompRequest.getFeatureId());
                                String[] filePathHeads = faceInfoRequest.getFilePathHead().split(";");
                                for (String filePathHead : filePathHeads) {
                                    if (filePathHead.indexOf(faceInfoCompRequest.getPhotoName()) > -1) {
                                        faceInfoCompRequest.setFilePath(filePathHead);
                                        break;
                                    }
                                }
                                if (faceInfoCompRequest.getFilePath() != null) {
                                    faceInfoRequestService.insertFaceInfoCompRequest(faceInfoCompRequest);
                                } else {
                                    logger.equals("人脸识别文件路径为空");
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("比较服务取队列错误", e);
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
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
        List<LogIcDbId> logIcDbIdList = new ArrayList();
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
        List<Row> rowList = new ArrayList();
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

    public void close() {
        this.run = false;
    }

    public boolean isRunning() {
        return run;
    }
}

package com.hzih.face.recognition.tcp;

import JACE.ASX.MessageBlock;
import JACE.ASX.MessageQueue;
import com.hzih.face.recognition.domain.CompareInfo;
import com.hzih.face.recognition.domain.FaceInfoRequest;
import com.hzih.face.recognition.service.FaceInfoRequestService;
import com.hzih.face.recognition.utils.DateUtils;
import com.hzih.face.recognition.utils.StringContext;
import com.hzih.face.recognition.utils.YUVToImage;
import com.hzih.face.recognition.utils.zip.ZipFileUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-29
 * Time: 下午4:59
 */
public class ReceiveService extends Thread{
    private static final Logger logger = Logger.getLogger(ReceiveService.class);
    private MessageQueue queue;
    private boolean run = false;
    private FaceInfoRequestService faceInfoRequestService;
    public static String backupDir;

    private CompareService compareService;

    public void init(CompareService compareService,FaceInfoRequestService faceInfoRequestService) {
        if(backupDir == null) {
            backupDir = StringContext.systemPath + "/data";
        }
        this.compareService = compareService;
        this.faceInfoRequestService = faceInfoRequestService;
    }

    public boolean haveMessages() {
        if(queue == null) {
            queue = new MessageQueue();
            return false;
        }
        return (!queue.isEmpty());
    }

    public void offer(FaceInfoRequest faceInfoRequest){
        try {
            queue.enqueueTail(new JACE.ASX.MessageBlock(faceInfoRequest));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String makeDir(String parent){
        String day = DateUtils.formatDate(new Date(), DateUtils.format);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String dir = parent +  "/" + day + "/" + hour;
        File dirFile = new File(dir);
        if(!dirFile.isDirectory()){
            dirFile.mkdirs();
        }
        return dir;
    } 

    public void run() {
        this.run = true;
        while (run){
            if(haveMessages()){
                FileOutputStream os = null;
                try {
                    MessageBlock messageBlock = queue.dequeueHead();
                    String dir = makeDir(ReceiveService.backupDir);
                    String zipDir = makeDir(ReceiveService.backupDir);
                    FaceInfoRequest faceInfoRequest = (FaceInfoRequest)messageBlock.obj();
                    String file = zipDir + "/" + faceInfoRequest.getFileName();
                    os = new FileOutputStream(file);
                    os.write(faceInfoRequest.getFileBuff());
                    os.close();

                    int cf = Integer.parseInt(faceInfoRequest.getCompressFormat().substring(2),16);
                    List<String> filePathList = new ArrayList<String>();
                    List<String> filePathResultList = new ArrayList<String>();
                    boolean isAllowDelete = false;
                    switch (cf) {
                        case 0://0x00 未压缩
                            break;
                        case 1://0x01 rar
                            if(ZipFileUtil.isEndsWithRAR(file)){
                                filePathList = ZipFileUtil.decompressRAR(file, dir);

                                isAllowDelete = true;
                            }
                            break;
                        case 2://0x02 zip
                            if(ZipFileUtil.isEndsWithZip(file)){
                                filePathList = ZipFileUtil.decompressZipList(file,dir);
                                isAllowDelete = true;
                            }
                            break;
                        case 3://0x03 gzip
                            if(ZipFileUtil.isEndsWithGZ(file)){
                                filePathList = ZipFileUtil.decompressGZ(file, dir);
                                isAllowDelete = true;
                            }
                            break;
                        case 4://0x04 bz2
                            break;
                        default:
                            logger.warn("未知的压缩格式:"+faceInfoRequest.getCompressFormat());
                    }

                    List<CompareInfo> compareInfoList = new ArrayList<CompareInfo>();

                    for (String filePath : filePathList) {
                        logger.info("处理yuv文件: "+filePath);
                        File _file = new File(filePath);
                        String name = _file.getName().substring(0,_file.getName().lastIndexOf("."));
                        if(name.indexOf("_h_")>-1){
                            _file.delete();
                            continue;
                        }
                        String[] strs = name.split("_");
                        if(strs.length>2){
                            int width = Integer.parseInt(strs[strs.length-2]);
                            int height = Integer.parseInt(strs[strs.length-1]);
                            try{
                                File outFile = new File(_file.getParent() + "/" + name +".jpg");
                                if(name.indexOf("_a_")>-1){
                                    String nameH = outFile.getName().replaceAll("_a_","_h_");
                                    File outFileH = new File(_file.getParent() + "/" + nameH);
                                    YUVToImage.I420ToImageYOnly(filePath, width, height, outFileH, "jpg");
                                    faceInfoRequest.setFilePathH(outFileH.getPath());
                                    YUVToImage.I420ToImage(filePath, width, height, outFile, "jpg");
                                    faceInfoRequest.setFilePathA(outFile.getPath());
                                } else {
                                    YUVToImage.I420ToImage(filePath,width,height,outFile,"jpg");
                                    filePathResultList.add(outFile.getPath());
                                    int compStatus = Integer.parseInt(faceInfoRequest.getCompStatus().substring(2),16);
                                    switch (compStatus) {
                                        case 0://0x00表示通过比对
                                            break;
                                        case -1://0xFF 表示未比对
                                        case 255://0xFF 表示未比对
                                            //TODO 连接比对服务
                                            CompareInfo compareInfo = new CompareInfo();
                                            compareInfo.setFilePath(outFile.getPath());
                                            compareInfo.setTaskId(faceInfoRequest.getTaskId());
                                            compareInfo.setTerminalId(faceInfoRequest.getDeviceId());
                                            compareInfoList.add(compareInfo);
                                            break;
                                        default:
                                            logger.warn("未知的比对状态:" + faceInfoRequest.getCompStatus());
                                    }
                                }
                                _file.delete();
                                logger.info("删除文件["+ _file.getPath()+"]");
                            } catch (Exception e) {
                                logger.error("YUV转JPG错误", e);
                            } finally {
                            }
                        } else {
                            logger.warn("图片长宽未知:" + filePath);
                        }
                    }
                    if(isAllowDelete){
                        new File(file).delete();
                        logger.info("删除文件["+ file+"]");
                    }
                    String filePath = "";
                    for (String fp : filePathResultList) {
                        filePath += fp + ";";
                    }
                   faceInfoRequest.setFilePathHead(filePath);
                    if (faceInfoRequestService != null) {
                        faceInfoRequestService.insert(faceInfoRequest);
                    }
                    for(CompareInfo compareInfo : compareInfoList){
                        compareService.offer(compareInfo);
                    }
                } catch (Exception e) {
                    logger.error("取队列错误",e);
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

    public void close(){
        this.run = false;
    }

    public boolean isRunning() {
        return run;
    }

}

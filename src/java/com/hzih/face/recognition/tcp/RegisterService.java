package com.hzih.face.recognition.tcp;

import JACE.ASX.MessageBlock;
import JACE.ASX.MessageQueue;
import com.hzih.face.recognition.domain.RegisterRequest;
import com.hzih.face.recognition.service.FaceInfoRequestService;
import com.hzih.face.recognition.utils.StringContext;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-29
 * Time: 下午4:59
 */
public class RegisterService extends Thread {
    private static final Logger logger = Logger.getLogger(RegisterService.class);
    private MessageQueue queue;
    private boolean run = false;
    private FaceInfoRequestService faceInfoRequestService;
    public static String backupDir;

    public void init(FaceInfoRequestService faceInfoRequestService) {
        this.faceInfoRequestService = faceInfoRequestService;
        if (backupDir == null) {
            backupDir = StringContext.systemPath + "/data";
        }
    }

    public boolean haveMessages() {
        if (queue == null) {
            queue = new MessageQueue();
            return false;
        }
        return (!queue.isEmpty());
    }

    public void offer(RegisterRequest register) {
        try {
            queue.enqueueTail(new MessageBlock(register));
        } catch (InterruptedException e) {
        }
    }

    public void run() {
        this.run = true;
        while (run) {
            if (haveMessages()) {
                try {
                    MessageBlock messageBlock = queue.dequeueHead();
                    RegisterRequest register = (RegisterRequest) messageBlock.obj();
                    if (faceInfoRequestService != null) {
                        faceInfoRequestService.updateRegister(register);
                    }
                } catch (Exception e) {
                    logger.error("终端注册错误", e);
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

    public void close() {
        this.run = false;
    }

    public boolean isRunning() {
        return run;
    }

}

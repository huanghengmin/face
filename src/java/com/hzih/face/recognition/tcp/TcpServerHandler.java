package com.hzih.face.recognition.tcp;

import com.hzih.face.recognition.domain.*;
import com.hzih.face.recognition.entity.SipType;
import com.hzih.face.recognition.entity.SipXml;
import com.hzih.face.recognition.utils.mina.MessageInfo;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * Created with IntelliJ IDEA.
 * User: sunny
 * Date: 14-7-25
 * Time: 上午10:26
 */
public class TcpServerHandler extends IoHandlerAdapter {
    final static Logger logger = Logger.getLogger(TcpServer.class);
    private ReceiveService receiveService;
    private CompareService compareService;
    private RegisterService registerService;

    public TcpServerHandler(ReceiveService receiveService, CompareService compareService, RegisterService registerService) {
        this.receiveService = receiveService;
        this.compareService = compareService;
        this.registerService = registerService;
    }

    public void messageReceived(IoSession session, Object message)
            throws Exception {
        if (message instanceof MessageInfo) {
            MessageInfo messageInfo = (MessageInfo) message;
            String charset = messageInfo.getCharset();
            String bodyStr = new String(messageInfo.getBody(), charset);
            logger.info(session.getRemoteAddress() + " 发送了 " + bodyStr);
            String responseBody;
            if (bodyStr != null) {
                if (bodyStr.startsWith("<?xml version=")) {
                    if (SipType.FaceInfo.equalsIgnoreCase(messageInfo.getCmdType())) {
                        ServiceUtils serviceUtils = ServiceUtils.getService();
                        FaceInfoRequest faceInfo = new FaceInfoRequest().xmlToBean(messageInfo.getBody());
                        faceInfo.setFileBuff(messageInfo.getFaceInfoRequest().getFileBuff());
                        receiveService.offer(faceInfo);
                        FaceInfoResponse faceInfoResponse = new FaceInfoResponse();
                        faceInfoResponse.setDeviceType(faceInfo.getDeviceType());
                        faceInfoResponse.setDeviceId(serviceUtils.deviceId);
                        faceInfoResponse.setResult(SipXml.ResultSuccess);
                        responseBody = faceInfoResponse.toString();
                    } else if (SipType.AlarmInfo.equalsIgnoreCase(messageInfo.getCmdType())) {
                        ServiceUtils serviceUtils = ServiceUtils.getService();
                        AlarmInfoRequest alarm = new AlarmInfoRequest().xmlToBean(messageInfo.getBody());
                        AlarmInfoResponse alarmInfoResponse = new AlarmInfoResponse();
                        alarmInfoResponse.setDeviceType(alarm.getDeviceType());
                        alarmInfoResponse.setDeviceId(serviceUtils.deviceId);
                        alarmInfoResponse.setResult(SipXml.ResultSuccess);
                        responseBody = alarmInfoResponse.toString();
                    } else if (SipType.FaceInfoComp.equalsIgnoreCase(messageInfo.getCmdType())) {
                        ServiceUtils serviceUtils = ServiceUtils.getService();
                        FaceInfoCompRequest faceInfoCompRequest = new FaceInfoCompRequest().xmlToBean(messageInfo.getBody());
                        FaceInfoCompResponse faceInfoCompResponse = new FaceInfoCompResponse();
                        faceInfoCompResponse.setDeviceId(serviceUtils.deviceId);
                        faceInfoCompResponse.setDeviceType(faceInfoCompRequest.getDeviceType());
                        faceInfoCompResponse.setResult(SipXml.ResultSuccess);
                        responseBody = faceInfoCompResponse.toString();
                        compareService.offer(faceInfoCompRequest);
                    } else if (SipType.Register.equalsIgnoreCase(messageInfo.getCmdType())) {
                        ServiceUtils serviceUtils = ServiceUtils.getService();
                        RegisterRequest register = new RegisterRequest().xmlToBean(messageInfo.getBody());
                        RegisterResponse response = new RegisterResponse();
                        response.setDeviceId(serviceUtils.deviceId);
                        response.setCmdType(register.getCmdType());
                        response.setDeviceType(register.getDeviceType());
                        response.setResult(SipXml.ResultSuccess);
                        responseBody = response.toString();
                    } else {
                        responseBody = "<?xml version=\"1.0\">\r\n\r\n<Response>\r\n<default>" + System.currentTimeMillis() + "</default>\r\n</Response>";
                    }
                } else {
                    String body = AESDecoder(bodyStr);
                    String cmdType = body.substring(body.indexOf("<CmdType>") + 9, body.indexOf("</CmdType>")).trim();
                    if (SipType.Register.equalsIgnoreCase(cmdType)) {
                        ServiceUtils serviceUtils = ServiceUtils.getService();
                        RegisterRequest register = new RegisterRequest().xmlToBean(body.getBytes(charset));
                        registerService.offer(register);
                        RegisterResponse response = new RegisterResponse();
                        response.setDeviceId(serviceUtils.deviceId);
                        response.setCmdType(register.getCmdType());
                        response.setDeviceType(register.getDeviceType());
                        response.setResult(SipXml.ResultSuccess);
                        responseBody = AESEncoder(response.toString());
                    } else {
                        responseBody = "<?xml version=\"1.0\">\r\n\r\n<Response>\r\n<default>" + System.currentTimeMillis() + "</default>\r\n</Response>";
                        responseBody = AESEncoder(responseBody);
                    }
                }
            } else {
                responseBody = "<?xml version=\"1.0\">\r\n\r\n<Response>\r\n<default>" + System.currentTimeMillis() + "</default>\r\n<msg>request body is null</msg>\r\n</Response>";
            }
            byte[] body = responseBody.getBytes(charset);
            messageInfo = new MessageInfo();
            messageInfo.setVersion(MessageInfo.Version);
            messageInfo.setBodyLen(body.length);
            messageInfo.setReserved(new byte[21]);
            messageInfo.setBody(body);
            session.write(messageInfo);
        } else {
            logger.info("string:" + message.toString());
        }
    }

    private String AESDecoder(String bodyStr) {
        byte[] decryptResult = Base64.decodeBase64(bodyStr.getBytes());
        return new String(decryptResult);
    }

    private String AESEncoder(String bodyStr) {
        byte[] encryptResult = Base64.encodeBase64(bodyStr.getBytes());
        return new String(encryptResult);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        logger.info("Disconnecting the idle.");
    }

    public void exceptionCaught(IoSession session, Throwable cause) {
        logger.warn(cause.getMessage(), cause);
        session.close(true);
    }
}

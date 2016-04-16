package com.hzih.face.recognition.tcp;

import com.hzih.face.recognition.utils.mina.MessageInfo;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 * Created by Administrator on 15-7-24.
 */
public class TcpShortClientHandler extends IoHandlerAdapter {

    private final Logger logger = Logger.getLogger(getClass());

    private static TcpShortClientHandler handler = null;
    private NioSocketConnector connector;
    public static TcpShortClientHandler getInstance(NioSocketConnector connector){
        if(handler == null) {
           handler = new TcpShortClientHandler(connector);
        }
        return handler;
    }

    private TcpShortClientHandler(NioSocketConnector connector) {
        this.connector = connector;
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        if(message instanceof MessageInfo) {
            MessageInfo messageInfo = (MessageInfo) message;
            session.setAttribute("result", new String(messageInfo.getBody()));
        }
        session.close(true);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
    }
}

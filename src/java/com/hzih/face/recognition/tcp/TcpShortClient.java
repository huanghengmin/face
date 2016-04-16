package com.hzih.face.recognition.tcp;

import com.hzih.face.recognition.utils.mina.MessageCodecFactory;
import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 * Created with IntelliJ IDEA.
 * User: sunny
 * Date: 14-7-25
 * Time: 上午11:08
 */
public class TcpShortClient {
    private final Logger logger = Logger.getLogger(getClass());

    private static TcpShortClient client = null;
    private NioSocketConnector connector;
    private DefaultIoFilterChainBuilder chain;

    public static TcpShortClient getInstance(){
        if(client == null) {
            client = new TcpShortClient();
        }
        return client;
    }

    private TcpShortClient() {
        connector = new NioSocketConnector();
        chain = connector.getFilterChain();
        chain.addLast("codec", new ProtocolCodecFilter(new MessageCodecFactory()));
        connector.setConnectTimeoutMillis(3 * 1000L);
        connector.getSessionConfig().setMaxReadBufferSize(1024 * 1024 * 2);
        connector.getSessionConfig().setSendBufferSize(1024 * 1024 * 2);
        connector.getSessionConfig().setReceiveBufferSize(Integer.MAX_VALUE);
        connector.setHandler(TcpShortClientHandler.getInstance(connector));

    }

    public NioSocketConnector getConnector() {
        return connector;
    }

}

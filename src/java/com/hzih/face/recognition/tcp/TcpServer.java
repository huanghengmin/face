package com.hzih.face.recognition.tcp;

import com.hzih.face.recognition.service.FaceInfoRequestService;
import com.hzih.face.recognition.utils.mina.MessageCodecFactory;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created with IntelliJ IDEA.
 * User: sunny
 * Date: 14-7-25
 * Time: 上午10:25
 */
public class TcpServer implements Runnable {
    final static Logger logger = Logger.getLogger(TcpServer.class);

    private NioSocketAcceptor acceptor;
    private TcpServerHandler serverHandler;
    private boolean isRunning = false;
    //本机监听
    private InetSocketAddress localAddressServer;

    public void init(InetSocketAddress localAddressServer,FaceInfoRequestService faceInfoRequestService) {
        CompareService compareService = new CompareService();
        compareService.init(faceInfoRequestService);
        compareService.start();
        ReceiveService receiveService = new ReceiveService();
        receiveService.init(compareService,faceInfoRequestService);
        receiveService.start();
        RegisterService registerService = new RegisterService();
        registerService.init(faceInfoRequestService);
        registerService.start();

        this.localAddressServer = localAddressServer;
        acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors());
        acceptor.setReuseAddress(true);
        acceptor.getSessionConfig().setKeepAlive(false);
        acceptor.getSessionConfig().setReadBufferSize(1024*1024);
        acceptor.getSessionConfig().setReceiveBufferSize(6553600);
        acceptor.getSessionConfig().setMaxReadBufferSize(6553600) ;
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 3);
        serverHandler = new TcpServerHandler(receiveService,compareService,registerService);
        acceptor.setHandler(serverHandler);
        acceptor.getFilterChain().addLast("codec",new ProtocolCodecFilter(new MessageCodecFactory()));
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
    }


    @Override
    public void run() {
        isRunning = true;
        startServer();
        while (isRunning) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
        }
    }

    public void startServer() {
        try {
            acceptor.bind(localAddressServer);
            logger.info("启动["+localAddressServer.toString()+"]监听,用于接收信令信息");
        } catch (IOException e) {
            logger.error("启动["+localAddressServer.toString()+"]监听错误",e);
        }
    }
}

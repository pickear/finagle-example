package com.biostime.finagle;

import com.biostime.finagle.interfaces.HelloInterface;
import com.biostime.finagle.interfaces.impl.HelloInterfaceImpl;
import com.biostime.finagle.zk.ZKClient;
import com.twitter.finagle.ListeningServer;
import com.twitter.finagle.Thrift;
import com.twitter.finagle.builder.ServerBuilder;
import com.twitter.util.Await;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dylan
 * @date 2016/7/18.
 */
public class FinagleThriftServer {

    private final static Logger log = LoggerFactory.getLogger(FinagleThriftServer.class);
    private final static String local = "127.0.0.1";
    private final static int port = 8082;

    public static void main(String[] args){

        HelloInterface.ServiceIface helloInterface = new HelloInterfaceImpl(local + ":" + port);
        ListeningServer server = Thrift.serveIface(local + ":" + port,helloInterface);
        log.info("服务启动[{}]",local + ":" + port);
        server.announce("zk!" + ZKClient.zkHosts + "!" + ZKClient.zkPath+"!0");
        log.info(ZKClient.zkHosts+ZKClient.zkPath);
        try {
            Await.ready(server);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

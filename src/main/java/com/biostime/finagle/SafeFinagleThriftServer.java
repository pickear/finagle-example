package com.biostime.finagle;

import com.biostime.finagle.interfaces.HelloInterface;
import com.biostime.finagle.interfaces.impl.HelloInterfaceImpl;
import com.biostime.finagle.zipkin.ZipKinTracerHolder;
import com.biostime.finagle.zk.ZKClient;
import com.twitter.common.zookeeper.Group;
import com.twitter.common.zookeeper.ServerSetImpl;
import com.twitter.finagle.builder.ServerBuilder;
import com.twitter.finagle.thrift.ThriftServerFramedCodec;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashMap;

/**
 * @author Dylan
 * @date 2016/7/19.
 */
public class SafeFinagleThriftServer extends BaseFinagleThrift{

    private final static Logger log = LoggerFactory.getLogger(SafeFinagleThriftServer.class);
    private final static String local = "127.0.0.1";
    private final static int port = 8081;

    public static void main(String[] args) throws Group.JoinException, InterruptedException {


        log.info("启动服务......");
        InetSocketAddress endpoint = new InetSocketAddress(port);

        HelloInterface.ServiceIface helloInterface = new HelloInterfaceImpl(local + ":" + port);
        final ServerSetImpl servers = new ServerSetImpl(zkClient, ZKClient.zkPath + "/" + HelloInterface.class.getSimpleName());
        ServerBuilder.safeBuild(new HelloInterface.Service(helloInterface,new TBinaryProtocol.Factory())
                                ,ServerBuilder.get()
                                              .name("finagle_server")
                                              .codec(ThriftServerFramedCodec.get())
                                              .bindTo(endpoint)
                                              .tracer(ZipKinTracerHolder.holdeTracer())
                                );

        servers.join(endpoint,new HashMap<String, InetSocketAddress>());

    }

}

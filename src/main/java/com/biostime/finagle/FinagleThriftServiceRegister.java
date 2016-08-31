package com.biostime.finagle;

import com.biostime.finagle.helper.GodHands;
import com.biostime.finagle.helper.NetHelper;
import com.biostime.finagle.zipkin.filter.TracingFilter;
import com.biostime.finagle.zk.ZKClient;
import com.twitter.common.zookeeper.Group;
import com.twitter.common.zookeeper.ServerSetImpl;
import com.twitter.finagle.Service;
import com.twitter.finagle.builder.ServerBuilder;
import com.twitter.finagle.thrift.ThriftServerFramedCodec;
import com.twitter.finagle.tracing.Tracer;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zipkin.finagle.kafka.KafkaZipkinTracer;

import java.net.InetSocketAddress;
import java.util.HashMap;

/**
 * @author Dylan
 * @date 2016/8/31.
 */
public final class FinagleThriftServiceRegister extends BaseFinagleThrift{

    private final static Logger logger = LoggerFactory.getLogger(FinagleThriftServiceRegister.class);

    static {
        System.setProperty("zipkin.kafka.bootstrapServers", "192.168.115.102:9092,192.168.115.106:9099");
        System.setProperty("zipkin.kafka.topic", "zipkin2");
    }

    private final static int startPort = 9000;

    private final static Tracer trace = new KafkaZipkinTracer();


    public static  <T> void register(String serviceName,T serviceInstance) throws Group.JoinException, InterruptedException {

        logger.info("register the service [{}]",serviceName);

        Class<?>[] clazzs = GodHands.getInterfaces(serviceInstance.getClass());

        for(Class<?> clazz : clazzs){
            String className = clazz.getName();

            if(StringUtils.endsWith(className,"$ServiceIface")){
                int port = NetHelper.getAvailablePort(null,startPort);
                InetSocketAddress endpoint = new InetSocketAddress(port);
                String serviceInstanceName = serviceInstance.getClass().getSimpleName();
                logger.info("register the instance [{}] to serivce",serviceInstanceName);
                final ServerSetImpl servers = new ServerSetImpl(zkClient, ZKClient.zkPath + "/" + serviceInstanceName);


                TracingFilter filter = new TracingFilter();
                String baseInterfaceName = StringUtils.substringBefore(className,"$");
                Service service = GodHands.newInstance(baseInterfaceName + "$Service", new Class[]{clazz, TProtocolFactory.class}, new Object[]{serviceInstance, new TBinaryProtocol.Factory()});
                service = filter.andThen(service);

                ServerBuilder.safeBuild(service, ServerBuilder.get()
                                                              .name(serviceName)
                                                              .codec(ThriftServerFramedCodec.get())
                                                              .bindTo(endpoint)
                                                              .tracer(trace)
                                        );

                servers.join(endpoint,new HashMap<String, InetSocketAddress>());
                logger.info("success register the service [{}],bind to port [{}]",serviceName,port);
                break;
            }
        }
    }


    private FinagleThriftServiceRegister(){}
}

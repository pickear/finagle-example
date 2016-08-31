package com.biostime.finagle;

import com.biostime.finagle.helper.GodHands;
import com.biostime.finagle.zk.ZKClient;
import com.twitter.common.zookeeper.ServerSetImpl;
import com.twitter.finagle.Service;
import com.twitter.finagle.builder.ClientBuilder;
import com.twitter.finagle.builder.Cluster;
import com.twitter.finagle.thrift.ThriftClientFramedCodec;
import com.twitter.finagle.thrift.ThriftClientRequest;
import com.twitter.finagle.tracing.Tracer;
import com.twitter.finagle.zookeeper.ZookeeperServerSetCluster;
import com.twitter.util.Duration;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zipkin.finagle.kafka.KafkaZipkinTracer;

import java.net.SocketAddress;

/**
 * @author Dylan
 * @date 2016/8/31.
 */
public final class FinagleThriftClientHolder extends BaseFinagleThrift{


    private final static Logger logger = LoggerFactory.getLogger(FinagleThriftClientHolder.class);

    static {
        System.setProperty("zipkin.initialSampleRate", "1.0");
        System.setProperty("zipkin.kafka.bootstrapServers", "192.168.115.102:9092,192.168.115.106:9099");
        System.setProperty("zipkin.kafka.topic", "zipkin2");
    }

    private final static Tracer trace = new KafkaZipkinTracer();

    public static <T> T getService(Class<T> clazz){

        String clazzName = clazz.getSimpleName();

        Class<?>[] clazzs = GodHands.getInterfaces(clazz);
        for(Class<?> _clazz : clazzs){
            String className = _clazz.getName();

            if(StringUtils.endsWith(className, "$ServiceIface")){
                String baseInterfaceName = StringUtils.substringBefore(className,"$");
                final ServerSetImpl servers = new ServerSetImpl(zkClient, ZKClient.zkPath + "/" + clazzName);

                Cluster<SocketAddress> cluster = new ZookeeperServerSetCluster(servers);
                Service<ThriftClientRequest,byte[]> service = ClientBuilder.safeBuild(ClientBuilder.get()
                                                                                                    .codec(ThriftClientFramedCodec.get())
                                                                                                    .hostConnectionLimit(100)
                                                                                                    .timeout(Duration.fromMilliseconds(5000))
                                                                                                    .cluster(cluster)
                                                                                                    .name("finagle_client")
                                                                                                    .tracer(trace)
                                                                                    );

                return GodHands.newInstance(baseInterfaceName + "$ServiceToClient", new Class[]{Service.class, TProtocolFactory.class}, new Object[]{service, new TBinaryProtocol.Factory()});
            }
        }

        throw new RuntimeException("can not find the service ["+clazzName+"]");
    }

    private FinagleThriftClientHolder(){}
}

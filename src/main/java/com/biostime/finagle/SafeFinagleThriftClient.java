package com.biostime.finagle;

import com.biostime.finagle.interfaces.HelloInterface;
import com.biostime.finagle.interfaces.impl.HelloInterfaceImpl;
import com.biostime.finagle.zk.ZKClient;
import com.twitter.common.zookeeper.ServerSetImpl;
import com.twitter.finagle.Service;
import com.twitter.finagle.builder.ClientBuilder;
import com.twitter.finagle.builder.Cluster;
import com.twitter.finagle.thrift.ThriftClientFramedCodec;
import com.twitter.finagle.thrift.ThriftClientRequest;
import com.twitter.finagle.tracing.Trace;
import com.twitter.finagle.zookeeper.ZookeeperServerSetCluster;
import com.twitter.util.Await;
import com.twitter.util.Duration;
import com.twitter.util.Function;
import com.twitter.util.Future;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.runtime.BoxedUnit;
import zipkin.finagle.kafka.KafkaZipkinTracer;

import java.net.SocketAddress;

/**
 * @author Dylan
 * @date 2016/7/19.
 */
public class SafeFinagleThriftClient{

    private final static Logger log = LoggerFactory.getLogger(SafeFinagleThriftClient.class);

    public static void main(String[] args) {

       /* System.setProperty("zipkin.initialSampleRate", "1.0");
        System.setProperty("zipkin.kafka.bootstrapServers", "192.168.115.102:9092,192.168.115.106:9099");
        System.setProperty("zipkin.kafka.topic", "zipkin2");

        Service<ThriftClientRequest,byte[]> service = null;*/
        try {
      /*      final ServerSetImpl servers = new ServerSetImpl(zkClient, ZKClient.zkPath + "/" + HelloInterface.class.getSimpleName());
            Cluster<SocketAddress> cluster = new ZookeeperServerSetCluster(servers);
            service = ClientBuilder.safeBuild(ClientBuilder.get()
                            .codec(ThriftClientFramedCodec.get())
                            .hostConnectionLimit(100)
                            .timeout(Duration.fromMilliseconds(5000))
                            .cluster(cluster)
                            .name("finagle_client")
                            .tracer(new KafkaZipkinTracer())
            );

            HelloInterface.ServiceIface client = new HelloInterface.ServiceToClient(service,new TBinaryProtocol.Factory());*/

            HelloInterface.ServiceIface client = FinagleThriftClientHolder.getService(HelloInterfaceImpl.class);
            Future<String> response = client.sayHello().onSuccess(new Function<String, BoxedUnit>() {

                @Override
                public BoxedUnit apply(String response) {
                    log.info("Received response: " + response);
                    log.info("[client]traceId : {},spanId : {}", Trace.id().traceId().toString(), Trace.id().spanId().toString());
                    return BoxedUnit.UNIT;
                }
            });

            Await.result(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

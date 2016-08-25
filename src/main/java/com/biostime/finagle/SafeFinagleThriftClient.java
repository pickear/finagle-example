package com.biostime.finagle;

import com.biostime.finagle.interfaces.HelloInterface;
import com.biostime.finagle.zipkin.ZipKinTracerHolder;
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

import java.net.SocketAddress;

/**
 * @author Dylan
 * @date 2016/7/19.
 */
public class SafeFinagleThriftClient extends BaseFinagleThrift{

    private final static Logger log = LoggerFactory.getLogger(SafeFinagleThriftClient.class);

    public static void main(String[] args) {

        Service<ThriftClientRequest,byte[]> service = null;
        try {
            final ServerSetImpl servers = new ServerSetImpl(zkClient, ZKClient.zkPath + "/" + HelloInterface.class.getSimpleName());
            Cluster<SocketAddress> cluster = new ZookeeperServerSetCluster(servers);
            service = ClientBuilder.safeBuild(ClientBuilder.get()
                                                           .codec(ThriftClientFramedCodec.get())
                                                           .hostConnectionLimit(100)
                                                           .timeout(Duration.fromMilliseconds(5000))
                                                           .cluster(cluster)
                                                           .tracer(ZipKinTracerHolder.holdeTracer())
                                              );

            HelloInterface.ServiceIface client = new HelloInterface.ServiceToClient(service,new TBinaryProtocol.Factory());

            Future<String> response = client.sayHello().onSuccess(new Function<String, BoxedUnit>() {

                @Override
                public BoxedUnit apply(String response) {
                    log.info("Received response: " + response);
                    return BoxedUnit.UNIT;
                }
            });
            Trace.record("starting some extremely expensive computation");

            Await.result(response);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            service.release();
        }

    }
}

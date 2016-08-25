package com.biostime.finagle;

import com.biostime.finagle.interfaces.HelloInterface;
import com.biostime.finagle.zk.ZKClient;
import com.twitter.finagle.ServiceFactory;
import com.twitter.finagle.Thrift;
import com.twitter.finagle.thrift.ThriftClientRequest;
import com.twitter.util.Await;
import com.twitter.util.Function;
import com.twitter.util.Future;
import org.apache.thrift.protocol.TBinaryProtocol;
import scala.runtime.BoxedUnit;

/**
 * @author Dylan
 * @date 2016/7/18.
 */
public class FinagleThriftClient {

    public static void main(String[] args) {

     //   HelloInterface.ServiceIface client = Thrift.newIface("localhost:8080",HelloInterface.ServiceIface.class);
        while (true){
            ServiceFactory<ThriftClientRequest, byte[]> factory = null;
            try {
                factory = Thrift.newClient("zk!"+ZKClient.zkHosts+"!"+ZKClient.zkPath);
                HelloInterface.ServiceIface client = new HelloInterface.ServiceToClient(factory.toService(),new TBinaryProtocol.Factory());
                Future<String> response = client.sayHello().onSuccess(new Function<String, BoxedUnit>() {

                    @Override
                    public BoxedUnit apply(String response) {
                        System.out.println("Received response: " + response);
                        return BoxedUnit.UNIT;
                    }
                });
                Await.result(response);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                factory.close();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

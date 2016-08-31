package com.biostime.finagle;

import com.biostime.finagle.interfaces.impl.HelloInterfaceImpl;
import com.biostime.finagle.interfaces.impl.HelloInterfaceImplThree;
import com.biostime.finagle.interfaces.impl.HelloInterfaceImplTwo;
import com.twitter.common.zookeeper.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dylan
 * @date 2016/7/19.
 */
public class SafeFinagleThriftServerTwo extends BaseFinagleThrift{

    private final static Logger log = LoggerFactory.getLogger(SafeFinagleThriftServerTwo.class);
    private final static String local = "127.0.0.1";
    private final static int port = 9000;

    public static void main(String[] args) throws Group.JoinException, InterruptedException {


        log.info("启动服务......");

        FinagleThriftServiceRegister.register("finagle_service_two",new HelloInterfaceImplTwo(local + ":" + port));

    }

}

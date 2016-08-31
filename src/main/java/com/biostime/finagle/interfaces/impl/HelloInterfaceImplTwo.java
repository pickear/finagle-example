package com.biostime.finagle.interfaces.impl;

import com.biostime.finagle.FinagleThriftClientHolder;
import com.biostime.finagle.interfaces.HelloInterface;
import com.twitter.util.Future;

/**
 * @author Dylan
 * @date 2016/7/18.
 */
public class HelloInterfaceImplTwo implements HelloInterface.ServiceIface{

    private String server;
    public HelloInterfaceImplTwo(String _server){
        this.server = _server;
    }

    @Override
    public Future<String> sayHello() {
        System.out.println(server + "two say hello");
        HelloInterface.ServiceIface helloInterface = FinagleThriftClientHolder.getService(HelloInterfaceImplThree.class);
        return helloInterface.sayHello();
    }

}

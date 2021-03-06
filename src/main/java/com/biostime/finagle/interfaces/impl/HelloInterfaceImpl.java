package com.biostime.finagle.interfaces.impl;

import com.biostime.finagle.FinagleThriftClientHolder;
import com.biostime.finagle.interfaces.HelloInterface;
import com.twitter.util.Future;

/**
 * @author Dylan
 * @date 2016/7/18.
 */
public class HelloInterfaceImpl implements HelloInterface.ServiceIface{

    private String server;
    public HelloInterfaceImpl(String _server){
        this.server = _server;
    }

    @Override
    public Future<String> sayHello() {
        System.out.println(server + "one say hello");
        HelloInterface.ServiceIface helloInterface = FinagleThriftClientHolder.getService(HelloInterfaceImplTwo.class);
        return helloInterface.sayHello();
    }

}

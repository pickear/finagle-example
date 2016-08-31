package com.biostime.finagle.interfaces.impl;

import com.biostime.finagle.interfaces.HelloInterface;
import com.twitter.util.Future;

/**
 * @author Dylan
 * @date 2016/7/18.
 */
public class HelloInterfaceImplThree implements HelloInterface.ServiceIface{

    private String server;
    public HelloInterfaceImplThree(String _server){
        this.server = _server;
    }

    @Override
    public Future<String> sayHello() {
        System.out.println(server + "three say hello");
        return Future.value("hello");
    }

}

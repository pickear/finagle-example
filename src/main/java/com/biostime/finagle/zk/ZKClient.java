package com.biostime.finagle.zk;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Dylan
 * @date 2016/7/19.
 */
public class ZKClient {

    public final static String zkHosts = "192.168.115.4:2181,192.168.115.47:2181,192.168.115.100:2181";
    public final static String zkPath = "/finagle/zk/test";

    public static Set<String> getHosts(){

        Set<String> hosts = new HashSet<String>();
        for(String host : zkHosts.split(",")){
            hosts.add(host);
        }
        return hosts;
    }
}

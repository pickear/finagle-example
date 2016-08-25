package com.biostime.finagle;

import com.biostime.finagle.zk.ZKClient;
import com.twitter.common.quantity.Amount;
import com.twitter.common.quantity.Time;
import com.twitter.common.zookeeper.ZooKeeperClient;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Dylan
 * @date 2016/7/19.
 */
public class BaseFinagleThrift {


    protected final static ZooKeeperClient zkClient = new ZooKeeperClient(Amount.of(10000, Time.MILLISECONDS), getZKClient());

    protected static Set<InetSocketAddress> getZKClient(){
        Set<InetSocketAddress> clients = new HashSet<InetSocketAddress>();
        for(String client : ZKClient.getHosts()){
            String [] addAndPort = client.split(":");
            clients.add(new InetSocketAddress(addAndPort[0], Integer.parseInt(addAndPort[1])));
        }
        return clients;
    }
}

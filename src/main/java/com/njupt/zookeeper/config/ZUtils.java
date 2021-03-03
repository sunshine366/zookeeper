package com.njupt.zookeeper.config;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZUtils {

    private static ZooKeeper zk;

    private static String address = "192.168.66.134:2181,192.168.66.135:2181,192.168.66.133:2181,192.168.66.136:2181/testConf";

    private static DefaultWatch watch = new DefaultWatch();

    private static CountDownLatch init = new CountDownLatch(1);
    public static ZooKeeper getZK(){
        try {
            zk = new ZooKeeper(address, 1000, watch);
            watch.setCd(init);
            init.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zk;
    }
}

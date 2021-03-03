package com.njupt.zookeeper.config;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestConfig {

    ZooKeeper zk;
    MyConf myConf;

    @Before
    public void conn(){
        zk = ZUtils.getZK();
    }

    @After
    public void close(){
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getConf(){
        WatchCallBack watchCallBack = new WatchCallBack();
        watchCallBack.setZk(zk);
        myConf = new MyConf();
        watchCallBack.setMyConf(myConf);


        watchCallBack.aWait();
        //1.节点不存在

        while (true){
            if (myConf.getConf().equals("")){
                System.out.println("myconf 丢了");
                watchCallBack.aWait();
            }
            System.out.println(myConf.getConf());
        }
    }
}

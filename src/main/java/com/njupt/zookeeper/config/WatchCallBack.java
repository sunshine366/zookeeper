package com.njupt.zookeeper.config;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class WatchCallBack implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback {
    ZooKeeper zk;
    MyConf myConf;
    CountDownLatch cd = new CountDownLatch(1);

    public void setMyConf(MyConf myConf) {
        this.myConf = myConf;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case None:
                break;
            case NodeCreated:
                zk.getData("/AppConf",this,this,"ABC");
                break;
            case NodeDeleted:
                //容忍性
                myConf.setConf("");
                cd = new CountDownLatch(1);
                break;
            case NodeDataChanged:
                zk.getData("/AppConf",this,this,"ABC");
                break;
            case NodeChildrenChanged:
                break;
            case DataWatchRemoved:
                break;
            case ChildWatchRemoved:
                break;
            case PersistentWatchRemoved:
                break;
        }
    }

    @Override
    public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
        if (bytes != null) {
            String s1 = new String(bytes);
            myConf.setConf(s1);
            cd.countDown();
        }
    }

    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
        if (stat != null) {
//            System.out.println("节点存在");
            zk.getData("/AppConf", this, this, "ABC");
        }
    }

    public void aWait(){
        zk.exists("/",this,this,"ABC");
        try {
            cd.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

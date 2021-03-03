package org.example;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");

        final CountDownLatch cdl = new CountDownLatch(1);
        //zk有session的概念，没有连接池的概念
        //watch：观察。回调
        //watch的注册只发生在读类型调用，比如get exites ...,写方法是产生事件
        //Watch有两类,第一类：new zk 时候，传入的Watch，session级别的，跟path、node没有关系
        final ZooKeeper zk = new ZooKeeper("192.168.66.134:2181,192.168.66.135:2181,192.168.66.133:2181,192.168.66.136:2181", 3000, new Watcher() {
            //Watch回调方法
            @Override
            public void process(WatchedEvent watchedEvent) {
                Event.KeeperState state = watchedEvent.getState();
                Event.EventType type = watchedEvent.getType();
                String path = watchedEvent.getPath();
                System.out.println("new zk watch:"+watchedEvent.toString());
                switch (state) {
                    case Unknown:
                        break;
                    case Disconnected:
                        break;
                    case NoSyncConnected:
                        break;
                    case SyncConnected:
                        System.out.println("连接");
                        cdl.countDown();
                        break;
                    case AuthFailed:
                        break;
                    case ConnectedReadOnly:
                        break;
                    case SaslAuthenticated:
                        break;
                    case Expired:
                        break;
                    case Closed:
                        break;
                }

                switch (type) {
                    case None:
                        break;
                    case NodeCreated:
                        break;
                    case NodeDeleted:
                        break;
                    case NodeDataChanged:
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
        });

        cdl.await();
        ZooKeeper.States state = zk.getState();
        switch (state) {
            case CONNECTING:
                System.out.println("ing...............");
                break;
            case ASSOCIATING:
                break;
            case CONNECTED:
                System.out.println("ed..................");
                break;
            case CONNECTEDREADONLY:
                break;
            case CLOSED:
                break;
            case AUTH_FAILED:
                break;
            case NOT_CONNECTED:
                break;
        }

        String pathName = zk.create("/xo", "hello".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        final Stat stat = new Stat();
        byte[] node = zk.getData("/xo", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("getData watch:"+watchedEvent.toString());
                try {
//                    zk.getData("/xo",true,stat);  //重新注册，true 是默认的Watch，这个是new zk时候的Watch
                    zk.getData("/xo",this,stat);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, stat);
        System.out.println(new String(node));

        //触发回调
        Stat stat1 = zk.setData("/xo", "newdata".getBytes(), 0);
        //如果没有重新注册Watch，将不会触发回调
        Stat stat2 = zk.setData("/xo", "newdata".getBytes(), stat1.getVersion());

        Thread.sleep(22222222);
    }
}

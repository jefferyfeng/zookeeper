package com.baizhi.api_test;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.junit.After;
import org.junit.Test;

import java.util.List;


public class TestZooKeeper {

    ZkClient client = new ZkClient("192.168.199.128:2181");
    //Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testCreateNode(){
        //client.create("/test", "haha", CreateMode.PERSISTENT);
        //client.createPersistent("/test");
        //client.createPersistent("/test2");
        client.createPersistent("/test", true);
    }

    @Test
    public void testReadNode(){
        //Object o = client.readData("/test1");
        List<String> children = client.getChildren("/test");
        for (String child : children) {
            System.out.println(client.readData("/test/"+child));
        }
        //client.readData("/test/"+child);
        //logger.info("====================================================================");
        //logger.info(o.toString());
        //logger.info("====================================================================");
        //System.out.println(o);
    }

    @Test
    public void testModifyNode(){
        client.writeData("/test", "你好");
    }

    @Test
    public void testDeleteNode(){
        client.delete("/test");
    }

    @Test
    public void testIsNodeExists(){
        boolean exists = client.exists("/test");
        System.out.println(exists);
    }

    @Test
    public void testChildNode(){
        if(!client.exists("/test/child2")){
            client.createPersistent("/test/child2", true);
        }else{
            System.out.println(client.exists("/test/child2"));
        }
    }

    @Test
    public void testWatchNodeData()throws Exception{
        client.subscribeDataChanges("/test", new IZkDataListener(){
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println(s+":"+o);
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println("delete"+s);
            }
        });
        System.in.read();
    }

    @Test
    public void testWatchNodeChange()throws Exception{
        client.subscribeChildChanges("/test", new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                System.out.println(list);
            }
        });
        System.in.read();
    }

    @After
    public void testAfter(){
        client.close();
    }
}

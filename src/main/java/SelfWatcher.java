import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.io.IOException;

/**
 * Created by qls on 9/10/15.
 */
public  class SelfWatcher implements Watcher{

    ZooKeeper zk=null;

    private Watcher getWatcher(final String msg){
        return new Watcher(){
            @Override
            public void process(WatchedEvent event) {
                System.out.println(msg+"\t"+event.toString());
            }
        };
    }

    SelfWatcher(String address){
        try{
            zk=new ZooKeeper(address,3000,null);     //在创建ZooKeeper时第三个参数负责设置该类的默认构造函数
            zk.create("/root", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        }catch(IOException e){
            e.printStackTrace();
            zk=null;
        }catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void setWatcher(){
        try {
            Stat s=zk.exists("/root", getWatcher("EXISTS"));
            if(s!=null){
                zk.getData("/root", getWatcher("GETDATA"), s);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void trigeWatcher(){
        try {
            Stat s=zk.exists("/root", false);        //此处不设置watcher
            zk.setData("/root", "a".getBytes(), s.getVersion());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    void disconnect(){
        if(zk!=null)
            try {
                zk.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    public static void main(String[] args){
        SelfWatcher inst=new SelfWatcher("127.0.0.1:2181");
        inst.setWatcher();
        inst.trigeWatcher();
        inst.disconnect();
    }

    @Override
    public void process(WatchedEvent event) {

    }
}

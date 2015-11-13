import org.apache.zookeeper.*;

/**
 * Created by qls on 9/10/15.
 */
public class TestDemo {


    private static String CLIENT_PORT ="172.16.2.25:2181";
    private static int CONNECTION_TIMEOUT=36000;

    public static void main(String[] args) throws  Exception{
        test();
//        Watcher & Version
//        watcher分为两大类：data watches和child watches。getData()和exists()上可以设置data watches，getChildren()上可以设置child watches。
//        setData()会触发data watches;
//        create()会触发data watches和child watches;
//        delete()会触发data watches和child watches.
    }

    private static void test() throws  Exception{
        // 创建一个与服务器的连接
        ZooKeeper zk = new ZooKeeper(CLIENT_PORT, CONNECTION_TIMEOUT, new Watcher() {
            // 监控所有被触发的事件
            public void process(WatchedEvent event) {
                System.out.println("已经触发了" + event.getType() + "事件！");
            }
        });
        // 创建一个目录节点
        zk.create("/testRootPath", "testRootData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        // 创建一个子目录节点
        zk.create("/testRootPath/testChildPathOne", "testChildDataOne".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        System.out.println(new String(zk.getData("/testRootPath",false,null)));
        // 取出子目录节点列表
        System.out.println(zk.getChildren("/testRootPath",true));
        // 修改子目录节点数据
        zk.setData("/testRootPath/testChildPathOne","modifyChildDataOne".getBytes(),-1);
        System.out.println("目录节点状态：["+zk.exists("/testRootPath",true)+"]");
        // 创建另外一个子目录节点
        zk.create("/testRootPath/testChildPathTwo", "testChildDataTwo".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        System.out.println(new String(zk.getData("/testRootPath/testChildPathTwo",true,null)));
        // 删除子目录节点
        zk.delete("/testRootPath/testChildPathTwo",-1);
        zk.delete("/testRootPath/testChildPathOne",-1);
        // 删除父目录节点
        zk.delete("/testRootPath",-1);
        // 关闭连接
        zk.close();
    }

}

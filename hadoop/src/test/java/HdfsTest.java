import com.bird.HadoopApp;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Author 李璞
 * @Date 2022/1/5 14:29
 * @Description
 */
@SpringBootTest(classes = HadoopApp.class)
public class HdfsTest {

    /**
     * @Author 李璞
     * @Date 2022/1/5 14:29
     * @Description 快速入门 判断文件目录是否存在
     */
    @Test
    void test1() throws Exception {
        //配置客户端参数
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://192.168.78.134:8020");
        configuration.set("HADOOP_USER_NAME", "root");
        //创建文件系统客户端对象
        FileSystem fileSystem = FileSystem.get(configuration);
        //操作HDFS
        boolean exists = fileSystem.exists(new Path("/bird"));

        if (exists) {
            System.out.println("存在");
        } else {
            System.out.println("不存在");
        }
        //关闭连接
        fileSystem.close();
    }

    /**
     * @Author 李璞
     * @Date 2022/1/5 15:46
     * @Description 验证线程池
     */
    @Test
    void pool() throws InterruptedException {
        LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>(2);

        Thread taskPut = new Thread(() -> {
            for (int i = 0; i < 500; i++) {
                //向阻塞队列加入数据
                try {
                    queue.put(i);
                    System.out.println("添加了" + i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Thread taskTake = new Thread(() -> {
            try {
                for (int i = 0; i < 500; i++) {
                    TimeUnit.SECONDS.sleep(1);
                    Integer take = queue.take();
                    System.out.println("移出了" + take);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //开启线程
        taskTake.start();
        taskPut.start();
        TimeUnit.HOURS.sleep(1);
    }

    /**
     * @Author 李璞
     * @Date 2022/1/5 15:58
     * @Description 异常测试
     */
    @Test
    void exceptionTest() {

    }
}

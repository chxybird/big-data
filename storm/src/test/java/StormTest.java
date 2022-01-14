import com.bird.StormApp;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;

/**
 * @Author 李璞
 * @Date 2022/1/14 15:09
 * @Description Storm中Bolt将导入的数据进行处理(转换)
 */
@SpringBootTest(classes = StormApp.class)
public class StormTest {
    /**
     * @Author 李璞
     * @Date 2022/1/14 15:10
     * @Description 文件读取测试
     */
    @Test
    void fileRead() throws Exception {
        InputStream inputStream = new FileInputStream(new File("F:\\log", "log.txt"));
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            System.out.println(str);
        }
    }
}

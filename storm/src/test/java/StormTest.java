import com.bird.StormApp;
import com.bird.entity.dto.CarDTO;
import com.bird.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * @Author 李璞
 * @Date 2022/1/14 15:09
 * @Description Storm中Bolt将导入的数据进行处理(转换)
 */
@SpringBootTest(classes = StormApp.class)
@Slf4j
public class StormTest {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * @Author 李璞
     * @Date 2022/1/14 15:10
     * @Description 文件读取测试
     */
    @Test
    void fileRead() throws Exception {
        InputStream inputStream = new FileInputStream(new File("F:\\log", "log.txt"));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            System.out.println(str);
        }
    }

    /**
     * @Author 李璞
     * @Date 2022/1/18 13:04
     * @Description 发送数据到kafka 模拟数据的源源不断的采集
     */
    @Test
    void sendMsg() {
        ArrayList<String> brandList = new ArrayList<>(10);
        brandList.add("玛莎拉蒂");
        brandList.add("奥迪");
        brandList.add("宝马");
        brandList.add("奔驰");
        brandList.add("保时捷");
        brandList.add("法拉利");
        brandList.add("宾利");
        brandList.add("别克");
        brandList.add("马自达");
        brandList.add("本田");
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(11) % brandList.size();
            CarDTO carDTO = new CarDTO();
            carDTO.setId(UUID.randomUUID().toString().replace("-", " "));
            carDTO.setSpeed(String.valueOf(random.nextInt(220)));
            carDTO.setBrand(brandList.get(index));
            String jsonData = JsonUtils.entityToJson(carDTO);
            //采用异步调用的方式
            kafkaTemplate.send("topic-storm", "car-data", jsonData).addCallback((success) -> {
                //发送成功
                log.info("发送成功!!!");
            }, (failure) -> {
                //获取失败信息
                String message = failure.getMessage();
                //TODO 进行相关补偿操作 例如失败消息入库、进入死信队列等等
                log.error("消息发送失败,失败的消息为:{}", jsonData);
                log.error("失败的原因为:{}", message);
            });
        }
    }
}

import com.bird.FlumeApp;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author 李璞
 * @Date 2022/1/5 14:47
 * @Description
 */
@SpringBootTest(classes = FlumeApp.class)
public class FlumeTest {
    @Test
    void test1() {
        System.out.println("hello flume");
    }
}

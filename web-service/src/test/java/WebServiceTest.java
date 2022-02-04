import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.junit.jupiter.api.Test;

/**
 * @Author 小鸟程序员
 * @Date 2022/2/4 14:43
 * @Description
 */
public class WebServiceTest {

    /**
     * @Author 小鸟程序员
     * @Date 2022/2/4 14:43
     * @Description 客户端调用WebService服务
     */
    @Test
    void getInfo() throws Exception {
        JaxWsDynamicClientFactory factory=JaxWsDynamicClientFactory.newInstance();
        //只需要指定wsdl地址就可以
        Client client = factory.createClient("http://localhost:8080/services/get-info?wsdl");
        //方法名 参数一 参数二 。。。。。
        Object[] result = client.invoke("getInfo", "1");
        System.out.println(result[0]);
    }
}

import com.wyfx.common.http.HttpClientUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author johnson liu
 * @date 2019/11/16
 * @description HttpClient测试
 */
public class HttpTest {

    public static void main(String[] args) {
        String url = "http://192.168.0.104:8081/aw/operationLogging/getAwFunctionServerByserverId";
        Map<String, String> param = new HashMap<String, String>();
        param.put("id", "4");
        String result = HttpClientUtil.doGet(url, param);
        System.out.println(result);
    }

}

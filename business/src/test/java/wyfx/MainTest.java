package wyfx;

import cn.hutool.crypto.SecureUtil;
import org.junit.Test;

/**
 * @Author muyongliang
 * @Date 2020/12/18
 * @Description MainTest
 */
public class MainTest {
    @Test
    public void MD5test() {
        String s = "wyfx001";
        String pass_word = "6c4092dd8dc4b27ac2a26dc02ff6dddf";
        String s1 = SecureUtil.md5().digestHex16(s);
        boolean equals = s1.equals(pass_word);
        System.out.println(equals);
    }
}

package auth;

import cn.dev33.satoken.stp.StpUtil;
import cn.zx.cang.ai.auth.CangAIAuthApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CangAIAuthApplication.class})
public class SaTokenTest {

    @Test
    public void test() {
        System.out.println(StpUtil.isLogin());
        Assert.assertFalse(StpUtil.isLogin());

        StpUtil.login(123321);

        System.out.println(StpUtil.isLogin());
        Assert.assertTrue(StpUtil.isLogin());
    }
}

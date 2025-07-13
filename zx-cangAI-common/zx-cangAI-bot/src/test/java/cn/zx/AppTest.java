package cn.zx;

import cn.zx.cang.ai.bot.BotService;
import cn.zx.cang.ai.bot.CozeServiceImpl;
import cn.zx.cang.ai.bot.config.BotConfiguration;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(classes = BotConfiguration.class)
public class AppTest 
    extends TestCase
{
    @Autowired
    private BotService cozeService;

    @Test
    public void test() {
        System.out.println(cozeService.getOAuthToken().getAccessToken());
    }
}

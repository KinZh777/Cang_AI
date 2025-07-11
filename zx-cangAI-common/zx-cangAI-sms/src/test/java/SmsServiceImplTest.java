import cn.zx.cang.ai.sms.MockSmsServiceImpl;
import cn.zx.cang.ai.sms.SmsService;
import cn.zx.cang.ai.sms.config.SmsConfiguration;
import cn.zx.cang.ai.sms.response.SmsSendResponse;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SmsConfiguration.class})
@ActiveProfiles("test")
public class SmsServiceImplTest {

    @Test
    @Ignore
    public void testSenMsg() {
        // 填写字符串。
        String phoneNumber = "12325815658";
        String code = "3456";
        SmsService smsService=new MockSmsServiceImpl();
        SmsSendResponse res=smsService.sendMsg(phoneNumber, code);
        Assert.assertTrue(res.getSuccess());
    }
}

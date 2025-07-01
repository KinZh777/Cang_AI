package cn.zx.cang.ai.notice.facade;

import cn.zx.cang.ai.api.notice.response.NoticeResponse;
import cn.zx.cang.ai.api.notice.service.NoticeFacadeService;
import cn.zx.cang.ai.base.exception.SystemException;
import cn.zx.cang.ai.limiter.SlidingWindowRateLimiter;
import cn.zx.cang.ai.notice.domain.constant.NoticeState;
import cn.zx.cang.ai.notice.domain.entity.Notice;
import cn.zx.cang.ai.notice.domain.service.NoticeService;
import cn.zx.cang.ai.rpc.facade.Facade;
import cn.zx.cang.ai.sms.SmsService;
import cn.zx.cang.ai.sms.response.SmsSendResponse;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static cn.zx.cang.ai.api.notice.constant.NoticeConstant.CAPTCHA_KEY_PREFIX;
import static cn.zx.cang.ai.base.exception.BizErrorCode.SEND_NOTICE_DUPLICATED;

/**
 * @author kinchou
 */
@DubboService(version = "1.0.0")
public class NoticeFacadeServiceImpl implements NoticeFacadeService {

    @Autowired
    private SlidingWindowRateLimiter slidingWindowRateLimiter;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private SmsService smsService;

    /**
     * 生成并发送短信验证码
     *
     * @param telephone
     * @return
     */
    @Facade
    @Override
    public NoticeResponse generateAndSendSmsCaptcha(String telephone) {
        // 限流-滑动窗口 限制60秒内通过一次
        if(!slidingWindowRateLimiter.tryAcquire(telephone,60, 1)){
            // 如果已经发送过了 抛出提示
            throw new SystemException(SEND_NOTICE_DUPLICATED);
        }
        // 生成验证码
        String captcha = RandomUtil.randomNumbers(4);
        // 验证码存入Redis
        redisTemplate.opsForValue().set(CAPTCHA_KEY_PREFIX+telephone, captcha, 1, TimeUnit.MINUTES);
        // 保存验证码进数据库
        Notice notice = noticeService.saveCaptcha(telephone, captcha);

        // 发送短信
        Thread.ofVirtual().start(() -> {
            SmsSendResponse result = smsService.sendMsg(notice.getTargetAddress(), notice.getNoticeContent());
            if (result.getSuccess()) {
                notice.setState(NoticeState.SUCCESS);
                notice.setSendSuccessTime(new Date());
                noticeService.updateById(notice);
            } else {
                notice.setState(NoticeState.FAILED);
                notice.addExtendInfo("executeResult", JSON.toJSONString(result));
                noticeService.updateById(notice);
            }
        });

        return new NoticeResponse.Builder().setSuccess(true).build();
    }
}

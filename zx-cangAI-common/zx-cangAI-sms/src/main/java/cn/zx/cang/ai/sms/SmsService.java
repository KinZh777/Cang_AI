package cn.zx.cang.ai.sms;

import cn.zx.cang.ai.sms.response.SmsSendResponse;

/**
 * 短信服务
 *
 * @author kinchou
 */
public interface SmsService {
    /**
     * 发送短信
     *
     * @param phoneNumber
     * @param code
     * @return
     */
    public SmsSendResponse sendMsg(String phoneNumber, String code);
}

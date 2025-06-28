package cn.zx.cang.ai.api.notice.service;


import cn.zx.cang.ai.api.notice.response.NoticeResponse;

/**
 * @author kinchou
 */
public interface NoticeFacadeService {
    /**
     * 生成并发送短信验证码
     *
     * @param telephone
     * @return
     */
    public NoticeResponse generateAndSendSmsCaptcha(String telephone);
}

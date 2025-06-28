package cn.zx.cang.ai.user.domain.service;

/**
 * 认证服务
 *
 * @author kinchou
 */
public interface AuthService {
    /**
     * 校验认证信息
     *
     * @param realName
     * @param idCard
     * @return
     */
    public boolean checkAuth(String realName, String idCard);
}

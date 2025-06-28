package cn.zx.cang.ai.user.domain.service;

/**
 * Mock的认证服务
 *
 * @author kinchou
 */
public class MockAuthServiceImpl implements AuthService {
    @Override
    public boolean checkAuth(String realName, String idCard) {
        return true;
    }
}

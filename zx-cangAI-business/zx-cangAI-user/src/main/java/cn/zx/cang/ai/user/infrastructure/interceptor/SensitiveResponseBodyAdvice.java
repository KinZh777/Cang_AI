package cn.zx.cang.ai.user.infrastructure.interceptor;

import cn.zx.cang.ai.api.user.response.data.UserInfo;
import cn.zx.cang.ai.user.domain.entity.User;
import cn.zx.cang.ai.user.domain.entity.convertor.UserConvertor;
import cn.zx.cang.ai.web.vo.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import com.github.houbb.sensitive.core.api.SensitiveUtil;

/**
 * 脱敏响应体处理
 *
 * @author kinchou
 */
@ControllerAdvice
public class SensitiveResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 只对特定类型的返回值执行处理逻辑，这里可以根据需要调整判断条件
        return Result.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 如果返回的对象是UserInfo，进行脱敏处理
        if (body instanceof Result && ((Result<?>) body).getData() instanceof UserInfo) {
            Result<UserInfo> result = (Result<UserInfo>) body;
            UserInfo userInfo = result.getData();
            User user = UserConvertor.INSTANCE.mapToEntity(userInfo);
            user = SensitiveUtil.desCopy(user);
            result.setData(UserConvertor.INSTANCE.mapToVo(user));
            return result;
        }
        return body;
    }
}
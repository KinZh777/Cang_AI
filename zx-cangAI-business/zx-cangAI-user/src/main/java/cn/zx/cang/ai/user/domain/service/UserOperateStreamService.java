package cn.zx.cang.ai.user.domain.service;

import cn.zx.cang.ai.api.user.constant.UserOperateTypeEnum;
import cn.zx.cang.ai.user.domain.entity.User;
import cn.zx.cang.ai.user.domain.entity.UserOperateStream;
import cn.zx.cang.ai.user.infrastructure.mapper.UserOperateStreamMapper;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 用户操作流水表 服务类
 * </p>
 *
 * @author kinchou
 * @since 2025-01-13
 */
@Service
public class UserOperateStreamService extends ServiceImpl<UserOperateStreamMapper, UserOperateStream> {

    public Long insertStream(User user, UserOperateTypeEnum type) {
        UserOperateStream stream = new UserOperateStream();
        stream.setUserId(String.valueOf(user.getId()));
        stream.setOperateTime(new Date());
        stream.setType(type.name());
        stream.setParam(JSON.toJSONString(user));
        boolean result = save(stream);
        if (result) {
            return stream.getId();
        }
        return null;
    }
}

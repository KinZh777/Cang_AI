package cn.zx.cang.ai.api.user.request.condition;

import lombok.*;

/**
 * @author kinchou
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserIdQueryCondition implements UserQueryCondition {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;
}

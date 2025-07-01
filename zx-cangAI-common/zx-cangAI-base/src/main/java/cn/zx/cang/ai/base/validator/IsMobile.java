package cn.zx.cang.ai.base.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = {})
public @interface IsMobile {
    String message() default "手机号格式不正确"; // 默认错误信息

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

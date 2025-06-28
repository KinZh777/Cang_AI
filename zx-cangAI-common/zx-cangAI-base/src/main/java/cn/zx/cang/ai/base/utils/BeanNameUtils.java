package cn.zx.cang.ai.base.utils;

import com.google.common.base.CaseFormat;

/**
 * @author kinchou
 */
public class BeanNameUtils {

    /**
     * 把一个策略名称转换成beanName
     * <pre>
     *     如 WEN_CHANG ，ChainService -> wenChangChainService
     * </pre>
     *
     * @param strategyName
     * @param serviceName
     * @return
     */
    public static String getBeanName(String strategyName, String serviceName) {
        //将服务转换成小写字母开头的驼峰形式，如A_BCD 转成 aBcd
        return CaseFormat.UPPER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL).convert(strategyName) + serviceName;
    }
}

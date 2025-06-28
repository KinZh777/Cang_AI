package cn.zx.cang.ai.web.util;

import cn.zx.cang.ai.base.response.PageResponse;
import cn.zx.cang.ai.web.vo.MultiResult;

import static cn.zx.cang.ai.base.response.ResponseCode.SUCCESS;

/**
 * @author kinchou
 */
public class MultiResultConvertor {

    public static <T> MultiResult<T> convert(PageResponse<T> pageResponse) {
        MultiResult<T> multiResult = new MultiResult<T>(true, SUCCESS.name(), SUCCESS.name(), pageResponse.getDatas(), pageResponse.getTotal(), pageResponse.getCurrentPage(), pageResponse.getPageSize());
        return multiResult;
    }
}

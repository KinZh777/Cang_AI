package cn.zx.cang.ai.notice.infrastructure.mapper;

import cn.zx.cang.ai.notice.domain.entity.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 链操作 Mapper 接口
 * </p>
 *
 * @author kinchou
 * @since 2025-01-19
 */
@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {

}

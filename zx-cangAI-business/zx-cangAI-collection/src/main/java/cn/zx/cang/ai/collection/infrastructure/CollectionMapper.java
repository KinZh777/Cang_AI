package cn.zx.cang.ai.collection.infrastructure;

import cn.zx.cang.ai.collection.domain.entity.Collection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CollectionMapper extends BaseMapper<Collection> {
}

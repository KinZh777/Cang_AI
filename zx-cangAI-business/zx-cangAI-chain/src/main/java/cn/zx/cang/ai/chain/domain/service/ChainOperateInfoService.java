package cn.zx.cang.ai.chain.domain.service;

import cn.zx.cang.ai.chain.domain.constant.ChainOperateStateEnum;
import cn.zx.cang.ai.chain.domain.entity.ChainOperateInfo;
import cn.zx.cang.ai.chain.infrastructure.mapper.ChainOperateInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class ChainOperateInfoService extends ServiceImpl<ChainOperateInfoMapper, ChainOperateInfo> {

    /**
     * 根据 链业务号、业务号、业务类型 查询链操作信息
     * @param bizId
     * @param bizType
     * @param outBizId
     * @return
     */
    public ChainOperateInfo queryByOutBizId(String bizId, String bizType, String outBizId){
        QueryWrapper<ChainOperateInfo> queryWrapper = new QueryWrapper<ChainOperateInfo>();
        queryWrapper.eq("out_biz_id", outBizId);
        queryWrapper.eq("biz_type", bizType);
        queryWrapper.eq("biz_id", bizId);
        List<ChainOperateInfo> infoList = list(queryWrapper);
        if(CollectionUtils.isEmpty(infoList)) return null;
        return infoList.get(0);
    }

    /**
     * 更新操作状态、操作结果
     * @param id
     * @param state
     * @param result
     * @return
     */
    public boolean updateResult(Long id, ChainOperateStateEnum state, String result) {
        ChainOperateInfo operateInfoDO = getById(id);
        operateInfoDO.setResult(result);
        operateInfoDO.setState(state);
        return updateById(operateInfoDO);
    }

    public Long insertInfo(String chainType, String bizId, String bizType, String operateType, String param,String operationId) {
        ChainOperateInfo operateInfo = new ChainOperateInfo();
        operateInfo.setOperateTime(new Date());
        operateInfo.setChainType(chainType);
        operateInfo.setBizId(bizId);
        operateInfo.setBizType(bizType);
        operateInfo.setOperateType(operateType);
        operateInfo.setParam(param);
        operateInfo.setOutBizId(operationId);
        operateInfo.setState(ChainOperateStateEnum.PROCESSING);

        boolean ret = save(operateInfo);
        if (ret) {
            return operateInfo.getId();
        }
        return null;
    }
}

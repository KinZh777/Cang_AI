package cn.zx.cang.ai.order.domain.service;

import cn.zx.cang.ai.order.domain.entity.TradeOrder;
import cn.zx.cang.ai.order.infrastructure.mapper.OrderMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;

/**
 * @author kinchou
 */
@Service
public class OrderReadService extends ServiceImpl<OrderMapper, TradeOrder> {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 按照订单号查询订单信息
     *
     * @param orderId
     * @return
     */
    public TradeOrder getOrder(String orderId) {
        return orderMapper.selectByOrderId(orderId);
    }

    public TradeOrder getOrder(String orderId, String buyerId) {
        return orderMapper.selectByOrderIdAndBuyer(orderId, buyerId);
    }

    /**
     * 分页查询已经超时的订单
     *
     * @param currentPage
     * @param pageSize
     * @param buyerIdTailNumber 买家 ID 的尾号
     * @return
     */
    public Page<TradeOrder> pageQueryTimeoutOrders(int currentPage, int pageSize, @Nullable String buyerIdTailNumber) {
        Page<TradeOrder> page = new Page<>(currentPage, pageSize);

        // 根据状态、时间、用户id分页查询

        return this.page(page, null);
    }

    /**
     * 分页查询待Confirm订单
     *
     * @param currentPage
     * @param pageSize
     * @param buyerIdTailNumber
     * @return
     */
    public Page<TradeOrder> pageQueryNeedConfirmOrders(int currentPage, int pageSize, @Nullable String buyerIdTailNumber) {
        Page<TradeOrder> page = new Page<>(currentPage, pageSize);

        // 根据状态、时间、用户id分页查询

        return this.page(page, null);
    }

}

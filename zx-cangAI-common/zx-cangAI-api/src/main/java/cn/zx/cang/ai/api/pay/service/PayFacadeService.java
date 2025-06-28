package cn.zx.cang.ai.api.pay.service;

import cn.zx.cang.ai.api.pay.model.PayOrderVO;
import cn.zx.cang.ai.api.pay.request.PayCreateRequest;
import cn.zx.cang.ai.api.pay.request.PayQueryRequest;
import cn.zx.cang.ai.api.pay.response.PayCreateResponse;
import cn.zx.cang.ai.base.response.MultiResponse;
import cn.zx.cang.ai.base.response.SingleResponse;

/**
 * @author kinchou
 */
public interface PayFacadeService {

    /**
     * 生成支付链接
     *
     * @param payCreateRequest
     * @return
     */
    public PayCreateResponse generatePayUrl(PayCreateRequest payCreateRequest);

    /**
     * 查询支付订单
     *
     * @param payQueryRequest
     * @return
     */
    public MultiResponse<PayOrderVO> queryPayOrders(PayQueryRequest payQueryRequest);

    /**
     * 查询支付订单
     *
     * @param payOrderId
     * @return
     */
    public SingleResponse<PayOrderVO> queryPayOrder(String payOrderId);

    /**
     * 查询支付订单
     *
     * @param payOrderId
     * @param payerId
     * @return
     */
    public SingleResponse<PayOrderVO> queryPayOrder(String payOrderId, String payerId);


}

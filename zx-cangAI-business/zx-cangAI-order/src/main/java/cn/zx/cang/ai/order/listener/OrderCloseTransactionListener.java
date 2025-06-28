package cn.zx.cang.ai.order.listener;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author kinchou
 */
@Component
public class OrderCloseTransactionListener implements TransactionListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderCloseTransactionListener.class);

    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        try {

            //本地事务操作

            return LocalTransactionState.COMMIT_MESSAGE;
        } catch (Exception e) {
            logger.error("executeLocalTransaction error, message = {}", message, e);
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {

        //本地事务结果查询
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}

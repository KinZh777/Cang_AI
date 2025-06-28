# 2024-08-31 trade_order 表新增reverse_buyer_id

ALTER TABLE `trade_order_0000`
	ADD COLUMN `reverse_buyer_id` varchar(32) NULL COMMENT '逆序的买家ID' AFTER `buyer_id`,
	ADD KEY `idx_rvbuyer_state`(`reverse_buyer_id`,`order_state`,`gmt_create`) USING BTREE
;

ALTER TABLE `trade_order_0001`
	ADD COLUMN `reverse_buyer_id` varchar(32) NULL COMMENT '逆序的买家ID' AFTER `buyer_id`,
	ADD KEY `idx_rvbuyer_state`(`reverse_buyer_id`,`order_state`,`gmt_create`) USING BTREE
;

ALTER TABLE `trade_order_0002`
	ADD COLUMN `reverse_buyer_id` varchar(32) NULL COMMENT '逆序的买家ID' AFTER `buyer_id`,
	ADD KEY `idx_rvbuyer_state`(`reverse_buyer_id`,`order_state`,`gmt_create`) USING BTREE
;

ALTER TABLE `trade_order_0003`
	ADD COLUMN `reverse_buyer_id` varchar(32) NULL COMMENT '逆序的买家ID' AFTER `buyer_id`,
	ADD KEY `idx_rvbuyer_state`(`reverse_buyer_id`,`order_state`,`gmt_create`) USING BTREE
;

update trade_order_0000 set `reverse_buyer_id`  = REVERSE(`buyer_id` );
update trade_order_0001 set `reverse_buyer_id`  = REVERSE(`buyer_id` );
update trade_order_0003 set `reverse_buyer_id`  = REVERSE(`buyer_id` );
update trade_order_0002 set `reverse_buyer_id`  = REVERSE(`buyer_id` );


# 2024-08-25 新增refund_order表

/******************************************/
/*   DatabaseName = nfturbo   */
/*   TableName = refund_order   */
/******************************************/
CREATE TABLE `refund_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `refund_order_id` varchar(32) NOT NULL COMMENT '支付单号',
  `identifier` varchar(128) NOT NULL COMMENT '幂等号',
  `pay_order_id` varchar(32) NOT NULL COMMENT '支付单号',
  `pay_channel_stream_id` varchar(64) DEFAULT NULL COMMENT '支付的渠道流水号',
  `paid_amount` decimal(18,6) DEFAULT NULL COMMENT '已支付金额',
  `payer_id` varchar(32) NOT NULL COMMENT '付款方iD',
  `payer_type` varchar(32) NOT NULL COMMENT '付款方类型',
  `payee_id` varchar(32) NOT NULL COMMENT '收款方id',
  `payee_type` varchar(32) NOT NULL COMMENT '收款方类型',
  `apply_refund_amount` decimal(18,6) NOT NULL COMMENT '申请退款金额',
  `refunded_amount` decimal(18,6) DEFAULT NULL COMMENT '退款成功金额',
  `refund_channel_stream_id` varchar(64) DEFAULT NULL COMMENT '退款的渠道流水号',
  `refund_channel` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '退款方式',
  `memo` varchar(512) DEFAULT NULL COMMENT '备注',
  `refund_order_state` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '退款单状态',
  `refund_succeed_time` datetime DEFAULT NULL COMMENT '退款成功时间',
  `deleted` tinyint DEFAULT NULL COMMENT '逻辑删除标识',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  PRIMARY KEY (`id`),
  KEY `idx_pay_order` (`pay_order_id`) USING BTREE,
  KEY `uk_identifier` (`identifier`,`pay_order_id`,`refund_channel`),
  KEY `idx_refund_order` (`refund_order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

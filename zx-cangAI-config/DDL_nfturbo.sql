/******************************************/
/*   DatabaseName = nfturbo   */
/*   TableName = chain_operate_info   */
/******************************************/
CREATE TABLE `chain_operate_info` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增主键）',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后更新时间',
  `chain_type` varchar(64) DEFAULT NULL COMMENT '链类型',
  `biz_id` varchar(128) DEFAULT NULL COMMENT '业务id',
  `biz_type` varchar(64) DEFAULT NULL COMMENT '业务类型',
  `operate_type` varchar(64) DEFAULT NULL COMMENT '操作类型',
  `state` varchar(64) DEFAULT NULL COMMENT '状态',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `succeed_time` datetime DEFAULT NULL COMMENT '成功时间',
  `param` text COMMENT '入参',
  `result` text COMMENT '返回结果',
  `out_biz_id` varchar(128) DEFAULT NULL COMMENT '外部业务id',
  `deleted` int DEFAULT NULL COMMENT '是否逻辑删除，0为未删除，非0为已删除',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8mb3 COMMENT='链操作'
;

/******************************************/
/*   DatabaseName = nfturbo   */
/*   TableName = collection   */
/******************************************/
CREATE TABLE `collection` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增主键）',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '最后更新时间',
  `name` varchar(512) DEFAULT NULL COMMENT '藏品名称',
  `cover` varchar(512) DEFAULT NULL COMMENT '藏品封面',
  `class_id` varchar(128) DEFAULT NULL COMMENT '藏品类目ID',
  `price` decimal(18,6) DEFAULT NULL COMMENT '价格',
  `quantity` bigint DEFAULT NULL COMMENT '藏品数量',
  `detail` text COMMENT '详情',
  `saleable_inventory` bigint DEFAULT NULL COMMENT '可销售库存',
  `identifier` varchar(128) DEFAULT NULL COMMENT '幂等号',
  `occupied_inventory` bigint DEFAULT NULL COMMENT '已占用库存',
  `state` varchar(128) DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '藏品创建时间',
  `sale_time` datetime DEFAULT NULL COMMENT '藏品发售时间',
  `sync_chain_time` datetime DEFAULT NULL COMMENT '藏品上链时间',
  `deleted` int DEFAULT NULL COMMENT '是否逻辑删除，0为未删除，非0为已删除',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  `creator_id` varchar(128) DEFAULT NULL COMMENT '创建者',
  `version` int DEFAULT NULL COMMENT '修改版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10023 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='藏品表'
;

/******************************************/
/*   DatabaseName = nfturbo   */
/*   TableName = collection_stream   */
/******************************************/
CREATE TABLE `collection_stream` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增主键）',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '最后更新时间',
  `name` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '藏品名称',
  `cover` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '藏品封面',
  `class_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '藏品类目ID',
  `collection_id` bigint DEFAULT NULL COMMENT '藏品id',
  `price` decimal(18,6) DEFAULT NULL COMMENT '价格',
  `quantity` bigint DEFAULT NULL COMMENT '藏品数量',
  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '详情',
  `state` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '状态',
  `saleable_inventory` bigint DEFAULT NULL COMMENT '可售库存',
  `occupied_inventory` bigint DEFAULT NULL COMMENT '已占库存',
  `create_time` datetime DEFAULT NULL COMMENT '藏品创建时间',
  `stream_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '流水类型',
  `sale_time` datetime DEFAULT NULL COMMENT '藏品发售时间',
  `sync_chain_time` datetime DEFAULT NULL COMMENT '藏品上链时间',
  `identifier` varchar(128) DEFAULT NULL COMMENT '幂等号',
  `deleted` int DEFAULT NULL COMMENT '是否逻辑删除，0为未删除，非0为已删除',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=209 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci AVG_ROW_LENGTH=16384 ROW_FORMAT=DYNAMIC COMMENT='藏品表流水'
;

/******************************************/
/*   DatabaseName = nfturbo   */
/*   TableName = held_collection   */
/******************************************/
CREATE TABLE `held_collection` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增主键）',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '最后更新时间',
  `collection_id` bigint unsigned DEFAULT NULL COMMENT '藏品id',
  `name` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '藏品名称',
  `cover` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '藏品封面地址',
  `purchase_price` decimal(18,6) DEFAULT NULL COMMENT '购入价格',
  `serial_no` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '藏品编号',
  `nft_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'NFT唯一编号',
  `pre_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '上一个持有人id',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '持有人id',
  `state` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '状态',
  `tx_hash` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '交易hash',
  `hold_time` datetime DEFAULT NULL COMMENT '藏品持有时间',
  `sync_chain_time` datetime DEFAULT NULL COMMENT '藏品同步时间',
  `delete_time` datetime DEFAULT NULL COMMENT '藏品销毁时间',
  `deleted` int DEFAULT NULL COMMENT '是否逻辑删除，0为未删除，非0为已删除',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  `biz_no` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务单据号',
  `biz_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT ' 业务类型',
  PRIMARY KEY (`id`),
  KEY `idx_user_state` (`user_id`,`state`,`gmt_create`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='藏品持有表'
;

/******************************************/
/*   DatabaseName = nfturbo   */
/*   TableName = notice   */
/******************************************/
CREATE TABLE `notice` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID（自增主键）',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后更新时间',
  `notice_title` varchar(512) CHARACTER SET utf8 DEFAULT NULL COMMENT '通知标题',
  `notice_content` text CHARACTER SET utf8 COMMENT '通知内容',
  `notice_type` varchar(128) CHARACTER SET utf8 DEFAULT NULL COMMENT '通知类型',
  `send_success_time` datetime DEFAULT NULL COMMENT '发送成功时间',
  `target_address` varchar(256) CHARACTER SET utf8 DEFAULT NULL COMMENT '接收地址',
  `state` varchar(128) CHARACTER SET utf8 DEFAULT NULL COMMENT '状态',
  `deleted` int DEFAULT NULL COMMENT '是否逻辑删除，0为未删除，非0为已删除',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  `retry_times` int DEFAULT NULL COMMENT '重试次数',
  `extend_info` varchar(1024) DEFAULT NULL COMMENT '扩展信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=125 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通知表'
;

/******************************************/
/*   DatabaseName = nfturbo   */
/*   TableName = pay_order   */
/******************************************/
CREATE TABLE `pay_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `pay_order_id` varchar(32) NOT NULL COMMENT '支付单号',
  `payer_id` varchar(32) NOT NULL COMMENT '付款方iD',
  `payer_type` varchar(32) NOT NULL COMMENT '付款方类型',
  `payee_id` varchar(32) NOT NULL COMMENT '收款方id',
  `payee_type` varchar(32) NOT NULL COMMENT '收款方类型',
  `biz_no` varchar(128) NOT NULL COMMENT '业务单号',
  `biz_type` varchar(32) NOT NULL COMMENT '业务单类型',
  `order_amount` decimal(18,6) NOT NULL COMMENT '订单金额',
  `paid_amount` decimal(18,6) DEFAULT NULL COMMENT '已支付金额',
  `channel_stream_id` varchar(64) DEFAULT NULL COMMENT '渠道流水号',
  `pay_url` varchar(512) DEFAULT NULL COMMENT '支付地址',
  `pay_channel` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付方式',
  `memo` varchar(512) DEFAULT NULL COMMENT '备注',
  `order_state` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '单据类型',
  `pay_succeed_time` datetime DEFAULT NULL COMMENT '支付成功时间',
  `pay_expire_time` datetime DEFAULT NULL COMMENT '支付超时时间',
  `deleted` tinyint DEFAULT NULL COMMENT '逻辑删除标识',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  `refunded_amount` decimal(18,6) DEFAULT NULL COMMENT '退款金额',
  `refund_channel_stream_id` varchar(64) DEFAULT NULL COMMENT '退款流水号',
  PRIMARY KEY (`id`),
  KEY `idx_biz` (`biz_no`,`biz_type`) USING BTREE,
  KEY `idx_pay_order` (`pay_order_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

/******************************************/
/*   DatabaseName = nfturbo   */
/*   TableName = trade_order_0000   */
/******************************************/
CREATE TABLE `trade_order_0000` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `order_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号',
  `buyer_id` varchar(32) NOT NULL COMMENT '买家ID',
  `reverse_buyer_id` varchar(32) DEFAULT NULL COMMENT '逆序的买家ID',
  `buyer_type` varchar(32) NOT NULL COMMENT '买家类型',
  `seller_id` varchar(32) NOT NULL COMMENT '卖家ID',
  `seller_type` varchar(32) NOT NULL COMMENT '卖家类型',
  `identifier` varchar(128) NOT NULL COMMENT '幂等号',
  `goods_id` varchar(32) NOT NULL COMMENT '商品ID',
  `goods_type` varchar(32) NOT NULL COMMENT '商品类型',
  `goods_pic_url` varchar(512) DEFAULT NULL COMMENT '商品图片地址',
  `goods_name` varchar(1024) DEFAULT NULL COMMENT '商品名称',
  `item_price` decimal(18,6) DEFAULT NULL COMMENT '商品单价',
  `item_count` int DEFAULT NULL COMMENT '商品数量',
  `order_amount` decimal(18,6) NOT NULL COMMENT '订单金额',
  `order_state` varchar(32) NOT NULL COMMENT '订单状态',
  `paid_amount` decimal(18,6) NOT NULL COMMENT '已支付金额',
  `pay_succeed_time` datetime DEFAULT NULL COMMENT '支付成功时间',
  `order_confirmed_time` datetime DEFAULT NULL COMMENT '订单确认时间',
  `order_finished_time` datetime DEFAULT NULL COMMENT '完结时间',
  `order_closed_time` datetime DEFAULT NULL COMMENT '关单时间',
  `pay_channel` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '支付方式',
  `pay_stream_id` varchar(256) DEFAULT NULL COMMENT '支付流水号',
  `close_type` varchar(32) DEFAULT NULL COMMENT '关闭类型',
  `deleted` tinyint DEFAULT NULL COMMENT '逻辑删除标识',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  `snapshot_version` int DEFAULT NULL COMMENT '商品快照版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_buyer_identifier` (`identifier`,`buyer_id`,`goods_id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE,
  KEY `idx_buyer_state` (`buyer_id`,`order_state`),
  KEY `idx_state_time` (`order_state`,`gmt_create`),
  KEY `idx_rvbuyer_state` (`reverse_buyer_id`,`order_state`,`gmt_create`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='交易订单'
;

/******************************************/
/*   DatabaseName = nfturbo   */
/*   TableName = trade_order_0001   */
/******************************************/
CREATE TABLE `trade_order_0001` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `order_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号',
  `buyer_id` varchar(32) NOT NULL COMMENT '买家ID',
  `reverse_buyer_id` varchar(32) DEFAULT NULL COMMENT '逆序的买家ID',
  `buyer_type` varchar(32) NOT NULL COMMENT '买家类型',
  `seller_id` varchar(32) NOT NULL COMMENT '卖家ID',
  `seller_type` varchar(32) NOT NULL COMMENT '卖家类型',
  `identifier` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '幂等号',
  `goods_id` varchar(32) NOT NULL COMMENT '商品ID',
  `goods_type` varchar(32) NOT NULL COMMENT '商品类型',
  `goods_pic_url` varchar(512) DEFAULT NULL COMMENT '商品图片地址',
  `goods_name` varchar(1024) DEFAULT NULL COMMENT '商品名称',
  `item_price` decimal(18,6) DEFAULT NULL COMMENT '商品单价',
  `item_count` int DEFAULT NULL COMMENT '商品数量',
  `order_amount` decimal(18,6) NOT NULL COMMENT '订单金额',
  `order_state` varchar(32) NOT NULL COMMENT '订单状态',
  `paid_amount` decimal(18,6) NOT NULL COMMENT '已支付金额',
  `pay_succeed_time` datetime DEFAULT NULL COMMENT '支付成功时间',
  `order_confirmed_time` datetime DEFAULT NULL COMMENT '订单确认时间',
  `order_finished_time` datetime DEFAULT NULL COMMENT '完结时间',
  `order_closed_time` datetime DEFAULT NULL COMMENT '关单时间',
  `pay_channel` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '支付方式',
  `pay_stream_id` varchar(256) DEFAULT NULL COMMENT '支付流水号',
  `close_type` varchar(32) DEFAULT NULL COMMENT '关闭类型',
  `deleted` tinyint DEFAULT NULL COMMENT '逻辑删除标识',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  `snapshot_version` int DEFAULT NULL COMMENT '商品快照版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_buyer_identifier` (`identifier`,`buyer_id`,`goods_id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE,
  KEY `idx_buyer_state` (`buyer_id`,`order_state`),
  KEY `idx_state_time` (`order_state`,`gmt_create`),
  KEY `idx_rvbuyer_state` (`reverse_buyer_id`,`order_state`,`gmt_create`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='交易订单'
;

/******************************************/
/*   DatabaseName = nfturbo   */
/*   TableName = trade_order_0002   */
/******************************************/
CREATE TABLE `trade_order_0002` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `order_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号',
  `buyer_id` varchar(32) NOT NULL COMMENT '买家ID',
  `reverse_buyer_id` varchar(32) DEFAULT NULL COMMENT '逆序的买家ID',
  `buyer_type` varchar(32) NOT NULL COMMENT '买家类型',
  `seller_id` varchar(32) NOT NULL COMMENT '卖家ID',
  `seller_type` varchar(32) NOT NULL COMMENT '卖家类型',
  `identifier` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '幂等号',
  `goods_id` varchar(32) NOT NULL COMMENT '商品ID',
  `goods_type` varchar(32) NOT NULL COMMENT '商品类型',
  `goods_pic_url` varchar(512) DEFAULT NULL COMMENT '商品图片地址',
  `goods_name` varchar(1024) DEFAULT NULL COMMENT '商品名称',
  `item_price` decimal(18,6) DEFAULT NULL COMMENT '商品单价',
  `item_count` int DEFAULT NULL COMMENT '商品数量',
  `order_amount` decimal(18,6) NOT NULL COMMENT '订单金额',
  `order_state` varchar(32) NOT NULL COMMENT '订单状态',
  `paid_amount` decimal(18,6) NOT NULL COMMENT '已支付金额',
  `pay_succeed_time` datetime DEFAULT NULL COMMENT '支付成功时间',
  `order_confirmed_time` datetime DEFAULT NULL COMMENT '订单确认时间',
  `order_finished_time` datetime DEFAULT NULL COMMENT '完结时间',
  `order_closed_time` datetime DEFAULT NULL COMMENT '关单时间',
  `pay_channel` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '支付方式',
  `pay_stream_id` varchar(256) DEFAULT NULL COMMENT '支付流水号',
  `close_type` varchar(32) DEFAULT NULL COMMENT '关闭类型',
  `deleted` tinyint DEFAULT NULL COMMENT '逻辑删除标识',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  `snapshot_version` int DEFAULT NULL COMMENT '商品快照版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_buyer_identifier` (`identifier`,`buyer_id`,`goods_id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE,
  KEY `idx_buyer_state` (`buyer_id`,`order_state`),
  KEY `idx_state_time` (`order_state`,`gmt_create`),
  KEY `idx_rvbuyer_state` (`reverse_buyer_id`,`order_state`,`gmt_create`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='交易订单'
;

/******************************************/
/*   DatabaseName = nfturbo   */
/*   TableName = trade_order_0003   */
/******************************************/
CREATE TABLE `trade_order_0003` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `order_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号',
  `buyer_id` varchar(32) NOT NULL COMMENT '买家ID',
  `reverse_buyer_id` varchar(32) DEFAULT NULL COMMENT '逆序的买家ID',
  `buyer_type` varchar(32) NOT NULL COMMENT '买家类型',
  `seller_id` varchar(32) NOT NULL COMMENT '卖家ID',
  `seller_type` varchar(32) NOT NULL COMMENT '卖家类型',
  `identifier` varchar(128) NOT NULL COMMENT '幂等号',
  `goods_id` varchar(32) NOT NULL COMMENT '商品ID',
  `goods_type` varchar(32) NOT NULL COMMENT '商品类型',
  `goods_pic_url` varchar(512) DEFAULT NULL COMMENT '商品图片地址',
  `goods_name` varchar(1024) DEFAULT NULL COMMENT '商品名称',
  `item_price` decimal(18,6) DEFAULT NULL COMMENT '商品单价',
  `item_count` int DEFAULT NULL COMMENT '商品数量',
  `order_amount` decimal(18,6) NOT NULL COMMENT '订单金额',
  `order_state` varchar(32) NOT NULL COMMENT '订单状态',
  `paid_amount` decimal(18,6) NOT NULL COMMENT '已支付金额',
  `pay_succeed_time` datetime DEFAULT NULL COMMENT '支付成功时间',
  `order_confirmed_time` datetime DEFAULT NULL COMMENT '订单确认时间',
  `order_finished_time` datetime DEFAULT NULL COMMENT '完结时间',
  `order_closed_time` datetime DEFAULT NULL COMMENT '关单时间',
  `pay_channel` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '支付方式',
  `pay_stream_id` varchar(256) DEFAULT NULL COMMENT '支付流水号',
  `close_type` varchar(32) DEFAULT NULL COMMENT '关闭类型',
  `deleted` tinyint DEFAULT NULL COMMENT '逻辑删除标识',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  `snapshot_version` int DEFAULT NULL COMMENT '商品快照版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_buyer_identifier` (`identifier`,`buyer_id`,`goods_id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE,
  KEY `idx_buyer_state` (`buyer_id`,`order_state`),
  KEY `idx_state_time` (`order_state`,`gmt_create`),
  KEY `idx_rvbuyer_state` (`reverse_buyer_id`,`order_state`,`gmt_create`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='交易订单'
;

/******************************************/
/*   DatabaseName = nfturbo   */
/*   TableName = trade_order__test   */
/******************************************/
CREATE TABLE `trade_order__test` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `order_id` varchar(32) NOT NULL COMMENT '订单号',
  `buyer_id` varchar(32) NOT NULL COMMENT '买家ID',
  `buyer_type` varchar(32) NOT NULL COMMENT '买家类型',
  `seller_id` varchar(32) NOT NULL COMMENT '卖家ID',
  `seller_type` varchar(32) NOT NULL COMMENT '卖家类型',
  `goods_id` varchar(32) NOT NULL COMMENT '商品ID',
  `goods_type` varchar(32) NOT NULL COMMENT '商品类型',
  `goods_name` varchar(1024) DEFAULT NULL COMMENT '商品名称',
  `order_amount` decimal(18,6) NOT NULL COMMENT '订单金额',
  `order_state` varchar(32) NOT NULL COMMENT '订单状态',
  `paid_amount` decimal(18,6) NOT NULL COMMENT '已支付金额',
  `pay_success_time` datetime DEFAULT NULL COMMENT '支付成功时间',
  `order_confirmed_time` datetime DEFAULT NULL COMMENT '订单确认时间',
  `order_finished_time` datetime DEFAULT NULL COMMENT '完结时间',
  `order_closed_time` datetime DEFAULT NULL COMMENT '关单时间',
  `pay_method` varchar(64) DEFAULT NULL COMMENT '支付方式',
  `pay_stream_id` varchar(256) DEFAULT NULL COMMENT '支付流水号',
  `close_type` varchar(32) DEFAULT NULL COMMENT '关闭类型',
  `deleted` tinyint DEFAULT NULL COMMENT '逻辑删除标识',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`) USING BTREE,
  KEY `idx_buyer_state` (`buyer_id`,`order_state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='交易订单'
;

/******************************************/
/*   DatabaseName = nfturbo   */
/*   TableName = trade_order_stream_0000   */
/******************************************/
CREATE TABLE `trade_order_stream_0000` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `order_id` varchar(32) NOT NULL COMMENT '订单号',
  `buyer_id` varchar(32) NOT NULL COMMENT '买家ID',
  `buyer_type` varchar(32) NOT NULL COMMENT '买家类型',
  `seller_id` varchar(32) NOT NULL COMMENT '卖家ID',
  `seller_type` varchar(32) NOT NULL COMMENT '卖家类型',
  `identifier` varchar(128) NOT NULL COMMENT '幂等号',
  `goods_id` varchar(32) NOT NULL COMMENT '商品ID',
  `goods_type` varchar(32) NOT NULL COMMENT '商品类型',
  `goods_name` varchar(1024) DEFAULT NULL COMMENT '商品名称',
  `goods_pic_url` varchar(1024) DEFAULT NULL COMMENT '商品主图',
  `order_amount` decimal(18,6) NOT NULL COMMENT '订单金额',
  `order_state` varchar(32) NOT NULL COMMENT '订单状态',
  `paid_amount` decimal(18,6) NOT NULL COMMENT '已支付金额',
  `item_price` decimal(18,6) NOT NULL COMMENT '商品单价',
  `item_count` int NOT NULL COMMENT '商品数量',
  `pay_succeed_time` datetime DEFAULT NULL COMMENT '支付成功时间',
  `order_confirmed_time` datetime DEFAULT NULL COMMENT '订单确认时间',
  `order_finished_time` datetime DEFAULT NULL COMMENT '完结时间',
  `order_closed_time` datetime DEFAULT NULL COMMENT '关单时间',
  `pay_channel` varchar(64) DEFAULT NULL COMMENT '支付方式',
  `pay_stream_id` varchar(256) DEFAULT NULL COMMENT '支付流水号',
  `close_type` varchar(32) DEFAULT NULL COMMENT '关闭类型',
  `deleted` tinyint DEFAULT NULL COMMENT '逻辑删除标识',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  `snapshot_version` int DEFAULT NULL COMMENT '商品快照版本号',
  `stream_identifier` varchar(128) NOT NULL COMMENT '幂等号',
  `stream_type` varchar(128) NOT NULL COMMENT '流水类型',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_type` (`stream_type`,`stream_identifier`,`order_id`),
  KEY `idx_order_id_buyer` (`order_id`,`buyer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

/******************************************/
/*   DatabaseName = nfturbo   */
/*   TableName = trade_order_stream_0001   */
/******************************************/
CREATE TABLE `trade_order_stream_0001` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `order_id` varchar(32) NOT NULL COMMENT '订单号',
  `buyer_id` varchar(32) NOT NULL COMMENT '买家ID',
  `buyer_type` varchar(32) NOT NULL COMMENT '买家类型',
  `seller_id` varchar(32) NOT NULL COMMENT '卖家ID',
  `seller_type` varchar(32) NOT NULL COMMENT '卖家类型',
  `identifier` varchar(128) NOT NULL COMMENT '幂等号',
  `goods_id` varchar(32) NOT NULL COMMENT '商品ID',
  `goods_type` varchar(32) NOT NULL COMMENT '商品类型',
  `goods_name` varchar(1024) DEFAULT NULL COMMENT '商品名称',
  `goods_pic_url` varchar(1024) DEFAULT NULL COMMENT '商品主图',
  `order_amount` decimal(18,6) NOT NULL COMMENT '订单金额',
  `order_state` varchar(32) NOT NULL COMMENT '订单状态',
  `paid_amount` decimal(18,6) NOT NULL COMMENT '已支付金额',
  `item_price` decimal(18,6) NOT NULL COMMENT '商品单价',
  `item_count` int NOT NULL COMMENT '商品数量',
  `pay_succeed_time` datetime DEFAULT NULL COMMENT '支付成功时间',
  `order_confirmed_time` datetime DEFAULT NULL COMMENT '订单确认时间',
  `order_finished_time` datetime DEFAULT NULL COMMENT '完结时间',
  `order_closed_time` datetime DEFAULT NULL COMMENT '关单时间',
  `pay_channel` varchar(64) DEFAULT NULL COMMENT '支付方式',
  `pay_stream_id` varchar(256) DEFAULT NULL COMMENT '支付流水号',
  `close_type` varchar(32) DEFAULT NULL COMMENT '关闭类型',
  `deleted` tinyint DEFAULT NULL COMMENT '逻辑删除标识',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  `snapshot_version` int DEFAULT NULL COMMENT '商品快照版本号',
  `stream_identifier` varchar(128) NOT NULL COMMENT '幂等号',
  `stream_type` varchar(128) NOT NULL COMMENT '流水类型',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_type` (`stream_type`,`stream_identifier`,`order_id`),
  KEY `idx_order_id_buyer` (`order_id`,`buyer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

/******************************************/
/*   DatabaseName = nfturbo   */
/*   TableName = trade_order_stream_0002   */
/******************************************/
CREATE TABLE `trade_order_stream_0002` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `order_id` varchar(32) NOT NULL COMMENT '订单号',
  `buyer_id` varchar(32) NOT NULL COMMENT '买家ID',
  `buyer_type` varchar(32) NOT NULL COMMENT '买家类型',
  `seller_id` varchar(32) NOT NULL COMMENT '卖家ID',
  `seller_type` varchar(32) NOT NULL COMMENT '卖家类型',
  `identifier` varchar(128) NOT NULL COMMENT '幂等号',
  `goods_id` varchar(32) NOT NULL COMMENT '商品ID',
  `goods_type` varchar(32) NOT NULL COMMENT '商品类型',
  `goods_name` varchar(1024) DEFAULT NULL COMMENT '商品名称',
  `goods_pic_url` varchar(1024) DEFAULT NULL COMMENT '商品主图',
  `order_amount` decimal(18,6) NOT NULL COMMENT '订单金额',
  `order_state` varchar(32) NOT NULL COMMENT '订单状态',
  `paid_amount` decimal(18,6) NOT NULL COMMENT '已支付金额',
  `item_price` decimal(18,6) NOT NULL COMMENT '商品单价',
  `item_count` int NOT NULL COMMENT '商品数量',
  `pay_succeed_time` datetime DEFAULT NULL COMMENT '支付成功时间',
  `order_confirmed_time` datetime DEFAULT NULL COMMENT '订单确认时间',
  `order_finished_time` datetime DEFAULT NULL COMMENT '完结时间',
  `order_closed_time` datetime DEFAULT NULL COMMENT '关单时间',
  `pay_channel` varchar(64) DEFAULT NULL COMMENT '支付方式',
  `pay_stream_id` varchar(256) DEFAULT NULL COMMENT '支付流水号',
  `close_type` varchar(32) DEFAULT NULL COMMENT '关闭类型',
  `deleted` tinyint DEFAULT NULL COMMENT '逻辑删除标识',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  `snapshot_version` int DEFAULT NULL COMMENT '商品快照版本号',
  `stream_identifier` varchar(128) NOT NULL COMMENT '幂等号',
  `stream_type` varchar(128) NOT NULL COMMENT '流水类型',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_type` (`stream_type`,`stream_identifier`,`order_id`),
  KEY `idx_order_id_buyer` (`order_id`,`buyer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

/******************************************/
/*   DatabaseName = nfturbo   */
/*   TableName = trade_order_stream_0003   */
/******************************************/
CREATE TABLE `trade_order_stream_0003` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `order_id` varchar(32) NOT NULL COMMENT '订单号',
  `buyer_id` varchar(32) NOT NULL COMMENT '买家ID',
  `buyer_type` varchar(32) NOT NULL COMMENT '买家类型',
  `seller_id` varchar(32) NOT NULL COMMENT '卖家ID',
  `seller_type` varchar(32) NOT NULL COMMENT '卖家类型',
  `identifier` varchar(128) NOT NULL COMMENT '幂等号',
  `goods_id` varchar(32) NOT NULL COMMENT '商品ID',
  `goods_type` varchar(32) NOT NULL COMMENT '商品类型',
  `goods_name` varchar(1024) DEFAULT NULL COMMENT '商品名称',
  `goods_pic_url` varchar(1024) DEFAULT NULL COMMENT '商品主图',
  `order_amount` decimal(18,6) NOT NULL COMMENT '订单金额',
  `order_state` varchar(32) NOT NULL COMMENT '订单状态',
  `paid_amount` decimal(18,6) NOT NULL COMMENT '已支付金额',
  `item_price` decimal(18,6) NOT NULL COMMENT '商品单价',
  `item_count` int NOT NULL COMMENT '商品数量',
  `pay_succeed_time` datetime DEFAULT NULL COMMENT '支付成功时间',
  `order_confirmed_time` datetime DEFAULT NULL COMMENT '订单确认时间',
  `order_finished_time` datetime DEFAULT NULL COMMENT '完结时间',
  `order_closed_time` datetime DEFAULT NULL COMMENT '关单时间',
  `pay_channel` varchar(64) DEFAULT NULL COMMENT '支付方式',
  `pay_stream_id` varchar(256) DEFAULT NULL COMMENT '支付流水号',
  `close_type` varchar(32) DEFAULT NULL COMMENT '关闭类型',
  `deleted` tinyint DEFAULT NULL COMMENT '逻辑删除标识',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  `snapshot_version` int DEFAULT NULL COMMENT '商品快照版本号',
  `stream_identifier` varchar(128) NOT NULL COMMENT '幂等号',
  `stream_type` varchar(128) NOT NULL COMMENT '流水类型',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_type` (`stream_type`,`stream_identifier`,`order_id`),
  KEY `idx_order_id_buyer` (`order_id`,`buyer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

/******************************************/
/*   DatabaseName = nfturbo   */
/*   TableName = user_operate_stream   */
/******************************************/
CREATE TABLE `user_operate_stream` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '流水ID（自增主键）',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后更新时间',
  `user_id` varchar(64) DEFAULT NULL COMMENT '用户ID',
  `type` varchar(64) DEFAULT NULL COMMENT '操作类型',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `param` text COMMENT '操作参数',
  `extend_info` text COMMENT '扩展字段',
  `deleted` int DEFAULT NULL COMMENT '是否逻辑删除，0为未删除，非0为已删除',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb3 COMMENT='用户操作流水表'
;

/******************************************/
/*   DatabaseName = nfturbo   */
/*   TableName = users   */
/******************************************/
CREATE TABLE `users` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID（自增主键）',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '最后更新时间',
  `nick_name` varchar(255) DEFAULT NULL COMMENT '用户昵称',
  `password_hash` varchar(255) DEFAULT NULL COMMENT '密码哈希',
  `state` varchar(64) DEFAULT NULL COMMENT '用户状态（ACTIVE，FROZEN）',
  `invite_code` varchar(255) DEFAULT NULL COMMENT '邀请码',
  `telephone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `inviter_id` varchar(255) DEFAULT NULL COMMENT '邀请人用户ID',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `profile_photo_url` varchar(255) DEFAULT NULL COMMENT '用户头像URL',
  `block_chain_url` varchar(255) DEFAULT NULL COMMENT '区块链地址',
  `block_chain_platform` varchar(255) DEFAULT NULL COMMENT '区块链平台',
  `certification` tinyint(1) DEFAULT NULL COMMENT '实名认证状态（TRUE或FALSE）',
  `real_name` varchar(255) DEFAULT NULL COMMENT '真实姓名',
  `id_card_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '身份证no',
  `user_role` varchar(128) DEFAULT NULL COMMENT '用户角色',
  `deleted` int DEFAULT NULL COMMENT '是否逻辑删除，0为未删除，非0为已删除',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表'
;


/******************************************/
/*   DatabaseName = nfturbo   */
/*   TableName = collection_inventory_stream   */
/******************************************/
CREATE TABLE `collection_inventory_stream` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增主键）',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '最后更新时间',
  `collection_id` bigint DEFAULT NULL COMMENT '藏品id',
  `changed_quantity` bigint DEFAULT NULL COMMENT '本次变更的数量',
  `price` decimal(18,6) DEFAULT NULL COMMENT '价格',
  `quantity` bigint DEFAULT NULL COMMENT '藏品数量',
  `state` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '状态',
  `saleable_inventory` bigint DEFAULT NULL COMMENT '可售库存',
  `occupied_inventory` bigint DEFAULT NULL COMMENT '已占库存',
  `stream_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '流水类型',
  `identifier` varchar(128) DEFAULT NULL COMMENT '幂等号',
  `deleted` int DEFAULT NULL COMMENT '是否逻辑删除，0为未删除，非0为已删除',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
   `extend_info` varchar(512) DEFAULT NULL COMMENT '扩展信息',
  PRIMARY KEY (`id`),
  KEY `idx_cid_ident_type` (`collection_id`,`identifier`,`stream_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=246 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci AVG_ROW_LENGTH=16384 ROW_FORMAT=DYNAMIC COMMENT='藏品表库存流水'
;

/******************************************/
/*   DatabaseName = nfturbo   */
/*   TableName = collection_snapshot   */
/******************************************/
CREATE TABLE `collection_snapshot` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增主键）',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '最后更新时间',
  `collection_id` bigint NOT NULL COMMENT '藏品id',
  `name` varchar(512) DEFAULT NULL COMMENT '藏品名称',
  `cover` varchar(512) DEFAULT NULL COMMENT '藏品封面',
  `class_id` varchar(128) DEFAULT NULL COMMENT '藏品类目ID',
  `price` decimal(18,6) DEFAULT NULL COMMENT '价格',
  `quantity` bigint DEFAULT NULL COMMENT '藏品数量',
  `detail` text COMMENT '详情',
  `saleable_inventory` bigint DEFAULT NULL COMMENT '可销售库存',
  `create_time` datetime DEFAULT NULL COMMENT '藏品创建时间',
  `sale_time` datetime DEFAULT NULL COMMENT '藏品发售时间',
  `sync_chain_time` datetime DEFAULT NULL COMMENT '藏品上链时间',
  `deleted` int DEFAULT NULL COMMENT '是否逻辑删除，0为未删除，非0为已删除',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
  `creator_id` varchar(128) DEFAULT NULL COMMENT '创建者',
  `version` int DEFAULT NULL COMMENT '修改版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10017 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='藏品快照表'
;

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

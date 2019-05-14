create schema society default character set utf8mb4 collate utf8mb4_unicode_ci;

CREATE TABLE `society_user_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL COMMENT '用户ID',
  `gds_id` int(11) NOT NULL COMMENT '商品ID',
  `deleted` tinyint(4) NOT NULL COMMENT '是否被删除 0-未删除， 1-已删除',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `k_cg_gds` (`cg_id`, `gds_id`)
) ENGINE=InnoDB COMMENT='咖啡商品分类与商品绑定表';
-- 逆向最新版本本地数据库补充字段
-- MySQL 8.0；用于 D:\work\kaiyueshangmao\dingyangmall-admin.jar

SET @sql = IF(
  EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema = DATABASE() AND table_name = 'order_info' AND column_name = 'pay_integral'
  ),
  'SELECT 1',
  'ALTER TABLE order_info ADD COLUMN pay_integral int DEFAULT 0 COMMENT ''支付积分'' AFTER payment_price'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = IF(
  EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema = DATABASE() AND table_name = 'goods_spu' AND column_name = 'coupon_type'
  ),
  'SELECT 1',
  'ALTER TABLE goods_spu ADD COLUMN coupon_type char(1) DEFAULT NULL COMMENT ''商品券子类型'' AFTER goods_type'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = IF(
  EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema = DATABASE() AND table_name = 'sys_user' AND column_name = 'invite_code'
  ),
  'SELECT 1',
  'ALTER TABLE sys_user ADD COLUMN invite_code varchar(64) DEFAULT NULL COMMENT ''邀请码'' AFTER parent_distributor_id'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = IF(
  EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema = DATABASE() AND table_name = 'sys_user' AND column_name = 'dealer_points'
  ),
  'SELECT 1',
  'ALTER TABLE sys_user ADD COLUMN dealer_points int DEFAULT 0 COMMENT ''经销商积分'' AFTER invite_code'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


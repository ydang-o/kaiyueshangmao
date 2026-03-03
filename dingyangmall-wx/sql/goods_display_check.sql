-- 商品不展示时自检与修复
-- 小程序/首页优先展示 shelf='1'（上架）且 del_flag='0'（未删除）的商品；无上架时会回退显示未删除商品。

-- 1. 检查表是否存在、总条数、上架条数
SELECT 'goods_spu 总行数' AS remark, COUNT(*) AS cnt FROM goods_spu;
SELECT '其中 shelf=1 上架' AS remark, COUNT(*) AS cnt FROM goods_spu WHERE shelf = '1';
SELECT '其中 del_flag=0 未删除' AS remark, COUNT(*) AS cnt FROM goods_spu WHERE (del_flag = '0' OR del_flag IS NULL);

-- 2. 若无上架商品导致首页 goodsList 为空，执行下面一句使未删除商品全部上架
UPDATE goods_spu SET shelf = '1' WHERE (del_flag = '0' OR del_flag IS NULL);

-- 3. 若表为空，请先执行项目中的 dingyangmall_ry_fixed.sql 或 dingyangmall_ry.sql 里的 goods_spu 建表及 insert

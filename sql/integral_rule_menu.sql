-- 积分规则配置：侧栏菜单与按钮权限（挂到「商城管理」下）
-- parent_id=2033 为商城管理；若你库中商城管理 ID 不同，请修改下方 2033

-- 1. 积分规则主菜单
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES ('积分规则', 2033, 10, 'integralRule', 'mall/integralRule/index', 1, 0, 'C', '0', '0', 'mall:integralrule:index', 'money', 'admin', NOW(), NULL, NULL, '积分规则菜单');

SET @integralRuleMenuId = LAST_INSERT_ID();

-- 2. 按钮权限（含查询 get，供详情/编辑使用）
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES ('积分规则查询', @integralRuleMenuId, 1, '#', '', 1, 0, 'F', '0', '0', 'mall:integralrule:get', '#', 'admin', NOW(), NULL, NULL, ''),
       ('积分规则新增', @integralRuleMenuId, 2, '#', '', 1, 0, 'F', '0', '0', 'mall:integralrule:add', '#', 'admin', NOW(), NULL, NULL, ''),
       ('积分规则修改', @integralRuleMenuId, 3, '#', '', 1, 0, 'F', '0', '0', 'mall:integralrule:edit', '#', 'admin', NOW(), NULL, NULL, ''),
       ('积分规则删除', @integralRuleMenuId, 4, '#', '', 1, 0, 'F', '0', '0', 'mall:integralrule:del', '#', 'admin', NOW(), NULL, NULL, '');

-- 3. 为角色分配菜单（管理员 1、普通角色 2）
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 1, @integralRuleMenuId FROM DUAL WHERE EXISTS (SELECT 1 FROM sys_role WHERE role_id = 1);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 1, menu_id FROM sys_menu WHERE parent_id = @integralRuleMenuId AND menu_type = 'F';

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 2, @integralRuleMenuId FROM DUAL WHERE EXISTS (SELECT 1 FROM sys_role WHERE role_id = 2);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 2, menu_id FROM sys_menu WHERE parent_id = @integralRuleMenuId AND menu_type = 'F';

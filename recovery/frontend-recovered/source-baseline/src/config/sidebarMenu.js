/**
 * 静态左侧菜单配置：不依赖后端 getRouters，左侧功能区由此配置直接生成
 * 与后端菜单结构一致：path、component、meta.title、children
 */
export const staticSidebarRoutes = [
  {
    path: '/system',
    component: 'Layout',
    redirect: '/system/notice',
    meta: { title: '系统管理', icon: 'system' },
    children: [
      { path: 'notice', component: 'system/notice/index', meta: { title: '通知公告', icon: 'message' } },
      { path: 'config', component: 'system/config/index', meta: { title: '参数设置', icon: 'edit' } }
    ]
  },
  {
    path: '/monitor',
    component: 'Layout',
    redirect: '/monitor/operlog',
    meta: { title: '系统监控', icon: 'monitor' },
    children: [
      { path: 'operlog', component: 'monitor/operlog/index', meta: { title: '操作日志', icon: 'form' } },
      { path: 'logininfor', component: 'monitor/logininfor/index', meta: { title: '登录日志', icon: 'logininfor' } }
    ]
  },
  {
    path: '/mall',
    component: 'Layout',
    redirect: '/mall/goodsspu',
    meta: { title: '商城管理', icon: 'shopping' },
    children: [
      { path: 'goodscategory', component: 'mall/goodscategory/index', meta: { title: '商品分类', icon: 'tree' } },
      { path: 'goodsspu', component: 'mall/goodsspu/index', meta: { title: '商品管理', icon: 'shopping' } },
      { path: 'orderinfo', component: 'mall/orderinfo/index', meta: { title: '订单管理', icon: 'list' } },
      { path: 'banner', component: 'mall/banner/index', meta: { title: '轮播图管理', icon: 'images' } }
    ]
  },
  {
    path: '/activity',
    component: 'Layout',
    redirect: '/activity/lottery',
    meta: { title: '营销活动', icon: 'star' },
    children: [
      { path: 'lottery', component: 'mall/lottery/index', meta: { title: '抽奖活动管理', icon: 'star' } },
      { path: 'coupon', component: 'mall/coupon/index', meta: { title: '商品券核销', icon: 'validCode' } },
      { path: 'integralRule', component: 'mall/integralRule/index', meta: { title: '积分规则配置', icon: 'money' } },
      { path: 'integralflow', component: 'mall/integralflow/index', meta: { title: '积分流水', icon: 'list' } }
    ]
  },
  {
    path: '/member',
    component: 'Layout',
    redirect: '/member/distributorL1',
    meta: { title: '人员管理', icon: 'peoples' },
    children: [
      { path: 'distributorL1', component: 'member/distributorL1/index', meta: { title: '一级经销商', icon: 'user' } },
      { path: 'distributorL2', component: 'member/distributorL2/index', meta: { title: '二级经销商', icon: 'user' } },
      { path: 'member', component: 'mall/member/index', meta: { title: '会员', icon: 'peoples' } },
      { path: 'normalUser', component: 'member/normalUser/index', meta: { title: '普通用户', icon: 'user' } }
    ]
  }
]

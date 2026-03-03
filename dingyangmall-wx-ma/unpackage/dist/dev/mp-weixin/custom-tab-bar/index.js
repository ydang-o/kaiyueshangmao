Component({
  data: {
    selected: 0,
    list: [
      { pagePath: '/pages/home/index', text: '首页', icon: 'cuIcon-home', iconActive: 'cuIcon-homefill' },
      { pagePath: '/pages/goods/goods-category/index', text: '分类', icon: 'cuIcon-apps', iconActive: 'cuIcon-apps' },
      { pagePath: '/pages/user/user-center/index', text: '我的', icon: 'cuIcon-my', iconActive: 'cuIcon-myfill' }
    ]
  },
  methods: {
    switchTab(e) {
      const idx = e.currentTarget.dataset.index
      const item = this.data.list[idx]
      wx.switchTab({ url: item.pagePath })
      this.setData({ selected: idx })
    }
  }
})

/*
主页面引用
*/
require(['jquery', 'util', 'Const'], function($, util, Const) {
    util.loading();
    $('.bv-preloader-container').remove();
    $('.bv-overlay').remove();
    $('#mainBody').show();
    // 异常上报
    if (Const.init.report) {
        if (Const.init.report === 'sentry' && Const.params.reportAddress) {
            require(['raven'], function (raven) {
                raven.config(Const.params.reportAddress).install();
            });
        }
    }

    // 初始化Const、部分组件定义及ajax error公共处理
    util.init();
    util.initEnums();

    require(['vue', 'jquery', 'util', 'Const', 'bootstrap', 'bvBasic', 'bvExtend', 'bvForm', 'bvTable'], function(vue, $, util, Const) {
        // 当前登录用户信息
        var user = {};
        if (util.type(Const.url.authority.info) === 'string') {
            util.get({
                url: Const.url.authority.info,
                async: false,
                success: function (res) {
                    var data = util.data(res);
                    if (data) {
                        user = data;
                    }
                }
            });
        }

        var vm = util.bind({
            container: 'root',
            data: {
                showNav: true,
                user: user,
                menuConfig: {
                    id: 'menuTree',
                    type: 'menu',
                    url: 'GET:' + util.mix(Const.url.authority.menus, {
                        appCode: Const.init.appCode
                    }),
                    // method: 'get',
                    icon: false,
                    line: false,
                    title: true,
                    /// singlePath: true,
                    pack: function (entity) {
                        return {
                            id: entity[Const.menu.id],
                            pId: entity[Const.menu.parentId],
                            name: entity[Const.menu.name],
                            target: entity[Const.menu.url],
                            open: true,
                            iconSkin: entity[Const.menu.icon] || null,
                            entity: entity
                        };
                    }
                },
                position: {
                    titles: [{
                        text: '首页'
                    }]
                },
                mainTabsConfig: {
                    attr: {
                        id: 'mainTabs'
                    },
                    clazz: 'bv-tabs',
                    type: 'menu',
                    tabs: [{
                        id: 'menu-home',
                        text: '首页',
                        target: Const.url.home,
                        prop: {
                            titles: '首页'
                        },
                        sticky: true        // 不允许关闭
                    }]
                }
            },
            computed: {
                navTitle: function () {
                    return this.showNav ? '点击隐藏菜单' : '点击显示菜单';
                }
            },
            watch: {
                showNav: function (value, oldValue) {
                    util.resize();
                    this.$nextTick(function () {
                        util.scroll();
                    });
                }
            },
            methods: {
                onMainTabsInit: function (vm) {
                    util.cache('mainTabs', vm);

                    $('.bv-preloader-container').remove();
                    $('.bv-overlay').remove();
                    $('body').show();
                },
                onMenuInit: function (vm) {
                    /// Const.global.menuTree = vm;
                    util.cache('menuTree', vm);
                    util.scroll($(".bv-nav .bv-menu"));
                },
                onMenuExpand: function () {
                    util.scroll($('.bv-nav .bv-menu'));
                },
                onMenuClick: function (e, treeId, treeNode) {
                    if (!treeNode.isParent) {
                        util.mainTabs(treeNode);
                    }
                },
                toHome: function () {
                    util.home();
                },
                removeAllTabs: function () {
                    util.cache('mainTabs').removeAll();
                },
                doLock: function () {
                    util.post({
                        url: Const.url.authority.logout,
                        complete: function () {
                            util.login();
                        }
                    });
                },
                changePassword: function () {
                    util.modal({
                        url: '/modules/userCenter/changePassword.html'
                    });
                },
                doLogout: function () {
                    util.post({
                        url: Const.url.authority.logout,
                        complete: function () {
                            util.redirect('/');
                        }
                    });
                }
            },
            mounted: function () {
                Const.vm.root = this;
                util.loading('hide');
            }
        });

        vm.$nextTick(function () {
            util.tooltip($(document));
            util.tooltip($(document), 'data-title');
        });

        $(document).on('click', '[data-mainTabs]', function() {
            if ($(this).attr('data-mainTabs-title')) {
                util.mainTabs({
                    // id: 'querybankAccountDetail',
                    text: $(this).attr('data-mainTabs-title'),
                    target: $(this).attr('data-mainTabs')
                }, $(this).attr('data-mainTabs-title'));
            } else {
                util.mainTabs($(this).attr('data-mainTabs'), 'target');
            }
        });
        $(document).on('click', '[data-href]', function() {
            util.open($(this).attr('data-href'));
        });
        $(document).on('click', '[data-modal]', function() {
            util.modal({
                url: $(this).attr('data-modal')
            });
        });
        $(document).on('click', '[data-redirect]', function() {
            util.redirect($(this).attr('data-redirect'), 'body');
        });
        $('#download').on('load', function(event) {
            // <h1>Whitelabel Error Page</h1>
            var data = util.json($(event.target).contents().find("body").text());
            util.show({
                title: '文件下载失败',
                message: data.message,
                level: 'error'
            });
        });
        $(window).on('unload', function () {
            util.capture('close');
        });
        util.keySwitch();
    });
});
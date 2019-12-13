/**
 * 扩展组件，包含 bv-tree bv-position bv-tabs bv-wizard bv-modal bv-editor bv-chart bv-pager bv-grant bv-upload bv-import bv-export bv-search bv-media bv-file bv-panel bv-carousel bv-list
 */
define([
    'vue',
    'jquery',
    'util',
    'Const'
], function (vue, $, util, Const) {
    vue.component('bv-tree', {
        props: {
            entity: {
                default: function () {
                    return {};
                }
            },
            name: {
                default: ''
            },

            id: {
                default: ''
            },
            clazz: {
                default: ''
            },
            // 属性定义
            attr: {
                default: function () {
                    return {};
                }
            },
            defaultValue: {
                default: ''
            },
            // 是否显示图标
            icon: {
                default: true
            },
            // 是否显示连接线
            line: {
                default: true
            },
            // 是否显示title
            title: {
                default: false
            },
            // 是否允许选择
            // 支持checkbox、radio
            check: {
                default: false
            },
            // 默认为空，可设置为s-表示父节点影响子节点，但子节点不影响父节点
            checkType: {
                default: ''
            },
            // 是否保持展开单一路径
            singlePath: {
                default: false
            },
            // 是否展开第一个根节点
            expandFirst: {
                default: false
            },
            // 类型，支持：menu-菜单select-下拉
            type: '',
            items: {
                default: ''
            },
            // 初始化数据url
            url: {
                default: ''
            },
            data: {
                default: function () {
                    return {};
                }
            },
            // 初始化method
            /*method: {
                default: 'post'
            },*/
            entityName: {
                default: ''
            },
            initParamList: {
                default: ''
            },
            orders: {
                default: ''
            },
            orderList: {
                default: function () {
                    return [];
                }
            },
            initEntity: {
                default: ''
            },
            // 根节点
            rootNode: {
                default: ''
            },
            // 来源 filter-精确查询
            from: {
                default: ''
            },

            // 函数，初始化完成调用
            /// onInit: '',
            // 函数，组装数据
            pack: {
                default: ''
            },
            // 下拉菜单时显示
            show: {
                default: ''
            }
            // 函数，展开前
            /// beforeExpand: '',
            // 展开时
            /// onExpand: '',
            // 函数，点击触发
            /// onClick: ''
        },
        data: function () {
            return {
                showSelect: false,
                resultName: this.name + 'Result',
                currentNode: null
            };
        },
        beforeCreated: function () {
            /// this.nodes = [];
            // 树的jquery对象
            this.$tree = '';
            this.config = '';
        },
        created: function() {
            if (this.id) {
                this.attr.id = this.id;
            } else {
                this.attr.id = util.guid();
            }
            this.eventId = util.guid();
            // this.orderList = util.transOrder(this.orderList, this.orders);
        },
        mounted: function() {
            var vm = this;
            util.require('ztree', function () {
                util.transOrder(vm.orderList, vm.orders);

                vm.init();
                if (util.isEmpty(vm.entity[vm.name]) && !util.isEmpty(vm.defaultValue)) {
                    vm.entity[vm.name] = vm.defaultValue;
                }

                if (vm.type === 'select') {
                    // $('ul', vm.$el).width($('.tree-choose', vm.$el).width() - 12);
                    $('ul', vm.$el).width(($('.tree-choose', vm.$el).width() - 12)<=0 ? 280:($('.tree-choose', vm.$el).width() - 12));
                    $(document).on('click.' + vm.eventId, function (event) {
                        if ($(event.target).is('.tree-choose,.ztree') || $(event.target).closest('.tree-choose,.ztree').length === 1) {
                            if ($(event.target).closest('.bv-tree').is($(vm.$el))) {
                                vm.showSelect = true;
                            } else {
                                vm.showSelect = false;
                            }
                        } else {
                            vm.showSelect = false;
                        }
                    });
                }
            });

            this.$emit('on-mounted', this);
        },
        beforeDestroy: function () {
            $(document).off('click.' + this.eventId);
        },
        methods: {
            init: function() {
                var callback = {};
                /*if (util.type(this.beforeExpand) === 'function') {
                    callback.beforeExpand = this.beforeExpand;
                }
                if (util.type(this.onExpand) === 'function') {
                    callback.onExpand = this.onExpand;
                }
                if (util.type(this.onClick) === 'function') {
                    callback.onClick = this.onClick;
                }*/
                var vm = this;
                callback.beforeExpand = function (treeId, treeNode) {
                    /*var menuTree = $.fn.zTree.getZTreeObj(treeId);
                    // 展开的所有节点，这是从父节点开始查找（也可以全文查找）
                    var expandedNodes = menuTree.getNodesByParam('open', true, treeNode.getParentNode());

                    for (var i = expandedNodes.length - 1; i >= 0; i--) {
                        var node = expandedNodes[i];
                        if (treeNode.id != node.id && node.level == treeNode.level && node.level !== 0) {
                            menuTree.expandNode(node, false);
                        }
                    }*/
                    if (vm.singlePath) {
                        util.singlePath(vm, treeNode);
                    }
                    vm.$emit('before-expand', treeId, treeNode);
                };
                callback.onExpand = function (event, treeId, treeNode) {
                    vm.currentNode = treeNode;
                    if (vm.type !== 'menu') {
                        util.scroll();
                    }
                    vm.$emit('on-expand');
                };
                callback.onCollapse = function (event, treeId, treeNode) {
                    if (vm.type !== 'menu') {
                        util.scroll();
                    }
                    vm.$emit('on-expand');
                };
                callback.onClick = function (e, treeId, treeNode) {
                    if (vm.type === 'menu' && treeNode.isParent) {
                        util.expand(treeId, treeNode);
                    }
                    vm.$emit('on-click', e, treeId, treeNode);
                };
                if (vm.title) {
                    callback.onNodeCreated = function (event, treeId, treeNode) {
                        // $('#' + treeNode.tId + '_a').attr('data-placement', 'auto right').attr('data-container', 'body');
                        util.tooltip($('#' + treeNode.tId + '_a'), 'title', true, {
                            placement: 'right',
                            container: 'body'
                        });
                    };
                }
                if (this.check) {
                    var vm = this;
                    callback.onCheck = function(event, treeId, treeNode) {
                        if(vm.check =='radio'){
                            var checked = [];
                            var checkedKey = [];
                            var checkedNodes = vm.$tree.getCheckedNodes();
                            if (checkedNodes && checkedNodes.length > 0) {
                                for (var i=0; i<checkedNodes.length; i++) {
                                    checkedKey.push(checkedNodes[i].id);
                                    if (checkedNodes[i].entity) {
                                        checked = checked.concat(checkedNodes[i].entity);
                                    }
                                }
                            }
                            if (vm.name) {
                                if (vm.type === 'select') {
                                    util.clone(vm.entity,checked[0],true);
                                } else {
                                    vm.entity[vm.name] = checked[0];
                                }
                            }
                            if (util.type(vm.show) === 'function') {
                                if (vm.type === 'select') {
                                    var showText = '';
                                    for (var i=0; i<checked.length; i++) {
                                        var result = vm.show.call(null, checked[i]);
                                        if (result) {
                                            if (showText) {
                                                showText += ',';
                                            }
                                            showText += result;
                                        }
                                    }
                                    vm.$set(vm.entity, vm.resultName, showText);
                                }
                            }
                        }
                        else{
                            var checked = [];
                            var checkedKey = [];
                            var checkedNodes = vm.$tree.getCheckedNodes();
                            if (checkedNodes && checkedNodes.length > 0) {
                                for (var i=0; i<checkedNodes.length; i++) {
                                    checkedKey.push(checkedNodes[i].id);
                                    if (checkedNodes[i].entity) {
                                        checked = checked.concat(checkedNodes[i].entity);
                                    }
                                }
                            }
                            if (vm.name) {
                                if (vm.type === 'select') {
                                    vm.entity[vm.name] = checkedKey;
                                } else {
                                    vm.entity[vm.name] = checked;
                                }
                            }
                            if (util.type(vm.show) === 'function') {
                                if (vm.type === 'select') {
                                    var showText = '';
                                    for (var i=0; i<checked.length; i++) {
                                        var result = vm.show.call(null, checked[i]);
                                        if (result) {
                                            if (showText) {
                                                showText += ',';
                                            }
                                            showText += result;
                                        }
                                    }
                                    vm.$set(vm.entity, vm.resultName, showText);
                                }
                            }
                        };
                    }

                }
                this.config = {
                    view: {
                        showIcon: this.icon,
                        showLine: this.line,
                        showTitle: this.title,
                        selectedMulti: !this.check,
                        dblClickExpand: false
                    },
                    check: {
                        enable: util.isTrue(this.check),
                        chkStyle: (this.check === false || this.check === true) ? 'checkbox' : this.check,
                        chkboxType: this.checkType ? {Y: this.checkType, N: this.checkType} : {Y: 'ps', N: 'ps'},
                        radioType: this.check === 'radio' ? 'all' : 'level'
                    },
                    data: {
                        simpleData: {
                            enable: true
                        }
                    },
                    callback: callback
                };
                var data = vm.data;
                /*if (vm.appCode) {
                    data.appCode = vm.appCode;
                }*/
                if (vm.entityName) {
                    data.entityName = vm.entityName;
                }
                if (!util.isEmpty(vm.initParamList)) {
                    data.paramList = vm.initParamList;
                }
                if (!util.isEmpty(vm.initEntity)) {
                    data = util.mix(data, vm.initEntity);
                }
                if (vm.orderList && vm.orderList.length > 0) {
                    data.orderList = vm.orderList;
                }
                if (util.type(vm.url) === 'array') {
                    util.error('后续版本url将不再支持数组，请使用items属性');
                    vm.items = vm.url;
                }
                if (util.type(vm.items) === 'array') {
                    vm.dataInit(vm.items);
                } else {
                    if (util.endsWith(vm.url, ".json")) {
                        $.getJSON(vm.url, function(res) {
                            vm.dataInit(res);
                        });
                        // Const.url.tree.query
                    } else {
                        util.request({
                            type: 'post',
                            url: vm.url || Const.url.tree.query,
                            //appCode: vm.appCode,
                            data: data,
                            success: function(res) {
                                if (util.data(res)) {
                                    vm.dataInit(util.data(res));
                                }
                            }
                        });
                    }
                }
            },
            dataInit: function(data) {
                if (data) {
                    var nodes = [];
                    if (this.rootNode) {
                        nodes.push(this.rootNode);
                    }
                    var expanded = false;
                    for (var i=0; i<data.length; i++) {
                        var node = this.pack.call(null, data[i], i);
                        if (this.expandFirst && (!node.pId || node.pId === '00') && !expanded) {
                            expanded = true;
                            node.open = true;
                        }
                        nodes.push(node);
                    }
                    if (!util.isEmpty(this.entity[this.name])) {
                        var showText = '';
                        for (var i=0; i<nodes.length; i++) {
                            if (this.entity[this.name].indexOf(nodes[i].id) >= 0 && nodes[i].entity) {
                                var result = this.show.call(null, nodes[i].entity);
                                if (result) {
                                    if (showText) {
                                        showText += ',';
                                    }
                                    showText += result;
                                }
                                nodes[i].checked = true;
                            }
                        }
                        this.$set(this.entity, this.resultName, showText);
                    }
                    this.$tree = $.fn.zTree.init($('.ztree', this.$el), this.config, nodes);

                    /*if (this.title) {
                        util.tooltip($(this.$el), undefined, 'auto right', 'body');
                        util.tooltip($(this.$el), 'data-title', 'auto right', 'body');
                    }*/

                    this.$emit('on-init', this);
                    /*if (util.type(this.onInit) === 'function') {
                        this.onInit.call(null, this);
                    }*/
                }
            },

            execute: function (param) {
                if (param.initParamList) {
                    this.initParamList = param.initParamList;
                }
                if (param.initEntity !== undefined) {
                    this.initEntity = param.initEntity;
                }
                if (this.$tree && param.expandFirst) {
                    this.$tree.expandNode(this.$tree.getNodeByParam('level', 0), true);
                } else {
                    this.init();
                }
            }
        },
        /****** 模板定义 ******/
        template: util.template('bv-tree')
    });

    vue.component('bv-position', {
        props: {
            // 您当前位置：
            head: {
                default: ''
            },
            titles: {
                default: function () {
                    return [];
                }
            }
        },
        template: util.template('bv-position')
    });

    vue.component('bv-tabs-pane', {
        props: {
            type: {
                default: 'inline'
            },
            index: {
                default: 0
            },
            active: {
                default: false
            },
            // 无实际意义，用于监听变动
            // timestamp: '',
            target: {
                default: ''
            },
            prop: {
                default: ''
            }
        },
        /*watch: {
            timestamp: function (val, oldVal) {
                this.refresh();
            }
        },*/
        methods: {
            refresh: function () {
                Const.vm.current = this;
                var vm = this;
                util.replace($(vm.$el), vm.target, function(response, status, xhr) {
                    util.scroll($(vm.$el));
                    // 设置属性
                    if (vm.prop) {
                        $(vm.$el).data('tab-prop', vm.prop);
                    }
                });
            }
        },
        mounted: function () {
            if (this.type !== 'inline') {
                this.refresh();
            }
            /// util.cache(this);
        },
        beforeDestroy: function () {
            // 取消scroll绑定
            util.scroll(this.$el, 'destroy');
            util.destroy(this);
        },
        template: util.template('bv-tabs-pane')
    });
    vue.component('bv-tabs', {
        props: {
            isMargin:{
                default:false
            },
            clazz: {
                default: ''
            },
            // 属性定义
            attr: {
                default: function () {
                    return {};
                }
            },
            // 可设置为menu-表示系统菜单，inline-标签页body都在当前页面中
            type: {
                default: 'inline'
            },
            // 支持vertical-垂直显示
            layout: {
                default: 'default'
            },
            // 标签页不可关闭
            sticky: {
                default: false
            },
            // 是否固定标题
            fixed: {
                default: true
            },
            // 切换方式 click hover
            trigger: {
                default: 'click'
            },
            //是否分页
            pagination: {
                default: true
            },
            // 初始标签页数组{id:,text:标题}
            tabs: {
                default: function () {
                    return [];
                }
            },
            // 返回按钮
            returnUrl: {
                default: ''
            }
        },
        data: function() {
            return {
                // 是否翻页
                overflow: false,
                // 当前页
                currentIndex: -1,
                // changed: false,
                // 标签页左边距，用于翻页
                marginLeft: 0
            };
        },
        watch: {
            currentIndex: function (val, oldVal) {
                if (this.currentIndex >= 0) {
                    this.$emit('on-active', this, this.currentIndex);
                    this.$nextTick(function () {
                        util.resize();
                    });

                    this.currentSelected = this.tabs[this.currentIndex];
                    this.calc();

                    if (this.type === 'menu') {
                        if (Const.global.menuTree && Const.global.menuTree.$tree) {
                            // tab用，内部用
                            /*positionTitles: function() {
                                var titles = $('.bv-content[data-active="true"]').data('tab-prop') && $('.bv-content[data-active="true"]').data('tab-prop').titles;
                                if (this.type(titles) === 'string') {
                                    return new Array({
                                        text: titles
                                    });
                                }
                                return titles;
                            },*/
                            var titles = this.currentSelected.prop.titles; ///util.positionTitles();
                            if (util.type(titles) === 'string') {
                                titles = new Array({
                                    text: titles
                                });
                            }
                            if (titles) {
                                Const.vm.root.position.titles = titles;
                            }
                            // 菜单
                            var treeNode = Const.global.menuTree.$tree.getNodeByParam('id', this.currentSelected[Const.menu.id]);
                            util.selectNode(treeNode);
                            // 调试信息
                            util.debug('菜单地址:', treeNode && treeNode.entity && treeNode.entity[Const.menu.url]);
                        }
                    }
                    else if (this.type === 'inline') {
                        $('.tab-content .tab-pane.active', this.$el).removeClass('active');
                        $('.tab-content .tab-pane[id=' + (this.currentSelected.id || this.currentSelected.target) + ']', this.$el).addClass('active');
                        util.scroll($('.tab-content', this.$el));
                    }
                }
            }
        },
        /*beforeMount: function () {
            if (this.tabs.length === 0) {
                this.currentIndex = -1;
            }
        },*/
        beforeCreate: function () {
            // 容器宽度
            this.width = 0;
            // 去掉左右翻页后的容器宽度
            this.containerWidth = 0;
            // 标签页标题宽度和
            this.navWidth = 0;
            // 当前标签页左侧标题宽度和
            this.leftTabsWidth = 0;
            // 最后一个标签页标题宽度
            this.lastTabsWidth = 0;
            // 当前选中标签页配置
            this.currentSelected = null;
            // 当前选中标签页元素
            this.$currentElement = null;

            this.eventId = util.guid();
        },
        mounted: function() {
            if (this.tabs.length >= 0) {
                this.currentIndex = 0;
            }
            if (this.type !== 'menu') {
                // 标签页嵌套
                $(this.$el).closest('.bv-tabs-pane').addClass('tabs-cascade');
                util.scroll($(this.$el).closest('.bv-tabs-pane'), 'destroy');
            }

            this.clazz += ' bv-tabs-' + this.type;
            if (this.fixed) {
                this.clazz += ' bv-tabs-fixed';
            }

            this.$emit('on-init', this);
            this.width = $(this.$el).outerWidth(true);

            if (this.type !== 'inline') {
                /// this.refresh();
                // util.loadTab(this, this.tabs[0]);
                this.calc();
                /*this.$watch('changed', function() {
                    this.calc();
                });*/
            } else {
                this.$nextTick(function () {
                    if (this.currentIndex >= 0 && this.tabs && this.currentIndex < this.tabs.length) {
                        this._trigger(undefined, this.currentIndex);
                        // util.scroll($('.tab-content', this.$el));
                    }
                });
               /* if (this.currentIndex >= 0 && this.tabs && this.tabs.length > this.currentIndex) {
                    this.currentSelected = this.tabs[this.currentIndex];
                    $('.tab-content .tab-pane.active', this.$el).removeClass('active');
                    $('.tab-content .tab-pane[id=' + (this.currentSelected.id || this.currentSelected.target) + ']', this.$el).addClass('active');
                    util.scroll($('.tab-content', this.$el));
                }*/
            }

            var vm = this;
            $(window).on('resize.' + vm.eventId, function () {
                vm.width = $(vm.$el).outerWidth(true);
                vm.calc();
            });
        },
        beforeDestroy: function () {
            $(window).off('resize.' + this.eventId);
        },
        methods: {
            // 计算标签页宽度
            // 调用来源：初始加载，切换标签页，窗口大小改变
            calc: function() {
                if (!util.isTrue(this.pagination)) {
                    return;
                }
                var vm = this;
                vm.$nextTick(function () {
                    // 当前选中标签页对应li
                    vm.$currentElement = $('[data-target=' + vm.tabs[vm.currentIndex].id + ']', vm.$el);
                    vm.navWidth = 0;
                    vm.leftTabsWidth = 0;
                    for (var i=0; i<vm.tabs.length; i++) {
                        var w = $('[data-target=' + vm.tabs[i].id + ']', vm.$el).outerWidth(true);
                        vm.navWidth += w;
                        if (i < vm.currentIndex) {
                            vm.leftTabsWidth += w;
                        }
                        if (i === vm.tabs.length - 1) {
                            vm.lastTabsWidth = w;
                        }
                    }
                    vm.overflow = vm.navWidth > vm.width;

                    if (vm.overflow) {
                        vm.containerWidth = vm.width - 60;

                        var currentElementLeft = vm.$currentElement.position().left;
                        var currentElementWidth = util.width(vm.$currentElement);
                        var diff = vm.leftTabsWidth + currentElementWidth - vm.containerWidth;

                        if (diff > -1 * vm.marginLeft) {
                            vm.marginLeft = -1 * diff + 30;
                        } else if (currentElementLeft < 0) {
                            vm.marginLeft = -1 * vm.leftTabsWidth + 30;
                        } else if (-1 * vm.marginLeft + 30 > this.navWidth - this.containerWidth + this.lastTabsWidth) {
                            vm.marginLeft = this.containerWidth - this.navWidth + 30;
                        }

                    } else {
                        vm.marginLeft = 0;
                        vm.containerWidth = vm.width;
                    }
                });
            },
            prev: function() {
                var marginLeft = this.marginLeft + this.containerWidth;
                if (marginLeft > 0) {
                    marginLeft = 30;
                }
                this.marginLeft = marginLeft;
                /*if (this.marginLeft > this.containerWidth - 80) {
                    this.marginLeft -= this.containerWidth - 80;
                } else {
                    this.marginLeft = 0;
                }*/
            },
            next: function() {
                var marginLeft = this.marginLeft - this.containerWidth;
                if (-1 * marginLeft > this.navWidth - this.containerWidth + this.lastTabsWidth) {
                    marginLeft = this.containerWidth - this.navWidth + 30;
                }
                this.marginLeft = marginLeft;
                /*if (this.marginLeft + this.containerWidth * 2 - 80 >= this.navWidth) {
                    this.marginLeft = this.navWidth - this.containerWidth;
                } else {
                    this.marginLeft += this.containerWidth - 80;
                }*/
            },
            _trigger: function(event, index) {
                if (!event || index !== this.currentIndex) {
                    if (this.type === 'inline') {
                        for (var i=0; i<this.$children.length; i++) {
                            if (this.$children[i].index >= 0) {
                                if (this.$children[i].index === index) {
                                    this.$children[i].active = true;
                                } else {
                                    this.$children[i].active = false;
                                }
                            } else {
                                if (i === index) {
                                    this.$children[i].active = true;
                                } else {
                                    this.$children[i].active = false;
                                }
                            }
                        }
                    }
                    this.currentIndex = index;
                    // 触发resize重设table宽度
                    var click = this.tabs[this.currentIndex].click;
                    if (click && click === 'refresh') {
                        this.refresh();
                        // util.loadTab(this.tabs, this.tabs[this.currentIndex]);
                    }
                    this.$nextTick(function () {
                        // util.resize();
                        util.scroll($('.tab-content', this.$el));
                        //util.scroll($(this.tabs[this.currentIndex].$el));
                    });
                }
            },
            refresh: function (index) {
                if (util.type(index) === 'undefined') {
                    this.$children[this.currentIndex].refresh();
                    // this.tabs[this.currentIndex].timestamp = new Date().getTime();
                } else {
                    this.$children[index].refresh();
                    // this.tabs[index].timestamp = new Date().getTime();
                }
                /// util.resize();
            },
            remove: function ($event, index) {
                // var $remove = $('#' + this.tabs[index].id, this.$el);
                // util.remove($remove);
                var child = this.$children[index];
                this.tabs.splice(index, 1);

                if (this.currentIndex >= index) {
                    this.currentIndex--;
                }
                this.calc();
                /// this.currentSelected = this.tabs[this.currentIndex];
                /// this.calc();
            },
            removeAll: function () {
                /// this.currentSelected = this.tabs[this.currentIndex];
                for (var i=this.tabs.length-1; i>=0; i--) {
                    var tab = this.tabs[i];
                    if (!this.sticky && !tab.sticky) {
                        /*var $remove = $('#' + tab.id, this.$element);
                        $('*', $remove).remove()
                        $remove.remove();*/
                        this.tabs.splice(i, 1);
                    }
                }
                this.currentIndex = 0;
                this.calc();
            },
            doReturn: function() {
                if (this.returnUrl) {
                    util.redirect(this.returnUrl, 'body');
                }
            },

            execute: function (param) {
                if (param.currentIndex !== undefined) {
                    this.currentIndex = param.currentIndex;
                }
            }
        },
        /****** 模板定义 ******/
        template: util.template('bv-tabs')
    });

    vue.component('bv-wizard', {
        props: {
            type: {
                default: ''
            },
            steps: {
                default: function () {
                    return [];
                }
            },
            operates: {
                default: function () {
                    return [];
                }
            }
        },
        data: function () {
            return {
                currentIndex: 0
            }
        },
        mounted: function () {
            this.$nextTick(function () {
                if (this.currentIndex < this.steps.length) {
                    this.active(undefined, this.currentIndex);
                    /*for (var i=0; i<this.$children.length; i++) {
                        if (this.$children[i].index === this.currentIndex) {
                            this.$children[i].active = true;
                            break;
                        }
                    }*/
                }
                util.tooltip($(this.$el), 'data-title', undefined, {
                    placement: 'bottom',
                    container: 'body'
                });
            });
        },
        methods: {
            active: function (event, index, enable) {
                if (index === 'prev') {
                    if (this.currentIndex > 0) {
                        index = this.currentIndex - 1;
                    } else {
                        index = 0;
                    }
                } else if (index === 'next') {
                    if (this.currentIndex < this.steps.length - 1) {
                        index = this.currentIndex + 1;
                    } else {
                        index = this.steps.length + 1;
                    }
                }
                //  && !this.steps[index].disabled
                if (index < this.steps.length && (!event || this.currentIndex !== index)) {
                    this.currentIndex = index;
                    //var foundIndex = 0;
                    //var resetTitle = false;
                    if (this.type && this.type === 'redirect') {
                        if (enable && this.steps[index].disabled) {
                            this.steps[index].disabled = false;
                        }
                    } else {
                        for (var i=0; i<this.$children.length; i++) {
                            if (this.$children[i].index === index) {
                                this.$children[i].active = true;
                                if (enable && this.steps[i].disabled) {
                                    /*resetTitle = (this.steps[i].disabled !== true);
                                    if (resetTitle) {
                                        util.tooltip($('[data-index=' + i + ']', this.$el), undefined, true);
                                    }*/
                                    this.steps[i].disabled = false;
                                    //foundIndex = i;
                                    /*if (resetTitle) {
                                        this.$nextTick(function () {
                                            util.tooltip($('[data-index=' + foundIndex + ']', this.$el), undefined, true, {
                                                placement: 'bottom',
                                                container: 'body'
                                            });
                                        });
                                    }*/
                                }
                            } else {
                                this.$children[i].active = false;
                            }
                        }
                    }
                }
            },
            enable: function (index) {
                if (index < this.steps.length) {
                    for (var i=0; i<this.$children.length; i++) {
                        if (this.$children[i].index === index) {
                            this.$children[i].disabled = false;
                            break;
                        }
                    }
                }
            },
            disable: function (index) {
                if (index < this.steps.length) {
                    for (var i=0; i<this.$children.length; i++) {
                        if (this.$children[i].index === index) {
                            this.$children[i].disabled = true;
                            break;
                        }
                    }
                }
            },
            checkOperateVisible: function (operate) {
                if (operate && operate.preset) {
                    if (operate.preset === 'prev') {
                        return this.currentIndex > 0;
                    } else if (operate.preset === 'next') {
                        return this.currentIndex < this.steps.length - 1;
                    } else if (operate.preset === 'submit') {
                        return this.currentIndex === this.steps.length - 1;
                    }
                }
            },
            _click: function (event, operate) {
                if (operate && operate.preset) {
                    var $childVm;
                    for (var i=0; i<this.$children.length; i++) {
                        if (this.$children[i].index === this.currentIndex) {
                            $childVm = this.$children[i];
                            break;
                        }
                    }
                    if (operate.validate) {
                        if ($childVm && !util.validate($($childVm.$el))) {
                            return;
                        }
                    }
                    if (operate.preset === 'prev') {
                        if (operate.click && util.type(operate.click) === 'function') {
                            var vm = this;
                            operate.click.call(null, event, $childVm.entity, function () {
                                vm.active(event, 'prev', operate.enable);
                            });
                        } else {
                            this.active(event, 'prev', operate.enable);
                        }
                    } else if (operate.preset === 'next') {
                        if (operate.click && util.type(operate.click) === 'function') {
                            var vm = this;
                            operate.click.call(null, event, $childVm.entity, function () {
                                vm.active(event, 'next', operate.enable);
                            });
                        } else {
                            this.active(event, 'next', operate.enable);
                        }
                    } else if (operate.preset === 'submit') {
                        console.log($childVm)
                        if (operate.click && util.type(operate.click) === 'function') {
                            operate.click.call(null, event, $childVm.entity);
                        }
                    }
                }
            },

            execute: function (param) {
                if (param.direction) {
                    this.active(param.event, param.direction, param.enable);
                }
            }
        },
        /****** 模板定义 ******/
        template: util.template('bv-wizard')
    });
    vue.component('bv-wizard-content', {
        props: {
            entity: {
                default: function () {
                    return {};
                }
            },
            index: {
                default: ''
            },
            active: {
                default: false
            },
            disabled: {
                default: false
            }
        },
        /****** 模板定义 ******/
        template: util.template('bv-wizard-content')
    });

    vue.component('bv-modal', {
        props: {
            size: {
                default: ''
            },
            clazz: {
                default: ''
            },
            title: {
                default: ''
            },
            // 可以设置为div，此时内部不会自动创建form，需要在外层嵌套form
            container: {
                default: 'form'
            }
        },
        created: function () {
            if (this.size) {
                this.clazz = 'modal-' + this.size;
            }
        },
        mounted: function () {
            Const.vm.modal = this.$parent;
        },
        /****** 模板定义 ******/
        template: util.template('bv-modal')
    });

    // 不能绑定属性
    // 取值时调用util.call()
    vue.component('bv-editor', {
        props: {
            entity: {
                default: function () {
                    return {};
                }
            },

            id: {
                default: ''
            },
            name: {
                default: ''
            },
            clazz: {
                default: ''
            },
            width: {
                default: '100%'
            },
            height: {
                default: 460
            },
            attr: {
                default: function () {
                    return {};
                }
            },
            defaultValue: {
                default: ''
            },
            value: {
                default: ''
            },
            url: {
                default: Const.url.file.upload
            }
        },
        beforeCreate: function () {
            this.$editor = '';
        },
        created: function () {
            util.initDefault(this);
            util.initId(this);
        },
        mounted: function () {
            var vm = this;
            util.require('summernote', function () {
                vm.$editor = $('#container', vm.$el).summernote({
                    lang: 'zh-CN',
                    height: vm.height,
                    dialogsInBody: true,
                    // fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New'],
                    toolbar: [
                        ['style', ['style']],
                        ['font', ['bold', 'underline', 'clear']],
                        ['fontname', ['fontname']],
                        ['color', ['color']],
                        ['para', ['ul', 'ol', 'paragraph']],
                        ['table', ['table']],
                        ['insert', ['link', 'customUpload']],
                        ['view', ['fullscreen', 'codeview']]
                    ],
                    buttons: {
                        customUpload: function (context) {
                            // create button
                            var button = context.ui.button({
                                container: 'body',
                                className: 'upload',
                                // 按钮样式
                                contents: '<i class="iconfont icon-browse" /><input type="file" />',
                                tooltip: '上传图片',
                                click: function () {
                                    // 点出时触发的事件
                                },
                                callback: function($element){
                                    // 这里的回调函数会在加载页面的时候直接执行
                                    $($element).on('change', 'input[type=file]', function (event) {
                                        var files = event.target.files;
                                        for (var i=0; i<files.length; i++) {
                                            util.upload({
                                                url: util.mix(vm.url, {
                                                    name: files[0].name
                                                }),
                                                data: files[i],
                                                progress: function (event) {
                                                    if (event.loaded && event.total) {
                                                        ///vm.uploadProgress(index, file, true, Math.ceil(100 * event.loaded / event.total));
                                                    }
                                                },
                                                success: function (res) {
                                                    var data = util.data(res);
                                                    if (data && data.fileId) {
                                                        vm.$editor.summernote('insertNode', $('<img src="' + util.url(Const.url.file.download) + '/' + data.fileId + '" />')[0]);
                                                    }
                                                },
                                                error: function (xhr, status, err) {
                                                }
                                            });
                                        }
                                    });
                                }
                            });

                            // 按钮渲染
                            return button.render();
                        }
                    },
                    callbacks: {
                        onInit: function () {
                            // console.log('onInit');
                        },
                        /*onImageUpload: function (files) {
                            if (files && files.length === 1) {
                                util.upload({
                                    url: util.mix(vm.url, {
                                        name: files[0].name
                                    }),
                                    data: files[0],
                                    success: function (res) {
                                        var data = util.data(res);
                                        if (data && data.fileId) {
                                            vm.$editor.summernote('insertNode', $('<img src="' + util.url(Const.url.file.download) + '/' + data.fileId + '" />')[0]);
                                        }
                                    }
                                });
                            }
                        },*/
                        onChange: function(contents, $editor) {
                            vm.entity[vm.name] = contents;
                        }
                    }
                });
                if (vm.entity[vm.name]) {
                    vm.$editor.summernote('code', vm.entity[vm.name]);
                }
                /*$('textarea', vm.$el).on('summernote.init', function () {
                    console.log('inited')
                });
                $('textarea', vm.$el).on('summernote.image.upload', function () {
                    console.log('summernote.image.upload')
                });*/
            });
        },
        beforeDestroy: function () {
            if (this.$editor) {
                this.$editor.summernote('destroy');
                this.$editor = null;
            }
        },
        methods: {
            execute: function (param) {
                if (param.type) {
                    if (param.type === 'getContent') {
                        return $('#container', this.$el).summernote('code');
                    } else if (param.type === 'setContent') {
                        $('#container', this.$el).summernote('code', param.content);
                    }
                }
            }
        },
        /****** 模板定义 ******/
        template: util.template('bv-editor')
    });

    vue.component('bv-chart', {
        props: {
            clazz: {
                default: ''
            },
            width: {
                default: ''
            },
            height: {
                default: ''
            },
            // 支持bar等
            type: {
                default: ''
            },
            title: {
                default: null
            },
            // 是否在柱状图或饼图上显示数值
            text: {
                default: null
            },
            // 横轴
            x: {
                default: function () {
                    return {};
                }
            },
            // 纵轴
            y: {
                default: function () {
                    return {};
                }
            },
            // 大小
            z: {
                default: function () {
                    return {};
                }
            },
            // 辅助线(line时用)
            crossLine: {
                default: false
            },
            labels: {
                default: ''
            },
            datasets: {
                default: ''
            },
            min: {
                default: null
            },
            max: {
                default: null
            },
            click: {
                default: null
            }
        },
        data: function () {
            return {
                style: {}
            }
        },
        /*watch: {
            labels: {
                handler: function (val, oldVal) {
                    this.refresh();
                },
                deep: true
            },
            datasets: {
                handler: function (val, oldVal) {
                    this.refresh();
                },
                deep: true
            }
        },*/
        beforeCreate: function () {
            this.$chart = '';
        },
        created: function () {
            if (this.labels) {
                this.localLabels = util.clone(this.labels);
            }
            if (this.datasets) {
                this.localDatasets = util.clone(this.datasets);
            }
            this.eventId = util.guid();
        },
        mounted: function () {
            if (!this.width && $(this.$el).closest('.tab-content').length === 1) {
                this.$nextTick(function () {
                    this.initLayout();
                    this.refresh();
                });
            } else {
                this.initLayout();
                this.refresh();
            }
            // this.refresh();
            var vm = this;
            $(window).on('resize.' + vm.eventId, vm.$el, function () {
                if (util.isCurrent(vm.$el)) {
                    if (!vm.width) {
                        vue.set(vm.style, 'width', $(vm.$el).parent().width() + 'px');
                    }
                    if (!vm.height) {
                        vue.set(vm.style, 'height', $(vm.$el).parent().width() / 2 + 'px');
                    }
                    vm.$nextTick(function () {
                        if (vm.$chart) {
                            vm.$chart.resize({
                                width: null,
                                height: null
                            });
                        }
                    });
                }
            });
        },
        beforeDestroy: function () {
            $(window).off('resize.' + this.eventId, this.$el);
            if (this.$chart) {
                this.$chart.dispose();
                this.$chart = null;
            }
        },
        methods: {
            initLayout: function () {
                if (this.width) {
                    vue.set(this.style, 'width', this.width);
                } else {
                    vue.set(this.style, 'width', $(this.$el).parent().width() + 'px');
                }
                if (this.height) {
                    vue.set(this.style, 'height', this.height);
                } else {
                    vue.set(this.style, 'height', $(this.$el).parent().width() / 2 + 'px');
                }
            },
            refresh: function(labels, datasets) {
                var vm = this;
                if (!labels) {
                    labels = vm.localLabels;
                }
                if (!datasets) {
                    datasets = vm.localDatasets;
                }
                util.require('echarts', function (echarts) {
                    if (!vm.$chart) {
                        vm.$chart = echarts.init(vm.$el);
                    }
                    var options = {
                        tooltip: {},
                        legend: {},
                        color: Const.chart.colors
                    };
                    if (vm.title) {
                        options.title = {
                            text: vm.title
                        };
                    }
                    if (vm.type !== 'pie') {
                        options.grid = {
                            x: 20,
                            x2: 40,
                            y: 60,
                            y2: 30,
                            containLabel: true
                        };
                        options.xAxis = {
                            type: 'category'
                        };
                        if (vm.y && util.type(vm.y) === 'array') {
                            options.xAxis.nameGap = 40;
                        }
                        if (vm.x) {
                            if (util.type(vm.x) === 'string') {
                                options.xAxis.name = vm.x;
                            } else {
                                options.xAxis.name = vm.x.name;
                                if (vm.x.type) {
                                    options.xAxis.type = vm.x.type;
                                }
                                if (util.type(vm.x.line) !== 'undefined') {
                                    options.xAxis.splitLine = {
                                        show: vm.x.line
                                    };
                                }
                                if (vm.x.label) {
                                    /*
                                    label: {
                                        interval: 0,
                                        rotate: 40
                                    }
                                    */
                                    options.xAxis.axisLabel = vm.x.label;
                                }
                            }
                        }
                        if (vm.y) {
                            if (util.type(vm.y) === 'string') {
                                options.yAxis = {};
                                options.yAxis.name = vm.y;
                            } else if (util.type(vm.y) === 'object') {
                                options.yAxis = {};
                                options.yAxis.name = vm.y.name;
                                if (vm.y.type) {
                                    options.yAxis.type = vm.y.type;
                                }
                                if (vm.y.formatter) {
                                    options.yAxis.axisLabel = {
                                        formatter: vm.y.formatter
                                    };
                                }
                                if (util.type(vm.y.line) !== 'undefined') {
                                    options.yAxis.splitLine = {
                                        show: vm.y.line
                                    };
                                }
                            } else if (util.type(vm.y) === 'array') {
                                options.yAxis = [];
                                for (var i=0; i<vm.y.length; i++) {
                                    if (util.type(vm.y[i]) === 'string') {
                                        options.yAxis.push({
                                            name: vm.y[i]
                                        });
                                    } else {
                                        var y = {
                                            name: vm.y[i].name
                                        };
                                        if (vm.y[i].formatter) {
                                            y.axisLabel = {
                                                formatter: vm.y[i].formatter
                                            };
                                        }
                                        options.yAxis.push(y);
                                    }
                                }
                            }
                        } else {
                            options.yAxis = {};
                        }
                        if (vm.crossLine) {
                            options.tooltip.axisPointer = {
                                type: 'cross'
                            };
                        }
                        if (labels && labels.length > 0) {
                            options.xAxis.data = labels;
                        }
                    }

                    if (vm.type === 'line' || vm.type === 'mix') {
                        options.tooltip.trigger = 'axis';
                        if (vm.type === 'line') {
                            options.xAxis.boundaryGap = false;
                        }
                    } else if (vm.type === 'pie') {
                        options.tooltip.formatter = '{a} <br/>{b}: {c} ({d}%)';
                    } else if (vm.type === 'scatter') {
                        options.xAxis.splitLine = {
                            show: false
                        };
                        options.yAxis.splitLine = {
                            show: false
                        };
                        options.tooltip.formatter = function (obj) {
                            var value = obj.value;
                            var result = '';
                            if (obj.seriesName) {
                                result = obj.seriesName + '<br/>';
                            }
                            if (options.xAxis.name) {
                                result += options.xAxis.name + ':';
                            }
                            result += value[0] + ',';
                            if (options.yAxis.name) {
                                result += options.yAxis.name + ':';
                            }
                            result += value[1];
                            if (value.length > 2) {
                                result += ',';
                                if (vm.z) {
                                    result += (vm.z.name || vm.z) + ':';
                                }
                                result += value[2];
                            }
                            return result;
                        };
                    }
                    if (datasets && datasets.length > 0) {
                        for (var i=0; i<datasets.length; i++) {
                            if (!datasets[i].type) {
                                datasets[i].type = vm.type || 'line';
                            }

                            if (!datasets[i].name) {
                                if (datasets[i].label) {
                                    datasets[i].name = datasets[i].label;
                                    delete datasets[i].label;
                                } else {
                                    datasets[i].name = vm.title;
                                }
                            }
                            if (vm.text) {
                                datasets[i].label = {
                                    normal: {
                                        show: true,
                                        position: datasets[i].type === 'pie' ? 'outer' : 'top'
                                    }
                                };
                                vm.initDatasetsLabel(datasets[i]);
                            } else {
                                datasets[i].label = {
                                    normal: {}
                                }
                            }
                            if (datasets[i].y) {
                                datasets[i].yAxisIndex = datasets[i].y;
                                delete datasets[i].y;
                            }

                            if (datasets[i].type === 'line') {
                                if (datasets[i].area) {
                                    datasets[i].areaStyle = {
                                        normal: {}
                                    };
                                    delete datasets[i].area;
                                }
                                if (util.type(datasets[i].showSymbol) !== 'undefined') {
                                    // vm.datasets[i].showSymbol = vm.datasets[i].showSymbol;
                                    if (!datasets[i].showSymbol) {
                                        datasets[i].hoverAnimation = false;
                                    }
                                }
                            } else if (datasets[i].type === 'pie') {
                                if (labels && labels.length > 0) {
                                    var data = [];
                                    for (var j=0; j<datasets[i].data.length; j++) {
                                        data.push({
                                            name: labels[j],
                                            value: datasets[i].data[j]
                                        });
                                    }
                                    datasets[i].data = data;
                                }
                                if (datasets[i].inline) {
                                    datasets[i].label.normal.position = 'inner';
                                    datasets[i].labelLine = {
                                        normal: {
                                            show: false
                                        }
                                    };
                                }
                            } else if (datasets[i].type === 'scatter') {
                                if (datasets[i].bubble) {
                                    var min = 1;
                                    for (var j=0; j<datasets[i].data.length; j++) {
                                        if (datasets[i].data[j].length === 2) {
                                            datasets[i].data[j].push(1);
                                        }
                                        if (datasets[i].data[j][2] < min) {
                                            min = datasets[i].data[j][2];
                                        }
                                    }
                                    datasets[i].symbolSize = function (data) {
                                        return data[2] * (10 / min);
                                    };
                                    delete datasets[i].bubble;
                                }
                            }
                        }
                        options.series = datasets;
                    }
                    vm.$chart.setOption(options, true);
                    //修改饼状图点击事件
                    if (vm.click && util.type(vm.click) === 'function') {
                        vm.$chart.on('click', function (param) {
                            vm.click.call(null, param);
                        });
                    }
                });
                /*require([Const.init.baseResourcePath + '/plugin/chart/Chart.min.js'], function (Chart) {
                    if (vm.datasets && vm.datasets.length > 0) {
                        for (var i=0; i<vm.datasets.length; i++) {
                            var type = vm.datasets[i].type || vm.type;
                            if (type === 'line') {
                                if (!vm.datasets[i].borderColor) {
                                    vm.datasets[i].borderColor = Const.chart.backgroundColors[i];
                                }
                                if (util.type(vm.datasets[i].fill) === 'undefined') {
                                    vm.datasets[i].fill = false;
                                }
                            } else if (type === 'pie' || type === 'doughnut') {
                                if (!vm.datasets[i].backgroundColor) {
                                    vm.datasets[i].backgroundColor = Const.chart.backgroundColors;
                                }
                            } else  {
                                if (!vm.datasets[i].backgroundColor) {
                                    vm.datasets[i].backgroundColor = Const.chart.backgroundColors[i];   // Chart.helpers.color(Const.chart.backgroundColors[i]).alpha(0.5).rgbString();
                                }
                            }
                        }
                    }
                    if (!vm.$chart) {
                        var ctx = $('canvas', vm.$el)[0];
                        var options = {};
                        if (vm.type != 'pie') {
                            var ticks = {};
                            if (vm.min !== null) {
                                ticks.min = vm.min;
                            }
                            if (vm.max !== null) {
                                ticks.max = vm.max;
                            }
                            options = {
                                scales: {
                                    xAxes: [],
                                    yAxes: [{
                                        ticks: ticks
                                    }]
                                }
                            };
                        }
                        if (vm.title) {
                            options.title = {
                                display: true,
                                fontSize: 18,
                                text: vm.title
                            };
                        }
                        if (!options.legend) {
                            options.legend = {};
                        }
                        if (vm.type === 'line') {
                            if (!options.tooltips) {
                                options.tooltips = {};
                            }
                            options.tooltips.mode = 'index';
                        }
                        // options.tooltips.intersect = false;
                        vm.$chart = new Chart(ctx, {
                            type: vm.type,
                            data: {
                                labels: vm.labels,
                                datasets: vm.datasets
                            },
                            options: options
                        });
                    } else {
                        // vm.$chart.type = vm.type;
                        vm.$chart.data.labels = vm.labels;
                        vm.$chart.data.datasets = vm.datasets;
                    }
                    vm.$chart.update();
                    if (type === 'init') {
                        util.scroll();
                    }
                });*/
            },

            initDatasetsLabel: function (dataset) {
                if (this.text) {
                    if (util.type(this.text) === 'boolean') {
                        if (dataset.type === 'pie') {
                            dataset.itemStyle = {
                                normal : {
                                    label : {
                                        formatter : function (params){
                                            return params.name + '：' + params.value + '（' + params.percent + '%）'
                                        },
                                        textStyle: {
                                            baseline : 'top'
                                        }
                                    }
                                }
                            };
                        }
                    } else if (util.type(this.text) === 'object') {
                        dataset.itemStyle = {
                            normal: {
                                label: {}
                            }
                        };
                        if (this.text.formatter) {
                            dataset.itemStyle.normal.label.formatter = function (params){
                                return params.name + '：' + params.value + '（' + params.percent + '%）'
                            };
                        } else if (dataset.type === 'pie') {
                            dataset.itemStyle.normal.label.formatter = this.text.formatter;
                        }
                        if (this.text.textStyle) {
                            dataset.itemStyle.normal.label.textStyle = this.text.textStyle;
                        } else if (dataset.type === 'pie') {
                            dataset.itemStyle.normal.label.textStyle = {
                                baseline : 'top'
                            };
                        }
                        if (this.text.fontSize) {
                            dataset.itemStyle.normal.label.fontSize = this.text.fontSize;
                        }
                    } else if (util.type(this.text) === 'function') {
                        var vm = this;
                        dataset.itemStyle = {
                            normal: {
                                label: {
                                    formatter: function (params) {
                                        return vm.text && vm.text.call(null, params);
                                    }
                                }
                            }
                        }
                    }
                }
            },

            execute: function (param) {
                if (param.get) {
                    // 获取数据
                    if (param.get === 'labels') {
                        return this.localLabels;
                    } else if (param.get === 'datasets') {
                        return this.localDatasets;
                    } else if (param.get === 'both') {
                        return {
                            labels: this.localLabels,
                            datasets: this.localDatasets
                        };
                    }
                }
                if (param.type) {
                    if (this.type !== param.type) {
                        this.type = param.type;
                        this.$chart = null;
                    }
                }
                if (param.labels) {
                    this.localLabels = param.labels;
                }
                if (param.datasets) {
                    this.localDatasets = param.datasets;
                }
                this.refresh(param.labels, param.datasets);
            }
        },
        /****** 模板定义 ******/
        template: util.template('bv-chart')
    });
    /**
     * 分页显示组件
     *
     * */
    vue.component('bv-pager', {
        props: {
            type: {//定义显示样式，分为两种情况：‘more’和‘normal’
                default: 'normal'
            },
            currentPage: {//当前页
                default: ''
            },
            totalPage: {//总页数
                default: ''
            },
            rowCount: {//共计条目数
                default: ''
            },
            limit: {//每页多少条目数
                default: ''
            },
            pageShow: {
                default: ''
            }
        },
        methods: {
            /*
            设置每页条目数
             */
            setLimit: function(limit) {
                this.$emit('on-limit', limit);
            },
            /*
                设置当前显示页
             */
            setPage: function(page) {
                this.$emit('on-page', page);
            }
        },
        template: util.template('bv-pager')
    });
    /**
     * normal 分页样式
     */
    vue.component('bv-pager-normal', {
        props: {
            currentPage: {//当前页
                default: ''
            },
            totalPage: {//总页数
                default: ''
            },
            rowCount: {//总条目数
                default: ''
            },
            limit: {//每页条目数
                default: ''
            }
        },
        methods: {
            /**
             * 返回将要跳转的页码数字；
             * @param page：点击的按钮（如：下一页，上一页，最后一页，首页，及数字等）
             * @param event：暂没用
             */
            jumpTo: function(page, event) {
                /*if (event) {
                    $(event.target).tooltip('hide');
                }*/
                var realPage;//记录当前是第几页；
                switch (page) {
                    case 'first':
                        realPage = 1;
                        break;
                    case 'last':
                        realPage = this.totalPage;
                        break;
                    case 'next':
                        realPage = this.currentPage + 1;
                        if (this.totalPage >= 0 && realPage > this.totalPage) {
                            realPage = this.totalPage;
                        }
                        break;
                    case 'prev':
                        realPage = this.currentPage - 1;
                        if (realPage < 1) {
                            realPage = 1;
                        }
                        break;
                    default:
                        realPage = page;
                        break;
                }
                this.$emit('on-page', realPage);
            },
            /**
             * 监听回车键，按回车键事件回调方法；设置每页多少条目
             * @param event
             */
            pressOnLimit: function(event) {
                if (event.which === 13) {//监听回车键，
                    event.preventDefault();
                    $(event.target).tooltip('hide');
                    this.setLimit(event);
                }
            },
            /**
             * 设置每页显示多少条数据方法
             * @param event
             */
            setLimit: function(event) {
                var $target = $('.bv-page-limit input', this.$el);
                var limit = $target.val();
                if (!util.check(limit, /^\+?[1-9][0-9]*$/)) {
                    limit = 10;
                    $target.val(limit);
                }
                if (util.toNumber(limit) > 500) {
                    limit = 500;
                } else {
                    limit = util.toNumber(limit);
                }
                this.$emit('on-limit', limit);
            },
            /**
             * 监听回车键，按回车键事件回调方法；设置当前页
             * @param event
             */
            pressOnPage: function(event) {
                if (event.which === 13) {
                    event.preventDefault();
                    $(event.target).tooltip('hide');
                    this.setPage(event);
                }
            },
            /**
             * 设置当前页方法
             * @param event
             */
            setPage: function(event) {
                var $target = $('.bv-page-current input', this.$el);
                var page = $target.val();
                if (!util.check(page, /^\+?[1-9][0-9]*$/)) {
                    page = 1;
                    $target.val(page);
                }
                page = util.toNumber(page);
                if (page > this.totalPage) {
                    page = this.totalPage;
                    $target.val(page);
                }
                this.$emit('on-page', page);
            }
        },
        template: util.template('bv-pager-normal')
    });
    /**
     * more 分页样式
     */
    vue.component('bv-pager-more', {
        props: {
            currentPage: {//当前页
                default: ''
            },
            totalPage: {//总页数
                default: ''
            },
            rowCount: {//总条目数
                default: ''
            },
            pageShow: {// 显示页码数
                default: ''
            },
            limit: {//每页多少条
                default: ''
            }
        },
        data:function(){
            return{
                pageShowCache:5
            }
        },
        beforeMount:function(){
            if(this.pageShow<=5){
                 this.pageShowCache=5;
             }else{
                this.pageShowCache=this.pageShow;
            }
        },
        computed: {
            pageStartComputed: function () {
                if (this.totalPage > this.pageShowCache && this.currentPage > this.prevShowComputed + 2) {
                    /*if (this.currentPage === this.totalPage) {
                        return this.currentPage - 5;
                    } else if (this.currentPage === this.totalPage - 1) {
                        return this.currentPage - 4;
                    }*/
                    if (this.currentPage >= this.totalPage - this.nextShowComputed) {
                        return this.totalPage - this.pageShowCache + 1;
                    }
                    return this.currentPage - this.prevShowComputed;
                }
                else{
                    return 2;
                }
            },
            pageShowComputed: function () {
                if (this.totalPage >= 2 && this.totalPage <= this.pageShowCache) {
                    return this.totalPage - 2;
                }
                var show = Math.min(this.totalPage, this.pageShowCache);
                if (show > 2) {
                    if (!this.prevPointComputed || !this.nextPointComputed) {
                        return show - 1;
                    }
                    return show - 2;
                }
                return 0;
            },

            /**
             *当前页前面显示数量
             */
            prevShowComputed: function () {
                if (this.pageShowCache % 2 === 0) {
                    return (this.pageShowCache - 2) / 2 - 1;
                }
                return (this.pageShowCache - 3) / 2;
            },
            /**
             * 当前页后面显示数量
              */
            nextShowComputed: function () {
                return this.pageShowCache - 2 - this.prevShowComputed;
            },
            /**
             * 计算判断是否显示前面“...”
             * @returns {boolean}
             */
            prevPointComputed: function () {
                return this.totalPage > this.pageShowCache && this.currentPage > this.prevShowComputed + 2;
            },
            /**
             * 计算判断是否显示后面“...”
             * @returns {boolean}
             */
            nextPointComputed: function () {
                return this.totalPage > this.pageShowCache && this.currentPage < this.totalPage - this.nextShowComputed - 1;
            }
        },
        methods: {
            jumpTo: function(page, event) {
                /*if (event) {
                    $(event.target).tooltip('hide');
                }*/
                var realPage;
                switch (page) {
                    case 'first':
                        realPage = 1;
                        break;
                    case 'last':
                        realPage = this.totalPage;
                        break;
                    case 'next':
                        realPage = this.currentPage + 1;
                        if (this.totalPage >= 0 && realPage > this.totalPage) {
                            realPage = this.totalPage;
                        }
                        break;
                    case 'prev':
                        realPage = this.currentPage - 1;
                        if (realPage < 1) {
                            realPage = 1;
                        }
                        break;
                    default:
                        realPage = page;
                        break;
                }
                this.$emit('on-page', realPage);
            }
        },
        template: util.template('bv-pager-more')
    });

    vue.component('bv-grant', {
        props: {
            entity: {
                default: function () {
                    return {};
                }
            },

            name: {
                default: ''
            },

            left: {
                default: ''
            },
            right: {
                default: ''
            },
            code: {
                default: ''
            },
            desc: {
                default: ''
            },
            label: {
                default: ''
            },
            /// id: '',
            options: {
                default: function () {
                    return [];
                }
            }
        },
        beforeCreate: function () {
            this.stopWatch = false;
        },
        created: function () {
            util.initDefault(this);
            if (!this.entity[this.left]) {
                this.$set(this.entity, this.left, null);
                // this.entity[this.left] = null;
            }
            if (!this.entity[this.right]) {
                this.$set(this.entity, this.right, null);
                // this.entity[this.right] = null;
            }
        },
        mounted: function() {
            this.$watch('entity.' + this.left, function () {
                if (!this.stopWatch) {
                    this.init(function () {
                        var $m = $('#' + this.left, this.$el).data('crlcu.multiselect');
                        var v = this.entity[this.left];
                        this.initOptions($m.$left, v);
                        this.options = this.options.concat(util.clone(v));
                    });
                }
            });
            this.$watch('entity.' + this.right, function () {
                if (!this.stopWatch) {
                    this.init(function () {
                        var $m = $('#' + this.left, this.$el).data('crlcu.multiselect');
                        var v = this.entity[this.right];
                        this.initOptions($m.$right, v);
                        this.options = this.options.concat(util.clone(v));
                    });
                }
            });
        },
        methods: {
            init: function (callback) {
                var vm = this;
                util.require('multiselect', function () {
                    if (!$('#' + vm.left, vm.$el).data('crlcu.multiselect')) {
                        $('#' + vm.left, vm.$el).multiselect({
                            sort: false,
                            afterMoveToRight: function (left, right, options) {
                                vm.setResult(left, right, options);
                            },
                            afterMoveToLeft: function (left, right, options) {
                                vm.setResult(left, right, options);
                            }
                        });
                    }
                    if (util.type(callback) === 'function') {
                        callback.call(vm);
                    }
                });
            },
            initOptions: function($element, options) {
                $('optgroup', $element).remove();
                $('option', $element).remove();
                if (options) {
                    var groups = [];
                    var seprators = [];

                    if (this.label) {
                        var currentGroup = '';
                        var currentSub = [];
                        for (var i=0; i<options.length; i++) {
                            var option = options[i];
                            if (option[this.label]) {
                                if (currentGroup && currentGroup !== option[this.label]) {
                                    groups.push({
                                        label: currentGroup,
                                        options: util.clone(currentSub)
                                    });
                                    currentSub = [];
                                }
                                currentGroup = option[this.label];
                                currentSub.push(option);

                                if (i === options.length - 1) {
                                    groups.push({
                                        label: option[this.label],
                                        options: util.clone(currentSub)
                                    });
                                    currentGroup = '';
                                    currentSub = [];
                                }
                            } else {
                                seprators.push(option);
                            }
                        }
                    } else {
                        seprators = options;
                    }

                    for (var i=0; i<groups.length; i++) {
                        var $optgroup = $('<optgroup label="' + groups[i].label + '"></optgroup>');
                        for (var j=0; j<groups[i].options.length; j++) {
                            $('<option value="' + groups[i].options[j][this.code] + '">' + groups[i].options[j][this.desc] + '</option>').appendTo($optgroup);
                        }
                        $optgroup.appendTo($element);
                    }
                    for (var i=0; i<seprators.length; i++) {
                        $('<option value="' + seprators[i][this.code] + '">' + options[i][this.desc] + '</option>').appendTo($element);
                    }
                }
            },
            setResult: function(left, right, options) {
                this.stopWatch = true;
                var $leftOptions = $('option', left);
                var lefts = [];
                for (var i=0; i<$leftOptions.length; i++) {
                    var code = $($leftOptions[i]).val();

                    var index = util.index(this.options, code, this.code);
                    if (index >= 0) {
                        lefts.push(this.options[index]);
                    }
                }
                this.entity[this.left] = lefts;

                var $rightOptions = $('option', right);
                var rights = [];
                for (var i=0; i<$rightOptions.length; i++) {
                    var code = $($rightOptions[i]).val();

                    var index = util.index(this.options, code, this.code);
                    if (index >= 0) {
                        rights.push(this.options[index]);
                    }
                }
                this.entity[this.right] = rights;
            },

            execute: function (param) {
                if (param.left) {
                    this.entity[this.left] = param.left;
                }
                if (param.right) {
                    this.entity[this.right] = param.right;
                }
            }
        },
        /****** 模板定义 ******/
        template: util.template('bv-grant')
    });
    /**
     * 文件上传组件
     */
    vue.component('bv-upload', {
        props: {
            entity: {
                default: function () {
                    return {};
                }
            },
            name: {
                default: ''
            },
            clazz: {
                default: ''
            },
            browserTitle: {
                default: '请选择文件：'
            },
            browserButtonText: {
                default: '浏览'
            },
            browserClass: {
                default: 'btn-primary'
            },
            // 是否允许多文件同时上传，默认为false：只能单个文件上传；
            multiple: {
                default: false
            },
            showDetail: {
                default: true
            },
            // false or default image url
            preview: {
                default: false
            },
            url: {
                default: ''
            },
            viewUrl: {
                default: Const.url.file.view
            },
            initParam: {
                default: ''
            },
            // keyId,fileSource
            filters: {
                default: function () {
                    return {};
                }
            },
            // 业务主键
            keyId: {
                default: ''
            },
            // 分类
            fileType: {
                default: ''
            },
            subType: {
                default: ''
            },
            // 是否自动上传
            autoUpload: {
                default: true
            },
            onReady: {
                default: ''
            },
            // 选择文件后触发
            onFilesAdded: {
                default: ''
            },
            // 上传进度
            onUploadProgress: {
                default: ''
            },
            // 上传完成后触发
            onFileUploaded: {
                default: ''
            },
            // 全部文件上传完成后触发（上传失败也算完成）
            onAllFileUploaded: {
                default: ''
            },
            // 计划作废
            onUpload: {
                default: ''
            }
        },
        data: function () {
            return {
                fileName: '',//文件名称
                fileSize: 0,//文件大小
                // startUpload: false,
                inProgress: false,
                progress: 0,//进度
                progressInfo: '',
                uploadError: '',
                files: ''
            }
        },
        computed: {
            fileSizeFormat: function () {
                return util.format(this.fileSize, 'file');
            }
        },
        created: function () {
            if (!this.url) {
                this.url = Const.url.file.upload;
            }
            if (util.type(this.onUpload) === 'function') {
                util.error('组件属性变更：onUpload(entity, $container, data)属性已弃用，请修改为onFileUploaded(container, entity, data)，注意参数顺序也有调整');
            }
            if (this.multiple) {
                this.entity[this.name] = [];
            }
        },
        mounted: function () {
            if (this.initParam && util.type(this.initParam) === 'object') {
                this.url = util.mix(this.url, this.initParam);
            }
            /*if (!this.entity[this.name] && this.preview) {
                $('img', this.$el).attr('src', this.preview);
            }*/
            // var vm = this;
            if (util.type(this.onReady) === 'function') {
                this.onReady.call(null, this.$el, this.entity);
            } else {
                this.$emit('on-ready', this.$el, this.entity);
            }

            var vm = this;
            $(this.$el).on('change', 'input[type=file]', function (event) {
                if (vm.multiple) {
                    vm.entity[vm.name] = [];
                }
                var files = event.target.files;

                if (files.length === 0) {
                    return;
                }
                for (var i=0; i<files.length; i++) {
                    files[i].id = util.guid();
                }
                vm.files = files;
                vm.$nextTick(function () {
                    if (util.type(vm.onFilesAdded) === 'function') {
                        vm.onFilesAdded.call(vm, event, vm.files, function () {
                            for (var i=0; i<vm.files.length; i++) {
                                vm.process(i, vm.files[i]);
                            }
                        });
                    } else {
                        for (var i=0; i<vm.files.length; i++) {
                            vm.process(i, vm.files[i]);
                        }
                    }
                });
            });
        },
        beforeDestroy: function () {
            $(this.$el).off('change');
        },
        methods: {
            process: function (index, file) {
                // 判断文件类型
                /*if (['image/jpg', 'image/jpeg', 'image/png', 'image/gif'].indexOf(file.type) < 0) {
                    util.alert('请上传图片');
                    return;
                }*/
                // 判断文件大小
                /*if (!this.compress && file.size > this.maxSize) {
                    util.alert('请上传不超过' + (this.maxSize / 1024 / 1024) + 'M的图片');
                    return;
                }*/

                /*if (this.compress) {
                    // 压缩
                    var vm = this;
                    util.loading();
                    util.compress(file, {
                        maxSize: vm.maxSize,
                        compress: vm.compress,
                        success: function (blob) {
                            vm.previewImage(index, blob || file);
                            util.loading('hide');

                            if (vm.autoUpload) {
                                // 自动上传
                                vm.ajaxUpload(index, blob || file);
                            } else {
                                vm.onSuccess(index, blob || file);
                            }
                        },
                        error: function () {
                            vm.onError(index, file);
                            util.loading('hide');
                        }
                    });
                }*/
                if (!this.multiple) {
                    this.fileName = file.name;
                    this.fileSize = file.size;
                    this.previewImage(index, file);
                }
                if (this.autoUpload) {
                    // 自动上传
                    this.ajaxUpload(index, file);
                } else {
                    this.onSuccess(index, file);
                }
            },

            ajaxUpload: function (index, file, onFileUploaded) {
                this.uploadProgress(index, file, true, 0);
                /*var formData = new FormData();
                formData.append('name', file.name);
                formData.append('type', file.type);
                formData.append('size', file.size);
                formData.append('lastModifiedDate', file.lastModifiedDate);
                formData.append(this.formName, file, file.name);*/

                if (util.type(index) === 'undefined') {
                    index = 0;
                }
                if (!file) {
                    file = this.files[index];
                }
                var vm = this;
                var param = {name: util.encode(file.name)};
                if (file.showName) {
                    param.showName = util.encode(file.showName);
                }
                if (vm.keyId) {
                    param.keyId = vm.keyId;
                }
                if (vm.fileType) {
                    param.fileType = vm.fileType;
                }
                if (vm.subType) {
                    param.subType = vm.subType;
                }
                util.upload({
                    url: util.mix(vm.url, param),
                    data: file,
                    progress: function (event) {
                        if (event.loaded && event.total) {
                            vm.uploadProgress(index, file, true, Math.ceil(100 * event.loaded / event.total));
                        }
                    },
                    success: function (res) {
                        vm.onSuccess(index, file, util.data(res), onFileUploaded);
                    },
                    error: function (xhr, status, err) {
                        vm.onError(index, file, (xhr.responseJSON && xhr.responseJSON.message) || '系统异常');
                    }
                });
            },
            previewImage: function (index, file) {
                // vue.set(this.innerFiles[index], 'url', util.createCache(file));
            },
            // index-序号file-文件流data-后台返回的数据
            onSuccess: function (index, file, data, onFileUploaded) {
                // this.inProgress = false;
                this.uploadProgress(index, file, true, '上传完成');
                if (!this.multiple) {
                    this.entity[this.name] = (data || file).fileId;
                } else {
                    this.entity[this.name].push({
                        index: index,
                        result: 'success',
                        fileId: (data || file).fileId
                    });
                }
                if (util.type(onFileUploaded) === 'function') {
                    onFileUploaded.call(this, this.$el, this.entity, data || file);
                }
                if (util.type(this.onFileUploaded) === 'function') {
                    this.onFileUploaded.call(this, this.$el, this.entity, data || file);
                } else {
                    this.$emit('on-uploaded', this.$el, this.entity, data || file);
                }
                if (this.multiple) {
                    if (this.entity[this.name].length === this.files.length) {
                        if (util.type(this.onAllFileUploaded) === 'function') {
                            this.onAllFileUploaded.call(this, this.$el, this.entity);
                        }
                    }
                }
            },
            onError: function (index, file, error) {
                // this.inProgress = false;
                this.uploadProgress(index, file, true, '上传失败' + (error && '[' + error + ']'));
                if (this.multiple) {
                    this.entity[this.name].push({
                        index: index,
                        result: 'error'
                    });
                    if (this.entity[this.name].length === this.files.length) {
                        if (util.type(this.onAllFileUploaded) === 'function') {
                            this.onAllFileUploaded.call(this, this.$el, this.entity);
                        }
                    }
                }
                /// this.loading(file, true);
                /// util.alert('上传失败');
                // util.removeCache(this.innerFiles[index].url);
                // vue.set(this.innerFiles[index], 'url', '');
                // vue.set(this.innerFiles[index], 'disabled', true);
            },
            uploadProgress: function (index, file, inProgress, progress) {
                var _progress = '';
                var _progressInfo = '';
                if (util.type(progress) === 'number') {
                    _progress = progress;
                    _progressInfo = this.progress + '%';
                } else {
                    _progress = 0;
                    _progressInfo = progress;
                }
                if (!this.multiple) {
                    this.inProgress = inProgress;
                    this.progress = progress;
                    this.progressInfo = _progressInfo;
                }
                if (util.type(this.onUploadProgress) === 'function') {
                    this.$nextTick(function () {
                        this.onUploadProgress.call(this, this.$el, index, file, _progress, _progressInfo);
                    });
                }
            },

            viewImage: function () {
                var fileId = '';
                if (this.preview) {
                    if (this.preview === true) {
                        if (this.entity[this.name]) {
                            fileId = this.entity[this.name];
                        }
                    } else {
                        // url
                        if (this.entity[this.name]) {
                            fileId = this.entity[this.name];
                        } else {
                            fileId = this.preview;
                        }
                    }
                }
                if (fileId) {
                    return util.url(this.viewUrl + '/' + fileId);
                }
            },

            execute: function (param) {
                if (param.method) {
                    if (param.method === 'upload') {
                        this.ajaxUpload(undefined, undefined, param.onFileUploaded);
                    }
                }
                if (param.fileType) {
                    this.fileType = param.fileType;
                }
                if (param.subType) {
                    this.subType = param.subType;
                }
            }
        },
        /****** 模板定义 ******/
        template: util.template('bv-upload')
    });

    vue.component('bv-import', {
        props: {
            name: {
                default: 'fileId'
            },
            title: {
                default: '导入数据'
            },
            url: {
                default: ''
            },
            template: {
                default: ''
            },
            uploadUrl: {
                default: Const.url.excel.upload
            },
            // default-自动modal-弹窗body-非弹窗
            layout: {
                default: 'default'
            },
            // 自定义数据填充：格式importData(preset, data)
            // preset为预留字段
            // data为默认上传文件信息
            data: {
                default: ''
            }
        },
        data: function () {
            return {
                resultFile: '',
                uploadHint: '',
                upload: {}
            }
        },
        mounted: function () {
            this.layout = util.layout();
            util.validateInit($(this.$el));
        },
        methods: {
            doImport: function(event) {
                if (!util.validate($('form', $(this.$el)))) {
                    return;
                }
                var vm = this;
                if (util.isEmpty(vm.upload.fileId || vm.upload.fileName)) {
                    vm.uploadHint = '请先上传导入文件!';
                } else {
                    vm.uploadHint = '';
                    var data = vm.upload;
                    if (vm.data && util.type(vm.data) === 'function') {
                        // 第一个参数预留
                        data = vm.data.call(null, event, data);
                    }
                    if (data.fileId) {
                        util.post({
                            $element: event.target,
                            url: vm.url,
                            data: data.fileId,
                            success: function(res) {
                                if (res) {
                                    vm.resultFile = util.data(res);
                                    vm.uploadHint = '导入完成，若有导入出错数据请下载检查结果进行重新导入!';
                                } else {
                                    vm.uploadHint = '导入成功';
                                }
                                /*var data = util.data(res);
                                if (data) {
                                    vm.resultFile = data;
                                    vm.uploadHint = '导入完成，若有导入出错数据请下载检查结果进行重新导入!';
                                } else {
                                    vm.uploadHint = '导入成功';
                                }*/
                            }
                        });
                    } else {
                        util.post({
                            $element: event.target,
                            url: vm.url,
                            data: data,
                            success: function(res) {
                                if (res) {
                                    vm.resultFile = util.data(res);
                                    vm.uploadHint = '导入完成，若有导入出错数据请下载检查结果进行重新导入!';
                                } else {
                                    vm.uploadHint = '导入成功';
                                }
                                /*var data = util.data(res);
                                if (data) {
                                    vm.resultFile = data;
                                    vm.uploadHint = '导入完成，若有导入出错数据请下载检查结果进行重新导入!';
                                } else {
                                    vm.uploadHint = '导入成功';
                                }*/
                            }
                        });
                    }
                }
            },
            downloadResult: function() {
                util.download(Const.url.excel.download + '?fileName=' + util.encode(this.resultFile));
            },
            downloadTemplate: function() {
                if (this.template) {
                    if (util.contains(this.template, '/')) {
                        // url
                        util.download(this.template);
                    } else {
                        // file
                        util.download(Const.url.excel.template + '?fileName=' + util.encode(this.template));
                    }
                }
            }
        },
        /****** 模板定义 ******/
        template: util.template('bv-import')
    });

    vue.component('bv-export', {
        props: {
            url: {
                default: ''
            },
            template: {
                default: ''
            },
            data: {
                default: ''
            }
        },
        methods: {
            doExport: function(event) {
                util.download({
                    $element: event.target,
                    url: this.url,
                    data: this.data
                });
            },
            downloadTemplate: function() {
                if (this.template) {
                    if (util.contains(this.template, '/')) {
                        // url
                        util.download(this.template);
                    } else {
                        // file
                        util.download(Const.url.excel.template + '?fileName=' + this.template);
                    }
                }
            }
        },
        /****** 模板定义 ******/
        template: util.template('bv-export')
    });

    vue.component('bv-print', {
        props: {
            // 打印类型，支持preview-预览print-直接打印
            type: {
                default: 'preview'
            },
            text: {
                default: '打印预览'
            },
            clazz: {
                default: ''
            },
            attr: {
                default: function () {
                    return {};
                }
            },
            // 打印内容
            content: {
                default: ''
            },
            // 打印url
            url: {
                default: ''
            },
            // 页面设置
            page: {
                default: function () {
                    return {
                        top: 0,
                        left: 0,
                        width: '100%',
                        height: '100%'
                    };
                }
            }
        },
        mounted: function () {
            if (!Const.global.LODOP) {
                var vm = this;
                // 检测打印控件
                util.require('lodop', function () {
                    try {
                        Const.global.LODOP = getCLodop();
                        if (!Const.global.LODOP) {
                            // alert("C-Lodop没准备好，请稍后再试！")
                            $(vm.$el).addClass('disabled').attr('data-placement', 'bottom').attr('data-title', 'C-Lodop没准备好，请稍后再试！');
                            util.tooltip(vm.$el, 'data-title', true);
                        }
                    } catch (err) {
                        // alert("getLodop出错:"+err);
                        $(vm.$el).addClass('disabled').attr('data-placement', 'bottom').attr('data-title', 'getLodop出错:' + err);
                        util.tooltip(vm.$el, 'data-title', true);
                    }
                });
            }
        },
        methods: {
            doPrint: function () {
                if (Const.global.LODOP) {
                    Const.global.LODOP.PRINT_INIT('');
                    if (this.content) {
                        if (util.type(this.content) === 'string') {
                            Const.global.LODOP.ADD_PRINT_HTM(this.page.top, this.page.left, this.page.width, this.page.height, this.content);
                            this.previewOrPrint();
                        } else if (util.type(this.content) === 'function') {
                            var vm = this;
                            this.content.call(this, function (html) {
                                Const.global.LODOP.ADD_PRINT_HTM(vm.page.top, vm.page.left, vm.page.width, vm.page.height, html);
                                vm.previewOrPrint();
                            });
                        }
                    } else if (this.url) {
                        Const.global.LODOP.ADD_PRINT_URL(this.page.top, this.page.left, this.page.width, this.page.height, this.url);
                        this.previewOrPrint();
                    }
                }
            },
            previewOrPrint: function () {
                if (this.type === 'preview') {
                    Const.global.LODOP.PREVIEW();
                } else if (this.type === 'print') {
                    Const.global.LODOP.PRINT();
                }
            }
        },
        template: util.template('bv-print')
    });

    vue.component('bv-report', {
        props: {
            size: {
                default: 'A4'
            },
            clazz: {
                default: ''
            }
        },
        created: function () {
            if (!Const.global.reportInited) {
                Const.global.reportInited = true;
                loader.style('/themes/default/report.css');
            }
        },
        mounted: function () {
        },
        /****** 模板定义 ******/
        template: util.template('bv-report')
    });

    vue.component('bv-search', {
        props: {
            placeholder: {
                default: ''
            },
            click: {
                default: ''
            }
        },
        data: function () {
            return {
                value: ''
            };
        },
        template: util.template('bv-search')
    });

    vue.component('bv-media', {
        props: {
            clazz: {
                default: ''
            },
            // url: '',
            // width: ''
            image: {
                default: function () {
                    return {};
                }
            },
            title: {
                default: ''
            },
            content: {
                default: ''
            }
        },
        data: function () {
            return {
                childImage: ''
            };
        },
        methods: {
        },
        mounted: function () {
            if (util.type(this.image) === 'string') {
                this.childImage = this.image;
            }
        },
        template: util.template('bv-media')
    });

    vue.component('bv-file', {
        props: {
            // 图档模式，支持readonly-只读update-修改delete-删除upload-上传capture-拍照及组合比如upload,capture
            mode: {
                default: 'update,delete,upload,capture'
            },
            keyId: {
                default: ''
            },
            fileType: {
                default: ''
            },
            /*showName: {
                default: ''
            },*/
            uploadUrl: {
                default: Const.url.file.upload
            },
            viewUrl: {
                default: Const.url.file.view
            },
            downloadUrl: {
                default: Const.url.file.download
            },
            thumbUrl: {
                default: Const.url.file.thumb
            },
            // 保存showName的url
            showNameUrl: {
                default: Const.url.file.updateShowName
            },
            rotateUrl: {
                default: Const.url.file.rotate
            },
            cropUrl: {
                default: Const.url.file.crop
            },
            deleteUrl: {
                default: Const.url.file.del
            },
            catalog: {
                default: function () {
                    return {};
                }
            },
            // 拍照默认是否预览
            capturePreview: {
                default: false
            }
        },
        data: function () {
            return {
                entity: {
                    device: null,
                    resolution: null
                },
                id: util.guid(),
                // 是否显示上传容器
                showUploadContainer: false,
                // 是否显示拍照容器
                showCaptureContainer: false,
                // 拍照设备是否就绪
                deviceReady: false,
                // 拍照按钮提示信息
                captureTooltip: '',
                // 拍照设备列表
                devices: [],
                // 默认选中设备
                defaultDevice: '',
                // 拍照预览
                // capturePreview: '',
                // 分辨率列表
                resolutions: [],
                // 是否显示确认
                showConfirm: false,
                captureBase64: '',
                // 是否在上传中
                uploading: false,
                // 上传进度
                uploadProgress: '',
                name: util.guid(),
                fileCatalogConfig: {},
                uploadListConfig: {},
                viewListConfig: {}
            }
        },
        created: function () {
            this.currentCatalogNode = null;
            this.viewItems = [];

            this.fileCatalogConfig = {
                rootNode: this.catalog.rootNode || this.fileType && {
                    id: this.fileType.code,
                    name: this.fileType.desc,
                    open: true
                },
                items: this.catalog.items,
                pack: this.catalog.pack
            };
            this.uploadListConfig = {
                title: '上传文件列表',
                layout: 'body',
                fixed: false,
                sort: false,
                loadType: false,
                progress: false,
                pagination: false,
                filterType: false,
                filterShow: false,
                columns: [
                    {
                        name: 'id',
                        hide: true
                    }, {
                        name: 'showName',
                        head: '文件名'
                    }, {
                        name: 'originName',
                        head: '原始文件名'
                    }, {
                        name: 'fileSize',
                        head: '文件大小',
                        config: {
                            format: 'file'
                        }
                    }, {
                        name: 'progress',
                        head: '上传进度',
                        config: {
                            key: 'id'
                        }
                    }
                ],
                operates: [
                    {
                        text: '隐藏',
                        click: function (event, tableId, entityName, selected) {
                            this.$parent.showUploadContainer = false;
                        }
                    }
                ]
            };
            var vm = this;
            this.viewListConfig = {
                layout: 'body',
                fixed: false,
                silence: true,
                filterVisible: false,
                entityName: 'logFile',
                keys: 'fileId',
                orders: 'showName',
                initParam: {
                    keyId: this.keyId,
                    fileType: this.fileType.code,
                    fileState: '0000'
                },
                extraColumnNames: 'fileId, extName',
                editable: true,
                defaultTag: 'static',
                columns: [
                    /*{
                        name: 'fileName',
                        head: '文件名'
                    }, */{
                        name: 'showName',
                        head: '文件名',
                        edit: {
                            type: this.showNameUrl && this.allow('update') ? ['static', 'textfield'] : 'static',
                            save: function (row, column, callback) {
                                util.request({
                                    url: this.$parent.showNameUrl,
                                    data: {
                                        fileId: row.fileId,
                                        showName: row.showName
                                    },
                                    success: function () {
                                        callback.call(null, true);
                                    },
                                    error: function () {
                                        callback.call(null);
                                    }
                                });
                            }
                        }
                    }, {
                        name: 'originName',
                        head: '原始文件名'
                    }, /*{
                            name: 'filePath',
                            head: '文件路径'
                        }, {
                            name: 'extName',
                            head: '扩展名'
                        }, */{
                        name: 'fileSize',
                        head: '文件大小',
                        config: {
                            format: 'file'
                        }
                    }, {
                        name: 'manageTime',
                        head: '上传时间',
                        config: {
                            format: 'datetime'
                        }
                    }, /*{
                            name: 'fileSign',
                            head: '文件签名'
                        }, */{
                        head: '操作',
                        type: 'operate',
                        operates: [
                            {
                                text: '浏览图片',
                                show: function (entity) {
                                    return entity && entity.extType && entity.extType === 'PIC';
                                },
                                click: function (event, tableId, entityName, selected) {
                                    this.$parent.stopCapture();
                                    var items = this.$parent.viewItems;
                                    var _vm = this.$parent;
                                    if (items.length === 0) {
                                        // 排序字段
                                        var orderList = util.call({
                                            vm: util.vm(_vm, 'viewList'),
                                            get: 'orderList'
                                        });
                                        var paramList = [
                                            {
                                                name: 'keyId',
                                                operate: '=',
                                                value: this.$parent.keyId
                                            }, {
                                                name: 'fileType',
                                                operate: '=',
                                                value: this.$parent.fileType.code
                                            }, {
                                                name: 'fileState',
                                                operate: '=',
                                                value: '0000'
                                            }
                                        ];
                                        var subType = this.$parent.currentCatalogNode && this.$parent.currentCatalogNode.level > 0 && this.$parent.currentCatalogNode.id;
                                        if (subType) {
                                            paramList.push({
                                                name: 'subType',
                                                operate: '=',
                                                value: subType
                                            });
                                        }
                                        paramList.push({
                                            name: 'extType',
                                            operate: '=',
                                            value: 'PIC'
                                        });
                                        util.post({
                                            url: Const.url.table.select,
                                            data: {
                                                entityName: 'logFile',
                                                orderList: orderList,
                                                paramList: paramList
                                            },
                                            success: function (res) {
                                                var data = util.data(res);
                                                if (data) {
                                                    for (var i=0; i<data.length; i++) {
                                                        items.push({
                                                            html: '<h4>' + data[i].showName + '</h4><p>' + (data[i].describe || '') + '</p>',
                                                            url: _vm.viewUrl + '/' + data[i].fileId,
                                                            downloadUrl: _vm.downloadUrl + '/' + data[i].fileId,
                                                            thumbUrl: 'custom/themes/images/placeholder.jpg',
                                                            lazyUrl:  _vm.thumbUrl + '/' + data[i].fileId,
                                                            tooltip: data[i].showName,
                                                            id: data[i].fileId
                                                        });
                                                    }
                                                    _vm.openGallery(items, selected);
                                                }
                                            }
                                        });
                                    } else {
                                        _vm.openGallery(items, selected);
                                    }
                                }
                            }
                        ]
                    }
                ],
                operates: [
                    {
                        type: 'upload',
                        show: function () {
                            return this.$parent.currentCatalogNode != null && this.$parent.currentCatalogNode.level > 0;
                        },
                        create: function () {
                            return this.$parent.allow('upload');
                        },
                        config: {
                            key: 'upload',
                            browserTitle: '',
                            browserButtonText: '选择文件',
                            browserClass: 'btn-default',
                            multiple: true,
                            keyId: this.keyId,
                            fileType: this.fileType.code,
                            // subType: '',
                            onFilesAdded: function (event, files, callback) {
                                this.$parent.$parent.showUploadContainer = true;
                                this.$parent.$parent.stopCapture();
                                this.$parent.$parent.$nextTick(function () {
                                    var rows = [];
                                    if (util.type(this.showName) === 'function') {
                                        var vm = this;
                                        this.showName.call(null, files, vm.currentCatalogNode.entity, util.call({
                                            vm: util.vm(vm, 'viewList'),
                                            get: 'count'
                                        }), function (files) {
                                            for (var i=0; i<files.length; i++) {
                                                rows.push({
                                                    id: files[i].id,
                                                    showName: files[i].showName,
                                                    originName: files[i].name,
                                                    fileSize: files[i].size,
                                                    progress: ''
                                                });
                                            }
                                            util.call({
                                                vm: util.vm(vm, 'uploadList'),
                                                type: 'noRefresh',
                                                showProgress: true,
                                                appendRows: rows
                                            });
                                            callback.call(null);
                                        });
                                    } else {
                                        var baseName = this.currentCatalogNode.entity.docName;
                                        var baseSn = util.call({
                                            vm: util.vm(this, 'viewList'),
                                            get: 'count'
                                        });
                                        for (var i=0; i<files.length; i++) {
                                            files[i].showName = baseName + '-' + util.fill(++baseSn, 3);
                                            rows.push({
                                                id: files[i].id,
                                                showName: files[i].showName,
                                                originName: files[i].name,
                                                fileSize: files[i].size,
                                                progress: ''
                                            });
                                        }
                                        util.call({
                                            vm: util.vm(this, 'uploadList'),
                                            type: 'noRefresh',
                                            showProgress: true,
                                            appendRows: rows
                                        });
                                        callback.call(null);
                                    }
                                });
                            },
                            onUploadProgress: function (container, index, file, progress, progressInfo) {
                                this.$parent.$parent.$nextTick(function () {
                                    util.call({
                                        vm: util.vm(this, 'uploadList', file.id),
                                        text: progressInfo
                                    });
                                });
                            },
                            onFileUploaded: function (container, entity, data) {
                            },
                            onAllFileUploaded: function (container, entity) {
                                this.$parent.$parent.viewItems = [];
                                util.call({
                                    vm: util.vm(this.$parent.$parent, 'viewList')
                                });
                            }
                        }
                    }, {
                        type: 'button',
                        // text: '拍照',
                        show: function () {
                            return vm.currentCatalogNode != null && vm.currentCatalogNode.level > 0 && !vm.showCaptureContainer;
                        },
                        create: function () {
                            return vm.allow('capture');
                        },
                        config: {
                            key: 'startCapture',
                            clazz: 'btn-default capture-control',
                            text: '开始拍照',
                            click: function () {
                                vm.startCapture();
                            }
                        }
                    }, {
                        type: 'button',
                        // text: '拍照',
                        show: function () {
                            return vm.currentCatalogNode != null && vm.currentCatalogNode.level > 0 && vm.showCaptureContainer;
                        },
                        create: function () {
                            return vm.allow('capture');
                        },
                        config: {
                            key: 'stopCapture',
                            clazz: 'btn-default capture-control',
                            text: '停止拍照',
                            click: function () {
                                vm.stopCapture();
                            }
                        }
                    }
                ]
            };
            this.eventId = util.guid();
        },
        mounted: function () {
            if (this.allow('capture')) {
                // 检测拍照设备
                this.initCapture();
                if (util.layout() === 'modal') {
                    $('.capture-confirm>img', this.$el).height($(window).height() - 30);
                }
                if (!Const.captureEventInited) {
                    Const.captureEventInited = true;

                    if (!Const.vm.captureVm) {
                        Const.vm.captureVm = this;
                    }

                    window.onunload = function() {
                        util.capture('close');
                    };

                    var vm = this;
                    $(document).on('keydown.' + vm.eventId, function (event) {
                        if (Const.vm.captureVm && Const.vm.captureVm.showCaptureContainer) {
                            // 空格或回车，拍照
                            if (event.which === 32 || event.which === 13) {
                                if (Const.vm.captureVm.showConfirm) {
                                    Const.vm.captureVm.doUpload();
                                } else {
                                    // 拍照
                                    $('#doCapture', Const.vm.captureVm.$el).button('loading');
                                    Const.vm.captureVm.doCapture();
                                }
                                event.preventDefault();
                                event.stopPropagation();
                                return false;
                            } else if (event.which === 27) {
                                // esc
                                if (Const.vm.captureVm.showConfirm) {
                                    Const.vm.captureVm.doCancel();
                                    return false;
                                }
                            }
                        }
                        return true;
                    });

                    $(window).on('resize.' + vm.eventId, function () {
                        if (Const.vm.captureVm && $(Const.vm.captureVm.$el).closest('.bv-modal').length === 1) {
                            $('.capture-confirm>img', Const.vm.captureVm.$el).height($(Const.vm.captureVm.$el).closest('.bv-modal').width() * 3 / 4);
                            $('.capture-confirm>img', Const.vm.captureVm.$el).css('max-height', $(window).height() - 30);
                        }
                    });
                }
            }
        },
        beforeDestroy: function () {
            // util.capture('close');
            if (this.allow('capture')) {
                if (Const.vm.captureVm && this.id === Const.vm.captureVm.id) {
                    this.stopCapture();
                } else {
                    this.stopCapture(true);
                }
                $(window).off('resize');
            }
        },
        methods: {
            onCatalogClick: function (event, treeId, treeNode) {
                this.currentCatalogNode = treeNode;
                this.viewItems = [];
                this.showUploadContainer = false;
                // this.showCaptureContainer = false;
                if (this.showCaptureContainer) {
                    this.initDevice();
                }
                if (treeNode.level === 0) {
                    util.call({
                        vm: util.vm(this, 'viewList'),
                        page: 1,
                        initParam: {
                            keyId: this.keyId,
                            fileType: this.fileType.code,
                            fileState: '0000'
                        }
                    });
                } else {
                    util.call({
                        vm: util.vm(this, 'viewList'),
                        page: 1,
                        initParam: {
                            keyId: this.keyId,
                            fileType: this.fileType.code,
                            subType: this.currentCatalogNode.id,
                            fileState: '0000'
                        }
                    });
                    // subType
                    util.call({
                        vm: util.vm(this, 'viewList', 'upload'),
                        subType: this.currentCatalogNode.id
                    });
                    // capture
                    util.call({
                        vm: util.vm(this, 'viewList', 'startCapture'),
                        attr: {
                            'data-title': this.captureTooltip,
                            'data-placement': 'bottom',
                            disabled: !this.deviceReady
                        }
                    });
                    this.$nextTick(function () {
                        util.tooltip(util.vm(this, 'viewList', 'startCapture').$el, 'data-title', true);
                    });
                }
            },
            initCapture: function (callback) {
                if (!Const.global.captureSocket) {
                    if (!Const.vm.captureVm || this.id !== Const.vm.captureVm.id) {
                        if (Const.vm.captureVm) {
                            Const.vm.captureVm.stopCapture();
                        }
                        Const.vm.captureVm = this;
                    }
                    try {
                        Const.global.captureSocket = new WebSocket('ws://127.0.0.1:12345');
                    } catch (e) {
                        util.error(e);
                        Const.vm.captureVm.captureTooltip = '拍照服务未启动';
                    }
                    Const.global.captureSocket.onerror = function (e) {
                        util.error(e);
                        Const.vm.captureVm.captureTooltip = '拍照服务未启动';
                    }
                    Const.global.captureSocket.onclose = function () {
                        util.capture('close');
                    }
                    Const.global.captureSocket.onopen = function () {
                        // vm.deviceReady = true;
                        Const.vm.captureVm.captureTooltip = '未检测到设备';

                        util.require('qwebchannel', function () {
                            new QWebChannel(Const.global.captureSocket, function (channel) {
                                Const.global.captureDialog = channel.objects.dialog;
                                //关闭/刷新信号，服务器主动关闭网页（未使用）
                                Const.global.captureDialog.send_close.connect(function () {
                                });
                                //接收设备名(在设备列表中添加或删除item)
                                Const.global.captureDialog.send_devName.connect(function (message) {
                                    //判断是否存在，否 加入，是 删除
                                    if (message.indexOf('delete') >= 0) {
                                    } else {
                                        //副头放在列表末
                                        if (message.indexOf("USB") >= 0) {
                                            Const.vm.captureVm.devices.push(message);
                                        } else {
                                            Const.vm.captureVm.devices.unshift(message);
                                            // vm.defaultDevice = message;
                                        }
                                    }
                                    Const.vm.captureVm.captureTooltip = '';
                                    Const.vm.captureVm.deviceReady = true;
                                    if (Const.vm.captureVm.devices.length > 1) {
                                        ///util.initSelectData(util.vm(Const.vm.captureVm, 'device'), 'select');
                                        Const.vm.captureVm.initDevice();
                                    }
                                });
                                //接收设备分辨率，若为空，则清空现有列表
                                Const.global.captureDialog.send_resolutionList.connect(function (message) {
                                    // console.log(message);
                                    if (message) {
                                        Const.vm.captureVm.resolutions.push(message);
                                        if (!Const.vm.captureVm.entity.resolution) {
                                            Const.vm.captureVm.entity.resolution = message;
                                        }
                                        /// util.initSelectData(util.vm(Const.vm.captureVm, 'resolution'), 'select');
                                    }
                                });
                                //接收设备出图模式，若为空，则清空现有列表
                                Const.global.captureDialog.send_modelList.connect(function (message) {
                                    // console.log(message);
                                });
                                //服务器返回消息
                                Const.global.captureDialog.sendPrintInfo.connect(function (message) {
                                    // console.log(message);
                                });
                                //接收图片流用来展示，多个，较小的base64
                                Const.global.captureDialog.send_imgData2.connect(function (message) {
                                    if (!Const.vm.captureVm.uploading) {
                                        $('.capture-preview>img', Const.vm.captureVm.$el).attr('src', 'data:image/jpg;base64,' + message);
                                    }
                                });
                                //接收图片流用来展示，单个，较大的base64
                                Const.global.captureDialog.send_imaData3.connect(function (message) {
                                    // console.log(message);
                                    Const.vm.captureVm.uploading = true;
                                    Const.vm.captureVm.captureBase64 = message;
                                    Const.vm.captureVm.showConfirm = true;
                                });
                                //网页加载完成信号
                                Const.global.captureDialog.html_loaded("one");
                            });
                        });
                    }
                } else {
                    if (this.id !== Const.vm.captureVm.id) {
                        this.deviceReady = Const.vm.captureVm.deviceReady;
                        this.captureTooltip = Const.vm.captureVm.captureTooltip;
                        this.devices = util.clone(Const.vm.captureVm.devices);
                        this.entity.device = Const.vm.captureVm.entity.device;
                        this.resolutions = util.clone(Const.vm.captureVm.resolutions);
                        this.entity.resolution = Const.vm.captureVm.entity.resolution;
                        this.$nextTick(function () {
                            Const.vm.captureVm.stopCapture(true);
                            Const.vm.captureVm = this;
                            // Const.vm.captureVm.changeDevice(null, Const.vm.captureVm.entity.device);
                        });
                    }
                }
            },
            initDevice: function (openCapture) {
                if (this.currentCatalogNode && this.currentCatalogNode.entity && this.currentCatalogNode.entity.captureType) {
                    if (this.currentCatalogNode.entity.captureType === 'face') {
                        if (this.devices.length > 1) {
                            if (this.defaultDevice !== this.devices[1]) {
                                this.defaultDevice = this.devices[1];
                                this.entity.device = this.devices[1];
                                this.resolutions = [];
                                this.entity.resolution = null;
                            }
                        }
                        if (this.resolutions.length === 0) {
                            Const.global.captureDialog.devChanged(this.devices[1]);
                        }
                    }
                } else {
                    if (this.defaultDevice !== this.devices[0]) {
                        this.defaultDevice = this.devices[0];
                        this.entity.device = this.devices[0];
                        this.resolutions = [];
                        this.entity.resolution = null;
                    }
                    if (this.resolutions.length === 0) {
                        Const.global.captureDialog.devChanged(this.devices[0]);
                    }
                }
                this.openCapture();
            },
            changeDevice: function (entity, value, item) {
                this.resolutions = [];
                this.entity.resolution = null;
                this.$nextTick(function () {
                    Const.global.captureDialog.devChanged(value);
                });
            },
            changeResolution: function (entity, value, item) {
                Const.global.captureDialog.devChanged(value);
            },
            openCapture: function () {
                this.$nextTick(function () {
                    $('#doCapture', this.$el).focus();
                });
                if (this.capturePreview) {
                    this.showPreview();
                } else {
                    this.hidePreview();
                }
            },
            showPreview: function () {
                // Const.global.captureDialog.devChanged(this.entity.device);
                this.capturePreview = true;
                Const.global.captureDialog.get_actionType('openVideo');
            },
            hidePreview: function () {
                this.capturePreview = false;
                Const.global.captureDialog.get_actionType('closeVideo');
                $('.capture-preview>img', this.$el).attr('src', '');
            },
            doCapture: function () {
                Const.global.captureDialog.photoBtnClicked('single');
                Const.global.captureDialog.get_actionType('savePhoto');
            },
            // 上传
            doUpload: function () {
                this.showConfirm = false;
                $('.capture-preview>img', this.$el).attr('src', 'data:image/jpg;base64,' + this.captureBase64);
                if (util.type(this.showName) === 'function') {
                    this.showName.call(null, null, this.currentCatalogNode.entity, util.call({
                        vm: util.vm(this, 'viewList'),
                        get: 'count'
                    }), function (showName) {
                        this.uploadCapture(showName, util.dataURItoBlob('data:image/jpg;base64,' + this.captureBase64));
                    });
                } else {
                    var baseName = this.currentCatalogNode.entity.docName;
                    var baseSn = util.call({
                        vm: util.vm(this, 'viewList'),
                        get: 'count'
                    });
                    this.uploadCapture(baseName + '-' + util.fill(++baseSn, 3), util.dataURItoBlob('data:image/jpg;base64,' + this.captureBase64));
                }
            },
            // 取消
            doCancel: function () {
                this.uploading = false;
                this.showConfirm = false;
                this.captureBase64 = '';
                $('#doCapture', this.$el).button('reset');
            },
            startCapture: function () {
                this.showUploadContainer = false;
                this.showCaptureContainer = true;
                this.initCapture();
            },
            stopCapture: function (keep) {
                if (!keep) {
                    util.capture('close');
                }
                this.devices = [];
                this.defaultDevice = null;
                this.entity.device = null;
                this.resolutions = [];
                this.entity.resolution = null;
                this.showCaptureContainer = false;
            },
            uploadCapture: function (showName, blob) {
                var param = {
                    name: 'capture.jpg',
                    showName: showName
                };
                if (this.keyId) {
                    param.keyId = this.keyId;
                }
                if (this.fileType) {
                    param.fileType = this.fileType.code;
                }
                param.subType = this.currentCatalogNode.id;
                var vm = this;
                util.upload({
                    url: util.mix(this.uploadUrl, param),
                    data: blob,
                    silence: true,
                    progress: function (event) {
                        if (event.loaded && event.total) {
                            vm.uploadProgress = Math.ceil(100 * event.loaded / event.total) + '%';
                        }
                    },
                    success: function (res) {
                        vm.uploadProgress = '100%';
                    },
                    error: function (xhr, status, err) {
                    },
                    complete: function () {
                        vm.uploading = false;
                        vm.captureBase64 = '';
                        vm.uploadProgress = '';
                        vm.viewItems = [];
                        util.call({
                            vm: util.vm(vm, 'viewList')
                        });
                        $('#doCapture', vm.$el).button('reset');
                        if (!vm.capturePreview) {
                            setTimeout(function () {
                                $('.capture-preview img', vm.$el).attr('src', '');

                                blob = null;
                            }, 2000);
                        }
                    }
                });
            },
            openGallery: function (items, selected) {
                var vm = this;
                util.gallery({
                    items: items,
                    thumbnail: true,
                    rotate: vm.allow('update') && function(event, index, item, rotate, callback) {
                        util.request({
                            url: vm.rotateUrl,
                            silence: true,
                            data: {
                                fileId: item.id,
                                z: 1,
                                angle: rotate
                            },
                            success: function (res) {
                                callback.call(null, 'refresh');
                            }
                        });
                    },
                    crop: vm.allow('update') && function (event, index, item, pos, callback) {
                        util.request({
                            url: vm.cropUrl,
                            data: {
                                fileId: item.id,
                                z: 1,
                                x: util.toNumber(pos.x),
                                y: util.toNumber(pos.y),
                                w: util.toNumber(pos.width),
                                h: util.toNumber(pos.height)
                            },
                            success: function (res) {
                                callback.call(null, 'refresh');
                            }
                        });
                    },
                    delete: vm.allow('delete') && function (event, index, item, callback) {
                        util.confirm({
                            title: '请确认',
                            content: '删除后将不能恢复，是否确认删除该文件？',
                            yes: function (event, editType, entity, callback2) {
                                util.request({
                                    url: vm.deleteUrl + '/' + item.id,
                                    success: function () {
                                        callback2.call(null);
                                        callback.call(null);
                                    }
                                });
                            }
                        });
                    },
                    index: util.index(items, selected[0].fileId, 'id'),
                    close: function () {
                        util.call({
                            vm: util.vm(vm, 'viewList')
                        });
                    }
                });
            },
            allow: function (type) {
                if (!type || !this.mode || this.mode === 'readonly') {
                    return false;
                }
                return this.mode.indexOf(type) >= 0;
            }
        },
        /****** 模板定义 ******/
        template: util.template('bv-file')
    });

    vue.component('bv-panel', {
        props: {
            title: {
                default: ''
            },
            /**
             * 是否显示更多，可以为字符串，也可以为function
             *格式为：{text: '字符串', click: '函数'}
             **/
            more: {
                default: ''
            }
        },
        methods: {
        },
        mounted: function () {
        },
        template: util.template('bv-panel')
    });

    /* 轮播 */
    vue.component('bv-carousel', {
        props: {
            id: {
                default: ''
            },
            clazz: {
                default: ''
            },
            // url
            // title
            width: {
                default: '100%'
            },
            height: {
                default: ''
            },
            vertical: {
                default: false
            },
            items: {
                default: function () {
                    return [];
                }
            },
            active: {
                default: 0
            }
        },
        methods: {
        },
        created: function () {
            if (!this.id) {
                this.id = util.guid();
            }
        },
        mounted: function () {
            $(this.$el).carousel();
        },
        beforeDestroy: function () {
            $(this.$el).carousel('pause');
        },
        template: util.template('bv-carousel')
    });

    vue.component('bv-list', {
        props: {
            inline: {
                default: true
            },
            // default media-带媒体对象
            type: {
                default: 'default'
            },
            // 媒体部分默认显示日期
            mediaType: {
                default: 'date'
            },
            // 是否分页
            pagination: {
                default: true
            },
            // 分页显示类型，支持normal,more
            pageType: {
                default: 'more'
            },
            // 显示页码数 值必须 > 5
            pageShow: {
                default: 7
            },
            // 每页显示数据条数
            limit: {
                default: 10
            },
            url: {
                default: ''
            },
            // 作废
            items: {
                default: ''
            },
            // 列表字段定义
            entityName: {
                default: ''
            },
            keys: {
                default: ''
            },
            // 格式:name type,name type
            // 设置为false表示不允许排序
            orders: {
                default: ''
            },
            // 固定数据
            initRows: {
                default: ''
            },
            rows: {
                default: function () {
                    return [];
                }
            },
            initParam: {
                default: ''
            },
            initParamList: {
                default: function () {
                    return [];
                }
            },
            date: {
                default: ''
            },
            text: {
                default: ''
            },
            title: {
                default: ''
            },
            content: {
                default: ''
            },
            // 图片地址
            src: {
                default: ''
            },
            // 跳转连接
            href: {
                default: ''
            },
            target: {
                default: ''
            },
            // 行点击事件
            click: {
                default: ''
            }
        },
        data: function () {
            return {
                pager: {
                    limit: this.limit,
                    // 当前页码
                    currentPage: 1,
                    // 总页数
                    totalPage: 0,
                    // 总条数
                    rowCount: 0,
                    type: this.pageType,
                    pageShow: this.pageShow
                },
                dataInited: false,
                // 当前页开始序号
                offset: 0,
                // 查询返回的字段
                columnNames: []
            }
        },
        computed: {
            offset: function () {
                return (this.pager.currentPage - 1) * this.pager.limit;
            }
        },
        watch: {
            rows: function () {
                if ($(this.$el).length === 1) {
                    if (this.rows.length > 0) {
                        this.$nextTick(function () {
                            util.tooltip($(this.$el), 'data-title');
                        });
                    }
                }
            }
        },
        created: function () {
            // 脱敏
            this.confuses = {};

            if (this.items) {
                util.warn('items属性作废，请使用initRows代替');
                this.initRows = this.items;
            }
            if (!this.url && !this.initRows) {
                if (this.pagination) {
                    this.url = Const.url.table.page;
                } else {
                    this.url = Const.url.table.select;
                }
            }
            if (this.keys) {
                // TODO: 未考虑复合主键
                this.columnNames.push(this.keys);
            }
            if (this.date) {
                this.columnNames.push(this.date);
            }
            if (this.text) {
                this.columnNames.push(this.text);
            }
            if (this.title) {
                this.columnNames.push(this.title);
            }
            if (this.content) {
                this.columnNames.push(this.content);
            }
            if (this.src) {
                this.columnNames.push(this.src);
            }
            if (this.href) {
                this.columnNames.push(this.href);
            }
            this.fill();
        },
        methods: {
            setLimit: function(limit) {
                // console.log(limit);
            },
            setPage: function(page) {
                this.fill(page);
            },
            fill: function (page) {
                if (this.initRows) {
                    if (util.type(this.initRows) === 'object') {
                        this.pager.rowCount = 1;
                        this.rows = [];
                        this.rows.push(this.initRows);
                    } else if (util.type(this.initRows) === 'array') {
                        this.rows = [];
                        for (var i=0; i<this.initRows.length; i++) {
                            this.rows.push(this.initRows[i]);
                        }
                        this.pager.rowCount = this.rows.length;
                    }
                    this.dataInited = true;
                    return;
                }
                var vm = this;
                if (!page) {
                    page = 1;
                }

                var url = vm.url;
                if (!vm.inited || !vm.pagination || page !== vm.pager.currentPage) {
                    vm.pager.currentPage = page;

                    var data = vm.initRequestParam();
                    if (vm.pagination) {
                        data.offset = util.offset(vm.pager.currentPage, vm.pager.limit);
                        data.limit = vm.pager.limit;
                        vm.dataInited = false;
                        util.post({
                            url: url,
                            data: data,
                            success: function(res) {
                                if (util.success(res)) {
                                    var data = util.data(res);
                                    vm.rows = [];
                                    vm.$nextTick(function () {
                                        vm.rows = data.data;
                                        vm.pager.rowCount = util.type(data.count) === 'number' ? data.count : -1;
                                        if (this.pager.rowCount >= 0) {
                                            this.pager.totalPage = Math.ceil(this.pager.rowCount / this.pager.limit);
                                        } else {
                                            this.pager.totalPage = -1;
                                        }
                                        vm.dataInited = true;
                                    });
                                }
                            },
                            error: function() {
                            }
                        });
                    } else {
                        vm.dataInited = false;
                        util.post({
                            url: url,
                            data: data,
                            success: function(res) {
                                if (util.success(res)) {
                                    var data = util.data(res);
                                    vm.rows = [];
                                    vm.$nextTick(function () {
                                        vm.rows = data;
                                        vm.pager.rowCount = data.length;
                                        vm.dataInited = true;
                                    });
                                }
                            },
                            error: function() {
                            }
                        });
                    }
                }
            },
            initRequestParam: function () {
                var data = {};
                if (this.entityName) {
                    data.entityName = this.entityName;
                }
                if (!util.isEmpty(this.initParamList)) {
                    data.paramList = this.initParamList;
                }
                if (this.columnNames && this.columnNames.length > 0) {
                    data.columns = this.columnNames.join(',');
                }
                if (!util.isEmpty(this.confuses)) {
                    data.confuses = this.confuses;
                }
                if (this.orderList && this.orderList.length > 0 && !util.isEmpty(this.orderList[0])) {
                    data.orderList = this.orderList;
                }
                return data;
            }
        },
        template: util.template('bv-list')
    });
});
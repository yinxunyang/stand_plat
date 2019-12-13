/*
 * bv-table标签
 */
define([
    'vue',
    'jquery',
    'util',
    'Const'
], function (vue, $, util, Const) {
    vue.component('bv-table', {
        props: {
            /*
             * 用于util.vm作为序号获取子组件
             */
            index: {
                default: -1
            },
            /*
             * 实体类内容
             */
            entity: {
                default: function () {
                    return {};
                }
            },
            /*
             * 实体类的属性名，table容器的id
             */
            name: {
                default: ''
            },
            /*
             * 显示标题，可设置为空
             */
            title: {
                default: '信息列表'
            },
            /*
             * 是否允许折叠隐藏
             */
            collapse: {
                default: Const.init.table.collapse
            },
            /*
             * 组件布局方式
             * 默认自动判断，支持modal、body等
             */
            layout: {
                default: ''
            },
            /*
             * 标签大小，支持md
             * 用于处理多个table并排展示，造成精确查询显示不开的问题
             * @deprecated 计划作废
             */
            size: {
                default: 'fill'
            },
            /*
             * 是否固定表头
             */
            fixed: {
                default: true
            },
            /*
             * 是否静默，仅显示table的加载条，不显示util.loading
             */
            silence: {
                default: false
            },
            /*
             * 是否允许排序
             */
            sort: {
                default: true
            },
            /*
             * 容器class
             */
            clazz: {
                default: ''
            },
            /*
             * 每页显示数据条数
             * 手动设置后会保存到localStorage
             * 优先级：页面定义标签参数设置 > 用户自定义设置 > 默认10
             */
            limit: {
                default: 10
            },
            /*
             * 分页显示类型，支持normal,more
             */
            pageType: {
                default: 'normal'
            },
            /*
             * 显示页码数 值必须 > 5
             * pageType = more时有效
             */
            pageShow: {
                default: 7
            },
            /*
             * 显示类型default-默认sub-子表
             */
            type: {
                default: 'default'
            },

            /*
             * 是否自动加载，可设置为false禁止自动加载数据
             */
            loadType: {
                default: 'auto'
            },
            /*
             * 是否显示加载进度
             */
            progress: {
                default: true
            },
            /*
             * 分页方式，支持true-分页 false-不分页 'hide'-分页但不显示页码
             */
            pagination: {
                default: true
            },
            /*
             * 选择框false-无checkbox-复选框radio-单选按钮
             * 支持{type: 'select', show: function ()这种格式}
             */
            select: {
                default: false
            },
            /*
             * 序号显示方式 false-无page-当前页db-全局
             */
            linenumber: {
                default: false
            },
            /*
             * 过滤方式 default-自动page-当前页过滤false-不过滤db-后台数据库过滤
             */
            filterType: {
                default: 'default'
            },
            /*
             * 查询条件是否可见true-显示false-隐藏
             */
            filterVisible: {
                default: true
            },
            /*
             * 查询方式default-模糊查询、精确查询filter-只支持模糊查询filterMore-只支持精确查询none-无查询条件
             */
            filterLayout: {
                default: 'default'
            },
            /*
             * 查询条件是否水平布局
             */
            filterHorizontal: {
                default: true
            },
            /*
             * 是否默认精确查询
             */
            filterMore: {
                default: false
            },
            /*
             * 模糊查询字段来源：default-默认，数据库全部字段；filter-页面定义的filter不为false的字段；filterMore-等同于精确查询
             */
            filterColumnSource: {
                default: 'default'
            },
            /*
             * 限制查询条件提示信息，可设置为false
             */
            limitFilterDefaultOption: {
                default: '请选择限制条件'
            },
            /*
             * 查询条件form表单中label标签后面的字符
             */
            labelAppend: {
                default: '：'
            },
            /*
             * table的模糊查询：默认不支持date类型，number类型会做精确查询（=）
             * 可设置属性forceFilter：all/date-date类型会转成字符串做模糊匹配
             */
            forceFilter: {
                default: ''
            },
            /*
             * 是否允许过滤显示列
             */
            filterShow: {
                default: Const.init.table.filterShow
            },
            /*
             * 是否允许收起查询条件
             */
            filterToggle: {
                default: Const.init.table.filterToggle
            },
            /*
             * 是否显示刷新按钮
             */
            showRefresh: {
                default: false
            },

            /*
             * 查询数据url
             */
            url: {
                default: ''
            },
            /*
             * 模糊查询url，不设置默认使用url
             */
            filterUrl: {
                default: ''
            },
            /*
             * 精确查询url，不设置默认使用url
             */
            filterMoreUrl: {
                default: ''
            },
            /*
             * 是否支持行内编辑
             */
            editable: {
                default: false
            },
            /*
             * 当前是否是编辑状态，动态编辑table有效，暂时没用
             */
            inEdit: {
                default: false
            },
            /*
             * 默认编辑类型
             */
            defaultTag: {
                default: 'textfield'
            },
            /*
             * 实体类名，注意首字母小写，驼峰式命名
             */
            entityName: {
                default: ''
            },
            /*
             * 主键定义，格式为 key1, key2, ...
             */
            keys: {
                default: ''
            },
            // 支持guid，多行编辑用
            keyGenerator: {
                default: ''
            },
            /*
             * 格式:name type,name type
             * 设置为false表示不允许排序
             */
            orders: {
                default: ''
            },
            /*
             * @deprecated
             * 计划作废
             */
            orderList: {
                default: function () {
                    return [];
                }
            },
            /*
             * 表格初始数据
             */
            initRows: {
                default: ''
            },
            /*
             * 表格数据
             */
            rows: {
                default: function () {
                    return [];
                }
            },
            /*
             * 初始参数，json格式
             */
            initParam: {
                default: ''
            },
            /*
             * 初始参数，数组格式
             */
            /*
            initParamList: [
              {
                ors: [
                   {
                      name: 'xx1',
                      value: 'yy1'
                   }, {
                      name: 'xx2',
                      value: 'yy2'
                   }
                ]
              }
            ]
            =>
            ( xx1 = ? or xx2 = ? )
            */
            initParamList: {
                default: function () {
                    return [];
                }
            },
            /*
             * 是否清空初始化initParamList的参数
             */
            clearInitParam: {
                default: false
            },
            /*
             * 数据权限过滤用，行政区域编码对应的字段名
             */
            range: {
                default: ''
            },
            /*
             * 表头定义，支持属性 columnNum列数colspan，rowNum行数
             */
            thead: {
                default: function () {
                    return {};
                }
            },
            /*
             * 额外查询的属性名
             */
            extraColumnNames: {
                default: ''
            },
            /*
             * 列定义
             */
            columns: {
                default: function () {
                    return [];
                }
            },
            /*
             * 特殊过滤条件，定义不在columns定义的查询字段
             * 特殊属性： position-FIRST最前面LAST最后面BEFORE:xxx在xxx前面AFTER:xxx在xxx后面at:xxx替换xxx
             */
            extraFilters: {
                default: function () {
                    return [];
                }
            },
            /*
             * 默认过滤条件值
             */
            filterValue: {
                default: ''
            },
            /*
             * 操作按钮定义
             */
            operates: {
                default: function () {
                    return [];
                }
            },
            /*
             * 表头选中时调用
             */
            onCheck: {
                default: ''
            },
            /*
             * 选择显示内容名
             */
            chooseDesc: {
                default: ''
            },
            /*
             * 选择显示，函数
             */
            chooseShow: {
                default: ''
            },
            /*
             * 选择结果
             */
            chooseResult: {
                default: ''
            },

            // 生命周期回调
            onMounted: {
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
                keyList: [],
                checkedLength: 0,
                filterLayoutClass: '',
                // 允许查询的字段定义
                filterColumns: [],
                // 限定查询字段定义
                limitFilterColumns: [],
                limitFilterColumnName: '',
                // 精确查询条件定义
                filterEntity: {},
                //
                filterShowEntity: {
                    filterShowChoose: ''
                },
                // 用户具备权限的按钮
                authedOperates: [],
                // 当前页开始序号
                // offset: 0,
                visible: true,
                width: {},
                // 字典格式为：{xxx: [{}, {}], yyy: []}
                dicts: {},
                // 是否显示过滤列
                filterShowVisible: false,
                chooseArrayResult: [],
                // 允许过滤显示的列
                filterShowColumns: [],
                // 查询返回的字段
                columnNames: []
            };
        },
        computed: {
            offset: function () {
                return (this.pager.currentPage - 1) * this.pager.limit;
            }
        },
        watch: {
            rows: function () {
                // this.offset = (this.pager.currentPage - 1) * this.pager.limit;
                if ($(this.$el).length === 1) {
                    // 生成bootstrap的tooltip
//                    this.tableScroll();

                    if (this.rows.length > 0) {
                        var vm = this;
                        vue.nextTick(function () {
                            // 计算固定表头宽度
                            if (vm.fixed) {
                                vm.calc();
                                vm.calcHeight('update');
                            }
                            util.tooltip($(vm.$el), 'data-title');
                        });
                    }
                }
            },
            filterColumns: function () {
                this.$nextTick(function () {
                    if (this.filterLayout !== 'filter' && this.filterLayout !== 'none') {
                        util.validateInit($('#queryForm', this.$el));
                    }
                });
            }
        },
        beforeCreate: function () {
            // 选中数据
            this.bodyChecked = [];
            this.inited = false;
            // 模糊查询-查询条件
            this.filter = '';
            // 精确查询-查询条件
            this.query = [];
            // 排序字段名
            this.orderName = null;
            // 排序方式asc desc
            this.orderType = null;
            // 是否全选
            this.headChecked = false;
            // 加载进度
            this.progressing = 0;
            // 脱敏
            /**
             * 脱敏参数的使用，columns中某列的定义支持confuse属性，格式为：3-****-4表示前3后4显示，中间部分用4个*代替
             */
            this.confuses = {};
            // 模糊查询的字段定义，逗号分开
            this.filterColumnNames = '';
            // 当前登录用户具有权限的按钮定义
            this.userOperates = [];
            // 行内编辑新增时添加的行数据
            this.initRow = {
                _insert: true
            };
            this.inserts = [];
            this.updates = [];
            this.deletes = [];
            this.initFilterEntity = {};
            this.initFilterShowColumnNames = '';

            this.eventId = util.guid();
        },
        created: function () {
            // 参数校验
            if (this.loadType === 'false') {
                util.warn('loadType兼容性问题', '将\'false\'修改为false');
                this.loadType = false;
            }
            this.initFilterShowColumns();
        },
        mounted: function() {
            if (!this.name && this.$vnode.key) {
                this.name = this.$vnode.key;
            }
            util.initDefault(this);
            if (this.name) {
                this.$watch('pager.rowCount', function (value, oldValue) {
                    if (this.name) {
                        this.entity[this.name] = this.rows;
                    }
                });
            }
            if (this.filterLayout === 'filterMore') {
                this.filterMore = true;
            }
            if (this.type) {
                this.clazz += ' bv-table-' + this.type;
            }
            if (!this.pagination) {
                this.clazz += ' bv-table-no-pagination';
            }
            if (this.editable) {
                this.clazz += ' bv-table-editable';
            }
            // TODO:
            var userOperates = util.userOperates($(this.$el));
            if (userOperates) {
                this.userOperates = userOperates;
            }

            if (this.name) {
                this.entity[this.name] = this.rows;
            }

            if (!this.layout) {
                this.layout = util.layout($(this.$el));
            }
            if (this.type === 'sub' && this.layout === 'modal') {
                this.layout = 'body';
            }
            if ((this.type && this.type === 'sub') || this.layout === 'modal') {
                this.fixed = false;
            }

            if (this.type === 'choose') {
                if (!this.chooseDesc && this.keys) {
                    this.chooseDesc = this.keys;
                }
            }

            if (this.filterType === 'default') {
                if (this.pagination && !this.initRows) {
                    this.filterType = 'db';
                } else {
                    this.filterType = 'page';
                }
            }
            if (!this.url && !this.initRows) {
                if (this.pagination) {
                    this.url = Const.url.table.page;
                } else {
                    this.url = Const.url.table.select;
                }
            }
            if (!this.filterUrl) {
                this.filterUrl = this.url;
            }
            if (!this.filterMoreUrl) {
                this.filterMoreUrl = this.url;
            }
            // 从localStroage获取页码设置
            var limit = util.storage('limit', undefined, this.name);
            if (limit) {
                limit = util.toNumber(limit);
                if (limit > 0 && limit <= 500) {
                    this.pager.limit = limit;
                }
            } else if (this.type && this.type === 'sub') {
                this.pager.limit = 5;
            }

            var keyList;
            if (this.keys) {
                keyList = util.replaceAll(this.keys, ' ', '').split(',');
            }

            // 根据字段定义取出主键
            if (!keyList || keyList.length === 0) {
                keyList = [];
                for (var i = 0; i < this.columns.length; i++) {
                    var column = this.columns[i];
                    if (column.type && column.type === 'id') {
                        keyList.push(column.name);
                    }
                }
            }
            this.keyList = keyList;

            // 数据权限
            if (this.range) {
                if (Const.vm.root.user.areaRange) {
                    if (Const.vm.root.user.areaRange !== '*') {
                        if (util.endsWith(Const.vm.root.user.areaRange, '%')) {
                            this.initParamList.push({
                                name: this.range,
                                operate: 'like',
                                value: Const.vm.root.user.areaRange
                            });
                        } else {
                            this.initParamList.push({
                                name: this.range,
                                operate: '=',
                                value: Const.vm.root.user.areaRange
                            });
                        }
                    }
                }
            }

            var vm = this;
            vue.nextTick(function () {
                vm.initColumns();

                vue.nextTick(function () {
                    if (vm.loadType === 'auto' && (!vm.type || vm.type !== 'sub')) {
                        if (vm.filterMore) {
                            vue.nextTick(function () {
                                vm.filterMoreData();
                            });
                        } else {
                            vm.jumpTo();
                        }
                    } else if (vm.limitFilterDefaultOption !== false && vm.loadType === false) {
                        vm.pager.rowCount = 0;
                        vm.rows = [];
                    }
                });
            });

            // 判断按钮权限
            for (var i=0; i<this.operates.length; i++) {
                var operate = this.operates[i];
                if (operate.type && (operate.type === 'insert' || operate.type === 'update' || operate.type === 'delete'
                        || operate.type === 'redirect' || operate.type === 'sub' || operate.type === 'post' || operate.type === 'modal')) {
                    util.error('组件调用方式变更：operate.type属性含义已变更，请修改为operate.preset方式引用');
                    util.warn('将下面配置');
                    util.warn(util.clone(operate));
                    util.warn('修改为：');
                    var temp = util.clone(operate);
                    temp.preset = temp.type;
                    delete temp.type;
                    util.warn(temp);
                    operate.preset = operate.type;
                    delete operate.type;
                }
                if (operate.auth) {
                    if (this.userOperates && this.userOperates.length > 0) {
                        // 需要判断按钮权限
                        for (var j=0; j<this.userOperates.length; j++) {
                            var userOperate = this.userOperates[j];
                            if (util.type(userOperate) === 'string') {
                                if (operate.id === userOperate) {
                                    // operate.text = userOperate.buttonName;
                                    this.authedOperates.push(operate);
                                    break;
                                }
                            } else {
                                if (operate.id === userOperate[Const.menu.buttonId]) {
                                    if (userOperate[Const.menu.buttonName]) {
                                        operate.text = userOperate.buttonName;
                                    }
                                    // operate.text = userOperate.buttonName;
                                    this.authedOperates.push(operate);
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    this.authedOperates.push(operate);
                }
            }
            // 处理默认排序
            this.width = {
                checkbox: Const.width.checkbox,
                radio: Const.width.radio,
                linenumber: Const.width.linenumber
            };

            var $container = $(vm.$el);
            if (vm.fixed) {
                $container.closest('.bv-content,body').scroll(function() {
                    var top = $('.bv-table-thead', $container).position().top;
                    if (top <= 0) {
                        $('.bv-table-thead', $container).css('margin-top', -1 * top);
                    } else {
                        $('.bv-table-thead', $container).css('margin-top', 0);
                    }
                    // promptTopPosition += field.closest('.bv-content').getNiceScroll()[0].newscrolly;
                });
                $('.bv-table-body', $container).scroll(function() {
                    $('.bv-table-thead', $container).css('left', -1 * $(this).scrollLeft());

                    if ($('body > .bootstrap-select.open').length === 1) {
                        var $bsContainer = $('body > .bootstrap-select.open');
                        var $element = $('.bootstrap-select.open', vm.$el);
                        $bsContainer.addClass($element.attr('class').replace(/form-control|fit-width/gi, '')).toggleClass('dropup', $element.hasClass('dropup'));
                        var pos = $element.offset();
                        var containerPos = { top: 0, left: 0 };
                        var actualHeight = $element.hasClass('dropup') ? 0 : $element[0].offsetHeight;

                        $bsContainer.css({
                            'top': pos.top - containerPos.top + actualHeight,
                            'left': pos.left - containerPos.left,
                            'width': $element[0].offsetWidth
                        });
                    }
                });
            }
            vm.calcHeight('init');

            if (this.editable) {
                util.validateInit($('#editForm', this.$el), {
                    positionType: 'relative'
                });
            }

            if (this.layout === 'modal') {
                Const.vm.modal = this.$parent;
            }

            vm.initLayout();
            $(window).on('resize.' + vm.eventId, function () {
                if (util.isCurrent($(vm.$el))) {
                    if (vm.fixed) {
                        vm.calc();
                        vm.calcHeight('resize');
                    }
                }
                vm.initLayout();
            });

            if (util.type(this.onMounted) === 'function') {
                this.onMounted.call(this);
            } else {
                this.$emit('on-mounted', this);
            }
        },
        // body数据加载完成调用
        updated: function() {

        },
        beforeDestroy: function () {
            if (this.fixed) {
                $(this.$el).closest('.bv-content,body').unbind('scroll');
            }
            $(window).off('resize.' + this.eventId);
        },
        methods: {
            initLayout: function () {
                this.$nextTick(function () {
                    this.filterLayoutClass = util.layout($(this.$el).width());
                });
            },
            initColumns: function (columns) {
                if (columns) {
                    this.columns = columns;
                }
                if (this.chooseResult) {
                    if (util.type(this.chooseResult) === 'string') {
                        var arr = this.chooseResult.split(',');
                        var chooseResult = [];
                        for (var i=0; i<arr.length; i++) {
                            chooseResult.push({
                                code: arr[i],
                                show: arr[i]
                            });
                        }
                        this.chooseArrayResult = chooseResult;
                    } else {
                        this.chooseArrayResult = [];
                    }
                }

                var columnNames = [];
                if (this.extraColumnNames) {
                    var arr = this.extraColumnNames.split(',');
                    for (var i=0; i<arr.length; i++) {
                        columnNames.push(arr[i]);
                    }
                }
                var filterColumns = [];
                var filterResort = false;
                for (var i = 0; i < this.columns.length; i++) {
                    var column = this.columns[i];

                    if (Const.init.table.columnNames && column.name && (!this.columns[i].preset || this.columns[i].preset !== 'operate') && (!this.thead.rowNum || column.rowspan === this.thead.rowNum || column.row === this.thead.rowNum - 1)) {
                        columnNames.push(column.name);
                    }
                    if (column.confuse) {
                        this.confuses[column.name] = column.confuse;
                    }

                    /*if (util.contains(column.name, '.')) {
                        column.name = column.name.substring(column.name.indexOf('.') + 1);
                    }*/

                    if (column.width) {
                        if (Const.width[column.width]) {
                            column.width = Const.width[column.width];
                        }
                    }
                    // 初始化select查询
                    // TODO:计划删除
                    if (column.format) {
                        util.error('组件调用方式变更：column.format属性已弃用，请修改为column.config.format方式引用');
                        util.warn('将下面配置');
                        util.warn(util.clone(column));
                        util.warn('修改为：');
                        var temp = util.clone(column);
                        if (!temp.config) {
                            temp.config = {};
                        }
                        temp.config.format = temp.format;
                        delete temp.format;
                        util.warn(temp);
                        if (!column.config) {
                            column.config = {};
                        }
                        column.config.format = column.format;
                    }
                    if (column.config && column.config.format) {
                        if (column.config.format === 'file') {
                            column.align = 'right';
                        } else if (column.config.format === 'date' || column.config.format === 'datetime') {
                            column.align = 'center';
                        }
                    }
                    if (column.order !== undefined) {
                        util.error('组件调用方式变更：column.order属性已弃用，请修改为column.sort方式引用');
                        util.warn('将下面配置');
                        util.warn(util.clone(column));
                        util.warn('修改为：');
                        var temp = util.clone(column);
                        temp.sort = (temp.order === false ? false : temp.order);
                        delete temp.order;
                        util.warn(temp);
                        column.sort = (column.order === false ? false : column.order);
                        delete column.order;
                    }
                    if (column.sort === 'false') {
                        util.warn('column.sort兼容性问题', '将\'false\'修改为false');
                        column.sort = false;
                    }
                    if (column.config && column.config.format && column.config.format === 'dict') {
                        if (!this.dicts[column.alias || column.name]) {
                            var preset = column.config.preset || 'default';
                            var code = column.config.code;
                            var desc = column.config.desc;
                            if (preset && !code) {
                                code = Const.init.preset[preset].code;
                                desc = Const.init.preset[preset].desc;
                            }
                            var initParamList = column.config.initParamList || [];
                            if (!util.isEmpty(column.config.initParam)) {
                                var data;
                                if (util.type(column.config.initParam) === 'function') {
                                    data = column.config.initParam.call(null, '');
                                } else {
                                    data = column.config.initParam;
                                }
                                for (var p in data) {
                                    initParamList.push({
                                        name: p,
                                        operate: '=',
                                        value: data[p]
                                    });
                                }
                            }
                            // util.clone(this.dicts, {});
                            vue.set(this.dicts, column.alias || column.name, {
                                url: column.config.url || Const.url.select.query,
                                name: column.alias || column.name,
                                entityName: column.config.entityName,
                                preset: preset,
                                code: code,
                                desc: desc,
                                initParamList: initParamList,
                                choose: column.config.choose,
                                options: ''
                            });
                            /// column.config.format = column.format;
                            // column.config.choose = this.dicts[column.name].options;

                            util.initSelectData(this.dicts[column.alias || column.name], 'dict');
                        }
                    } else if (column.href && column.href.preset === 'sub') {
                        if (!column.config) {
                            column.config = {};
                        }
                        /// column.config.type = 'a';
                    }
                    if (column.filter && column.filter !== false) {
                        column = this.initFilterColumn(column);
                        filterColumns.push(column);
                        if (column.filter === 'false') {
                            util.warn('util.filter兼容性问题', '将\'false\'修改为false');
                            column.filter = false;
                        }
                        if (column.filter.limit === 'false') {
                            util.warn('util.filter.limit兼容性问题', '将\'false\'修改为false');
                            column.filter.limit = false;
                        }
                        // limitFilterColumns
                        if (column.filter.limit) {
                            this.limitFilterColumns.push({
                                name: column.name,
                                head: column.head,
                                operate: column.filter.limit
                            });
                        }
                    }
                    if (this.filterColumnSource === 'filter' && column.filter !== false) {
                        if (this.filterColumnNames) {
                            this.filterColumnNames += ',';
                        }
                        this.filterColumnNames += column.name;
                    }

                    // 处理行内编辑
                    if (this.editable && (!column.type || column.type !== 'operate')) {
                        if (!column.editConfig) {
                            column.editConfig = {};
                        }
                        /*if (!column.edit) {
                            column.edit = {};
                        }*/
                        if (!column.edit || !column.edit.type) {
                            if (column.href) {
                                column.type = 'href';
                            } else {
                                column.type = this.defaultTag || 'textfield';
                            }
                        } else {
                            column.type = column.edit.type;
                        }
                        if (column.alias || column.name) {
                            column.editConfig.name = column.alias || column.name;
                        }

                        // 行内编辑
                        this.initRow[column.alias || column.name] = null;
                    } else if (!column.type) {
                        if (column.href) {
                            column.type = 'href';
                        } else {
                            column.type = 'static';
                        }
                    }

                    // title处理
                    if (!column.title) {
                        column.title = '';
                    }
                    if (column.title === true) {
                        column.title = 'true';
                    }

                    if (column.filter && column.filter.position) {
                        filterResort = true;
                    }

                    if (column.edit && column.edit.type && util.type(column.edit.type) === 'array') {
                        // column.edit.inEdit = false;
                        this.$set(column.edit, 'inEdit', false);
                    }

                    this.columns[i] = column;
                }
                // 判断主键是否定义
                if (this.keyList && this.keyList.length > 0) {
                    for (var k=0; k<this.keyList.length; k++) {
                        if (columnNames.indexOf(this.keyList[k]) < 0) {
                            columnNames.push(this.keyList[k]);
                        }
                    }
                }
                if (columnNames && columnNames.length > 0) {
                    this.columnNames = columnNames;
                }
                // this.$set(this.filterShowEntity, 'filterShowChoose', filterShowChoose);
                // this.filterShowEntity.filterShowChoose = filterShowChoose;
                this.initFilterShow();
                if (this.extraFilters && this.extraFilters.length > 0) {
                    for (var i=0; i<this.extraFilters.length; i++) {
                        if (!this.extraFilters[i].filter) {
                            this.extraFilters[i].filter = {};
                        }
                        if (!this.extraFilters[i].filter.type) {
                            this.extraFilters[i].filter.type = 'textfield';
                        }
                        if (!this.extraFilters[i].filter.operate) {
                            this.extraFilters[i].filter.operate = 'like';
                        }
                        filterColumns.push(this.initFilterColumn(this.extraFilters[i]));

                        if (this.extraFilters[i].filter.position) {
                            filterResort = true;
                        }
                    }
                }
                // 排序
                if (filterResort) {
                    var a, b, temp;
                    var filterColumnsClone = util.clone(filterColumns);
                    for (var i=0; i<filterColumns.length; i++) {
                        for (var j=0; j<filterColumnsClone.length; j++) {
                            a = filterColumns[i];
                            b = filterColumnsClone[j];
                            if (a.name === b.name || (!a.filter.position && !b.filter.position) || a.filter.position === 'FIRST' || b.filter.position === 'LAST'
                                || (a.filter.position && a.filter.position.indexOf('BEFORE:') === 0) || (b.filter.position && b.filter.position.indexOf('AFTER:') === 0)) {
                                continue;
                            }
                            if (a.filter.position === 'LAST') {
                                temp = filterColumns[i];
                                filterColumns[i] = filterColumns[j];
                                filterColumns[j] = temp;
                            } else if (a.filter.position && a.filter.position.indexOf('AFTER:') === 0) {
                                var name = a.filter.position.substring(6);
                                if (name && name === b.name) {
                                    temp = filterColumns[i];
                                    filterColumns[i] = filterColumns[j];
                                    filterColumns[j] = temp;
                                }
                                continue;
                            } else if (b.filter.position === 'FIRST') {
                                temp = filterColumns[i];
                                filterColumns[i] = filterColumns[j];
                                filterColumns[j] = temp;
                            } else if (b.filter.position && b.filter.position.indexOf('BEFORE:') === 0) {
                                var name = b.filter.position.substring(7);
                                if (name && name === a.name) {
                                    temp = filterColumns[i];
                                    filterColumns[i] = filterColumns[j];
                                    filterColumns[j] = temp;
                                }
                                continue;
                            }
                        }
                    }
                    filterColumnsClone.splice(0, filterColumnsClone.length);
                    delete filterColumnsClone;
                }

                this.filterColumns = filterColumns;

                this.filterEntity = util.clone(this.initFilterEntity);

                if (!util.isEmpty(this.limitFilterColumns)) {
                    if (this.limitFilterColumns.length > 1 && !util.isEmpty(this.limitFilterDefaultOption) && this.limitFilterDefaultOption !== false) {
                        this.limitFilterColumns.splice(0, 0, {
                            name: '',
                            head: this.limitFilterDefaultOption
                        });
                    }
                    if (this.limitFilterColumns.length === 1) {
                        this.limitFilterColumnName = this.limitFilterColumns[0].name;
                    }
                }

                if (!this.thead.columnNum || columns) {
                    // 计算总列数
                    var columnNum = this.columns.length;
                    // if (this.select === 'checkbox' || this.select === 'radio') {
                    if (this.select) {
                        columnNum++;
                    }
                    if (this.linenumber !== false) {
                        columnNum++;
                    }
                    this.$set(this.thead, 'columnNum', columnNum);
                } else {
                    if (this.select) {
                        this.thead.columnNum++;
                    }
                    if (this.linenumber !== false) {
                        this.thead.columnNum++;
                    }
                }
            },
            initFilterShowColumns: function () {
                var filterShowColumns = [];
                var filterShowChoose = '';
                for (var i=0; i<this.columns.length; i++) {
                    if (!this.columns[i].hide) {
                        if (filterShowChoose) {
                            filterShowChoose += ',';
                        }
                        filterShowChoose += this.columns[i].name;
                        filterShowColumns.push({
                            name: this.columns[i].name,
                            head: this.columns[i].head
                        });
                    }
                }
                this.filterShowColumns = filterShowColumns;
                this.initFilterShowColumnNames = filterShowChoose;
            },
            initFilterColumn: function (column) {
                // 取出精确查询字段
                // column.filter column.config
                if (column.filter === true || column.filter === 'like') {
                    column.filter = {
                        type: 'textfield',
                        operate: 'like'
                    };
                } else if (column.filter === '=') {
                    column.filter = {
                        type: 'textfield',
                        operate: '='
                    };
                } else if (column.filter === 'select') {
                    column.filter = {
                        type: 'select',
                        operate: '='
                    };
                } else if (column.filter === 'radio') {
                    column.filter = {
                        type: 'radio',
                        operate: '='
                    };
                } else if (column.filter === 'checkbox') {
                    column.filter = {
                        type: 'checkbox',
                        operate: '='
                    };
                } else if (column.filter === 'auto') {
                    column.filter = {
                        type: 'auto',
                        operate: '='
                    };
                } else if (column.filter === 'between') {
                    column.filter = {
                        operate: 'between'
                    };
                } else if (util.type(column.filter) === 'object') {
                    if (column.filter.type && !column.filter.operate) {
                        if (column.filter.type === 'select' || column.filter.type === 'radio' || column.filter.type === 'checkbox' || column.filter.type === 'auto') {
                            column.filter.operate = '=';
                        }
                    }
                }
                if (column.filter.operate === 'false') {
                    util.warn('column.filter.operate兼容性问题', '将\'false\'修改为false');
                    column.filter.operate = false;
                }
                if (column.filter.operate !== false) {
                    if (!column.config) {
                        column.config = {};
                    }
                    if (!column.filterConfig) {
                        column.filterConfig = {};
                    }
                    if (!column.filterLayout) {
                        column.filterLayout = {};
                    }
                    if (!column.filter.type) {
                        column.filter.type = 'textfield';
                    }
                    if (column.filter.operate && column.filter.operate === 'between') {
                        column.filterLayout.containerClass = 'container-auto container-2';
                        column.filterConfig.start = (column.alias || column.name) + 'Start';
                        column.filterConfig.end = (column.alias || column.name) + 'End';
                        this.initFilterEntity[column.filterConfig.start] = null;
                        this.initFilterEntity[column.filterConfig.end] = null;

                        column.config.operate = column.filter.operate;
                        column.filterLayout.for = column.filterConfig.start;

                        if (column.config.period) {
                            column.filterConfig.range = (column.alias || column.name) + 'Range';
                            this.initFilterEntity[column.filterConfig.range] = null;
                        }

                        column.filterConfig.tagName = 'bv-' + column.filter.type;
                        column.filterConfig.operateTagName = 'bv-between';
                    } else {
                        if (column.filter.width === 2) {
                            column.filterLayout.containerClass = 'container-2';
                            /*column.filterLayout.containerClass = 'col-md-6';
                            column.filterLayout.labelClass = 'col-sm-2 col-sm-2y col-md-2';
                            column.filterLayout.tagClass = 'col-sm-10 col-sm-10x col-md-10';*/
                        } else {
                            column.filterLayout.containerClass = 'container-auto';
                            /*column.filterLayout.containerClass = 'col-sm-6 col-md-4 col-lg-3';
                            column.filterLayout.labelClass = 'col-sm-4';
                            column.filterLayout.tagClass = 'col-sm-8';*/
                        }
                        if (this.filterValue && (this.filterValue[(column.alias || column.name) + 'Filter'] || this.filterValue[column.alias || column.name])) {
                            this.initFilterEntity[(column.alias || column.name) + 'Filter'] = this.filterValue[(column.alias || column.name) + 'Filter'] || this.filterValue[column.alias || column.name];
                        } else {
                            this.initFilterEntity[(column.alias || column.name) + 'Filter'] = null;
                        }

                        column.filterLayout.for = (column.alias || column.name) + 'Filter';
                        column.filterConfig.operateTagName = 'bv-' + column.filter.type;
                        column.filterConfig.tagName = 'bv-' + column.filter.type;
                    }
                    if (!column.filterConfig.tagName) {
                        column.filterConfig.tagName = 'bv-textfield';
                    }
                    if (column.alias || column.name) {
                        column.filterConfig.name = (column.alias || column.name) + 'Filter';
                    }
                    column.filterConfig.from = 'filter';

                    if (this.filterColumnSource === 'filterMore') {
                        if (this.filterColumnNames) {
                            this.filterColumnNames += ',';
                        }
                        this.filterColumnNames += column.name;
                    }
                }
                return column;
            },
            showColumn: function (name) {
                for (var i=0; i<this.columns.length; i++) {
                    if (this.columns[i].name === name) {
                        this.$set(this.columns[i], 'show', true);
                        break;
                    }
                }
            },
            hideColumn: function (name) {
                for (var i=0; i<this.columns.length; i++) {
                    if (this.columns[i].name === name) {
                        this.$set(this.columns[i], 'show', false);
                        break;
                    }
                }
            },
            calc: function() {
                var w = $('.bv-table-tbody', this.$el).width();
                $('.bv-table-thead', this.$el).width(w).css({
                    'min-width': w,
                    'max-width': w
                });
                var vm = this;
                $('.bv-table-thead thead th:not(.fixed,.th-select,.th-linenumber,.th-progress)', vm.$el).each(function(index, element) {
                    var w = $('.bv-table-tbody thead th:not(.fixed,.th-select,.th-linenumber,.th-progress)', vm.$el).eq(index).width();
                    $(element).width(w);
                    $('.bv-table-tbody thead th:not(.fixed,.th-select,.th-linenumber,.th-progress)', vm.$el).eq(index).width(w);
                });
            },
            calcHeight: function(from) {
                if (this.fixed) {
                    var toolbarHeight = $('.bv-table-toolbar', this.$el).outerHeight(true);
                    var filterHeight = 0;
                    if (this.filterVisible) {
                        if (this.filterMore) {
                            filterHeight += $('.bv-table-filter-more', this.$el).outerHeight(true) + 4;
                        } else {
                            filterHeight = $('.bv-table-filter', this.$el).outerHeight(true);
                        }
                    }
                    if (this.layout === 'body' && $(this.$el).closest('.bv-content,body').length === 1) {
                        /*if (!this.tableHeight) {
                         this.tableHeight = $(this.$el).closest('.bv-content').height() - 5;
                         }*/
                        var tableContainerHeight = $(this.$el).closest('.bv-content,body').height() - 5 - ($(this.$el).offset().top - $(this.$el).closest('.bv-content,body').offset().top);
                        /// $('.bv-table-container', this.$el).css('max-height', tableContainerHeight);

                        var footerHeight = 0;
                        if (this.pagination && this.pagination !== 'hide') {
                            footerHeight = $('.bv-table-footer', this.$el).outerHeight(true);
                            // extraHeight += 25;
                        }
                        var tableBodyHeight = tableContainerHeight - toolbarHeight - filterHeight - footerHeight;
                        $('.bv-table-body', this.$el).css('max-height', tableBodyHeight);
                    }

                    // this.tableTop = toolbarHeight + filterHeight;
                    /// $('.bv-table-thead', this.$el).css('top', toolbarHeight + filterHeight);
                    /*if ($('.bv-table-tbody', this.$el).width() > 0) {
                        $('.bv-table-thead', this.$el).width($('.bv-table-tbody', this.$el).width());
                    }*/
                    $('.bv-table-thead', this.$el).css('top', $('.bv-table-tbody', this.$el).position.top);
                    /*if (from && from === 'init') {
                        $('.bv-table-thead', this.$el).show();
                    }*/
                }
            },
            isShowCheckboxOrRadio: function (row) {
                if (!this.select) {
                    return false;
                }
                /*if (this.select !== 'checkbox' || this.select.type !== 'checkbox') {
                    return false;
                }*/
                if (this.select.show === undefined || this.select.show === true) {
                    return true;
                }
                if (util.type(this.select.show) === 'function') {
                    return this.select.show.call(null, row);
                }
            },
            allowSort: function (column) {
                return this.sort && (column.alias || column.name) && (!column.type || column.type !== 'operate') && column.sort !== false;
            },
            isButtonCreate: function (data) {
                if (!data || data.create === true) {
                    return true;
                }
                if (util.type(data.create) === 'function') {
                    return data.create.call(this, util.selected(this), this.checkedLength);
                }
                return true;
            },
            isButtonVisible: function(data) {
                if (!data || data.show === false) {
                    return false;
                }
                if (data.show === 'none') {
                    return this.checkedLength === 0;
                } else if (data.show === 'one') {
                    return this.checkedLength === 1;
                } else if (data.show === 'oneOrMore') {
                    return this.checkedLength >= 1;
                } else if (data.show === 'import') {
                    return data.url || data.page;
                } else if (data.show === 'export') {
                    return data.url || data.page;
                } else if (data.show === 'return') {
                    return data.url || util.type(data.click) === 'function';
                } else if (util.type(data.show) === 'function') {
                    return data.show.call(this, util.selected(this), this.checkedLength);
                } else if (util.type(data.check) === 'function') {
                    return data.check.call(this, this.checkedLength);
                } else if (!data.show) {
                    return true;
                }
                return true;
            },
            jumpTo: function(page) {
                this.hideSub();
                this.fill(page);
            },
            initChecked: function() {
                this.onHeadCheck(undefined, false, true);
                this.bodyChecked = [];
                this.checkedLength = 0;
                if (this.pager.rowCount >= 0) {
                    this.pager.totalPage = Math.ceil(this.pager.rowCount / this.pager.limit);
                } else {
                    this.pager.totalPage = -1;
                }

                $('tbody :checkbox, tbody :radio', $(this.$el).closest('.bv-table-container')).prop('checked', false);

                if (this.type === 'choose' && this.chooseArrayResult.length > 0 && this.rows.length > 0) {
                    this.$nextTick(function () {
                        for (var i=0; i<this.rows.length; i++) {
                            for (var j=0; j<this.chooseArrayResult.length; j++) {
                                if (this.chooseArrayResult[j].code === this.rows[i][this.keys]) {
                                    this.onBodyCheck(undefined, i);
                                    break;
                                }
                            }
                        }
                    });
                }
                this.checkedLength = this.bodyChecked.length;
            },
            refresh: function(event, initParamList) {
                this.hideSub();
                if (initParamList) {
                    this.initParamList = initParamList;
                }
                this.fill('refresh');
                /*if (this.filterLayout === 'filterMore') {
                    this.doFilterMore();
                } else {
                    this.fill('refresh');
                }*/
            },
            handleProgress: function(t, isFail) {
                if (!this.progress) {
                    return;
                }
                var $progress = $('.bv-progress-container', this.$el);
                if ($progress.length === 1) {
                    if (!t) {
                        this.progressing = 0;
                        $('.progress', $progress).hide();
                        $('.progress', $progress).removeClass('error');
                        $('.progress-bar', $progress).css('width', '0');
                        $('.progress-bar', $progress).removeClass('progress-bar-success progress-bar-danger');
                        setTimeout(function() {
                            $('.progress', $progress).show();
                        }, 50);
                        var vm = this;
                        return setInterval(function() {
                            if (vm.progressing <= 60) {
                                vm.progressing += 20;
                            } else if (vm.progressing <= 70) {
                                vm.progressing += 10;
                            } else if (vm.progressing <= 80) {
                                vm.progressing += 5;
                            } else if (vm.progressing <= 90) {
                                vm.progressing += 2;
                            } else if (vm.progressing <= 95) {
                                vm.progressing += 1;
                            } else if (vm.progressing <= 98) {
                                vm.progressing += 0.2;
                            } else if (vm.progressing <= 99) {
                                vm.progressing += 0.1;
                            } else if (vm.progressing <= 99.5) {
                                vm.progressing += 0.01;
                            }
                            $('.progress-bar', $progress).css('width', vm.progressing + '%');
                        }, 100);
                    } else {
                        this.progressing = 100;
                        this.initChecked();
                        var vm = this;
                        setTimeout(function() {
                            clearInterval(t);
                            $('.progress-bar', $progress).css('width', vm.progressing + '%');
                            if (isFail) {
                                $('.progress-bar', $progress).addClass('progress-bar-danger');
                                $('.progress', $progress).addClass('error');
                            } else {
                                vm.inited = true;
                                $('.progress-bar', $progress).addClass('progress-bar-success');
                            }
                            util.scroll($(vm.$el));
                        }, 200);
                    }
                }
            },
            // table加载数据
            // table用
            fill: function(page) {
                // 加上后页面刷新显示效果不太合理
                /// this.pager.rowCount = 0;
                /// this.rows.removeAll();
                // 去除自定义标签的缓存 name + '-' + el.name + '-td' + '-' + i
                /*if (Const.global[this.name] && Const.global[this.name].length > 0) {
                    util.clearVmCache(Const.global[this.name]);
                 Const.global[this.name] = [];
                }*/
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
                    //this.inited = true;
                    this.initChecked();
                    var t = this.handleProgress();
                    this.handleProgress(t);
                    this.dataInited = true;
                    return;
                }
                var vm = this;
                var refresh = false;
                if (!page) {
                    page = 1;
                }
                if (page === 'refresh') {
                    refresh = true;
                    page = vm.pager.currentPage;
                } else if (page === 'limit' || page === 'order' || page === 'filter') {
                    refresh = true;
                    vm.pager.currentPage = 1;
                    page = 1;
                }

                var url = vm.url;
                if (!vm.inited || !vm.pagination || refresh || page !== vm.pager.currentPage) {
                    vm.pager.currentPage = page;

                    var data = vm.initRequestParam();
                    if (!vm.filterMore && vm.filter) {
                        if (vm.limitFilterColumnName) {
                            url = vm.filterMoreUrl;
                        } else {
                            url = vm.filterUrl;
                        }
                    }
                    if (vm.filterMore && vm.query && vm.query.length > 0) {
                        url = vm.filterMoreUrl;
                    }
                    if (vm.pagination) {
                        var t = vm.handleProgress();
                        data.offset = util.offset(vm.pager.currentPage, vm.pager.limit);
                        data.limit = vm.pager.limit;
                        vm.dataInited = false;
                        util.post({
                            url: url,
                            data: data,
                            silence: vm.silence,
                            loading: vm.$el,
                            success: function(res) {
                                if (util.success(res)) {
                                    var data = util.data(res);
                                    if (!util.isEmpty(data.filterValue)) {
                                        vm.filterValue = data.filterValue;
                                    } else {
                                        vm.filterValue = '';
                                    }
                                    if (data.columns && data.columns.length > 0) {
                                        vm.initColumns(data.columns);
                                        vm.initFilterShowColumns();
                                    }
                                    vm.rows = [];
                                    vm.$nextTick(function () {
                                        vm.rows = data.data;
                                        vm.pager.rowCount = util.type(data.count) === 'number' ? data.count : -1;
                                        vm.handleProgress(t);
                                        vm.dataInited = true;
                                    });
                                }
                            },
                            error: function() {
                                vm.rows = [];
                                vm.$nextTick(function () {
                                    vm.handleProgress(t, true);
                                });
                            }
                        });
                    } else {
                        var t = vm.handleProgress();
                        vm.dataInited = false;
                        util.post({
                            url: url,
                            data: data,
                            silence: vm.silence,
                            loading: vm.$el,
                            success: function(res) {
                                if (util.success(res)) {
                                    var data = util.data(res);
                                    vm.rows = [];
                                    vm.$nextTick(function () {
                                        vm.rows = data;
                                        vm.pager.rowCount = data.length;
                                        vm.handleProgress(t);
                                        vm.dataInited = true;
                                    });
                                }
                            },
                            error: function() {
                                vm.rows = [];
                                vm.$nextTick(function () {
                                    vm.handleProgress(t, true);
                                });
                            }
                        });
                    }
                }
            },
            toImport: function(event, config) {
                util.modal({
                    url: config.page || Const.url.template.imports,
                    data: {
                        url: config.url,
                        template: config.template,
                        async: config.async
                    },
                    refresh: true,
                    vm: this
                });
            },
            toExport: function(event, config) {
                // this.filterMoreData();
                var data = this.initRequestParam();
                if (config.url) {
                    data.url = config.url;
                }
                if (config.template) {
                    data.template = config.template;
                }
                util.modal({
                    url: config.page || Const.url.template.exports,
                    data: data
                });
            },
            initRequestParam: function () {
                var data = {};
                // 实体类名
                if (this.entityName) {
                    data.entityName = this.entityName;
                }
                // 初始查询条件，合并到paramList
                if (!util.isEmpty(this.initParamList)) {
                    data.paramList = this.initParamList;
                }
                // 查询的列名（空的话返回实体类所有列）
                if (this.columnNames && this.columnNames.length > 0) {
                    data.columns = this.columnNames.join(',');
                }
                // 脱敏的列及规则
                if (!util.isEmpty(this.confuses)) {
                    data.confuses = this.confuses;
                }
                // 模糊查询条件
                if (!this.filterMore && this.filter) {
                    if (this.limitFilterColumnName) {
                        var $limitFilter = {
                            name: this.limitFilterColumnName
                        };
                        for (var i=0; i<this.limitFilterColumns.length; i++) {
                            if (this.limitFilterColumns[i].name === this.limitFilterColumnName) {
                                $limitFilter.operate = this.limitFilterColumns[i].operate;
                            }
                        }
                        if ($limitFilter.operate === 'like') {
                            $limitFilter.value = this.filter;
                        } else {
                            $limitFilter.value = this.filter;
                        }
                        if (!data.paramList || data.paramList.length === 0) {
                            data.paramList = [$limitFilter];
                        } else {
                            data.paramList.push($limitFilter);
                        }
                    } else {
                        //console.log('$');
                        //一进入页面带有固定参数的情况,再点击查询时应该清除点原初始化进入页面的条件  add by 'zsc'  else前面的那段逻辑没来得及明白应用场景
                        if (this.clearInitParam){
                            data.paramList = undefined;
                        }

                        data.filter = {
                            value: this.filter
                        };
                        if (this.filterColumnNames) {
                            data.filter.columns = this.filterColumnNames;
                        }
                    }
                }
                // 精确查询条件，合并到paramList
                if (this.filterMore && this.query && this.query.length > 0) {
                    //add by 'zsc'  clearFlag为true不赋值,默认为false赋值
                    if (this.clearInitParam || !data.paramList || data.paramList.length === 0) {
                        data.paramList = this.query;
                    } else {
                        data.paramList = data.paramList.concat(this.query);
                    }
                }
                /*if (this.customParam) {
                    var paramMap = {};
                    for (var i=0; i<data.paramList.length; i++) {
                        paramMap[data.paramList[i].name] = data.paramList[i].value;
                    }
                    data.paramMap = paramMap;
                }*/
                // 排序方式
                if (this.orderList && this.orderList.length > 0 && !util.isEmpty(this.orderList[0])) {
                    data.orderList = this.orderList;
                }
                //
                if (this.forceFilter) {
                    data.forceFilter = this.forceFilter;
                }
                return data;
            },
            initColumnType: function (row, column) {
                if (column.type) {
                    if (util.type(column.type) === 'function') {
                        return 'bv-' + column.type.call(this, row, column);
                    } else if (util.type(column.type) === 'array' && column.type.length === 2) {
                        if (row._inEdit && column.edit && column.edit.inEdit) {
                            return 'bv-' + column.type[1];
                        }
                        return 'bv-' + column.type[0];
                    }
                    return 'bv-' + column.type;
                }
            },
            columnFormat: function(row, column) {
                if (util.type(column.config && column.config.format) === 'function') {
                    return column.config.format.call(null, row);
                } else if (column.config && column.config.format === 'dict') {
                    return util.format(util.trans(row[column.alias || column.name], this.dicts[column.alias || column.name]), column.config.format);
                }
                return util.format(util.trans(row[column.alias || column.name], column.config), column.config && column.config.format);
            },
            columnTitle: function(row, column) {
                if (column.title) {
                    if (column.title === 'true') {
                        return row[column.alias || column.name];
                    } else if (util.startsWith(column.title, '#')) {
                        return row[column.title.substring(1)] || '';
                    } else {
                        return column.title;
                    }
                }
                return '';
            },
            columnAttr: function(row, column, index) {
                var result = {};

                if (util.type(index) !== 'undefined') {
                    result['data-index'] = index;
                }
                result['data-name'] = column.alias || column.name;
                result['data-value'] = (column.config && (column.config.text || column.config.value)) || row[column.alias || column.name];
                var title = this.columnTitle(row, column);
                if (title) {
                    result.attr = {};
                    result.attr['data-title'] = title;
                    result.attr['data-original-title'] = title;
                    /*result['data-title'] = title;
                    result['data-original-title'] = title;*/
                }
                /*var param = {
                };*/
                if (!util.isEmpty(column.href)) {
                    /// attr.type = 'a';
                    result.href = column.href;
                    result.click = this.href;
                    /// result.parent = this;
                } else if (column.type && column.type === 'operate') {
                    result.click = this.click;
                    result.operates = column.operates;
                }
                /*if (!util.isEmpty(column.sub)) {
                    param.sub = column.sub;
                    param.icon = 'icon-more';
                }*/
                /// param.$vm = this;

                ///if (!this.editable) {
                    // row[column.name + 'Static'] = this.columnFormat(row, column);
                if (!column.type || column.type === 'static' || column.type === 'href' || (util.type(column.type) === 'array') && column.type.indexOf('static') >= 0) {
                    result['text'] = row[column.alias || column.name];  // this.columnFormat(row, column);
                }
                ///}

                result.name = column.alias || column.name;
                /*if (!util.isEmpty(param)) {
                    result.param = param;
                }*/
                return result;
                // return attr;
            },
            onHeadClick: function(event, column) {
                var $head = $(event.target);
                if (!$head.hasClass('bv-order')) {
                    $head = $head.closest('th');
                }
                this.bodyChecked = [];
                this.checkedLength = 0;
                if ($head.hasClass('bv-order')) {
                    this.hideSub();
                    if ($head.hasClass('bv-order-asc')) {
                        this.orderType = 'desc';
                    } else if ($head.hasClass('bv-order-desc')) {
                        this.orderType = 'asc';
                    } else {
                        this.orderType = 'asc';
                    }
                    // 允许指定排序字段
                    this.orderName = util.type(column.sort) === 'string' ? column.sort : column.name;
                    this.orderList = [{
                        name: this.orderName,
                        sort: this.orderType
                    }];
                    if (this.pagination) {
                        this.fill('order');
                    } else {
                        var orderName = this.orderName;
                        var orderType = this.orderType;

                        var rows = util.clone(this.rows);
                        this.rows = [];
                        this.$nextTick(function () {
                            this.rows = util.orderBy(rows, orderName, orderType && orderType === 'desc');
                            this.calc();
                            this.calcHeight('resize');
                        });
                    }
                }
            },
            // event：事件，暂时不用
            // checked：表头是否选中
            // updateBody：表头选中状态不更新body
            onHeadCheck: function(event, checked, updateBody) {
                var $header = $('thead tr :checkbox', this.$el);
                if (util.type(checked) === 'undefined') {
                    // 点击触发
                    if (util.checked($header)) {
                        this.headChecked = true;
                    } else {
                        this.headChecked = false;
                    }
                } else {
                    // 手工触发
                    if (checked) {
                        if (checked === true) {
                            this.headChecked = true;
                            util.checked($header, true, false);
                        } else if (checked === 'half') {
                            this.headChecked = false;
                            util.checked($header, false, true);
                        }
                    } else {
                        this.headChecked = false;
                        util.checked($header, false, false);
                    }
                }
                if (updateBody) {
                    if (this.headChecked) {
                        util.checked($('tbody :checkbox', this.$el), true);
                        this.bodyChecked = [];
                        for (var i=0; i<this.rows.length; i++) {
                            this.bodyChecked.push(i);
                        }
                    } else {
                        util.checked($('tbody :checkbox', this.$el), false);
                        this.bodyChecked = [];
                    }
                    this.checkedLength = this.bodyChecked.length;
                }
                if (event) {
                    if (this.type === 'choose') {
                        this._chooseCompare();
                    }
                }
                if (util.type(this.onCheck) === 'function') {
                    this.onCheck.call(null, 'body', util.selected(this));
                }
            },
            onBodyCheck: function(event, index) {
                if (!this.select) {
                    return;
                }
                var $check;
                if (util.type(index) !== 'undefined') {
                    $check = $(':checkbox', $('.bv-table-tbody tbody tr', this.$el).eq(index));
                    util.checked($check, true);
                } else {
                    $check = $(event.target);
                }
                var $tr = $check.closest('tr');

                // var $tr = $(event.target).closest('tr');
                if (this.select === 'checkbox' || this.select.type === 'checkbox') {
                    // var index = $tr.attr('data-index');
                    if (util.checked($check)) {
                        this.bodyChecked.push(util.toNumber($tr.attr('data-index')));
                    } else {
                        var i = util.index(this.bodyChecked, util.toNumber($tr.attr('data-index')));
                        if (i >= 0) {
                            this.bodyChecked.splice(i, 1);
                        }
                    }
                    // $('.bv-table-tbody tbody tr .table-select checkbox', this.$el)
                    // if (this.bodyChecked.length === this.rows.length) {
                    if (this.bodyChecked.length === $('.bv-table-tbody tbody tr .table-select :checkbox', this.$el).length) {
                        this.onHeadCheck(undefined, true);
                    } else if (this.bodyChecked.length === 0) {
                        this.onHeadCheck(undefined, false);
                    } else {
                        this.onHeadCheck(undefined, 'half');
                    }
                    this.checkedLength = this.bodyChecked.length;
                } else if (this.select === 'radio' || this.select.type === 'radio') {
                    this.bodyChecked = [util.toNumber($tr.attr('data-index'))];
                    this.checkedLength = this.bodyChecked.length;
                }

                if (event) {
                    if (this.type === 'choose') {
                        this._chooseCompare();
                    }
                }
            },
            href: function(event, row, column) {
                if (!row) {
                    /// TODO: 待确认
                    var index = util.toNumber($(event.target).closest('tr').attr('data-index'));
                    if (!util.isEmpty(index) && index >= 0) {
                        row = this.rows[index];
                    }
                }
                if (!column) {
                    var name = $(event.target).attr('data-name');
                    if (name) {
                        for (var i=0; i<this.columns.length; i++) {
                            if (name === this.columns[i].name) {
                                column = this.columns[i];
                            }
                        }
                    }
                }
                if (util.type(column.href) === 'object') {
                    this.handleOperate(event, column.href, [row]);
                    /*if (column.href.preset) {
                        if (column.href.preset === 'sub') {
                            if (column.href.config) {
                                this.triggerSub(event, column.href.config);
                            }
                        }
                    }*/
                } else if (util.type(column.href) === 'function') {
                    if (this.keyList.length === 1) {
                        column.href.call(null, event, this.name, row[this.keyList[0]], row, column);
                    } else {
                        var idValues = {};
                        for (var i=0; i<this.keyList.length; i++) {
                            idValues[this.keyList[i]] = row[this.keyList[i]];
                        }
                        column.href.call(null, event, this.name, idValues, row, column);
                    }
                }
            },
            doFilter: function(event) {
                this.hideSub();
                if (this.filterType === 'page' && this.loadType !== false) {
                    this.onHeadCheck(undefined, false, true);
                    this.bodyChecked = [];
                    this.checkedLength = 0;
                    // 当前页查询
                    $('tbody tr', $(this.$el)).hide().filter(":contains('" + this.filter + "')").show();

                    this.calc();
                    this.calcHeight('resize');
                } else {
                    // 后台查询
                    this.fill('filter');
                }
            },
            doFilterMore: function(event) {
                if (event && event.which === 13) {
                    event.preventDefault();
                    return;
                }
                if (!util.validate($('#queryForm', this.$el))) {
                    return;
                }
                this.hideSub();
                this.filterMoreData();
            },
            filterMoreData: function() {
                this.query = [];

                for (var i=0; i<this.filterColumns.length; i++) {
                    var column = this.filterColumns[i];
                    if (this.filterCheck(column, 'default')) {
                        if (!util.isEmpty(this.filterEntity[(column.alias || column.name) + 'Filter'])) {
                            var operate = column.filter.operate;
                            if (!operate) {
                                operate = 'like';
                            } else if (util.type(column.filter.operate) === 'function') {
                                operate = column.filter.operate.call(null, this.name, this.filterEntity);
                            }
                            var value = this.filterEntity[(column.alias || column.name) + 'Filter'];
                            if (util.type(column.filter.value) === 'function') {
                                value = column.filter.value.call(null, this.name, this.filterEntity);
                            }
                            this.query.push({
                                name: column.name,
                                operate: operate,
                                value: value,
                                format: (column.config && column.config.format) || ''
                            });
                        }
                    } else if (this.filterCheck(column, 'between')) {
                        if (!util.isEmpty(this.filterEntity[column.filterConfig.start])) {
                            var valueStart = this.filterEntity[column.filterConfig.start];
                            if (column.config && column.config.format === 'timestamp') {
                                valueStart = Date.parse(new Date(valueStart));
                            }
                            this.query.push({
                                name: column.name,
                                operate: '>=',
                                value: valueStart,
                                format: (column.config && column.config.format) || ''
                            });
                        }
                        if (this.filterEntity[column.filterConfig.end]) {
                            var valueEnd = this.filterEntity[column.filterConfig.end];
                            if (column.config && column.config.format === 'timestamp') {
                                valueEnd = Date.parse(new Date(valueEnd));
                            }
                            this.query.push({
                                name: column.name,
                                operate: '<=',
                                value: valueEnd,
                                format: (column.config && column.config.format) || ''
                            });
                        }
                    }
                }

                var vm = this;
                if (this.filterType === 'page' && this.loadType !== false) {
                    if (vm.query.length === 0) {
                        $("tbody tr", this.$el).show();
                    } else {
                        $("tbody tr", this.$el).hide().filter(function() {
                            var match = true;
                            for (var i=0; i<vm.query.length; i++) {
                                var $element = $("[data-name='" + vm.query[i].name + "']", $(this));
                                if (vm.query[i].operate === 'like') {
                                    if ($element.text().indexOf(vm.query[i].value) < 0 && $element.attr('data-value') != vm.query[i].value) {
                                        match = false;
                                    }
                                } else {
                                    if ($element.text() !== vm.query[i].value && $element.attr('data-value') != vm.query[i].value) {
                                        match = false;
                                    }
                                }
                            }
                            return match;
                        }).show();
                        this.calc();
                        this.calcHeight('resize');
                    }
                } else {
                    this.fill('filter');
                }
            },
            toggleShowOrHide: function (event) {
                this.filterVisible = !this.filterVisible;
                if (!this.filterVisible) {
                    this.filterShowVisible = false;
                }
                this.calcHeight('filter');
            },
            initFilterMore: function(event, type) {
                this.filterShowVisible = false;
                this.filterVisible = true;
                if (type && type !== 'reset') {
                    this.filterMore = !this.filterMore;
                }
                if (this.filterMore) {
                    // 显示精确查询
                    $('.bv-table-filter .bv-left', this.$el).hide();
                    $('.bv-table-filter-more', this.$el).show();
                    $('.bv-table-filter .bv-right #filterToggleButton i', this.$el).removeClass('icon-more').addClass('icon-less');
                    $('.bv-table-filter .bv-right #filterToggleButton span', this.$el).text('模糊查询');
                } else {
                    // 显示模糊查询
                    $('.bv-table-filter-more', this.$el).hide();
                    $('.bv-table-filter .bv-left', this.$el).show();
                    $('.bv-table-filter .bv-right #filterToggleButton i', this.$el).removeClass('icon-less').addClass('icon-more');
                    $('.bv-table-filter .bv-right #filterToggleButton span', this.$el).text('精确查询');
                }
                this.filter = '';
                $('#filter', this.$el).val('');

                if (type && type === 'reset') {
                    util.clone(this.filterEntity, this.initFilterEntity);
                    // this.filterEntity = util.clone(this.initFilterEntity);
                    if (Const.init.table.filterResetQuery) {
                        this.doFilterMore();
                    }
                }

                this.calcHeight('filter');
            },
            initOrder: function(name, sort) {
                if (this.orderList && this.orderList.length > 0) {
                    for (var i=0; i<this.orderList.length; i++) {
                        if (name === this.orderList[i].name || (util.type(sort) === 'string' && sort === this.orderList[i].name)) {
                            return !this.orderList[i].sort || this.orderList[i].sort === 'asc' ? 'bv-order-asc' : 'bv-order-desc';
                        }
                    }
                }
            },
            columnShowOrHide: function(column) {
                if (util.isTrue(column.hide)) {
                    return false;
                }
                return util.isTrue(column.show, true);
            },
            filterName: function(el, type, name) {
                if (this.filterCheck(el, type)) {
                    return name;
                }
                return name + '-' + type;
            },
            filterCheck: function(el, type) {
                if (type === 'default') {
                    return !el.filter.operate || el.filter.operate === 'like' || el.filter.operate === '='
                        || el.filter.operate === '>' || el.filter.operate === '>='
                        || el.filter.operate === '<' || el.filter.operate === '<='
                        || el.filter.operate === 'in' || el.filter.operate === 'is' || el.filter.operate === 'is not'
                        || el.filter.operate === 'custom' || util.type(el.filter.operate) === 'function';
                    // return !el.filter.operate || el.filter.operate === 'like' || el.filter.operate === '=' || el.filter.operate === 'select';
                } else if (type === 'between') {
                    return el.filter.operate === 'between';
                }
                if (type === 'text') {
                    return !el.filter.type || el.filter.type === 'text';
                }
                return el.filter.type === type;
            },
            pressOnFilter: function(event) {
                if (event.which === 13) {
                    event.preventDefault();
                    this.doFilter();
                }
                this.filter = $(event.target).val();
            },
            setLimit: function(limit) {
                this.hideSub();
                // 保存每页显示数到localStorage
                this.pager.limit = limit;
                util.storage('limit', this.pager.limit, this.name);
                this.fill('limit');
            },
            setPage: function(page) {
                this.hideSub();
                this.fill(page);
            },
            /*            tableScroll: function() {
             util.scroll($(this.$el).closest('.bv-content'));
             // util.scroll($('.bv-table-body', this.$el));
             },*/
            triggerSub: function(event, sub) {
                var $trigger = $(event.target);
                if ($trigger.is('i')) {
                    $trigger = $trigger.closest('a');
                }
                if ($('i', $trigger).hasClass('icon-more')) {
                    $('i.icon-less', $trigger.closest('table')).removeClass('icon-less').addClass('icon-more');
                    $('<tr class="bv-table-sub-container-tr"><td colspan="' + this.thead.columnNum + '"></td></tr>').insertAfter($trigger.closest('tr'));
                    this.hideSub();
                    $('#' + sub.id + '-container').show().appendTo($('td', $trigger.closest('tr').next()));
                    var subVm = util.vm(this.$parent, sub.id);
                    if (subVm && sub.initParamList) {
                        var initParamList = sub.initParamList;
                        for (var i=0; i<initParamList.length; i++) {
                            var param = initParamList[i];
                            var index = $trigger.closest('tr').attr('data-index');
                            if (param.name && param.from) {
                                Const.global.row = util.clone(this.rows[index]);   // util.find(this.rows, this.id, idValue);
                                if (Const.global.row) {
                                    param.value = Const.global.row[param.from];
                                    initParamList[i] = param;
                                }
                            }
                        }
                        subVm.refresh(undefined, initParamList);
                    }
                    $('i', $trigger).removeClass('icon-more').addClass('icon-less');
                } else {
                    $('i', $trigger).removeClass('icon-less').addClass('icon-more');
                    $('#' + sub.id + '-container').hide().insertAfter($trigger.closest('.bv-table-container'));
                    $trigger.closest('tr').next().remove();
                }

                this.calcHeight('triggerSub');
//                this.tableScroll();
            },
            hideSub: function() {
                if (!this.type || this.type !== 'sub') {
                    var $container = this.$el;
                    $('.bv-table-sub', $container).each(function() {
                        var $tr = $(this).closest('tr');
                        // TODO:
                        $(this).closest('.bv-table').hide().insertAfter($container);
                        $tr.remove();
                    });
                    $('table i.icon-less', $container).removeClass('icon-less').addClass('icon-more');

                    this.calcHeight('hideSub');
//                    this.tableScroll();
                }
            },
            click: function(event, operate, position) {
                var selected = [];
                var index;
                if (position === 'head') {
                    // 表头按钮
                    if (operate && util.type(operate.check) === 'function') {
                        selected = this.rows;
                    } else {
                        selected = util.selected(this);
                    }
                } else if (position === 'body') {
                    // body按钮
                    index = util.toNumber($(event.target).closest('tr').attr('data-index'));
                    if (!util.isEmpty(index) && index >= 0) {
                        selected.push(this.rows[index]);
                    }
                }
                this.handleOperate(event, operate, selected, index);
            },
            handleOperate: function (event, operate, selected, index) {
                if (util.type(operate.click) === 'function') {
                    if (util.type(index) === 'undefined') {
                        var indexes = [];
                        if (this.linenumber) {
                            for (var i=0; i<this.bodyChecked.length; i++) {
                                indexes.push(this.initIndex(this.bodyChecked[i]));
                            }
                        }
                        if (this.editable) {
                            if (util.validate($('#editForm', this.$el))) {
                                operate.click.call(this, event, this.name, this.entityName, selected, indexes);
                            }
                        } else {
                            operate.click.call(this, event, this.name, this.entityName, selected, indexes);
                        }
                    } else {
                        var i;
                        if (this.linenumber) {
                            i = this.initIndex(index);
                        }
                        if (this.editable) {
                            if (util.validate($('#editForm', this.$el))) {
                                operate.click.call(this, event, this.name, this.entityName, selected, i);
                            }
                        } else {
                            operate.click.call(this, event, this.name, this.entityName, selected, i);
                        }
                    }
                } else if (operate.preset) {
                    if (operate.preset === 'insert') {
                        if (this.editable) {
                            this.rows.push(util.clone(this.initRow));
                            // this.inserts.push(this.rows[this.rows.length - 1]);
                            this.pager.rowCount = this.rows.length;
                        } else {
                            if (operate.url) {
                                if (operate.prepare && util.type(operate.prepare) === 'function') {
                                    operate.prepare.call(null);
                                }
                                util.modal({
                                    url: operate.url,
                                    refresh: true,
                                    vm: this
                                });
                            }
                        }
                    } else if (operate.preset === 'update') {
                        if (operate.url) {
                            if (operate.prepare && util.type(operate.prepare) === 'function') {
                                operate.prepare.call(null);
                            }
                            util.modal({
                                url: util.mix(operate.url, {
                                    type: 'update'
                                }),
                                data: (selected && selected.length > 0) ? selected[0] : (this.rows && this.rows.length > 0 ? this.rows[0] : {}),
                                refresh: true,
                                vm: this
                            });
                        }
                    } else if (operate.preset === 'save') {
                        if (this.editable) {
                            for (var i=0; i<this.rows.length; i++) {
                                if (this.rows[i]._insert) {
                                    delete this.rows[i]._insert;
                                    this.inserts.push(this.rows[i]);
                                } else {
                                    this.updates.push(this.rows[i]);
                                }
                            }
                            var vm = this;
                            util.request({
                                $element: event.target,
                                type: 'post',
                                url: operate.url || Const.url.table.save,
                                data: {
                                    entityName: this.entityName,
                                    generator: this.keyGenerator,
                                    inserts: this.inserts,
                                    updates: this.updates,
                                    deletes: this.deletes
                                },
                                success: function(data) {
                                    vm.refresh(event);
                                    vm.inserts = [];
                                    vm.updates = [];
                                    vm.deletes = [];
                                }
                            });
                        }
                    } else if (operate.preset === 'delete') {
                        if (this.editable) {
                            // selected
                            if (this.bodyChecked && this.bodyChecked.length > 0) {
                                this.bodyChecked.sort(function (a, b) {
                                    return a === b ? 0 : a > b ? -1 : 1;
                                });
                                for (var i=0; i<this.bodyChecked.length; i++) {
                                    this.deletes.push(util.id(this.keyList, this.rows[this.bodyChecked[i]]));
                                    this.rows.splice(this.bodyChecked[i], 1);
                                }
                                this.initChecked();
                                this.pager.rowCount = this.rows.length;
                            }
                        } else {
                            util.confirm({
                                type: 'delete',
                                url: operate.url || '',
                                vm: this
                            });
                        }
                    } else if (operate.preset === 'redirect') {
                        if (operate.url) {
                            if (util.type(operate.url) === 'object') {
                                util.warn('不兼容警告', '系统不再支持operate.url为object类型');
                                return;
                            }
                            // 支持operate.target（新页面，暂不支持）
                            if (Const.init.layout === 'tabs') {
                                if (operate.data && util.type(operate.data) === 'function') {
                                    util.redirect(util.mix(operate.url, operate.data.call(null, event, this.name, this.entityName, selected)), 'body');
                                } else {
                                    util.redirect(operate.url, 'body');
                                }
                            } else if (Const.init.layout === 'route') {
                                if (operate.data && util.type(operate.data) === 'function') {
                                    if (operate.target && operate.target === '_blank') {
                                        util.open(Const.route.baseLocation + Const.route.versionLocation + '/#!' + util.mix(operate.url, operate.data.call(null, event, this.name, this.entityName, selected)));
                                    } else {
                                        util.redirect({
                                            url: util.mix(operate.url, operate.data.call(null, event, this.name, this.entityName, selected)),
                                            popstate: operate.popstate,
                                            ignore: operate.ignore,
                                            back: operate.back
                                        }, 'body');
                                    }
                                } else {
                                    util.redirect(operate.url, 'body');
                                }
                            }
                        }
                    } else if (operate.preset === 'post') {
                        if (operate.url) {
                            // post方式调用url，参数为当前选中数据第一条对应的id
                            util.post({
                                url: operate.url,
                                data: util.id(this.keyList, selected[0]),
                                refresh: true,
                                vm: this,
                                success: operate.success
                            });
                        }
                    } else if (operate.preset === 'sub') {
                        if (operate.config) {
                            this.triggerSub(event, operate.config);
                        }
                    } else if (operate.preset === 'modal') {
                        if (operate.url) {
                            if (operate.prepare && util.type(operate.prepare) === 'function') {
                                operate.prepare.call(null);
                            }
                            util.modal({
                                url: operate.url,
                                vm: this
                            });
                        }
                    }
                } else if (operate.show === 'return') {
                    if (operate.url) {
                        util.redirect(operate.url, 'body');
                    }
                }
            },
            isRequired: function(tagName, column) {
                if (tagName === 'bv-static') {
                    return false;
                }
                return util.isRequired(column.validate, column.attr);
            },
            initIndex: function (index) {
                var start = 0;
                if (this.linenumber === 'page') {
                    start += 1;
                } else if (this.linenumber === 'db') {
                    start += util.offset(this.pager.currentPage, this.pager.limit) + 1;
                }
                return index + start;
            },
            toggleFilterShow: function (event) {
                this.filterShowVisible = !this.filterShowVisible;
                if (this.filterShowVisible) {
                    this.filterVisible = true;
                }
            },
            showToggleEdit: function (column) {
                return column.edit && column.edit.type && util.type(column.edit.type) === 'array';
            },
            toggleEdit: function (row, column) {
                this.$set(row, '_inEdit', !row._inEdit);
                column.edit.inEdit = !column.edit.inEdit;
                if (!column.edit.inEdit) {
                    if (util.type(column.edit.save) === 'function') {
                        column.edit.save.call(this, row, column, function (isSuccess) {
                            if (!isSuccess) {
                                row[column.alias || column.name] = row._oldValue;
                                delete row._oldValue;
                            }
                        });
                    }
                } else if (row._oldValue === undefined) {
                    row._oldValue = row[column.alias || column.name];
                }
            },
            saveFilterShow: function () {
                if (this.filterShowEntity.filterShowChoose) {
                    var showColumns = this.filterShowEntity.filterShowChoose.split(',');
                    var columnNames = []; // this.extraColumnNames;
                    if (this.extraColumnNames) {
                        var arr = this.extraColumnNames.split(',');
                        for (var i=0; i<arr.length; i++) {
                            columnNames.push(arr[i]);
                        }
                    }
                    for (var i=0; i<this.columns.length; i++) {
                        if (showColumns.indexOf(this.columns[i].name) >= 0) {
                            // this.columns[i].show = false;
                            if (Const.init.table.columnNames && this.columns[i].name && (!this.columns[i].preset || this.columns[i].preset !== 'operate')) {
                                columnNames.push(this.columns[i].name);
                            }
                            this.$set(this.columns[i], 'show', true);
                        } else {
                            this.$set(this.columns[i], 'show', false);
                        }
                    }
                    if (Const.init.table.columnNames) {
                        this.columnNames = columnNames;
                    }
                }
                this.filterShowVisible = false;
                this.refresh();
            },
            initFilterShow: function () {
                /// this.entity.filterShowChoose = this.initFilterShowColumnNames;
                var filterShowVm = util.vm(this, 'filterShowChoose');
                if (filterShowVm) {
                    filterShowVm.changeSelected(this.initFilterShowColumnNames);
                }
            },
            confirmChoose: function(event) {
                var codes = '';
                var descs = '';
                for (var i=0; i<this.chooseArrayResult.length; i++) {
                    if (codes) {
                        codes += ',';
                        descs += ',';
                    }
                    codes += this.chooseArrayResult[i].code;
                    descs += this.chooseArrayResult[i].desc;
                }
                this.$emit('on-choose', this.name, {
                    codes: codes,
                    descs: descs
                });
                util.modal('hide');
            },
            _chooseCompare: function() {
                if (this.rows && this.rows.length > 0 && this.chooseArrayResult && this.chooseArrayResult.length > 0) {
                    for (var i=0; i<this.rows.length; i++) {
                        var index = util.index(this.chooseArrayResult, this.rows[i][this.keys], 'code');
                        if (index >= 0) {
                            this.chooseArrayResult.splice(index, 1);
                        }
                    }
                }
                // 处理本页选择
                if (this.bodyChecked && this.bodyChecked.length > 0) {
                    for (var i=0; i<this.bodyChecked.length; i++) {
                        var row = this.rows[this.bodyChecked[i]];
                        if (this.chooseArrayResult && this.chooseArrayResult.length > 0) {
                            var found = false;
                            for (var j=0; j<this.chooseArrayResult.length; j++) {
                                if (this.chooseArrayResult[j][this.keys] === row[this.keys]) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                this.chooseArrayResult.push({
                                    code: row[this.keys],
                                    desc: row[this.chooseDesc],
                                    show: util.type(this.chooseShow) === 'function' ? this.chooseShow.call(null, row) : row[this.chooseDesc]
                                });
                            }
                        } else {
                            this.chooseArrayResult.push({
                                code: row[this.keys],
                                desc: row[this.chooseDesc],
                                show: util.type(this.chooseShow) === 'function' ? this.chooseShow.call(null, row) : row[this.chooseDesc]
                            });
                        }
                    }
                }
            },

            // 外部调用
            execute: function (param) {
                if (param.checked === false) {
                    this.onHeadCheck(undefined, false, true);
                    return;
                }
                if (param.get) {
                    if (param.get === 'count') {
                        return this.pager.rowCount || 0;
                    } else if (param.get === 'orderList') {
                        return this.orderList;
                    }
                }
                if (param.query) {
                    this.filterMore = true;
                    this.query = param.query;
                }
                if (param.initParam) {
                    var paramList = [];
                    for (var p in param.initParam) {
                        paramList.push({
                            name: p,
                            operate: '=',
                            value: param.initParam[p]
                        });
                    }
                    this.initParamList = paramList;
                }
                if (param.initParamList) {
                    this.initParamList = param.initParamList;
                }
                if (param.initRows) {
                    this.initRows = param.initRows;
                }
                if (param.chooseResult) {
                    this.chooseArrayResult = param.chooseResult;
                }
                if (param.columns) {
                    this.initColumns(param.columns);
                }
                if (param.show) {
                    if (param.show.name) {
                        this.showColumn(param.show.name);
                    }
                    if (param.show.names) {
                        var names = param.show.names.split(',');
                        for (var i=0; i<names.length; i++) {
                            this.showColumn($.trim(names[i]));
                        }
                    }
                    if (!param.type) {
                        param.type = 'showOrHide';
                    }
                }
                if (param.hide) {
                    if (param.hide.name) {
                        this.hideColumn(param.hide.name);
                    }
                    if (param.hide.names) {
                        var names = param.hide.names.split(',');
                        for (var i=0; i<names.length; i++) {
                            this.hideColumn($.trim(names[i]));
                        }
                    }
                    if (!param.type) {
                        param.type = 'showOrHide';
                    }
                }
                if (param.appendRows) {
                    // 追加数据
                    var t;
                    if (param.showProgress) {
                        t = this.handleProgress();
                    }
                    this.rows.push.apply(this.rows, param.appendRows);
                    this.pager.rowCount = this.pager.rowCount + param.appendRows.length;
                    this.dataInited = true;
                    if (param.showProgress) {
                        var vm = this;
                        setTimeout(function () {
                            vm.handleProgress(t);
                        }, 200);
                    }
                } else if (param.rows) {
                    var t = this.handleProgress();
                    this.rows = [];
                    this.$nextTick(function () {
                        this.rows = param.rows;
                        this.pager.rowCount = param.count;
                        this.handleProgress(t);
                        this.dataInited = true;
                    });
                }
                if (param.type === 'calc') {
                    vue.nextTick(function () {
                        this.calc();
                    });
                } else if (param.type !== 'showOrHide' && param.type !== 'noRefresh') {
                    // type支持load-仅加载一次，空或refresh-每次都做加载
                    if (!param.type || param.type === 'refresh' || param.type !== 'load' || !this.inited) {
                        if (util.type(param.page) !== 'undefined') {
                            this.pager.currentPage = param.page;
                        }
                        this.refresh();
                    }
                }
            },
        },
        /****** 模板定义 ******/
        template: util.template('bv-table')
    });

    vue.component('bv-operate', {
        props: {
            name: {
                default: ''
            },
            clazz: {
                default: ''
            },
            attr: {
                default: function () {
                    return {};
                }
            },
            entity: {
                default: function () {
                    return {};
                }
            },
            from: {
                default: ''
            },
            click: {
                default: ''
            },
            operates: {
                default: ''
            }
        },
        methods: {
            checkShow: function(op) {
                if (op.show === undefined) {
                    return true;
                } else if (util.type(op.show) === 'function') {
                    return op.show.call(null, this.entity);
                }
                return op.show;
            },
            _click: function(event, op, position) {
                this.click.call(this.$parent, event, op, position);
            }
        },
        /****** 模板定义 ******/
        template: util.template('bv-operate')
    });

    // 目前应用场景为table的精确查询
    vue.component('bv-between', {
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
            tagName: {
                default: ''
            },
            start: {
                default: ''
            },
            end: {
                default: ''
            },
            range: {
                default: ''
            },
            format: {
                default: ''
            },
            // 格式:{units: ['秒', '分'], options: [5， 10， 15], input: false}
            // units支持秒、分、时、天、月、年
            // input为true时允许输入
            period: {
                default: ''
            },
            from: {
                default: ''
            },
            operate: {
                default: ''
            },
            operateTagName: {
                default: ''
            },
            defaultValueFrom: {
                default: ''
            },
            defaultValueTo: {
                default: ''
            }
        },
        /****** 模板定义 ******/
        template: util.template('bv-between')
    });
});
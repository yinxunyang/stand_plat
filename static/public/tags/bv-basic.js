/**
 * 基础组件，包含 bv-static bv-label bv-textfield bv-date bv-hidden bv-textarea bv-select bv-radio bv-checkbox bv-auto bv-toggle bv-button bv-href bv-image
 */
define([
    'vue',
    'jquery',
    'util',
    'Const',
    'moment'
], function (vue, $, util, Const, moment) {
    /**
     * 静态文本
     */
    vue.component('bv-static', {
        props: {
            entity: {
                default: function () {
                    return {};
                }
            },

            // html属性定义开始
            id: {
                default: ''
            },
            name: {
                default: ''
            },
            // class
            clazz: {
                default: ''
            },
            // 属性定义
            attr: {
                default: function () {
                    return {};
                }
            },
            // html属性定义结束
            // 默认值
            defaultValue: {
                default: ''
            },
            // 初始值
            value: {
                default: ''
            },
            // 设置值
            text: {
                default: ''
            },
            // 来源
            from: {
                default: ''
            },
            // 格式化
            format: {
                default: ''
            },
            // 自定义显示
            show: {
                default: ''
            },
            // dicts来源于table
            dicts: {
                default: ''
            },
            // 字典预设
            preset: {
                default: 'default'
            },
            code: {
                default: ''
            },
            desc: {
                default: ''
            },
            // 字典项翻译
            choose: {
                default: ''
            },
            url: {
                default: Const.url.select.query
            },
            entityName: {
                default: ''
            },
            initParam: {
                default: ''
            },
            initParamList: {
                default: function () {
                    return [];
                }
            },
            orders: {
                default: ''
            }
        },
        data: function () {
            return {
                // 静态文本不需要多余的属性配置
                staticAttr: {},
                showText: '',
                options: []
            }
        },
        created: function() {
            util.initDefault(this);

            // 静态文本组件对于attr属性只读取其中一部分（id、name、title）过来
            if (this.attr.id) {
                this.staticAttr.id = this.attr.id;
            } else if (this.id) {
                this.staticAttr.id = this.id;
            }
            if (this.attr.name) {
                this.staticAttr.name = this.attr.name;
            } else if (this.name) {
                this.staticAttr.name = this.name;
            }

            this.initAttr();
        },
        mounted: function () {
            if (this.from === 'table') {
                this.showText = this.text;
                // 如果是字典
                if (this.format === 'dict') {
                    // 初始化字典选项
                    this.initDictVal();
                    // 监听字典选项的变动（动态刷新显示内容）
                    this.$watch('dicts.' + this.name + '.options', function (value, oldValue) {
                        this.initDictVal();
                    });
                } else {
                    this.initVal(this.showText);

                    // table编辑用
                    this.$watch('entity.' + this.name, function(value, oldValue) {
                        this.initVal(value);
                    });
                }
            } else if (this.from === 'form') {
                this.showText = this.value || this.defaultValue || this.entity[this.name];
                if (this.format === 'dict') {
                    util.initSelectData(this, 'dict');
                    this.initVal(this.showText);
                } else {
                    this.initVal(this.showText);
                }
                this.$watch('options', function (value, oldValue) {
                    this.initVal(this.showText);
                });
                this.$watch('entity.' + this.name, function(value, oldValue) {
                    this.initAttr();
                    this.initVal(value);
                });
            } else if (this.from === 'operate') {
                this.showText = this.text;
            }
            /*if (this.format === 'dict') {
                this.initDictVal();
            } else {
                this.initVal(this.showText);
            }*/
            /*if (this.format === 'dict') {
                this.initVal(this.value);
            }*/
        },
        /*mounted: function () {
            if (this.from === 'table') {
                this.value = this.text;
                this.$watch('text', function(val, oldVal) {
                    this.initAttr();
                    this.value = val;
                });
            } else {
                this.initVal(this.value);
                this.$watch('entity.' + this.name, function(val, oldVal) {
                    this.initAttr();
                    this.initVal(val);
                });
            }
        },*/
        methods: {
            initAttr: function () {
                // this.innerClass = this.clazz || '';
                /// this.attr = {};//this.attr || {};
                if (this.attr.title || this.attr['data-title']) {
                    this.staticAttr.title = this.attr.title || this.attr['data-title'];
                    this.staticAttr['data-title'] = this.attr.title || this.attr['data-title'];
                    this.staticAttr['data-original-title'] = this.attr.title || this.attr['data-title'];
                    this.clazz += 'abbr';
                    /*if (!this.href) {
                        this.innerClass += 'abbr';
                    }*/
                }
                if (this.from) {
                    this.clazz += ' from-' + this.from;
                }
            },
            initVal: function (value) {
                if (this.show && util.type(this.show) === 'function') {
                    this.showText = util.format(this.show.call(null, this.entity, value, this.name), this.format);
                } else {
                    this.showText = util.format(util.trans(value, {
                        // preset: this.preset,
                        code: this.code,
                        desc: this.desc,
                        options: this.options
                    }), this.format);
                }
            },
            initDictVal: function () {
                if (this.dicts && this.dicts[this.name] && this.dicts[this.name].options && this.dicts[this.name].options.length > 0) {
                    for (var i=0; i<this.dicts[this.name].options.length; i++) {
                        if (this.entity[this.name] === this.dicts[this.name].options[i][this.code]) {
                            this.showText = this.dicts[this.name].options[i][this.desc] || this.dicts[this.name].options[i][this.code];
                        }
                    }
                }
            },

            execute: function (param) {
                if (util.type(param.text) !== 'undefined') {
                    this.initVal(param.text);
                }
            }
        },
        /****** 模板定义 ******/
        template: util.template('bv-static')
    });

    vue.component('bv-label', {
        props: {
            clazz: {
                default: ''
            }
            /*wrap: {
                default: false
            }*/
        },
        created: function () {
            /// this.eventId = util.guid();
        },
        mounted: function () {
            /*var vm = this;
            util.sync(function () {
                // 判断label是否换行
                util.wrap($(vm.$el));
                ///$(vm.$el).removeClass('wrap');
            });
            $(window).on('resize.' + vm.eventId, vm.$el, function () {
                util.sync(function () {
                    util.wrap($(vm.$el));
                });
            });*/
        },
        beforeDestroy: function () {
            /// $(window).off('resize.' + this.eventId, this.$el);
        },
        /****** 模板定义 ******/
        template: util.template('bv-label')
    });

    vue.component('bv-textfield', {
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
            // 大小，支持lg
            size: {
                default: ''
            },
            // 外层容器class
            containerClass: {
                default: ''
            },
            // class
            clazz: {
                default: ''
            },
            // 属性定义
            attr: {
                default: function () {
                    return {};
                }
            },
            // 默认值
            defaultValue: {
                default: ''
            },
            // 初始值
            value: {
                default: ''
            },

            // 数据校验
            validate: {
                default: ''
            },
            // 输入限制
            fix: {
                default: ''
            },

            type: {
                default: 'text'
            },
            // 是否显示图标
            addon: {
                default: ''
            },
            onChange: {
                default: ''
            },
            chooseUrl: {
                default: ''
            },
            onChoosePreset: {
                default: ''
            }
        },
        created: function() {
            util.initDefault(this);
            util.initId(this);
            if (!this.attr.type) {
                this.attr.type = this.type;
            }
        },
        mounted: function () {
            if (this.addon) {
                $('input', this.$el).on('focus', function() {
                    $(this).closest('.input-group').addClass('focus');
                });
                $('input', this.$el).on('blur', function() {
                    $(this).closest('.input-group').removeClass('focus');
                });
            }
        },
        beforeDestroy: function () {
            if (this.addon) {
                $('input', this.$el).off('focus').off('blur');
            }
        },
        methods: {
            onBlur: function (event) {
                if (this.onChange && util.type(this.onChange) === 'function') {
                    this.onChange.call(this, this.entity, this.entity[this.name]);
                }
            },
            checkInput: function(event) {
                if (this.fix) {
                    this.entity[this.name] = util.fix(event, this.fix);
                }
            },
            addonClick: function (event) {
                if (this.addon && this.addon.focus) {
                    $('input', this.$el).focus();
                }
                if (this.addon && util.type(this.addon.click) === 'function') {
                    this.addon.click.call(null, event);
                }
            },
            openModal: function () {
                if (util.type(this.onChoosePreset) === 'function') {
                    this.onChoosePreset.call(null, this.entity);
                }
                util.modal({
                    url: this.chooseUrl,
                    vm: this
                    /*callback: function () {
                        console.log('xxxxxx');
                    }*/
                });
            }
        },
        /****** 模板定义 ******/
        template: util.template('bv-textfield')
    });

    vue.component('bv-date', {
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
            // class
            clazz: {
                default: ''
            },
            // 属性定义
            attr: {
                default: function () {
                    return {};
                }
            },
            // 默认值
            defaultValue: {
                default: ''
            },
            // 初始值
            value: {
                default: ''
            },
            // 数据校验
            validate: {
                default: ''
            },
            load: {
                default: ''
            },

            // 标签调用方来源，主要区别在于有没有日历图标
            // filter-table过滤;form-表单
            from: {
                default: 'form'
            },
            /*
             日期格式yyyy-MM-dd hh:mm:ss
             */
            format: {
                default: 'yyyy-MM-dd'
            },
            // 最小日期值
            startDate: {
                default: ''
            },
            // 最大日期值
            endDate: {
                default: ''
            },
            // 最小日期属性
            triggerStart: {
                default: ''
            },
            // 最大日期属性
            triggerEnd: {
                default: ''
            }
        },
        data: function () {
            return {
                dateFormat: this.format
            }
        },
        created: function() {
            util.initDefault(this);
            util.initId(this);
            // 初始化日期格式
            if (this.format === 'date') {
                this.dateFormat = 'yyyy-MM-dd';
            } else if (this.format === 'datetime') {
                this.dateFormat = 'yyyy-MM-dd hh:mm:ss';
            } else if (this.format === 'time') {
                this.dateFormat = 'hh:mm:ss';
            } else if (!this.format) {
                this.dateFormat = 'yyyy-MM-dd';
            } else if (this.format === 'timestamp') {
                this.dateFormat = 'yyyy-MM-dd hh:mm:ss';
            }
        },
        mounted: function() {
            // 初始化日期选择器
            var $selector;
            if (this.from === 'filter') {
                $selector = $('input', this.$el);
            } else {
                $selector = $(this.$el).addClass('input-group');
            }

            var vm = this;
            util.require('datetimepicker', function () {
                // 监听日期插件的值变动及失去焦点事件
                util.datepicker($selector, vm.dateFormat).on('dp.change blur', function(event) {
                    // 赋值并进行数据校验
                    /// vm.value = $('input', vm.$el).val();
                    vm.entity[vm.name] = $('input', vm.$el).val();

                    util.validate($('input', vm.$el), true);
                });

                // 如果有最小日期属性定义（此时该标签为最大日期）并且有时间段定义，则进行初始值设定
                if (vm.load && vm.triggerStart) {
                    vm.$watch('entity.' + vm.load, function(val, oldVal) {
                        var date = vm.entity[vm.triggerStart];
                        if (val && date) {
                            date = util.date(vm.dateFormat, date, val);
                        }
                        vm.entity[vm.name] = date;
                        $('input', vm.$el).val(date);
                    });
                }
                if (vm.triggerStart) {
                    // 最小日期对应值
                    vm.startDate = vm.entity[vm.triggerStart];
                    // 监听最小日期变动
                    vm.$watch('entity.' + vm.triggerStart, function (val, oldVal) {
                        vm.startDate = val;
                        if (!val) {
                            // 如果置空，则不限定最小日期
                            $selector.data('DateTimePicker').minDate(false).locale('zh_cn');
                        } else {
                            // 如果最小日期不空，则限定该标签的最小日期
                            $selector.data('DateTimePicker').minDate(val).locale('zh_cn');
                        }
                        // 如果同时定义了时间段并且有值，则设置该标签的值
                        if (vm.load && vm.entity[vm.load]) {
                            var date = util.date(vm.dateFormat, val, vm.entity[vm.load]);
                            vm.entity[vm.name] = date;
                            $('input', vm.$el).val(date);
                        }
                    });
                }
                if (vm.triggerEnd) {
                    // 最大日期对应值
                    vm.endDate = vm.entity[vm.triggerEnd];
                    // 监听最大日期变动
                    vm.$watch('entity.' + vm.triggerEnd, function (val, oldVal) {
                        vm.endDate = val;
                        if (!val) {
                            // 如果置空，则不限定最大日期
                            $selector.data('DateTimePicker').maxDate(false).locale('zh_cn');
                        } else {
                            // 如果最大日期不空，则限定该标签的最大日期
                            $selector.data('DateTimePicker').maxDate(val).locale('zh_cn');
                        }
                    });
                }

                // 如果有日期最小值定义，进行限定
                if (vm.startDate) {
                    $selector.data("DateTimePicker").minDate(vm.startDate).locale('zh_cn');
                }
                // 如果有日期最大值定义，进行限定
                if (vm.endDate) {
                    $selector.data("DateTimePicker").maxDate(vm.endDate).locale('zh_cn');
                }

                vm.$watch('entity.' + vm.name, function (value, oldValue) {
                    // $('input', vm.$el).val(value);
                    if (!value) {
                        $selector.data("DateTimePicker").clear();
                    }
                });
            });
        },
        /****** 模板定义 ******/
        template: util.template('bv-date')
    });

    vue.component('bv-hidden', {
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
            // 属性定义
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
            }
        },
        watch: {
            value: function (val, oldVal) {
                this.$emit('on-change', this.name, val, oldVal);
            }
        },
        created: function() {
            util.initDefault(this);
        },
        /****** 模板定义 ******/
        template: util.template('bv-hidden')
    });

    vue.component('bv-textarea', {
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
            // 属性定义
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
            // 数据校验
            validate: {
                default: ''
            }
        },
        created: function() {
            util.initDefault(this);
            util.initId(this);
        },
        /****** 模板定义 ******/
        template: util.template('bv-textarea')
    });

    vue.component('bv-select', {
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
            // class
            clazz: {
                default: ''
            },
            // 属性定义
            attr: {
                default: function () {
                    return {};
                }
            },
            // 默认值
            defaultValue: {
                default: ''
            },
            // 初始值
            value: {
                default: ''
            },
            // 数据校验
            validate: {
                default: ''
            },
            load: {
                default: ''
            },

            // 字典类型，支持enums,dicts
            preset: {
                default: 'default'
            },
            code: {
                default: ''
            },
            desc: {
                default: ''
            },
            // 数据
            choose: {
                default: ''
            },
            // 数据接口
            url: {
                default: Const.url.select.query
            },
            method: {
                default: 'post'
            },
            entityName: {
                default: ''
            },
            initParam: {
                default: ''
            },
            initParamList: {
                default: function () {
                    return [];
                }
            },
            orders: {
                default: ''
            },
            // 翻译code用的字典
            trans: {
                default: ''
            },
            // 额外显示
            extras: {
                default: ''
            },
            // 不予显示的code值
            excludes: {
                default: ''
            },
            show: {
                default: ''
            },
            onInit: {
                default: ''
            },
            onChange: {
                default: ''
            },
            // load触发后执行
            onLoad: {
                default: ''
            },

            initOption: {
                default: '请选择'
            },
            multiple: {
                default: false
            },
            // select专用
            label: {
                default: ''
            },
            from: {
                default: ''
            }
        },
        data: function () {
            return {
                /// entity: this.entity,
                groups: [],
                options: []
            };
        },
        beforeCreate: function () {
            this.orderList = [];
            this.customLoad = false;
        },
        created: function () {
            if (this.multiple) {
                this.entity[this.name] = [];
            }
        },
        mounted: function () {
            if (this.url !== Const.url.select.query) {
                this.customLoad = true;
            }
            util.initDefault(this);
            util.initId(this);
            util.initSelectParam(this);

            var vm = this;
            util.require('bootstrap-select', function () {
                /*var container = false;
                if (vm.from === 'table') {
                    container = $(vm.$el).closest('.bv-table-body');
                }*/
                $('select', vm.$el).selectpicker({
                    container: vm.from === 'table' ? 'body' : false,
                    // 替代i18n
                    noneSelectedText: vm.initOption || '请选择',
                    noneResultsText: '没有找到匹配项',
                    countSelectedText: '选中{1}中的{0}项',
                    maxOptionsText: ['超出限制 (最多选择{n}项)', '组选择超出限制(最多选择{n}组)'],
                    multipleSeparator: ', ',
                    selectAllText: '全选',
                    deselectAllText: '取消全选'

                    // liveSearch: true
                }).show().on('hidden.bs.select', function () {
                    $('select', vm.$el).trigger('blur');
                });
                if (util.type(vm.onInit) === 'function') {
                    vm.onInit.call(null, vm.entity, vm.entity[vm.name]);
                }
                if (vm.from === 'table') {
                    $('select', vm.$el).on('show.bs.select', function (event) {
                        // 判断是否该向上显示下拉菜单
                        var allowBottom = $(event.target).closest('.bv-table-body').offset().top + $(event.target).closest('.bv-table-body').outerHeight(true);
                        var needBottom = $(event.target).offset().top + $(event.target).outerHeight(true) + $(event.target).prevAll('.dropdown-menu').outerHeight(true);
                        $(event.target).closest('.bootstrap-select').toggleClass('dropup', allowBottom < needBottom);
                    });
                }

                if (vm.attr && util.type(util.attr.disabled) !== 'undefined') {
                    $('button.dropdown-toggle', vm.$el).attr('disabled', vm.attr.disabled);
                }

                vm.$watch('options', function (value, oldValue) {
                    vm.$nextTick(function () {
                        $('select', vm.$el).selectpicker('refresh');
                    });
                });

                if (util.type(vm.choose) === 'array') {
                    vm.$watch('choose', function (value, oldValue) {
                        util.initSelectData(vm, 'select');
                    });
                }

                vm.$watch('entity.' + vm.name, function (value, oldValue) {
                    $('select', vm.$el).selectpicker('val', value);
                });
            });

            if (this.load) {
                vm.$watch('entity.' + vm.load + (vm.from === 'filter' ? 'Filter' : ''), function(val, oldVal) {
                    vm.entity[vm.name] = null;
                    vm.value = null;
                    if (!util.isEmpty(val)) {
                        util.initSelectData(vm, 'select', 'load');
                    } else {
                        vm.options = [];
                    }
                    if (util.type(vm.onLoad) === 'function') {
                        vm.$nextTick(function () {
                            vm.onLoad.call(null, vm.entity, vm.entity[vm.name]);
                        });
                    }
                });
            }
            util.initSelectData(this, 'select');

            vm.$watch('entity.' + vm.name, function (val, oldVal) {
                if (util.type(this.onChange) === 'function') {
                    this.onChange.call(this, this.entity, this.entity[this.name], util.index(this.options, this.entity[this.name], this.code, true));
                } else {
                    this.$emit('on-change', this.entity, this.entity[this.name], util.index(this.options, this.entity[this.name], this.code, true));
                }
            })
        },
        beforeDestroy: function () {
            $('select', this.$el).selectpicker('destroy');
        },
        methods: {
            execute: function (param) {
                if (param.initParamList) {
                    this.initParamList = param.initParamList;
                    util.initSelectData(this, 'select', 'load');
                }
            }
        },
        /****** 模板定义 ******/
        template: util.template('bv-select')
    });

    vue.component('bv-radio', {
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
            // class
            clazz: {
                default: ''
            },
            // 属性定义
            attr: {
                default: function () {
                    return {};
                }
            },
            // 默认值
            defaultValue: {
                default: ''
            },
            // 初始值
            value: {
                default: ''
            },
            // 数据校验
            validate: {
                default: ''
            },
            load: {
                default: ''
            },

            // 字典类型，支持enums,dicts
            preset: {
                default: 'default'
            },
            code: {
                default: ''
            },
            desc: {
                default: ''
            },
            // 数据
            choose: {
                default: ''
            },
            // 数据接口
            url: {
                default: Const.url.select.query
            },
            method: {
                default: 'post'
            },
            entityName: {
                default: ''
            },
            initParam: {
                default: ''
            },
            initParamList: {
                default: function () {
                    return [];
                }
            },
            orders: {
                default: ''
            },
            // 翻译code用的字典
            trans: {
                default: ''
            },
            // 额外显示
            extras: {
                default: ''
            },
            // 不予显示的code值
            excludes: {
                default: ''
            },
            show: {
                default: ''
            },
            onChange: {
                default: ''
            },

            // radio、checkbox专用
            labelClass: {
                default: ''
            },
            labelStyle: {
                default: ''
            },
            extraColumns: {
                default: ''
            },
            // 每行显示几列选项
            cols: {
                default: ''
            },
            from: {
                default: ''
            }
        },
        data: function () {
            return {
                /// entity: this.entity,
                options: []
            };
        },
        beforeCreate: function () {
            this.orderList = [];
            this.customLoad = false;
        },
        mounted: function () {
            if (this.url !== Const.url.select.query) {
                this.customLoad = true;
            }
            util.initDefault(this);
            util.initSelectParam(this);

            if (this.cols && this.cols > 0 && this.cols <= 12) {
                this.labelClass = 'col-custom col-md-' + (12 / this.cols);
            }

            var vm = this;
            if (this.load) {
                vm.$watch('entity.' + vm.load + (vm.from === 'filter' ? 'Filter' : ''), function(val, oldVal) {
                    if (!util.isEmpty(val)) {
                        util.initSelectData(vm, 'radio', 'load');
                    } else {
                        vm.entity[vm.name] = null;
                        vm.options = [];
                    }
                });
            }
            util.initSelectData(this, 'radio');

            vm.$watch('entity.' + vm.name, function (val, oldVal) {
                if (util.type(this.onChange) === 'function') {
                    this.onChange.call(this, this.entity, this.entity[this.name], util.index(this.options, this.entity[this.name], this.code, true));
                } else {
                    this.$emit('on-change', this.entity, this.entity[this.name]);
                }
            })
        },
        /****** 模板定义 ******/
        template: util.template('bv-radio')
    });

    vue.component('bv-checkbox', {
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
            // class
            clazz: {
                default: ''
            },
            // 属性定义
            attr: {
                default: function () {
                    return {};
                }
            },
            // 默认值
            defaultValue: {
                default: ''
            },
            // 初始值
            value: {
                default: ''
            },
            // 数据校验
            validate: {
                default: ''
            },
            load: {
                default: ''
            },

            // 字典类型，支持enums,dicts
            preset: {
                default: 'default'
            },
            code: {
                default: ''
            },
            desc: {
                default: ''
            },
            // 数据
            choose: {
                default: ''
            },
            // 数据接口
            url: {
                default: Const.url.select.query
            },
            method: {
                default: 'post'
            },
            entityName: {
                default: ''
            },
            initParam: {
                default: ''
            },
            initParamList: {
                default: function () {
                    return [];
                }
            },
            orders: {
                default: ''
            },
            // 翻译code用的字典
            trans: {
                default: ''
            },
            // 额外显示
            extras: {
                default: ''
            },
            // 不予显示的code值
            excludes: {
                default: ''
            },
            show: {
                default: ''
            },
            onChange: {
                default: ''
            },

            // radio、checkbox专用
            labelClass: {
                default: ''
            },
            extraColumns: {
                default: ''
            },
            // 每行显示几列选项
            cols: {
                default: ''
            },
            sticky: {
                default: ''
            },
            // 初始是否全选
            initSelectAll: {
                default: false
            },
            /*values: {
                default: function () {
                    return [];
                }
            },*/
            from: {
                default: ''
            },
            // 点击选项
            click: {
                default: ''
            }
        },
        data: function () {
            return {
                /// entity: this.entity,
                options: [],
                values: [],
                stickValue: this.sticky
            };
        },
        watch: {
            values: function (val, oldVal) {
                this.entity[this.name] = util.arrayToString(val, this.seprator);
            }
        },
        beforeCreate: function () {
            this.orderList = [];
            this.customLoad = false;
            this.seprator = ',';
        },
        mounted: function () {
            if (this.url !== Const.url.select.query) {
                this.customLoad = true;
            }
            if (this.sticky && util.startsWith(this.sticky, '#')) {
                this.stickValue = this.entity[this.sticky.substring(1)];
            }
            util.initDefault(this);
            util.initSelectParam(this);

            if (this.cols && this.cols > 0 && this.cols <= 12) {
                this.labelClass = 'col-custom col-md-' + (12 / this.cols);
            }

            if (util.type(this.value) !== 'array' ) {
                if (this.value) {
                    if (util.type(this.value) === 'string' && this.value.indexOf(this.seprator) !== -1) {
                        this.values = util.stringToArray(this.value, this.seprator);
                    } else{
                        this.values.push(this.value);
                    }
                }
            } else {
                this.values = this.value;
            }
            if (this.stickValue && !util.contains(this.values, this.stickValue)) {
                this.values.push(this.stickValue);
                var value = this.value;
                if (util.type(value) !== 'array' ) {
                    if (!value) {
                        value = this.stickValue;
                    } else {
                        value += this.seprator + this.stickValue;
                    }
                    this.entity[this.name] = value;
                }
            }

            var vm = this;
            if (this.load) {
                vm.$watch('entity.' + vm.load + (vm.from === 'filter' ? 'Filter' : ''), function(val, oldVal) {
                    if (!util.isEmpty(val)) {
                        vm.entity[vm.name] = null;
                        vm.values = [];
                        util.initSelectData(vm, 'checkbox', 'load');
                    } else {
                        vm.entity[vm.name] = null;
                        vm.options = [];
                    }
                });
            }
            util.initSelectData(this, 'checkbox');

            if (util.type(this.onChange) === 'function') {
                vm.$watch('entity.' + vm.name, function (val, oldVal) {
                    this.onChange.call(this, this.entity, this.entity[this.name], util.index(this.options, this.entity[this.name], this.code, true), this.values);
                })
            } else {
                this.$emit('on-change', this.entity, this.entity[this.name], util.index(this.options, this.entity[this.name], this.code, true), this.values);
            }
            if (this.initSelectAll) {
                this.$watch('options', function (value, oldValue) {
                    this.selectAll();
                }, {
                    deep: true
                });
                this.selectAll();
            }
        },
        methods: {
            changeSelected: function (selected) {
                if (!selected) {
                    this.values = [];
                } else if (selected.indexOf(',') > 0) {
                    this.values = selected.split(',');
                } else {
                    this.values = selected;
                }
            },
            selectAll: function () {
                var values = [];
                for (var i=0; i<this.options.length; i++) {
                    values.push(this.options[i][this.code]);
                }
                this.values = values;
            }
        },
        /****** 模板定义 ******/
        template: util.template('bv-checkbox')
    });

    vue.component('bv-auto', {
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
            // class
            clazz: {
                default: ''
            },
            // 属性定义
            attr: {
                default: function () {
                    return {};
                }
            },
            // 默认值
            defaultValue: '',
            // 初始值
            value: {
                default: ''
            },
            // 数据校验
            validate: {
                default: ''
            },
            load: {
                default: ''
            },

            // 字典类型，支持enums,dicts
            preset: {
                default: 'default'
            },
            code: {
                default: ''
            },
            desc: {
                default: ''
            },
            // 数据
            choose: {
                default: ''
            },
            // 数据接口
            url: {
                default: ''
            },
            method: {
                default: 'post'
            },
            entityName: {
                default: ''
            },
            initParam: {
                default: ''
            },
            initParamList: {
                default: ''
            },
            orders: {
                default: ''
            },
            // 翻译code用的字典
            trans: {
                default: ''
            },
            // 额外显示
            extras: {
                default: ''
            },
            // 不予显示的code值
            excludes: {
                default: ''
            },
            show: {
                default: ''
            },

            initUrl: {
                default: Const.url.auto.init
            },
            // 是否增加去重
            distinct: {
                default: ''
            },
            extraColumns: {
                default: ''
            },
            /*
             每次最多显示数量
             */
            limit: {
                default: 10
            },
            onItemInit: {
                default: ''
            },
            onChange: {
                default: ''
            },
            from: {
                default: ''
            }
        },
        data: function () {
            return {
                options: []
            };
        },
        beforeCreate: function () {
            this.orderList = [];
            this.customLoad = false;
            /*
             内部用，结构为：{code: {code: '', name: '', desc: ''}}
             */
            this.map = {};
            this.shows = [];
        },
        created: function () {
            if (!this.url) {
                if (this.limit) {
                    this.url = Const.url.auto.page;
                } else {
                    this.url = Const.url.auto.query;
                }
            }
        },
        mounted: function () {
            util.initDefault(this);
            util.initId(this);
            util.initSelectParam(this);

            var vm = this;
            if (this.load) {
                vm.$watch('entity.' + vm.load + (vm.from === 'filter' ? 'Filter' : ''), function(val, oldVal) {
                    $('input', vm.$el).val('');
                    vm.entity[vm.name] = null;
                    vm.map = {};
                    vm.shows = [];

                    if (!util.isEmpty(val)) {
                        util.initSelectData(vm, 'auto', 'load');
                    }
                });
                vm.$watch('entity.' + vm.name, function(val, oldVal) {
                    if (util.isEmpty(val)) {
                        $('input', vm.$el).val('');
                    }
                });
            }
            util.initSelectData(this, 'auto');

            util.require('typeahead', function () {
                $('input', vm.$el).typeahead({
                    source: function (query, process) {
                        util.doAutoProcess(vm, query, process);
                    },
                    matcher: !vm.choose && vm.url && function() {
                        return true;
                    },
                    items: vm.limit,
                    minLength: 0,
                    showHintOnFocus: true,
                    fitToElement: true,
                    autoSelect: false,
///                    delay: 500,
                    afterSelect: function (item) {
                        vm.entity[vm.name] = vm.map[item][vm.code];
                        util.validate($('input', vm.$el), true);
                        if (util.type(vm.onChange) === 'function') {
                            vm.onChange.call(vm, vm.entity, vm.entity[vm.name], vm.map[item]);
                        }
                    }
                });

                $('input', vm.$el).on('blur', function() {
                    if (util.isEmpty(vm.entity[vm.name])) {
                        $(this).val('');
                    }
                    if (util.isEmpty($(this).val())) {
                        vm.entity[vm.name] = '';
                    }

                    var $typeahead = $(this).data('typeahead');
                    if ($typeahead) {
                        $typeahead.focused = false;
                    }
                });
            });
        },
        beforeDestroy: function () {
            $('input', this.$el).off('blur').typeahead('destroy');
        },
        methods: {
            trigger: function(event) {
                $('input', this.$el).typeahead('lookup').focus();
            },
            change: function(event) {
                this.entity[this.name] = '';
                $('input', this.$el).val('');
                util.validate($('input', this.$el), true);
                $('input', this.$el).typeahead('focus').focus();
                if (util.type(this.onChange) === 'function') {
                    this.onChange.call(this, {});
                }
            }
        },
        /****** 模板定义 ******/
        template: util.template('bv-auto')
    });

    vue.component('bv-toggle', {
        props: {
            entity: {
                default: function () {
                    return {};
                }
            },

            name: {
                default: ''
            },
            defaultValue: {
                default: ''
            },
            value: {
                default: ''
            },
            preset: {
                default: ''
            },
            choose: {
                default: function () {
                    return ['ON', 'OFF'];
                }
            },
            onChange: {
                default: ''
            }
        },
        created: function () {
            util.initDefault(this);
        },
        mounted: function () {
            var vm = this;
            util.initSelectData(this, 'toggle');
            util.require('switch', function () {
                $('input', vm.$el).bootstrapSwitch({
                    state: vm.entity[vm.name] === vm.choose[0].code,
                    onText: vm.choose[0].desc,
                    offText: vm.choose[1].desc,
                    onSwitchChange: function(event, value) {
                        if (value) {
                            vm.entity[vm.name] = vm.choose[0].code;
                        } else {
                            vm.entity[vm.name] = vm.choose[1].code;
                        }

                        if (util.type(vm.onChange) === 'function') {
                            vm.onChange.call(vm, vm.entity, vm.entity[vm.name]);
                        }
                    }
                });
            });
        },
        /****** 模板定义 ******/
        template: util.template('bv-toggle')
    });

    vue.component('bv-button', {
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
            // class
            clazz: {
                default: ''
            },
            // 属性定义
            attr: {
                default: function () {
                    return {};
                }
            },
            // 默认值
            defaultValue: {
                default: ''
            },
            // 初始值
            value: {
                default: ''
            },

            // 支持button，close
            type: {
                default: 'button'
            },

            text: {
                default: ''
            },
            icon: {
                default: ''
            },
            loading: {
                default: ''
            },
            click: {
                default: ''
            }
        },
        data: function () {
            return {
                disabled: this.attr && this.attr.disabled
            };
        },
        created: function() {
            util.initDefault(this);
            if (this.text) {
                this.value = this.text;
            }
            if (!this.clazz) {
                this.clazz = 'btn-default';
            }
            if (this.attr && this.attr.disabled) {
                delete this.attr.disabled;
            }
            if (this.type === 'close') {
                this.icon = 'icon-cancel';
                this.attr['data-dismiss'] = 'modal';
            }
            if (this.loading) {
                this.attr['data-loading-text'] = this.loading;
            }
        },
        methods: {
            doClick: function (event) {
                if (util.type(this.click) === 'function') {
                    if (this.loading) {
                        $(util.button(this.$el)).button('loading');
                    }
                    var vm = this;
                    this.click.call(null, event, this.entity, function () {
                        if (vm.loading) {
                            setTimeout(function() {
                                $(util.button(vm.$el)).button('reset');
                            }, 500);
                        }
                    });
                }
            }
        },
        /****** 模板定义 ******/
        template: util.template('bv-button')
    });

    vue.component('bv-href', {
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
            // class
            clazz: {
                default: ''
            },
            // 属性定义
            attr: {
                default: function () {
                    return {};
                }
            },
            // 默认值
            defaultValue: {
                default: ''
            },
            // 初始值
            value: {
                default: ''
            },
            text: {
                default: ''
            },
            from: {
                default: ''
            },
            format: {
                default: ''
            },
            show: {
                default: ''
            },

            icon: {
                default: ''
            },
            href: {
                default: ''
            },
            click: {
                default: ''
            }
        },
        created: function() {
            util.initDefault(this);
            /*if (this.text) {
                this.value = this.text;
            }*/
            if (this.attr.title) {
                this.attr['data-title'] = this.attr.title;
                this.attr['data-original-title'] = this.attr.title;
            }
            if (this.href && this.href.preset === 'sub') {
                this.icon = 'icon-more';
                this.clazz += ' trigger-sub';
            }
        },
        mounted: function () {
            if (this.from === 'table') {
                this.value = this.text;
                this.$watch('text', function(val, oldVal) {
                    this.value = val;
                });
            } else {
                if (this.show && util.type(this.show) === 'function') {
                    this.value = util.format(this.show.call(null, this.entity, this.value), this.format);
                } else {
                    this.value = util.format(this.value, this.format);
                }

                this.$watch('entity.' + this.name, function(value, oldValue) {
                    if (this.show && util.type(this.show) === 'function') {
                        this.value = util.format(this.show.call(null, this.entity, value), this.format);
                    } else {
                        this.value = util.format(value, this.format);
                    }
                });
            }
        },
        methods: {
            // TODO: 暂时只支持table的column调用
            doClick: function (event) {
                if (util.type(this.click) === 'function') {
                    this.click.call(this.$parent, event);
                }
            }
        },
        /****** 模板定义 ******/
        template: util.template('bv-href')
    });

    vue.component('bv-image', {
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
            // class
            clazz: {
                default: ''
            },
            // 属性定义
            attr: {
                default: function () {
                    return {};
                }
            },
            //
            src: {
                default: ''
            },
            click: {
                default: ''
            }
        },
        methods: {
            initSrc: function () {
                if (util.isEmpty(this.src)) {
                    return '';
                }
                if (util.type(this.src) === 'string') {
                    return this.src;
                }
                if (util.type(this.src) === 'function') {
                    return this.src.call(null, this.entity);
                }
            }
        },
        /****** 模板定义 ******/
        template: util.template('bv-image')
    });
});
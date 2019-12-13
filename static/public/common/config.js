/*
定义requirejs的配置，以及一些全局变量
*/
// 全局变量
var __global = {
    isMobile: navigator.userAgent.match(/mobile/i),        // 判断是否为移动版浏览器
    isIe: !!window.ActiveXObject || 'ActiveXObject' in window,
    formId: 0,
    vm: {},
    // 拍照相馆变量
    capture: {}
};
require.config({
    /*urlArgs: function(moduleName, url) {
        return '?_=' + (_version[url] || _version[moduleName] || _version._);
    },*/
    baseUrl: __baseResourcePath || '/public',
    map: {
        '*': {
            'css': 'plugin/require-css/css.min',
            'text': 'plugin/require-text/text.min'
        }
    },
    paths: {
        vue: 'plugin/vue/vue.min',
        jquery: 'plugin/jquery/jquery-3.min',
        bootstrap: 'plugin/bootstrap/js/bootstrap.min',
        moment: 'plugin/moment/moment.min',
        mousewheel: 'plugin/jquery-mousewheel/jquery.mousewheel.min',
        lazy: 'plugin/echo/echo.min',
        md5: 'plugin/md5/md5.min',

        raven: 'https://cdn.ravenjs.com/3.24.1/raven.min',

        util: 'common/util.js?v=1805281649',
        Const: 'common/const.js?v=180426',
        bvBasic: 'tags/bv-basic.js?v=180426',
        bvExtend: 'tags/bv-extend.js?v=1806051957',
        bvForm: 'tags/bv-form.js?v=180426',
        bvTable: 'tags/bv-table.js?v=180426',
        bvView: 'tags/bv-view.js?v=180426'
    },
    shim: {
        vue: {
            exports: 'vue'
        },
        jquery: {
            exports: 'jquery'
        },
        bootstrap: {
            deps: ['jquery'],
            exports: 'bootstrap'
        },
        moment: {
            exports: 'moment'
        },
        mousewheel: {
            deps: ['jquery'],
            exports: 'mousewheel'
        },
        lazy: {
            exports: 'lazy'
        },
        md5: {
            exports: 'md5'
        },
        raven: {
            exports: 'raven'
        },
        util: {
            exports: 'util'
        },
        Const: {
            exports: 'Const'
        },
        bvBasic: {
            exports: 'bvBasic'
        },
        bvExtend: {
            exports: 'bvExtend'
        },
        bvForm: {
            deps: ['bvBasic'],
            exports: 'bvForm'
        },
        bvTable: {
            deps: ['bvBasic'],
            exports: 'bvTable'
        },
        bvView: {
            exports: 'bvView'
        }
    }
});
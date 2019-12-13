/**
__appCode定义应用编码，注意：portal为特殊编码不能占用，指内网门户
本文件的__Const变量会覆盖public/common/const.js中相关配置
__Const.init.debug：是否开启调试，设置为true后会在浏览器控制台输出部分调试信息，调用输出信息的方式为util.debug(title, content)
__Const.init.login：session超时后是否弹出登录窗口，设置为true会弹出，否则仅提示错误信息
 */
// 支持dev,run
var __env = 'dev';

var __appPath = '';
if (location.pathname && location.pathname.substring(0, 1) === '/') {
    if (location.pathname.substring(location.pathname.length - 1) !== '/') {
        // => /oa/main.html
        __appPath = location.pathname.substring(1, location.pathname.lastIndexOf('/'));
    } else {
        // => /oa/
        __appPath = location.pathname.replace(new RegExp('/', "gm"), '');
    }
}
var __appCode = 'PLR';
var __baseResourcePath = '/' + __appPath + '/' + (__env === 'run' ? 'run/public' : 'public');
var __baseAppPath = __env === 'run' ? 'run/' : '';
var __Const = {
    init: {
        debug: true,
        // login: true,
        report: false, //'sentry',
        appCode: __appCode,
        appPath: __appPath,
        baseResourcePath: __baseResourcePath,
        baseAppPath: __baseAppPath,
        table: {
            collapse: true,
            filterResetQuery: false
        }
    },
    url: {
        table: {
            page: '/api/plr/common/table/page',
            select: '/api/plr/common/table/select',
            deletes: '/api/plr/common/table/deletes'
        },
        form: {
            init: '/api/plr/common/form/init',
            insert: '/api/plr/common/form/insert',
            update: '/api/plr/common/form/update',
            del: '/api/plr/common/form/delete'
        },
        select: {
            query: '/api/plr/common/select/query',
            init: '/api/plr/common/select/init'
        },
        auto: {
            query: '/api/plr/common/auto/query',
            page: '/api/plr/common/auto/page',
            init: '/api/plr/common/auto/init'
        },
        authority: {
            info: '${root}/user',
            menus: '${portal}/api/mMenus/authMenus?appId=' + __appCode,
            logout: '${portal}/logout'
        },
        cache: {
            enums: '/api/plr/cache/enums',
            dicts: '/api/plr/cache/dicts',
            params: '/api/plr/cache/params'
        },
        tree: {
            query: '/api/plr/common/tree/query'
        }
    },
    /*rest: {
        baseUrl: window.location.protocol + "//" + window.location.host
    },*/
    route: {
        baseUrl: window.location.protocol + "//" + window.location.host,
        baseLocation: '/' + __appPath,
        versionLocation: '',
        htmlLocation: '/modules',
        scriptLocation: __baseAppPath + '/js'
    },
    menu: {
        parentId: 'parentMenu',
        url: 'menuUrl',
        buttonId: 'buttonId',
        buttonName: 'buttonName'
    },
    params: {
        reportAddress: 'https://e962b8795ade4cbb8abbce35b4fb704c@sentry.io/1187142'
    },
    chart: {
        // 定制chart图表颜色
        colors: ['#c23531','#2f4554', '#61a0a8', '#d48265', '#91c7ae','#749f83',  '#ca8622', '#bda29a','#6e7074', '#546570', '#c4ccd3']
    }
};

window.loader = {
    script: function (url, async, callback) {
        if (url) {
            var innerAsync;
            if (typeof(async) === 'function') {
                callback = async;
                innerAsync = false;
            } else {
                innerAsync = async;
            }
            if (typeof(url) === 'string') {
                var script = document.createElement("script");
                script.type = "text/javascript";
                if (typeof(callback) !== 'undefined') {
                    if (script.readyState) {
                        script.onreadystatechange = function () {
                            if (script.readyState == "loaded" || script.readyState == "complete") {
                                script.onreadystatechange = null;
                                callback();
                            }
                        };
                    } else {
                        script.onload = function () {
                            callback();
                        };
                    }
                }
                var src;
                if (url.indexOf('/') === 0) {
                    src = window.__baseResourcePath + url;
                } else {
                    src = window.__baseAppPath + url;
                }
                if (!window.__env || window.__env === 'dev') {
                    src += '?_=' + new Date().getTime();
                }
                script.src = src;
                document.body.appendChild(script);
            } else {
                if (innerAsync) {
                    // 异步加载
                    var count = 0;
                    for (var i=0; i<url.length; i++) {
                        this.script(url[i], innerAsync, function () {
                            count++;
                            if (typeof(callback) === 'function' && count >= url.length) {
                                callback();
                            }
                        });
                    }
                } else {
                    // 顺序加载
                    var loader = this;
                    loader.script(url[0], innerAsync, function () {
                        url.splice(0, 1);
                        if (url.length > 0) {
                            loader.script(url, innerAsync, callback);
                        } else if (typeof(callback) === 'function') {
                            callback();
                        }
                    });
                }
            }
        }
    },
    style: function (url, async, callback) {
        if (url) {
            var innerAsync;
            if (typeof(async) === 'function') {
                callback = async;
                innerAsync = false;
            } else {
                innerAsync = async;
            }
            if (typeof(url) === 'string') {
                var link = document.createElement('link');
                link.type = 'text/css';
                link.rel = 'stylesheet';
                if (typeof(callback) === 'function') {
                    if (link.readyState) {
                        link.onreadystatechange = function () {
                            if (link.readyState == "loaded" || link.readyState == "complete") {
                                link.onreadystatechange = null;
                                callback();
                            }
                        };
                    } else {
                        link.onload = function () {
                            callback();
                        };
                    }
                }
                var src;
                if (url.indexOf('/') === 0) {
                    src = window.__baseResourcePath + url;
                } else {
                    src = window.__baseAppPath + url;
                }
                if (!window.__env || window.__env === 'dev') {
                    src += '?_=' + new Date().getTime();
                }
                link.href = src;
                document.getElementsByTagName("head")[0].appendChild(link);
            } else {
                if (innerAsync) {
                    // 异步加载
                    var count = 0;
                    for (var i=0; i<url.length; i++) {
                        this.style(url[i], innerAsync, function () {
                            count++;
                            if (typeof(callback) === 'function' && count >= url.length) {
                                callback();
                            }
                        });
                    }
                } else {
                    var loader = this;
                    loader.style(url[0], innerAsync, function () {
                        url.splice(0, 1);
                        if (url.length > 0) {
                            loader.style(url, innerAsync, callback);
                        } else if (typeof(callback) === 'function') {
                            callback();
                        }
                    });
                }
            }
        }
    }
};
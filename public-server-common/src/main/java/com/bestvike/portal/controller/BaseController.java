package com.bestvike.portal.controller;

import com.bestvike.commons.exception.AuthorityException;
import com.bestvike.commons.exception.ServiceException;
import com.bestvike.commons.exception.TradeException;
import com.bestvike.commons.redis.Cache;
import com.bestvike.commons.support.RestError;
import com.bestvike.commons.support.RestStatus;
import com.bestvike.commons.support.TradeError;
import com.bestvike.portal.data.SUser;
import com.bestvike.portal.utils.HttpUtils;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * basecontroller, need to be extented by all the controller class.
 * @author Li Hua
 * @since v1.0.0
 */
public class BaseController {
    protected Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    protected HttpServletRequest httpServletRequest;
    @Autowired
    protected Cache cache;

    @Value("${app.error.prefix:}")
    protected String prefix;
    @Value("${app.instance.code:}")
    protected String appCode;

    protected String getLoginUserId() {
        String token = HttpUtils.getToken(httpServletRequest);
        if (!StringUtils.isEmpty(token)) {
            return cache.get("token_to_user:" + token);
        }
        return null;
    }

    protected SUser getLoginUser() {
        String userId = this.getLoginUserId();
        if (!StringUtils.isEmpty(userId)) {
            return cache.get("user_details:" + userId, SUser.class);
        }
        return null;
    }

    @ExceptionHandler(TradeException.class)
    @ResponseBody
    public ResponseEntity<TradeError> handleTradeException(TradeException e) {
        logger.error(e);
        logger.error(e.getCause());
        TradeError tradeError = TradeError.build(e.getResultCode(), e.getResultMsg(), e.getData());
        return new ResponseEntity<>(tradeError, RestStatus.SERVICE_ERROR.getStatus());
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResponseEntity<RestError> handleBusinessException(ServiceException e) {
        logger.error(e);
        logger.error(e.getCause());
        RestError restError = RestError.build(this.appCode, this.prefix, RestStatus.SERVICE_ERROR, e.getCode(), e.getMessage(), e.getDebug());
        return new ResponseEntity<>(restError, RestStatus.SERVICE_ERROR.getStatus());
    }

    // 内部鉴权失败
    @ExceptionHandler(AuthorityException.class)
    @ResponseBody
    public ResponseEntity<RestError> handleAuthorityException(AuthorityException e) {
        logger.error(e);
        if (e.getCause() != null) {
            logger.error(e.getCause());
        }
        RestError restError = RestError.build(this.appCode, this.prefix, RestStatus.FORBIDDEN, e.getDebug());
        return new ResponseEntity<>(restError, RestStatus.FORBIDDEN.getStatus());
    }

    /**
     * 其他未处理的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<RestError> handleException(Exception e) {
        logger.error(e);
        logger.error(e.getCause());
        RestError restError = RestError.build(this.appCode, this.prefix, RestStatus.INTERNAL_SERVER_ERROR, RestStatus.INTERNAL_SERVER_ERROR.getCode(), RestStatus.INTERNAL_SERVER_ERROR.getMessage(), e.getMessage());
        return new ResponseEntity<>(restError, RestStatus.INTERNAL_SERVER_ERROR.getStatus());
    }
}

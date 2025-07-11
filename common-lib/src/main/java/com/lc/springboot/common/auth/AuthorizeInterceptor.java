package com.lc.springboot.common.auth;

import cn.hutool.core.collection.CollectionUtil;
import com.lc.springboot.common.utils.SysUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限拦截器
 *
 * @author liangchao
 */
public abstract class AuthorizeInterceptor extends HandlerInterceptorAdapter {

    protected AuthProperties authProperties;

    public AuthorizeInterceptor(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 如果在白名单里，直接放行
        if (CollectionUtil.isNotEmpty(authProperties.getWhiteIpList())) {
            String ip = SysUtil.getRemoteAddrIp();
            if (authProperties.getWhiteIpList().contains(ip)) {
                return true;
            }
        }

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Authorize authorize = handlerMethod.getMethod().getAnnotation(Authorize.class);
        if (authorize == null) {
            // 不需要做权限校验
            return true;
        }

        String authz = AuthContext.getAuthz();

        //指定令牌放行
        if(CollectionUtil.isNotEmpty(authProperties.getWhiteTokenList()) && authProperties.getWhiteTokenList().contains(authz)){
            return true;
        }

        if (StringUtils.isEmpty(authz)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new PermissionDeniedException(authProperties.getErrorMsgMissingAuthHeader());
        }

        return checkUser(authz, authorize.value());
    }

    /**
     * 检验用户是否是合法的用户
     *
     * @param authzHeader 请求头token信息
     * @return boolean 如果验证成功返回true，否则返回false
     */
    public abstract boolean checkUser(String authzHeader, String[] authValues);
}

package com.gangbean.stockservice.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    public static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    public static final String AUTHORIZATION_HEADER_VALUE_PREFIX = "Bearer ";
    public static final int AUTHORIZATION_HEADER_VALUE_PREFIX_LENGTH = 7;
    public static final String EMPTY_TOKEN = "";
    private TokenProvider tokenProvider;
    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse
            , FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else {
            logger.info("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveToken(HttpServletRequest request) {
        return hasTextAndStartsWithValidPrefix(request.getHeader(AUTHORIZATION_HEADER_KEY)) ?
                prefixRemovedToken(request.getHeader(AUTHORIZATION_HEADER_KEY)) : EMPTY_TOKEN;
    }

    private static String prefixRemovedToken(String authorizationHeaderValue) {
        return authorizationHeaderValue.substring(AUTHORIZATION_HEADER_VALUE_PREFIX_LENGTH);
    }

    private static boolean hasTextAndStartsWithValidPrefix(String bearerToken) {
        return StringUtils.hasText(bearerToken)
                && bearerToken.startsWith(AUTHORIZATION_HEADER_VALUE_PREFIX);
    }
}
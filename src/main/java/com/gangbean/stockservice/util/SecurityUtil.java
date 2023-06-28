package com.gangbean.stockservice.util;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import org.springframework.util.StringUtils;

public class SecurityUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String ACCESS_TOKEN_PREFIX = "Bearer ";
    public static final String BLANK_STRING = "";

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityUtil.class);

    private SecurityUtil() {}

    public static String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(ACCESS_TOKEN_PREFIX)) {
            return bearerToken.substring(ACCESS_TOKEN_PREFIX.length());
        }

        return BLANK_STRING;
    }

    public static String resolveToken(HttpHeaders headers) {
        String bearerToken = headers.getFirst(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(ACCESS_TOKEN_PREFIX)) {
            return bearerToken.substring(ACCESS_TOKEN_PREFIX.length());
        }

        return BLANK_STRING;
    }

    public static Optional<String> getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            LOGGER.debug("Security Context에 인증 정보가 없습니다.");
            return Optional.empty();
        }

        String username = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            username = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            username = (String) authentication.getPrincipal();
        }

        return Optional.ofNullable(username);
    }
}

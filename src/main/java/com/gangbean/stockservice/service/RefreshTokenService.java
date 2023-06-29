package com.gangbean.stockservice.service;

import com.gangbean.stockservice.dto.TokenDto;
import com.gangbean.stockservice.entity.Member;
import com.gangbean.stockservice.entity.RefreshToken;
import com.gangbean.stockservice.jwt.TokenProvider;
import com.gangbean.stockservice.repository.RefreshTokenRepository;
import java.util.Date;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
        TokenProvider tokenProvider) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenProvider = tokenProvider;
    }

    public TokenDto tokenGeneratedFromLoginResultOf(Authentication loginUserAuthentication, Member requestMember) {
        Date now = new Date();
        String accessToken = tokenProvider.newAccessTokenOf(loginUserAuthentication,
            tokenProvider.accessTokenExpirationDateFrom(now),
            requestMember.getId());

        Date refreshTokenExpiration = tokenProvider.refreshTokenExpirationDateFrom(now);
        String refreshToken = tokenProvider.newRefreshTokenOf(loginUserAuthentication,
            refreshTokenExpiration,
            requestMember.getId());

        refreshTokenRepository.save(RefreshToken.builder()
            .member(requestMember)
            .refreshToken(refreshToken)
            .expiration(refreshTokenExpiration)
            .build());

        return new TokenDto.Builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    public TokenDto reissue(String accessToken, Date now, TokenDto tokenDto) {
        Long loginMemberId = tokenProvider.claimsOf(accessToken)
            .get(TokenProvider.MEMBER_ID, Long.class);

        RefreshToken refreshToken = this.validRefreshTokenOf(loginMemberId, now, tokenDto);
        Authentication authentication = currentAuthentication();

        String reissuedAccessToken = tokenProvider.newAccessTokenOf(authentication,
            tokenProvider.accessTokenExpirationDateFrom(now), loginMemberId);

        refreshToken.reissue(tokenProvider.refreshTokenExpirationDateFrom(now));
        refreshTokenRepository.save(refreshToken);

        return new TokenDto.Builder()
            .accessToken(reissuedAccessToken)
            .refreshToken(refreshToken.getRefreshToken())
            .build();
    }

    private RefreshToken validRefreshTokenOf(Long loginMemberId, Date now, TokenDto tokenDto) {
        RefreshToken refreshToken = refreshTokenRepository
            .findByRefreshToken(tokenDto.getRefreshToken())
            .orElseThrow(() -> new IllegalArgumentException("Refresh Token이 존재하지 않습니다."));

        refreshToken.isExpired(now);
        refreshToken.isSameMember(loginMemberId);

        return refreshToken;
    }

    private Authentication currentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}

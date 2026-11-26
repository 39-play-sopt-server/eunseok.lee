package org.sopt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {

    private static final String TOKEN_TYPE_CLAIM = "tokenType";
    private static final String ACCESS = "ACCESS";
    private static final String REFRESH = "REFRESH";

    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String createAccessToken(Long memberId) {
        return buildToken(memberId, accessTokenExpiration, ACCESS).compact();
    }

    public String createRefreshToken(Long memberId) {
        return buildToken(memberId, refreshTokenExpiration, REFRESH)
                .id(UUID.randomUUID().toString())
                .compact();
    }

    private JwtBuilder buildToken(Long memberId, long expiration, String tokenType) {
        Date now = new Date();

        return Jwts.builder()
                .subject(String.valueOf(memberId))
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiration))
                .claim(TOKEN_TYPE_CLAIM, tokenType)
                .signWith(secretKey);
    }

    public Long getMemberId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String tokenType = claims.get(TOKEN_TYPE_CLAIM, String.class);
        if (tokenType != null && !ACCESS.equals(tokenType)) {
            throw new JwtException("Access Token이 아닙니다.");
        }

        return Long.valueOf(claims.getSubject());
    }

    public Long getRefreshTokenMemberId(String refreshToken) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .require(TOKEN_TYPE_CLAIM, REFRESH)
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload();

        if (claims.getExpiration() == null) {
            throw new JwtException("Refresh Token의 만료 시간이 없습니다.");
        }

        return Long.valueOf(claims.getSubject());
    }
}

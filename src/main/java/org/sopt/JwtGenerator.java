package org.sopt;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

import static org.sopt.JwtConfig.ACCESS;
import static org.sopt.JwtConfig.REFRESH;
import static org.sopt.JwtConfig.TOKEN_TYPE_CLAIM;

@Component
public class JwtGenerator {

    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtGenerator(
            SecretKey secretKey,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration
    ) {
        this.secretKey = secretKey;
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
}

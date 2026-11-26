package org.sopt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import static org.sopt.JwtConfig.ACCESS;
import static org.sopt.JwtConfig.REFRESH;
import static org.sopt.JwtConfig.TOKEN_TYPE_CLAIM;

@Component
public class JwtParser {

    private final io.jsonwebtoken.JwtParser parser;

    public JwtParser(SecretKey secretKey) {
        this.parser = Jwts.parser().verifyWith(secretKey).build();
    }

    public Long getAccessTokenMemberId(String token) {
        Claims claims = parseSignedClaims(token);
        String tokenType = claims.get(TOKEN_TYPE_CLAIM, String.class);
        // 실습에서 발급한 Access Token에는 tokenType이 없었다.
        if (tokenType != null && !ACCESS.equals(tokenType)) {
            throw new JwtException("Access Token이 아닙니다.");
        }

        return Long.valueOf(claims.getSubject());
    }

    public Long getRefreshTokenMemberId(String token) {
        Claims claims = parseSignedClaims(token);
        if (!REFRESH.equals(claims.get(TOKEN_TYPE_CLAIM, String.class))) {
            throw new JwtException("Refresh Token이 아닙니다.");
        }
        if (claims.getExpiration() == null) {
            throw new JwtException("Refresh Token의 만료 시간이 없습니다.");
        }

        return Long.valueOf(claims.getSubject());
    }

    private Claims parseSignedClaims(String token) {
        return parser.parseSignedClaims(token).getPayload();
    }
}

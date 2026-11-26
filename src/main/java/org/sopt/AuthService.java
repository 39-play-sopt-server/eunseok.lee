package org.sopt;

import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Service
public class AuthService {

    private static final String INVALID_REFRESH_TOKEN = "유효하지 않은 Refresh Token입니다.";

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;
    private final JwtParser jwtParser;

    public AuthService(
            MemberRepository memberRepository,
            PasswordEncoder passwordEncoder,
            JwtGenerator jwtGenerator,
            JwtParser jwtParser
    ) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.jwtParser = jwtParser;
    }

    @Transactional
    public Long signup(String email, String name, String password) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        Member member = new Member(email, name, passwordEncoder.encode(password));
        return memberRepository.save(member).getId();
    }

    @Transactional
    public LoginTokens login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        String accessToken = jwtGenerator.createAccessToken(member.getId());
        String refreshToken = jwtGenerator.createRefreshToken(member.getId());
        member.updateRefreshTokenHash(hashRefreshToken(refreshToken));

        return new LoginTokens(accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public String reissue(String refreshToken) {
        Member member = validateRefreshToken(refreshToken, false);
        return jwtGenerator.createAccessToken(member.getId());
    }

    @Transactional
    public void logout(String refreshToken) {
        Member member = validateRefreshToken(refreshToken, true);
        member.updateRefreshTokenHash(null);
    }

    private Member validateRefreshToken(String refreshToken, boolean forUpdate) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BadCredentialsException(INVALID_REFRESH_TOKEN);
        }

        Long memberId;
        try {
            memberId = jwtParser.getRefreshTokenMemberId(refreshToken);
        } catch (JwtException | IllegalArgumentException e) {
            throw new BadCredentialsException(INVALID_REFRESH_TOKEN, e);
        }

        Member member = (forUpdate
                ? memberRepository.findByIdForUpdate(memberId)
                : memberRepository.findById(memberId))
                .orElseThrow(() -> new BadCredentialsException(INVALID_REFRESH_TOKEN));
        String storedHash = member.getRefreshTokenHash();
        String tokenHash = hashRefreshToken(refreshToken);
        if (storedHash == null || !MessageDigest.isEqual(
                storedHash.getBytes(StandardCharsets.US_ASCII),
                tokenHash.getBytes(StandardCharsets.US_ASCII)
        )) {
            throw new BadCredentialsException(INVALID_REFRESH_TOKEN);
        }

        return member;
    }

    private String hashRefreshToken(String refreshToken) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] hash = sha256.digest(refreshToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256을 사용할 수 없습니다.", e);
        }
    }

    public record LoginTokens(String accessToken, String refreshToken) {
    }
}

package org.sopt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final Duration refreshCookieMaxAge;

    public AuthController(
            AuthService authService,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration
    ) {
        this.authService = authService;
        this.refreshCookieMaxAge = Duration.ofMillis(refreshTokenExpiration);
    }

    @PostMapping("/signup")
    public ResponseEntity<Long> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(authService.signup(request.email(), request.name(), request.password()));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        AuthService.LoginTokens tokens = authService.login(request.email(), request.password());
        ResponseCookie refreshCookie = buildRefreshCookie(tokens.refreshToken(), refreshCookieMaxAge);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new LoginResponse(tokens.accessToken()));
    }

    @PostMapping("/reissue")
    public ResponseEntity<LoginResponse> reissue(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            @RequestHeader(name = "X-CSRF-Protection", required = false) String csrfProtection
    ) {
        requireCsrfProtection(csrfProtection);
        return ResponseEntity.ok(new LoginResponse(authService.reissue(refreshToken)));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            @RequestHeader(name = "X-CSRF-Protection", required = false) String csrfProtection
    ) {
        requireCsrfProtection(csrfProtection);
        authService.logout(refreshToken);

        ResponseCookie expiredCookie = buildRefreshCookie("", Duration.ZERO);
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, expiredCookie.toString())
                .build();
    }

    private void requireCsrfProtection(String csrfProtection) {
        if (!"1".equals(csrfProtection)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    private ResponseCookie buildRefreshCookie(String value, Duration maxAge) {
        return ResponseCookie.from("refreshToken", value)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/auth")
                .maxAge(maxAge)
                .build();
    }
}


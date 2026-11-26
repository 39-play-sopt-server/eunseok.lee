
package org.sopt;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "회원", description = "회원 API")
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/me")
    public ResponseEntity<Long> me(
            @AuthenticationPrincipal Long memberId
    ) {
        return ResponseEntity.ok(memberId);
    }

    @Operation(
            summary = "회원 삭제",
            description = "회원의 게시글을 먼저 삭제한 후 회원 정보를 삭제합니다."
    )
    @DeleteMapping("/api/v1/members/{memberId}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(
            @PathVariable(name = "memberId") Long memberId,
            @AuthenticationPrincipal Long authenticatedMemberId
    ) {
        if (!memberId.equals(authenticatedMemberId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        memberService.deleteMember(memberId);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, AuthController.expiredRefreshCookie().toString())
                .body(new ApiResponse<>("회원이 삭제되었습니다.", null));
    }
}

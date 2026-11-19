
package org.sopt;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원", description = "회원 API")
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/me")
    public ResponseEntity<Long> me(
            @AuthenticationPrincipal Long memberId
    ) {
        return ResponseEntity.ok(memberId);
    }

    @Operation(
            summary = "회원 삭제",
            description = "회원의 게시글을 먼저 삭제한 후 회원 정보를 삭제합니다."
    )
    @DeleteMapping("/{memberId}")
    public ApiResponse<Void> deleteMember(
            @PathVariable(name = "memberId") Long memberId
    ) {
        memberService.deleteMember(memberId);
        return new ApiResponse<>("회원이 삭제되었습니다.", null);
    }
}

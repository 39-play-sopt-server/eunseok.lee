package org.sopt;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원", description = "회원 API")
@RestController
@RequestMapping(path = "/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(
            summary = "회원 생성",
            description = "이름을 받아 회원 정보를 생성하고 ID와 이름을 반환합니다.",
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "회원 생성 성공",
                    useReturnTypeSchema = true
            )
    )
    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponse>> createMember(
            @RequestBody CreateMemberRequest request
    ) {
        Member member = memberService.createMember(request.getName());
        MemberResponse memberResponse = new MemberResponse(member);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("회원이 생성되었습니다.", memberResponse));
    }

    @Operation(summary = "회원 삭제", description = "회원의 게시글을 먼저 삭제한 후 회원 정보를 삭제합니다.")
    @DeleteMapping(path = "/{memberId}")
    public ApiResponse<Void> deleteMember(
            @PathVariable(name = "memberId") Long memberId
    ) {
        memberService.deleteMember(memberId);
        return new ApiResponse<>("회원이 삭제되었습니다.", null);
    }
}

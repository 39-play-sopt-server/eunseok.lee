package org.sopt;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "응답")
public class MemberResponse {
    @Schema(description = "회원 ID", example = "1")
    private final Long id;
    @Schema(description = "회원 이름", example = "은석")
    private final String name;
    @Schema(description = "생성일")
    private final LocalDateTime createdAt;

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.createdAt = member.getCreatedAt();
    }

    public Long getId() {
        return this.id;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public String getName() {
        return this.name;
    }
}

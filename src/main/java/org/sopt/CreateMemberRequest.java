package org.sopt;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 생성 요청")
public class CreateMemberRequest {
    @Schema(description = "회원 이름", example = "은석", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    public CreateMemberRequest() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

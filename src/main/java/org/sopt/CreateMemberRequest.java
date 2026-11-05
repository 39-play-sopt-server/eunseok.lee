package org.sopt;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "회원 생성 요청")
public class CreateMemberRequest {
    @Schema(description = "회원 이름", example = "은석", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "회원 이름을 입력하세요.")
    @Size(max = 255, message = "회원 이름은 255자를 넘을 수 없습니다.")
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

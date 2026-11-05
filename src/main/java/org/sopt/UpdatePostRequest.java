package org.sopt;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "게시글 수정 요청")
public class UpdatePostRequest {
    @Schema(description = "게시글 제목", example = "첫 게시글입니다", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "제목을 입력하세요.")
    @Size(max = 255, message = "제목은 255자를 넘을 수 없습니다.")
    private String title;
    @Schema(description = "게시글 내용", example = "게시글 내용입니다", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "내용을 입력하세요.")
    private String content;

    public UpdatePostRequest() {
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

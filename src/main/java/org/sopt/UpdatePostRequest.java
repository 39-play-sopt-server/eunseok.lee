package org.sopt;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 수정 요청")
public class UpdatePostRequest {
    @Schema(description = "게시글 제목", example = "첫 게시글입니다", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;
    @Schema(description = "게시글 내용", example = "게시글 내용입니다", requiredMode = Schema.RequiredMode.REQUIRED)
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

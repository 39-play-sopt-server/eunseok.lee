package org.sopt;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 생성 요청")
public class CreatePostRequest {
    @Schema(description = "게시글 제목", example = "첫 게시글입니다", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;
    @Schema(description = "게시글 내용", example = "게시글 내용입니다", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;
    @Schema(description = "게시글 카테고리", example = "FREE", requiredMode = Schema.RequiredMode.REQUIRED)
    private Post.Category category;

    public CreatePostRequest() {
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

    public Post.Category getCategory() {
        return this.category;
    }

    public void setCategory(Post.Category category) {
        this.category = category;
    }
}

package org.sopt;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "게시글 생성 요청")
public class CreatePostRequest {
    @Schema(description = "게시글 제목", example = "첫 게시글입니다", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "제목을 입력하세요.")
    @Size(max = 255, message = "제목은 255자를 넘을 수 없습니다.")
    private String title;
    @Schema(description = "게시글 내용", example = "게시글 내용입니다", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "내용을 입력하세요.")
    private String content;
    @Schema(description = "게시글 카테고리", example = "FREE", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "카테고리를 입력하세요.")
    private Post.Category category;

    @Schema(description = "작성자 회원 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "작성자 ID를 입력하세요.")
    @Positive(message = "작성자 ID는 1 이상이어야 합니다.")
    private Long memberId;

    public CreatePostRequest() {
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
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

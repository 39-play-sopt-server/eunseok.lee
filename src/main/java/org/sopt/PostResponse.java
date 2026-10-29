package org.sopt;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 응답")
public class PostResponse {
    @Schema(description = "게시글 ID", example = "1")
    private final Long id;
    @Schema(description = "게시글 제목", example = "첫 게시글입니다")
    private final String title;
    @Schema(description = "게시글 내용", example = "게시글 내용입니다")
    private final String content;
    @Schema(description = "게시글 카테고리", example = "FREE")
    private final Post.Category category;
    @Schema(description = "상세 조회 시 증가하는 조회수", example = "0")
    private final int views;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.views = post.getViews();
    }

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public Post.Category getCategory() {
        return this.category;
    }

    public int getViews() {
        return this.views;
    }
}

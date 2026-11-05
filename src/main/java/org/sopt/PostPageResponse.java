package org.sopt;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;
import java.util.List;

@Schema(description = "게시글 목록 페이지 응답")
public class PostPageResponse {
    @Schema(description = "게시글 목록")
    private final List<PostResponse> content;
    @Schema(description = "페이지 번호 (0부터 시작)", example = "0")
    private final int number;
    @Schema(description = "페이지 크기", example = "10")
    private final int size;
    @Schema(description = "전체 게시글 수", example = "25")
    private final long totalElements;
    @Schema(description = "전체 페이지 수", example = "3")
    private final int totalPages;

    public PostPageResponse(Page<PostResponse> posts) {
        this.content = posts.getContent();
        this.number = posts.getNumber();
        this.size = posts.getSize();
        this.totalElements = posts.getTotalElements();
        this.totalPages = posts.getTotalPages();
    }

    public List<PostResponse> getContent() {
        return this.content;
    }

    public int getNumber() {
        return this.number;
    }

    public int getSize() {
        return this.size;
    }

    public long getTotalElements() {
        return this.totalElements;
    }

    public int getTotalPages() {
        return this.totalPages;
    }
}

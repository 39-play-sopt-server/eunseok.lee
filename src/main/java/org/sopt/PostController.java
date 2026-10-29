package org.sopt;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@Tag(name = "게시글", description = "게시글 생성, 조회, 수정, 삭제 API")
@RestController
@RequestMapping(path = "/api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(
            summary = "게시글 생성",
            description = "게시글을 생성하고 ID를 포함한 게시글 정보를 반환합니다.",
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "게시글 생성 성공",
                    useReturnTypeSchema = true
            )
    )
    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @RequestBody CreatePostRequest request
    ) {
        PostResponse postResponse = postService.createPost(
                request.getTitle(), request.getContent(), request.getCategory(), request.getMemberId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("게시글이 작성되었습니다.", postResponse));
    }

    @Operation(summary = "게시글 목록 조회", description = "생성일 최신순으로 페이지를 조회합니다. 조회수는 증가하지 않습니다.")
    @GetMapping
    public ApiResponse<Page<PostResponse>> readPosts(
            @Parameter(description = "페이지 번호 (1부터 시작)", example = "1")
            @RequestParam(name = "page", defaultValue = "1") int page,
            @Parameter(description = "페이지 크기 (1~100)", example = "10")
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<PostResponse> postResponses = postService.getAllPosts(page, size);
        return new ApiResponse<>("게시글 목록 조회 성공", postResponses);
    }

    @Operation(summary = "게시글 상세 조회", description = "게시글 상세 정보를 조회하고 조회수를 1 증가시킵니다.")
    @GetMapping(path = "/{postId}")
    public ApiResponse<PostResponse> readPost(
            @PathVariable(name = "postId") Long postId
    ) {
        PostResponse postResponse = postService.readPost(postId);
        return new ApiResponse<>("게시글 조회 성공", postResponse);
    }

    @Operation(summary = "게시글 수정", description = "게시글의 제목과 내용을 수정합니다.")
    @PutMapping(path = "/{postId}")
    public ApiResponse<Void> updatePost(
            @PathVariable(name = "postId") Long postId,
            @RequestBody UpdatePostRequest request
    ) {
        postService.updatePost(postId, request.getTitle(), request.getContent());
        return new ApiResponse<>("게시글이 수정되었습니다.", null);
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @DeleteMapping(path = "/{postId}")
    public ApiResponse<Void> deletePost(
            @PathVariable(name = "postId") Long postId
    ) {
        postService.deletePost(postId);
        return new ApiResponse<>("게시글이 삭제되었습니다.", null);
    }
}
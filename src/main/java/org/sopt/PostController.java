package org.sopt;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ApiResponse<?> createPost(
            @RequestBody CreatePostRequest request
    ) {
        postService.createPost(request.getTitle(), request.getContent(), request.getCategory());
        return new ApiResponse<>("게시글이 작성되었습니다.", null);
    }

    @GetMapping
    public ApiResponse<?> readPosts(
            @RequestParam(name = "page", defaultValue = "1") int page
    ) {
        List<Post> posts = postService.getAllPosts();
        return new ApiResponse<>("게시글 목록 조회 성공", posts);
    }

    @GetMapping(path = "/{postId}")
    public ApiResponse<?> readPost(
            @PathVariable(name = "postId") Long postId
    ) {
        return new ApiResponse<>("게시글 조회 성공", postService.readPost(postId));
    }

    @PutMapping(path = "/{postId}")
    public ApiResponse<?> updatePost(
            @PathVariable(name = "postId") Long postId,
            @RequestBody UpdatePostRequest request
    ) {
        postService.updatePost(postId, request.getTitle(), request.getContent());
        return new ApiResponse<>("게시글이 수정되었습니다.", null);
    }

    @DeleteMapping(path = "/{postId}")
    public ApiResponse<?> deletePost(
            @PathVariable(name = "postId") Long postId
    ) {
        postService.deletePost(postId);
        return new ApiResponse<>("게시글이 삭제되었습니다.", null);
    }
}
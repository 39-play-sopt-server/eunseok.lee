// PostController
package org.sopt;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestBody(required = true) CreatePostRequest request
    ) {
        postService.createPost(request.getTitle(), request.getContent(), request.getCategory());
        return new ApiResponse<>("게시글이 작성되었습니다.");
    }

    @GetMapping
    public ApiResponse<?> readPosts(
            @RequestParam(name = "page", defaultValue = "1") int page
    ) {
        // page 값을 받는 바인딩 실습입니다. 실제 페이지네이션은 심화 과제에서 구현합니다.
        List<Post> posts = postService.getAllPosts();
        return new ApiResponse<>(posts);
    }

    @GetMapping(path = "/{postId}")
    public ApiResponse<?> readPost(
            @PathVariable(name = "postId") Long postId
    ) {
        return new ApiResponse<>(postService.getPost(postId));
    }

    @PutMapping(path = "/{postId}")
    public ApiResponse<?> updatePost(
            @PathVariable(name = "postId") Long postId,
            @RequestBody UpdatePostRequest request
    ) {
        postService.updatePost(postId, request.getTitle(), request.getContent());
        return new ApiResponse<>("게시글이 수정되었습니다.");
    }

    @DeleteMapping(path = "/{postId}")
    public ResponseEntity<?> deletePost(
            @PathVariable(name = "postId") Long postId
    ) {
        postService.deletePost(postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("삭제 완료");
    }
}
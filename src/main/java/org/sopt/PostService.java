package org.sopt;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.ArrayList;
import java.util.List;
import org.sopt.exception.MemberNotFoundException;
import org.sopt.exception.PostNotFoundException;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public PostService(PostRepository postRepository, MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    public PostResponse createPost(String title, String content, Post.Category category, Long memberId) {
        if (memberId == null || memberId < 1) {
            throw new IllegalArgumentException("작성자 ID를 입력하세요.");
        }
        Member member = memberRepository.findByIdForUpdate(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        Post post = new Post(title, content, category, member);
        postRepository.save(post);
        return new PostResponse(post);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getAllPosts(int page, int size) {
        if (page < 1 || size < 1 || size > 100) {
            throw new IllegalArgumentException("page는 1 이상, size는 1~100이어야 합니다.");
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt", "id");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Post> posts = postRepository.findAll(pageable);
        List<PostResponse> postResponses = new ArrayList<>();
        for (Post post : posts.getContent()) {
            postResponses.add(new PostResponse(post));
        }
        return new PageImpl<>(postResponses, pageable, posts.getTotalElements());
    }

    private Post getPost(Long postId) {
        if (postId == null || postId < 1) {
            throw new PostNotFoundException("존재하지 않는 게시글입니다.");
        }
        return postRepository.findByIdForUpdate(postId)
                .orElseThrow(() -> new PostNotFoundException("존재하지 않는 게시글입니다."));
    }

    public void updatePost(Long postId, String title, String content) {
        Post post = getPost(postId);
        post.update(title, content);
    }

    public void deletePost(Long postId) {
        Post post = getPost(postId);
        postRepository.delete(post);
    }

    public PostResponse readPost(Long postId) {
        Post post = getPost(postId);
        post.increaseViews();
        return new PostResponse(post);
    }
}

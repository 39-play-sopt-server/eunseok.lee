package org.sopt;

import org.springframework.stereotype.Service;

import java.util.List;
import org.sopt.exception.PostNotFoundException;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(String title, String content, Post.Category category) {
        Post post = new Post(title, content, category);
        postRepository.addPost(post);
        return post;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    private Post getPost(Long postId) {
        Post post = postRepository.findById(postId);

        if (post == null) {
            throw new PostNotFoundException("존재하지 않는 게시글입니다.");
        }
        return post;
    }

    public void updatePost(Long postId, String title, String content) {
        Post post = getPost(postId);

        post.update(title, content);
    }

    public void deletePost(Long postId) {
        Post post = getPost(postId);
        postRepository.deletePost(post);
    }

    public Post readPost(Long postId) {
        Post post = getPost(postId);
        post.increaseViews();
        return post;
    }


}

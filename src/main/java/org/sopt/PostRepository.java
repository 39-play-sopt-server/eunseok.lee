package org.sopt;


import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Repository
public class PostRepository {
    private final Map<Long, Post> posts = new HashMap<>();

    private Long temporaryId = 1L;

    public void addPost(Post post) {
        post.assignId(temporaryId++);
        posts.put(post.getId(), post);
    }

    public void deletePost(Post post) {
        posts.remove(post.getId());
    }

    public Post findById(Long postId){
        if (postId == null || postId < 1) {
            return null;
        }
        return posts.get(postId);
    }

    public List<Post> findAll() {
        return new ArrayList<>(posts.values());
    }

}

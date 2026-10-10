package org.sopt;

import java.util.ArrayList;
import java.util.List;

public class PostRepository {
    private final List<Post> posts = new ArrayList<>();

    public void addPost(Post post) {
        posts.add(post);
    }

    public void deletePost(Post post) {
        posts.remove(post);
    }

    public Post findByIndex(int index){
        return posts.get(index);
    }

    public List<Post> findAll() {
        return posts;
    }
}

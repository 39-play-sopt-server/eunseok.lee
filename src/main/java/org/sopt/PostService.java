package org.sopt;

import java.util.List;


public class PostService {

    private final PostRepository repository = new PostRepository();

    public void createPost(String title, String content, Post.Category category) {
        Post post = new Post(title, content, category);
        repository.addPost(post);
    }

    public List<Post> getAllPosts() {
        return repository.findAll();
    }

    public Post getPost(int index) {
        if (index < 0 || index >= repository.findAll().size()) throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        return repository.findByIndex(index);
    }

    public void updatePost(int index, String title, String content) {
        Post post = getPost(index);

        post.updateTitle(title);
        post.updateContent(content);
    }

    public void deletePost(int index) {
        Post post = getPost(index);
        repository.deletePost(post);
    }


}

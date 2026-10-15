package org.sopt;

public class CreatePostRequest {
    private String title;
    private String content;
    private Post.Category category;

    public CreatePostRequest() {
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Post.Category getCategory() {
        return this.category;
    }

    public void setCategory(Post.Category category) {
        this.category = category;
    }
}

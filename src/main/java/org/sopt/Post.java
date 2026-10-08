package org.sopt;



public class Post {
    private String title;
    private String content;
    private Category category;

    public enum Category {
        FREE,
        QUESTION,
        INFORMATION
    }

    public Post(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;

        checkTitle(title);
        checkContent(content);
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public Category getCategory() {
        return this.category;
    }

    public void updateTitle(String title) {
        checkTitle(title);
        this.title = title;
    }

    public void updateContent(String content) {
        checkContent(content);
        this.content = content;

    }

    private void checkTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목을 입력하세요.");
        }
    }

    private void checkContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("내용을 입력하세요.");
        }
    }
}
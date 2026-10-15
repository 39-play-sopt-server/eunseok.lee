package org.sopt;



public class Post {
    private Long id;
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
        if (category == null) {
            throw new IllegalArgumentException("카테고리를 입력하세요.");
        }
    }

    public Long getId() {
        return this.id;
    }

    // ID는 저장소에서 처음 저장할 때 부여합니다.
    void assignId(Long id) {
        this.id = id;
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

    public void update(String title, String content) {
        checkTitle(title);
        checkContent(content);
        this.title = title;
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
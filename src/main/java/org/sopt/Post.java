package org.sopt;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    @Enumerated(EnumType.STRING)
    private Category category;
    private int views = 0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public enum Category {
        FREE,
        QUESTION,
        INFORMATION
    }

    protected Post() {
    }

    public Post(String title, String content, Category category, Member member) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.member = member;

        if (member == null) {
            throw new IllegalArgumentException("작성자를 입력하세요.");
        }
        checkTitle(title);
        checkContent(content);
        if (category == null) {
            throw new IllegalArgumentException("카테고리를 입력하세요.");
        }
    }

    public Long getId() {
        return this.id;
    }

    //2차 과제 : 조회수 필드 추가
    public int getViews() {
        return this.views;
    }

    public void increaseViews() {
        this.views++;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public Member getMember() {
        return this.member;
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
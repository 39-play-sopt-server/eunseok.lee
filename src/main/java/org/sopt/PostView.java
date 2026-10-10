// PostView
package org.sopt;

import java.util.Scanner;

public class PostView {
    private final Scanner scanner = new Scanner(System.in);

    public void printMenu() {
        System.out.println("\n=== 게시판 ===");
        System.out.println("1. 게시글 작성");
        System.out.println("2. 게시글 목록 조회");
        System.out.println("3. 게시글 단건 조회");
        System.out.println("4. 게시글 수정");
        System.out.println("5. 게시글 삭제");
        System.out.println("6. 종료");
    }

    public int readCommand() {
        System.out.print("선택: ");
        return Integer.parseInt(scanner.nextLine());
    }

    public String readTitle() {
        System.out.print("제목: ");
        return scanner.nextLine();
    }

    public String readContent() {
        System.out.print("내용: ");
        return scanner.nextLine();
    }

    public Post.Category readCategory() {
        System.out.println("카테고리를 입력하세요");
        System.out.println("1. 자유게시판");
        System.out.println("2. 질문게시판");
        System.out.println("3. 정보게시판");

        int num = Integer.parseInt(scanner.nextLine());

        return switch(num) {
            case 1 -> Post.Category.FREE;
            case 2 -> Post.Category.QUESTION;
            case 3 -> Post.Category.INFORMATION;
            default -> throw new IllegalArgumentException("올바른 카테고리를 다시 입력해주세요.");
        };
    };



    public int readPostNumber(String message) {
        System.out.print(message);
        return Integer.parseInt(scanner.nextLine());
    }

    public void printPost(Post post) {
        System.out.println("\n=== 게시글 ===");
        System.out.println("제목: " + post.getTitle());
        System.out.println("카테고리: " + post.getCategory());
        System.out.println("내용: " + post.getContent());
    }

    public void printMessage(String message) {
        System.out.println(message);
    }
}

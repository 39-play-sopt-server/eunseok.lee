// PostController
package org.sopt;

import java.util.List;

public class PostController {
    private final PostService service = new PostService();
    private final PostView view;

    public PostController(PostView view) {
        this.view = view;
    }

    public void run() {
        while (true) {
            view.printMenu();
            int command = view.readCommand();
            switch (command) {
                case 1 -> createPost();
                case 2 -> readPosts();
                case 3 -> readPost();
                case 4 -> updatePost();
                case 5 -> deletePost();
                case 6 -> {
                    view.printMessage("프로그램을 종료합니다.");
                    return;
                }
                default -> view.printMessage("잘못된 입력입니다.");
            }
        }
    }

    private void createPost() {
        try {
            String title = view.readTitle();
            String content = view.readContent();
            Post.Category category = view.readCategory();

            service.createPost(title, content, category);
            view.printMessage("게시글이 작성되었습니다.");
        } catch (IllegalArgumentException e) {
            view.printMessage(e.getMessage());
        }

    }

    private void readPosts() {
        List<Post> posts = service.getAllPosts();

        if (posts.isEmpty()) {
            view.printMessage("게시글이 없습니다.");
            return;
        }
        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);
            view.printMessage((i + 1) + ". [" + post.getCategory() + "] " + post.getTitle());        }
    }

    private void readPost() {
        if (service.getAllPosts().isEmpty()) {
            view.printMessage("게시글이 없습니다.");
            return;
        }

        try {
            int index = view.readPostNumber("조회할 게시글 번호: ") - 1;

            Post post = service.getPost(index);
            view.printPost(post);
        } catch (IllegalArgumentException e) {
            view.printMessage(e.getMessage());
        }
    }

    private void updatePost() {
        if (service.getAllPosts().isEmpty()) {
            view.printMessage("게시글이 없습니다.");
            return;
        }


        try {
            int index = view.readPostNumber("수정할 게시글 번호: ") - 1;

            String newTitle = view.readTitle();
            String newContent = view.readContent();

            service.updatePost(index, newTitle, newContent);

            view.printMessage("게시글이 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            view.printMessage(e.getMessage());
        }
    }

    private void deletePost() {
        if (service.getAllPosts().isEmpty()) {
            view.printMessage("게시글이 존재하지 않습니다.");
            return;
        }

        try {
            int index = view.readPostNumber("삭제할 게시글 번호: ") - 1;

            service.deletePost(index);
            view.printMessage("게시글이 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            view.printMessage(e.getMessage());
        }
    }

}
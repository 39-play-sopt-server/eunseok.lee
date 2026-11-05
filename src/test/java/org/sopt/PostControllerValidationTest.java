package org.sopt;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sopt.exception.GlobalExceptionHandler;
import org.sopt.exception.MemberNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import java.util.List;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostControllerValidationTest {
    private PostService postService;
    private MockMvc mockMvc;
    private LocalValidatorFactoryBean validator;

    @BeforeEach
    void setUp() {
        postService = mock(PostService.class);
        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mockMvc = MockMvcBuilders.standaloneSetup(new PostController(postService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @AfterEach
    void tearDown() {
        validator.close();
    }

    @Test
    void rejectsMissingEmptyAndBlankTitlesBeforeService() throws Exception {
        String[] titles = {"null", "\"\"", "\"   \""};
        for (String title : titles) {
            String body = "{\"title\":" + title
                    + ",\"content\":\"본문\",\"category\":\"FREE\",\"memberId\":1}";
            mockMvc.perform(post("/api/v1/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("제목을 입력하세요."))
                    .andExpect(jsonPath("$.data").value(nullValue()));
        }
        verifyNoInteractions(postService);
    }

    @Test
    void validTitleStillCreatesPost() throws Exception {
        Member member = new Member("회원");
        Post entity = new Post("검증 제목", "본문", Post.Category.FREE, member);
        when(postService.createPost("검증 제목", "본문", Post.Category.FREE, 1L))
                .thenReturn(new PostResponse(entity));

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"검증 제목\",\"content\":\"본문\",\"category\":\"FREE\",\"memberId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("게시글이 작성되었습니다."))
                .andExpect(jsonPath("$.data.title").value("검증 제목"));
        verify(postService).createPost("검증 제목", "본문", Post.Category.FREE, 1L);
    }

    @Test
    void validTitleStillReturnsNotFoundForUnknownMember() throws Exception {
        when(postService.createPost("검증 제목", "본문", Post.Category.FREE, 1L))
                .thenThrow(new MemberNotFoundException("존재하지 않는 회원입니다."));

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"검증 제목\",\"content\":\"본문\",\"category\":\"FREE\",\"memberId\":1}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 회원입니다."));
    }

    @Test
    void returnsOnlySelectedPageFields() throws Exception {
        Member member = new Member("회원");
        Post entity = new Post("목록 제목", "본문", Post.Category.FREE, member);
        PageRequest pageable = PageRequest.of(1, 1, Sort.by(Sort.Direction.DESC, "createdAt", "id"));
        Page<PostResponse> posts = new PageImpl<>(List.of(new PostResponse(entity)), pageable, 3);
        when(postService.getAllPosts(2, 1)).thenReturn(posts);

        mockMvc.perform(get("/api/v1/posts").param("page", "2").param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("게시글 목록 조회 성공"))
                .andExpect(jsonPath("$.data").value(aMapWithSize(5)))
                .andExpect(jsonPath("$.data.content").value(hasSize(1)))
                .andExpect(jsonPath("$.data.content[0].title").value("목록 제목"))
                .andExpect(jsonPath("$.data.number").value(1))
                .andExpect(jsonPath("$.data.size").value(1))
                .andExpect(jsonPath("$.data.totalElements").value(3))
                .andExpect(jsonPath("$.data.totalPages").value(3))
                .andExpect(jsonPath("$.data.pageable").doesNotExist())
                .andExpect(jsonPath("$.data.sort").doesNotExist());
        verify(postService).getAllPosts(2, 1);
    }

    @Test
    void emptyPageKeepsSelectedFieldsAndDefaultPageSettings() throws Exception {
        when(postService.getAllPosts(1, 10)).thenReturn(Page.empty(PageRequest.of(0, 10)));

        mockMvc.perform(get("/api/v1/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(aMapWithSize(5)))
                .andExpect(jsonPath("$.data.content").value(hasSize(0)))
                .andExpect(jsonPath("$.data.number").value(0))
                .andExpect(jsonPath("$.data.size").value(10))
                .andExpect(jsonPath("$.data.totalElements").value(0))
                .andExpect(jsonPath("$.data.totalPages").value(0));
        verify(postService).getAllPosts(1, 10);
    }
}

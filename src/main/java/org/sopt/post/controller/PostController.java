package org.sopt.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sopt.common.exception.SuccessCode;
import org.sopt.common.response.SuccessResponse;
import org.sopt.post.dto.request.CreatePostRequest;
import org.sopt.post.dto.request.DeletePostRequest;
import org.sopt.post.dto.request.UpdatePostRequest;
import org.sopt.post.dto.response.CreatePostResponse;
import org.sopt.post.dto.response.PostListResponse;
import org.sopt.post.dto.response.PostResponse;
import org.sopt.post.service.PostService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Post", description = "게시글 관련 API")
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(
            summary = "게시글 생성",
            description = "신규 게시글 생성 API."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공"),
            @ApiResponse(responseCode = "404", description = "사용자을 찾을 수 없음 — 존재하지 않는 사용자 ID로 요청한 경우")
    })
    @PostMapping
    public ResponseEntity<SuccessResponse<CreatePostResponse>> createPost(
            @Valid @RequestBody CreatePostRequest request
    ) {
        CreatePostResponse response = postService.createPost(request);

        return ResponseEntity
                .status(SuccessCode.POST_CREATED.getStatus())
                .body(SuccessResponse.of(SuccessCode.POST_CREATED, response));
    }

    @Operation(
            summary = "게시글 다건 조회",
            description = "검색어로 제목 검색 및 게시판 종류에 따른 게시글 다건 조회 API. type: FREE(자유게시판), HOT(HOT 게시판), SECRET(비밀 게시판), Default : ALL(전체 조회)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 — 올바르지 않은 type값으로 요청한 경우")
    })
    @GetMapping
    public ResponseEntity<SuccessResponse<List<PostListResponse>>> getAllPosts(
            @Parameter(description = "제목 검색 키워드 ", example = "톰캣")
            @RequestParam(required = false) String keyword,
            @Parameter(description = "조회할 게시판 타입", example = "FREE", required = false)
            @RequestParam(defaultValue = "ALL") String type,
            @Parameter(description = "조회할 페이지", example = "1", required = false)
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "조회할 페이지 사이즈", example = "10", required = true)
            @RequestParam int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<PostListResponse> responses = postService.getAllPosts(keyword, type, pageable);

        return ResponseEntity
                .ok(SuccessResponse.of(SuccessCode.POST_LIST_FOUND, responses));
    }

    @Operation(
            summary = "게시글 단건 조회",
            description = "게시글 ID로 특정 게시글을 조회하는 API."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음 — 존재하지 않는 게시글 ID로 요청한 경우")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<PostResponse>> getPost(
            @Parameter(description = "조회할 게시글 id", example = "1", required = true)
            @PathVariable Long id
    ) {
        PostResponse response = postService.getPost(id);

        return ResponseEntity
                .ok(SuccessResponse.of(SuccessCode.POST_FOUND, response));
    }

    @Operation(
            summary = "게시글 수정",
            description = "게시글 ID로 특정 게시글을 수정하는 API."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "403", description = "올바르지 않은 접근 — 게시글 작성자 본인이 아닌 경우"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음 — 존재하지 않는 게시글 ID로 요청한 경우")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<PostResponse>> updatePost(
            @Parameter(description = "수정할 게시글 id", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdatePostRequest request
    ) {
        PostResponse response = postService.updatePost(id, request);

        return ResponseEntity
                .ok(SuccessResponse.of(SuccessCode.POST_UPDATED, response));
    }

    @Operation(
            summary = "게시글 삭제",
            description = "게시글 ID로 특정 게시글을 삭제하는 API."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "403", description = "올바르지 않은 접근 — 게시글 작성자 본인이 아닌 경우"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음 — 존재하지 않는 게시글 ID로 요청한 경우")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> deletePost(
            @PathVariable Long id,
            @Valid @RequestBody DeletePostRequest request
    ) {
        postService.deletePost(id, request);
        return ResponseEntity
                .ok(SuccessResponse.of(SuccessCode.POST_DELETED));
    }
}

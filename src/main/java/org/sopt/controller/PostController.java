package org.sopt.controller;

import org.sopt.common.ApiResponse;
import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.response.CreatePostResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.exception.CustomException;
import org.sopt.service.PostService;

import java.util.List;

public class PostController {
    private final PostService postService = new PostService();

    // POST /posts 
    public CreatePostResponse createPost(CreatePostRequest request) {
        try {
            return postService.createPost(request);
        } catch (IllegalArgumentException e) {
            return new CreatePostResponse(null, "🚫 " + e.getMessage());
        }
    }

    // GET /posts 📝 과제
    public ApiResponse<List<PostResponse>> getAllPosts() {
        return ApiResponse.ok(postService.getAllPosts(), "게시글 목록 조회 성공.");
    }

    // GET /posts/{id} 📝 과제
    public ApiResponse<PostResponse> getPost(Long id) {
        // TODO: postService.getPost(id) 호출, 예외 발생 시 null 반환
        try{
            return ApiResponse.ok(postService.getPost(id), "게시글 조회 성공.");
        }
        catch (CustomException e){
            return ApiResponse.fail(e.getErrorCode().getStatus(), e.getErrorCode().getMessage());
        }
    }

    // PUT /posts/{id} 📝 과제
    public ApiResponse<Void> updatePost(Long id, String newTitle, String newContent) {
        // TODO: postService.updatePost() 호출, 예외 발생 시 에러 메시지 출력
        try{
            postService.updatePost(id, newTitle, newContent);
            return ApiResponse.success(200, "게시글 수정 성공.");
        }
        catch (CustomException e){
            return ApiResponse.fail(e.getErrorCode().getStatus(), e.getErrorCode().getMessage());
        }
    }

    // DELETE /posts/{id} 📝 과제
    public ApiResponse<Void> deletePost(Long id) {
        // TODO: postService.deletePost() 호출, 예외 발생 시 에러 메시지 출력
        try{
            postService.deletePost(id);
            return ApiResponse.success(200, "게시글 삭제 성공.");
        }
        catch (CustomException e){
            return ApiResponse.fail(e.getErrorCode().getStatus(), e.getErrorCode().getMessage());
        }
    }
}

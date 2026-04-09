package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.response.CreatePostResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.exception.CustomException;
import org.sopt.exception.ErrorCode;
import org.sopt.repository.PostRepository;

import java.util.List;

public class PostService {
    private final PostRepository postRepository = new PostRepository();

    // CREATE
    public CreatePostResponse createPost(CreatePostRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다!");
        }
        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new IllegalArgumentException("내용은 필수입니다!");
        }
        String createdAt = java.time.LocalDateTime.now().toString();
        Post post = new Post(postRepository.generateId(), request.getTitle(), request.getContent(), request.getAuthor(), createdAt);
        postRepository.save(post);
        return new CreatePostResponse(post.getId(), "게시글 등록 완료!");
    }

    // READ - 전체 📝 과제
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(PostResponse::from)
                .toList();
    }

    // READ - 단건 📝 과제
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id);

        if (post == null)
            throw new CustomException(ErrorCode.POST_NOT_FOUND);

        return PostResponse.from(post);
    }

    // UPDATE 📝 과제
    public void updatePost(Long id, String newTitle, String newContent) {
        Post post =  postRepository.findById(id);

        if (post == null)
            throw new CustomException(ErrorCode.POST_NOT_FOUND);

        post.update(newTitle, newContent);
        postRepository.deleteById(id);  // 기존 post는 삭제 처리
        postRepository.save(post);
    }

    // DELETE 📝 과제
    public void deletePost(Long id) {
        if (!postRepository.deleteById(id))
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
    }
}

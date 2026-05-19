package org.sopt.domain.post.service;

import org.sopt.domain.like.repository.LikeRepository;
import org.sopt.domain.post.entity.Post;
import org.sopt.domain.post.dto.request.CreatePostRequest;
import org.sopt.domain.post.dto.request.DeletePostRequest;
import org.sopt.domain.post.dto.request.UpdatePostRequest;
import org.sopt.domain.post.dto.response.CreatePostResponse;
import org.sopt.domain.post.dto.response.PostListResponse;
import org.sopt.domain.post.dto.response.PostResponse;
import org.sopt.domain.post.exception.PostErrorCode;
import org.sopt.domain.user.entity.User;
import org.sopt.post.dto.request.*;
import org.sopt.post.dto.response.*;
import org.sopt.domain.post.entity.BoardType;
import org.sopt.global.exception.CustomException;
import org.sopt.domain.post.repository.PostRepository;
import org.sopt.domain.user.exception.UserErrorCode;
import org.sopt.domain.user.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
    }

    // 게시글 생성
    @Transactional
    public CreatePostResponse createPost(CreatePostRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        Post post = Post.create(request.title(), request.content(), request.boardType(), user);
        postRepository.save(post);
        return CreatePostResponse.from(post);
    }

    // 게시글 다건 조회
    @Transactional(readOnly = true)
    public List<PostListResponse> getAllPosts(String keyword, String strType, Pageable pageable) {
        BoardType type;

        try {
            // ALL인 경우 type null값
            type = "ALL".equals(strType) ? null : BoardType.valueOf(strType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(PostErrorCode.INVALID_BOARD_TYPE_VALUE);
        }

        String trimmedKeyword = (keyword == null || keyword.isBlank()) ? null : keyword.trim();

        return postRepository.findAllWithLikeCount(trimmedKeyword, type, pageable).getContent();
    }

    // 게시글 단건 조회
    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(PostErrorCode.POST_NOT_FOUND));

        return PostResponse.from(post);
    }

    // 게시글 수정
    @Transactional
    public PostResponse updatePost(Long id, UpdatePostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(PostErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(request.userId()))
            throw new CustomException(PostErrorCode.NOT_POST_OWNER);

        post.update(request.newTitle(), request.newContent());

        return PostResponse.from(post);
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long id, DeletePostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(PostErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(request.userId()))
            throw new CustomException(PostErrorCode.NOT_POST_OWNER);

        // 게시글 삭제 시 관련 좋아요도 삭제
        likeRepository.deleteAllByPostId(id);

        postRepository.delete(post);
    }
}

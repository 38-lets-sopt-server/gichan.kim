package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.request.*;
import org.sopt.dto.response.*;
import org.sopt.enums.BoardType;
import org.sopt.exception.CustomException;
import org.sopt.exception.ErrorCode;
import org.sopt.repository.PostRepository;
import org.sopt.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // CREATE
    @Transactional
    public CreatePostResponse createPost(CreatePostRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Post post = Post.createPost(request.title(), request.content(), request.boardType(), user);
        postRepository.save(post);
        return CreatePostResponse.from(post);
    }

    // READ - 전체 📝 과제
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(String strType, int page, int size) {
        BoardType type;

        try {
            // ALL인 경우 type null값
            type = "ALL".equals(strType) ? null : BoardType.valueOf(strType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_BOARD_TYPE_VALUE);
        }

        List<Post> filteredPosts = postRepository.findAll()
                .stream()
                .filter(post -> type == null || post.getBoardType() == type)
                .toList();

        int start = page * size;
        int end = Math.min((start + size), filteredPosts.size());

        if (start >= filteredPosts.size() || start < 0)
            return List.of();

        return filteredPosts.subList(start, end)
                .stream()
                .map(PostResponse::from)
                .toList();
    }

    // READ - 단건 📝 과제
    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        return PostResponse.from(post);
    }

    // UPDATE 📝 과제
    @Transactional
    public PostResponse  updatePost(Long id, UpdatePostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        post.update(request.newTitle(), request.newContent());

        return PostResponse.from(post);
    }

    // DELETE 📝 과제
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        postRepository.deleteById(id);
    }
}

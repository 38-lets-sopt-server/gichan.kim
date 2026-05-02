package org.sopt.like.service;

import org.sopt.common.exception.CustomException;
import org.sopt.like.doamin.Like;
import org.sopt.like.exception.LikeErrorCode;
import org.sopt.like.repository.LikeRepository;
import org.sopt.post.domain.Post;
import org.sopt.post.exception.PostErrorCode;
import org.sopt.post.repository.PostRepository;
import org.sopt.user.domain.User;
import org.sopt.user.exception.UserErrorCode;
import org.sopt.user.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeTransactionService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    public LikeTransactionService(PostRepository postRepository, LikeRepository likeRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void likeInternal(Long postId, Long userId) {
        if (likeRepository.existsByPostIdAndUserId(postId, userId))
            throw new CustomException(LikeErrorCode.ALREADY_LIKED);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(PostErrorCode.POST_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        try {
            likeRepository.save(Like.create(post, user));
            // likeCount 변경으로 dirty checking 발생 → @Version 기반 낙관적 락 검증 유발
            post.addLike();
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(LikeErrorCode.ALREADY_LIKED);
        }
    }

    @Transactional
    public void unlikeInternal(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(PostErrorCode.POST_NOT_FOUND));

        Like like = likeRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new CustomException(LikeErrorCode.LIKE_NOT_FOUND));

        likeRepository.delete(like);
        post.cancelLike();
    }
}

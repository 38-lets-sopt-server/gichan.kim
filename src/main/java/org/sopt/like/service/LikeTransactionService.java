package org.sopt.like.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
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
    private final EntityManager entityManager;

    public LikeTransactionService(PostRepository postRepository, LikeRepository likeRepository, UserRepository userRepository, EntityManager entityManager) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
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
            // Post의 version을 강제로 증가시켜 낙관적 락 검증 유발
            entityManager.lock(post, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
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
        entityManager.lock(post, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
    }
}

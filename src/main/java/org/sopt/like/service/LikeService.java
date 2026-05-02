package org.sopt.like.service;

import org.sopt.common.exception.CustomException;
import org.sopt.like.exception.LikeErrorCode;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    private final LikeTransactionService likeTransactionService;

    public LikeService(LikeTransactionService likeTransactionService) {
        this.likeTransactionService = likeTransactionService;
    }

    @Retryable(
            retryFor = { ObjectOptimisticLockingFailureException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 100, multiplier = 2)
    )
    public void addLike(Long postId, Long userId) {
        likeTransactionService.likeInternal(postId, userId);
    }

    @Retryable(
            retryFor = { ObjectOptimisticLockingFailureException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 100, multiplier = 2)
    )
    public void cancelLike(Long postId, Long userId) {
        likeTransactionService.unlikeInternal(postId, userId);
    }

    @Recover
    public void recoverLike(ObjectOptimisticLockingFailureException e, Long postId, Long userId) {
        throw new CustomException(LikeErrorCode.LIKE_CONFLICT_RETRY_FAILED);
    }

    @Recover
    public void recoverUnlike(ObjectOptimisticLockingFailureException e, Long postId, Long userId) {
        throw new CustomException(LikeErrorCode.LIKE_CONFLICT_RETRY_FAILED);
    }
}

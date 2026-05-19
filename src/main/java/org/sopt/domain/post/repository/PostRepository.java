package org.sopt.domain.post.repository;

import org.sopt.domain.post.entity.BoardType;
import org.sopt.domain.post.entity.Post;
import org.sopt.domain.post.dto.response.PostListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("""
        SELECT new org.sopt.post.dto.response.PostListResponse(
            p.id,
            u.nickname,
            p.title,
            p.content,
            p.boardType,
            p.likeCount,
            p.createdAt
        )
        FROM Post p
        JOIN p.user u
            WHERE (:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
                  AND (:boardType IS NULL OR p.boardType = :boardType)
        """)
    Page<PostListResponse> findAllWithLikeCount(
            @Param("keyword") String keyword,
            @Param("boardType") BoardType boardType,
            Pageable pageable
    );
}
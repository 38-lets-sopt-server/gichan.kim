package org.sopt.domain.like.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sopt.global.common.code.SuccessCode;
import org.sopt.global.common.response.SuccessResponse;
import org.sopt.domain.like.dto.request.AddLikeRequest;
import org.sopt.domain.like.dto.request.CancelLikeRequest;
import org.sopt.domain.like.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Like", description = "좋아요 관련 API")
@RestController
@RequestMapping("/api/v1/posts/{postId}/likes")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @Operation(
            summary = "게시글 좋아요 등록",
            description = "특정 게시글에 좋아요를 등록합니다. 동시 요청 시 낙관적 락으로 처리되며, 충돌 발생 시 자동 재시도됩니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "좋아요 등록 성공"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자 또는 게시글을 찾을 수 없음"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "이미 좋아요한 게시글이거나, 동시성 충돌 재시도 실패"
            )
    })
    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> addLike(
            @Parameter(description = "게시글 ID", example = "1", required = true)
            @PathVariable Long postId,
            @RequestBody @Valid AddLikeRequest request
    ) {
        likeService.addLike(postId, request.userId());

        return ResponseEntity
                .status(SuccessCode.LIKE_CREATED.getStatus())
                .body(SuccessResponse.of(SuccessCode.LIKE_CREATED));
    }

    @Operation(
            summary = "게시글 좋아요 취소",
            description = "특정 게시글에 등록한 좋아요를 취소합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "좋아요 취소 성공"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시글 또는 좋아요 내역을 찾을 수 없음"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "동시성 충돌 재시도 실패"
            )
    })
    @DeleteMapping
    public ResponseEntity<SuccessResponse<Void>> cancelLike(
            @Parameter(description = "게시글 ID", example = "1", required = true)
            @PathVariable Long postId,
            @RequestBody @Valid CancelLikeRequest request
    ) {
        likeService.cancelLike(postId, request.userId());

        return ResponseEntity
                .status(SuccessCode.LIKE_DELETED.getStatus())
                .body(SuccessResponse.of(SuccessCode.LIKE_DELETED));
    }
}

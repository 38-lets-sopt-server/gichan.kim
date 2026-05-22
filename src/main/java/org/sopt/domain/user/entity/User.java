package org.sopt.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.sopt.global.common.entity.BaseTimeEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
@Entity
@Table(name = "users")  // "user"는 SQL 예약어라 테이블명을 변경해요
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    @Column(unique = true)
    private String providerId;

    private boolean deleted = false;

    private User(String nickname, String email, String password, AuthProvider provider, String providerId) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
    }

    public static User createKakaoUser(String providerId, String email, String nickname) {
        return new User(
                nickname,
                email,
                null,
                AuthProvider.KAKAO,
                providerId
        );
    }

    public static User createLocalUser(String email, String encodedPassword, String nickname) {
        return new User(
                nickname,
                email,
                encodedPassword,
                AuthProvider.LOCAL,
                null
        );
    }
}
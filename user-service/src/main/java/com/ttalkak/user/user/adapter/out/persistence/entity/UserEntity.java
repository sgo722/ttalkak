package com.ttalkak.user.user.adapter.out.persistence.entity;

import jakarta.persistence.*;
import com.ttalkak.user.user.domain.UserRole;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "username_unique_constraint", columnNames = "username")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 300)
    private String password;

    @Column(name = "profile_image")
    private String profileImage;

    private String email;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "github_token")
    private String githubToken;

    @Column(name = "is_email_verified", nullable = false)
    @ColumnDefault("false")
    private boolean isEmailVerified = false;

    public UserEntity(String username, String password, String email, UserRole userRole) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.userRole = userRole;
    }

    public UserEntity(String username, String password, String email, UserRole userRole, String providerId, String profileImage, String githubToken) {
        this(username, password, email, userRole);
        this.providerId = providerId;
        this.githubToken = githubToken;
        this.profileImage = profileImage;
    }
}

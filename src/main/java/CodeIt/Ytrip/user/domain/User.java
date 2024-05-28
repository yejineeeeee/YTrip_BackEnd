package CodeIt.Ytrip.user.domain;

import CodeIt.Ytrip.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class User extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    private String nickname;
    private String email;
    private String password;
    private String refreshToken;

    public User() {}

    public void createUser(String nickname, String email, String password, String refreshToken) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

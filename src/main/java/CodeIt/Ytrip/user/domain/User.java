package CodeIt.Ytrip.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private int id;
    private String username;
    private String nickname;
    private String email;
    private String password;
    private String refreshToken;

    public User(String username, String nickname, String email, String password, String refreshToken) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.refreshToken = refreshToken;
    }
}

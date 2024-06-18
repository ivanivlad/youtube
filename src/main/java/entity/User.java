package entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "nickname", nullable = false)
    private String nickname;
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Video> videoList;

    public User(String nickname) {
        this.nickname = nickname;
    }
}
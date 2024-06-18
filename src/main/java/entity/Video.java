package entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "videos")
@NoArgsConstructor
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User owner;
    @OneToMany()
    @JoinColumn(name = "video_id")
    @ToString.Exclude
    private List<Comment> comments;

    public Video(String name, String description, User owner) {
        this.name = name;
        this.description = description;
        this.owner = owner;
    }
}

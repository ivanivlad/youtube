package entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "text", nullable = false)
    String commentText;
    @ManyToOne()
    @JoinColumn(name = "video_id")
    private Video video;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User owner;

    public Comment(String commentText, Video video, User owner) {
        this.commentText = commentText;
        this.video = video;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Comment: " + commentText + ", автор = " + owner.getNickname();
    }
}

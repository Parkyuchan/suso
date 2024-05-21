package ldpd.suso.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    private boolean correct;

    public Result(Member member, Quiz quiz, boolean correct) {
        this.member = member;
        this.quiz = quiz;
        this.correct = correct;
    }
}
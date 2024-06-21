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


    private int correct_count;
    private int wrong_count;

    public Result(Member member, Quiz quiz) {
        this.member = member;
        this.quiz = quiz;
        this.correct_count = 0;
        this.wrong_count = 0;
    }
    public void upCorrectCount() { this.correct_count++; }

    public void upWrongCount() { this.wrong_count++; }
}
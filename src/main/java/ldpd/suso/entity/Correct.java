package ldpd.suso.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class Correct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cor_id;

    private boolean cor;    //맞혔는지 여부

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    //private Integer quiz_id;

    @ManyToOne  //사용자 한명이 답 여러개 가능
    @JoinColumn(name = "member_id")
    private Member member;

    private String mem_ans;

    @CreationTimestamp
    private Timestamp createdAt;

    public Correct(Quiz quiz, Member member){
        this.quiz = quiz;
        this.member = member;
    }

}

package ldpd.suso.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Correct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String originalWords;

    private String translatedSentence;

    public Correct(String originalWords, String translatedSentence){
        this.originalWords = originalWords;
        this.translatedSentence = translatedSentence;
    }

}
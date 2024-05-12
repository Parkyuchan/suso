package ldpd.suso.service;

import ldpd.suso.entity.Quiz;
import ldpd.suso.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    //Service는 db와 직접적인 연관

    public void registerQuiz(Quiz quiz) throws Exception {  //퀴즈 db에 등록해준다.

        quizRepository.save(quiz);
    }

    public void quizDelete(Integer id) {       //퀴즈를 db에서 삭제하는 메소드로 sign에 CASCADE 제약조건으로 수어 테이블과 같이 삭제된다.

        quizRepository.deleteById(id);
    }

    public List<Quiz> quizList() {
        return quizRepository.findAll();
    }   //퀴즈 목록을 보기 위한 모든 퀴즈 글을 가져온다.

    public Quiz quizDetail(Integer id){
        return quizRepository.findById(id).get();
    }   //퀴즈 디테일에서 사용될 정보를 가져온다.
}
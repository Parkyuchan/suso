package ldpd.suso.service;

import ldpd.suso.entity.Member;
import ldpd.suso.entity.Quiz;
import ldpd.suso.entity.Result;
import ldpd.suso.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResultService {
    @Autowired
    private ResultRepository resultRepository;

    public void save(boolean isCorrect, Member member, Quiz quiz) {
        Optional<Result> result = resultRepository.findByQuiz(quiz);
        if (result.isPresent()) {
            if (result.get().getMember() == member) {
                if (isCorrect) {
                    result.get().upCorrectCount();
                }
                else{
                    result.get().upWrongCount();
                }
                resultRepository.save(result.get());
            }
        } else {
            Result newResult = new Result(member, quiz);
            if (isCorrect) {
                newResult.upCorrectCount();
            }
            else{
                newResult.upWrongCount();
            }
            resultRepository.save(newResult);
        }
    }
}

package ldpd.suso.repository;

import ldpd.suso.entity.Quiz;
import ldpd.suso.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, Integer> {
    List<Result> findByMemberUsername(String username);
    Optional<Result> findByQuiz(Quiz quiz);
}
package ldpd.suso.controller;

import ldpd.suso.entity.Quiz;
import ldpd.suso.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/quiz/list")   //퀴즈 글 리스트에 대한 Get 방식 프로토콜 메소드
    public String quizList(Model model) {

        List<Quiz> quiz = null;
        quiz = quizService.quizList();
        model.addAttribute("list", quiz);

        return "quiz/quiz_list";
    }

    @GetMapping("/quiz/{id}")   //퀴즈 상세 페이지에 대한 Post 방식 프로토콜 메소드
    public String quizDetail(@PathVariable("id") Integer id, Model model){

        model.addAttribute("quiz", quizService.quizDetail(id));
        return "quiz/quiz_detail";

    }
}
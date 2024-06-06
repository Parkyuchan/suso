package ldpd.suso.controller;

import ldpd.suso.repository.MemberRepository;
import ldpd.suso.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@RequiredArgsConstructor
@Controller
public class MainController {

    @Autowired
    private MainService mainService;

    @GetMapping("/home")    //메인 화면에 대한 Get 방식 프로토콜 메소드
    public String mainPage(Model model) {

        model.addAttribute("mainVideo", mainService.getRandomSign());
        return "main";
    }
}

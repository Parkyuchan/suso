package ldpd.suso.service;

import ldpd.suso.entity.Quiz;
import ldpd.suso.entity.Sign;
import ldpd.suso.repository.SignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    @Autowired
    private SignRepository signRepository;
    public List<Sign> getRandomSign() {
        List<Sign> allSigns = signRepository.findAll(); //모든 수어 속성을 가져옴

        List<Sign> mainSign = new ArrayList<>(allSigns);

        //랜덤하게 수어 가져옴
        Collections.shuffle(mainSign);

        //4개 선택
        List<Sign> randomChoices = mainSign.subList(0, 4);

        //선택지를 랜덤하게 섞음
        Collections.shuffle(randomChoices);

        return randomChoices;
    }
}

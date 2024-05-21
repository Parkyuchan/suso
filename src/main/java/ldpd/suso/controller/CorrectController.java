package ldpd.suso.controller;

import ldpd.suso.entity.Correct;
import ldpd.suso.service.CorrectService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class CorrectController {
    @Autowired
    private CorrectService correctService;

    @PostMapping("/sign/translate")
    public ResponseEntity<String> saveTranslatedSentence(@RequestParam String originalWord) {
        correctService.saveCorrect(originalWord);
        return ResponseEntity.ok("Translated sentence saved successfully");
    }

//    private final CorrectService correctService;
//
//    @GetMapping("/sign/show/{id}")
//    public String showTranslatedSentence(@PathVariable Integer id, Model model) {
//        Correct translatedSentence = correctService.findTranslationById(id);
//        model.addAttribute("translatedSentence", translatedSentence);
//        return "sign/sign_change";
//    }
//
//    @PostMapping("sign/translate/change")
//    public ResponseEntity<String> saveTranslatedSentence(@RequestParam String originalWords) {
//        String translatedSentence = correctService.translateWord(originalWords);
//        Correct savedTranslatedSentence = correctService.saveTranslation(originalWords, translatedSentence);
//        return ResponseEntity.ok("Translated sentence saved with id: " + savedTranslatedSentence.getId());
//    }
}

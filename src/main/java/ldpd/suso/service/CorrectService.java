package ldpd.suso.service;

import ldpd.suso.entity.Correct;
import ldpd.suso.repository.CorrectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CorrectService {
    @Autowired
    private CorrectRepository correctRepository;

    @Value("${chatGPT.apiKey}")
    private String chatGPTApiKey;

    @Value("${chatGPT.apiUrl}")
    private String chatGPTApiUrl;

    public CorrectService() {
    }

    public void saveCorrect(String originalWord) {
        String translatedSentence = translateWord(originalWord);
        Correct correct = new Correct(originalWord, translatedSentence);
        correctRepository.save(correct);
    }

    private String translateWord(String originalWord) {
        String requestBody = "{\"model\": \"text-davinci-003\", \"prompt\": \"" + originalWord + "\", \"max_tokens\": 50}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(chatGPTApiKey);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(chatGPTApiUrl, HttpMethod.POST, requestEntity, String.class);

        return response.getBody();
    }

//    @Autowired
//    private CorrectRepository correctRepository;
//
//    @Value("${chatGPT.apiKey}")
//    private String chatGPTApiKey;
//
//    @Value("${chatGPT.apiUrl}")
//    private String chatGPTApiUrl;
//
//    public String translateWord(String word) {
//        String requestBody = "{\"model\": \"text-davinci-003\", \"prompt\": \"" + word + "\", \"max_tokens\": 50}";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(chatGPTApiKey);
//
//        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.exchange(chatGPTApiUrl, HttpMethod.POST, requestEntity, String.class);
//
//        return response.getBody();
//    }
//
//    public Correct saveTranslation(String originalWord, String translatedWord) {
//        Correct newTranslation = new Correct(originalWord, translatedWord);
//        return correctRepository.save(newTranslation);
//    }
//
//    public Correct findTranslationById(Integer id) {
//        return correctRepository.findById(id).orElse(null);
//    }
}

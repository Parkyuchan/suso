package ldpd.suso.controller;

<<<<<<< HEAD
import ldpd.suso.entity.Quiz;
import ldpd.suso.entity.Sign;
import ldpd.suso.service.QuizService;
import ldpd.suso.service.SignService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignControllerTest {

    @Mock
    private SignService signService;

    @Mock
    private QuizService quizService;

    @Test
    void registerSign_fileTooLarge_throwException() {
        // Arrange
        SignController controller = new SignController(signService, quizService);
        Sign sign = new Sign();
        Model model = mock(Model.class);
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password");
        MultipartFile file = new MockMultipartFile("file", "test.mp4", "video/mp4", new byte[150 * 1024 * 1024]);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                controller.registerSign(sign, model, file, authentication)
        );
        assertEquals("업로드된 영상의 길이가 너무 깁니다.", exception.getMessage());

    }

    @Test
    void registerSign_emptyFields_returnErrorMessage() throws Exception {
        // Arrange
        SignController controller = new SignController(signService, quizService);
        Sign sign = new Sign();
        Model model = mock(Model.class);
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password");
        MultipartFile file = new MockMultipartFile("file", "test.mp4", "video/mp4", new byte[10 * 1024 * 1024]);

        assertThrows(NullPointerException.class, () -> {
            controller.registerSign(sign, model, file, authentication);
        });

    }

    @Test
    void registerSign_validFile_registerSignAndQuiz() throws Exception {
        // Arrange
        SignController controller = new SignController(signService, quizService);
        Sign sign = new Sign();
        sign.setId(1);
        sign.setTitle("수어 제목입니다.");
        sign.setSign_desc("수어 설명입니다.");
        Model model = mock(Model.class);
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password");
        MultipartFile file = new MockMultipartFile("file", "test.mp4", "video/mp4", new byte[10 * 1024 * 1024]);

        // Act
        String result = controller.registerSign(sign, model, file, authentication); //결과 result로 반환

        // Assert
        assertEquals("message", result);    //result가 예상한 값인 message와 같은지 검증
        verify(signService).registerSign(eq(sign), eq(file));   //regisgerSign 메소드가 sign, file 객체를 인자로 받았는지 확인
        verify(quizService).registerQuiz(any(Quiz.class));  //regisgerQuiz 메소드가 quiz 객체를 인자로 받았는지 확인
        verify(model).addAttribute(eq("message"), eq("수어 사전 등록이 완료됐습니다."));
        verify(model).addAttribute(eq("searchUrl"), eq("/sign/list"));

    }

    @Test
    void signDelete_validInput_returnMessage() {
        // Arrange
        SignController controller = new SignController(signService, quizService);
        int id = 1;
        Model model = mock(Model.class);

        // Act
        String result = controller.signDelete(id, model);

        // Assert
        assertEquals("message", result);
        verify(signService).signDelete(id); // signService.signDelete(id) 메서드가 호출되었는지 검증
        verify(model).addAttribute(eq("message"), eq("수어 삭제가 완료됐습니다."));
        verify(model).addAttribute(eq("searchUrl"), eq("/sign/list"));
    }

    @Test
    void signUpdate_validInput_returnMessage() throws Exception {
        // Arrange
        SignController controller = new SignController(signService, quizService);
        int id = 1;
        Sign sign = new Sign(id, "변경전 수어명", "변경전 수어 설명", "변경전 파일명", "변경전 파일 경로");

        Sign signTemp = new Sign(id, "새로운 수어명", "새로운 수어 설명", "새로운 파일명", "새로운 파일 경로");
        sign = signTemp;
        Model model = mock(Model.class);
        MultipartFile file = new MockMultipartFile("file", "test.mp4", "video/mp4", new byte[10 * 1024 * 1024]);

        // Mock signService.signDetail(id) 호출 시 반환되는 Sign 객체 설정
        when(signService.signDetail(id)).thenReturn(sign);

        // Act
        String result = controller.signUpdate(id, signTemp, model, file);

        // Assert
        assertEquals("message", result);
        verify(signService).signDetail(id); // signService.signDetail(id) 메서드가 호출되었는지 검증
        verify(signService).deleteFile(sign.getFilepath()); // signService.deleteFile(sign.getFilepath()) 메서드가 호출되었는지 검증
        verify(signService).registerSign(eq(sign), eq(file)); // signService.registerSign(sign, file) 메서드가 호출되었는지 검증
        verify(model).addAttribute(eq("message"), eq("수어 사전 수정이 완료됐습니다."));
        verify(model).addAttribute(eq("searchUrl"), eq("/sign/list"));

    }
=======
import ldpd.suso.entity.Sign;
import ldpd.suso.service.SignService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)    //JUnit에 내장된 실행자 외에 다른 실행자를 실행, 스프링 부터 테스트와 JUnit 사이에 연결자 역할
@WebMvcTest(controllers = SignController.class) //Web에만 집중할 수 있는 어노테이션으로 Controller만 사용 가능
public class SignControllerTest {

    @Autowired
    private SignService signService;
    @InjectMocks
    private SignController signController;

    @Test
    public void testregisterSign() throws Exception {
        // 모의 객체 생성
        Model model = Mockito.mock(Model.class);
        MultipartFile file = Mockito.mock(MultipartFile.class);
        Sign sign = new Sign(); // 필요하다면 sign 역시 목 객체로 만들 수 있음

        // 테스트 수행
        when(file.getSize()).thenReturn((long) (50 * 1024 * 1024)); // 파일 사이즈 설정
        signController.registerSign(sign, model, file);

        // 모델에 메시지가 올바르게 추가되었는지 검증
        verify(model).addAttribute("message", "수어 사전 등록이 완료됐습니다.");
    }

>>>>>>> 25d43dd (05.13 깃 풀을 위한 커밋)
}
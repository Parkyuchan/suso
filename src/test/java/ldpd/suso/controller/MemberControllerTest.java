package ldpd.suso.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import ldpd.suso.entity.Member;
import ldpd.suso.repository.MemberRepository;
import ldpd.suso.security.MemberCreateForm;
import ldpd.suso.security.MemberSecurityService;
import ldpd.suso.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberSecurityService memberSecurityService;

    @Test
    void signup_SuccessfulRegistration_ReturnMainPage() throws Exception {
        //MemberController 객체 생성(서비스, 리포지토리, 시큐리티 서비스 생성자 매개변수로 사용)
        MemberController controller = new MemberController(memberService, memberRepository, memberSecurityService);
        // Arrange(조건 설정)
        // 멤버 생성할 때 필요한 값들 지정하고 생성 폼 만들기
        MemberCreateForm memberCreateForm = new MemberCreateForm("username", "password", "password",
                "name", "email@example.com");
        //폼 데이터의 바인딩 및 검증 결과를 수집하기 위한 객체 생성
        BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");

        // Act(메서드 실행)
        //컨트롤러의 signup 메서드 실행
        String result = controller.signup(memberCreateForm, bindingResult);

        // Assert(결과 확인)
        // result(signup)가 실행되고 main 페이지가 호출됐는지 확인
        assertEquals("main", result);

        // 아이디, 이름, 이메일이 존재하는지 확인하는 서비스 메서드가 1번씩 호출됐는지 확인
        verify(memberService, times(1)).userFindUsername(eq("username"));
        verify(memberService, times(1)).userFindName(eq("name"));
        verify(memberService, times(1)).userFindEmail(eq("email@example.com"));
        // 회원 가입을 처리하는 서비스 메서드가 1번 호출되었는지를 확인
        verify(memberService, times(1)).signup(eq(memberCreateForm));
    }

    @Test
    void updateUsername_Successful() throws Exception {
        // Arrange
        MemberController controller = new MemberController(memberService, memberRepository, memberSecurityService);
        String newUsername = "newUsername";
        String currentUsername = "oldUsername";
        Member existingMember = null; // 새로운 이름을 가지고 있는 객체를 확인하기 위해 생성
        //findByusername을 호출할 때 반환값이 existingMember이도록 설정
        when(memberRepository.findByusername(newUsername)).thenReturn(existingMember);

        //현재 사용자의 정보를 담기 위한 mock 인스턴스 생성
        UserDetails userDetails = mock(UserDetails.class);
        //getUsername 호출되면 currentUsername이 반환되도록 설정
        when(userDetails.getUsername()).thenReturn(currentUsername);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        Model model = mock(Model.class);
        // Act
        String result = controller.updateUsername(newUsername, authentication, model);

        // Assert
        verify(memberRepository, times(1)).findByusername(newUsername);
        verify(memberService, times(1)).updateUsername(currentUsername, newUsername);
        verify(authentication, times(1)).getPrincipal();
        verify(model, times(1)).addAttribute("message", "아이디 변경에 성공했습니다.");
        verify(model, times(1)).addAttribute("searchUrl", "/user/mypage");
        assertEquals("message", result);
    }

    @Test
    void updateEmail_Successful() throws Exception {
        // Arrange
        MemberController controller = new MemberController(memberService, memberRepository, memberSecurityService);
        String newEmail = "newEmail";
        String currentEmail = "oldEmail";
        Member existingMember = null; // Assuming no existing member with the new username
        when(memberRepository.findByEmail(newEmail)).thenReturn(existingMember);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(currentEmail);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        Model model = mock(Model.class);

        // Act
        String result = controller.updateEmail(newEmail, authentication, model);

        // Assert
        verify(memberRepository, times(1)).findByEmail(newEmail);
        verify(memberService, times(1)).updateEmail(currentEmail, newEmail);
        verify(authentication, times(1)).getPrincipal();
        verify(model, times(1)).addAttribute("message", "이메일 변경에 성공했습니다.");
        verify(model, times(1)).addAttribute("searchUrl", "/user/mypage");
        assertEquals("message", result);

    }

    @Test
    void updateName_Successful() throws Exception {
        // Arrange
        MemberController controller = new MemberController(memberService, memberRepository, memberSecurityService);
        String newName = "newName";
        String currentName = "oldName";
        Member existingMember = null; // Assuming no existing member with the new username
        when(memberRepository.findByname(newName)).thenReturn(existingMember);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(currentName);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        Model model = mock(Model.class);

        // Act
        String result = controller.updateName(newName, authentication, model);

        // Assert
        verify(memberRepository, times(1)).findByname(newName);
        verify(memberService, times(1)).updateName(currentName, newName);
        verify(authentication, times(1)).getPrincipal();
        verify(model, times(1)).addAttribute("message", "이름 변경에 성공했습니다.");
        verify(model, times(1)).addAttribute("searchUrl", "/user/mypage");
        assertEquals("message", result);

    }

    @Test
    void updatePassword_Successful() {
        // Arrange
        MemberController controller = new MemberController(memberService, memberRepository, memberSecurityService);
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String username = "currentUsername";
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);
        when(memberService.updatePassword(username, currentPassword, newPassword)).thenReturn(1);

        // Act
        String result = controller.updatePassword(currentPassword, newPassword, newPassword, authentication, mock(Model.class));

        // Assert
        assertEquals("message", result);
        verify(memberService, times(1)).updatePassword(username, currentPassword, newPassword);
    }

    @Test
    void userDelete_Successful() {
        // Arrange
        MemberController controller = new MemberController(memberService, memberRepository, memberSecurityService);
        String username = "currentUsername";
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        Model model = mock(Model.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);
        when(request.getSession()).thenReturn(session);

        // Act
        String result = controller.userDelete(request, model, authentication);

        // Assert
        assertEquals("message", result);
        verify(memberService, times(1)).userDelete(username);
        verify(request.getSession(), times(1)).invalidate();
        verify(model, times(1)).addAttribute(eq("message"), eq("회원 정보가 삭제됐습니다."));
        verify(model, times(1)).addAttribute(eq("searchUrl"), eq("/home"));
    }

}
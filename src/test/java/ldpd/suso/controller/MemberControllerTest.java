package ldpd.suso.controller;

import ldpd.suso.entity.Member;
import ldpd.suso.repository.MemberRepository;
import ldpd.suso.security.MemberCreateForm;
import ldpd.suso.security.MemberSecurityService;
import ldpd.suso.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberSecurityService memberSecurityService;

    @InjectMocks
    private MemberController memberController;

    @Test
    public void testSignup_WithValidForm() {
        MemberCreateForm memberCreateForm = new MemberCreateForm();
        memberCreateForm.setPassword1("password"); // password1 초기화
        memberCreateForm.setPassword2("password"); // password2 초기화
        BindingResult bindingResult = mock(BindingResult.class);

        // 테스트용 회원 가입 폼 설정

        // 테스트 수행
        String viewName = memberController.signup(memberCreateForm, bindingResult);

        // 결과 검증
        // 폼이 유효하면 main 뷰로 리다이렉트되는지 확인
        assertEquals("main", viewName);
    }

    @Test
    public void testSignup_WithInvalidForm() {
        MemberCreateForm memberCreateForm = new MemberCreateForm();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // 테스트 수행
        String viewName = memberController.signup(memberCreateForm, bindingResult);

        // 결과 검증
        // 폼이 유효하지 않으면 회원 가입 폼 뷰로 리턴되는지 확인
        assertEquals("member/signup_form", viewName);
    }

    @Test
    public void testUpdateEmail_Success() {

        // Given
        String newEmail = "newemail@example.com";
        Authentication authentication = mock(Authentication.class);
        Model model = mock(Model.class);
        UserDetails userDetails = mock(UserDetails.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("currentUser");

        // When
        String result = memberController.updateEmail(newEmail, authentication, model);

        // Then
        assertEquals("message", result);
        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(memberService, times(1)).updateEmail(anyString(), anyString());
        verify(model, times(1)).addAttribute("message", "이메일 변경에 성공했습니다.");
        verify(model, times(1)).addAttribute("searchUrl", "/user/mypage");
    }

    @Test
    public void testUpdateEmail_DuplicateEmail() {
        // Given
        String newEmail = "duplicate@example.com";
        Authentication authentication = mock(Authentication.class);
        Model model = mock(Model.class);
        UserDetails userDetails = mock(UserDetails.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("currentUser");
        Member existingMember = new Member();
        existingMember.setEmail("duplicate@example.com");
        when(memberRepository.findByEmail(newEmail)).thenReturn(existingMember);

        // When
        String result = memberController.updateEmail(newEmail, authentication, model);

        // Then
        assertEquals("message", result);
        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(memberService, never()).updateEmail(anyString(), anyString());
        if(existingMember != null) {
            verify(model, times(1)).addAttribute("message", "이미 사용중인 이메일입니다.");
            verify(model, times(1)).addAttribute("searchUrl", "/user/mypage");
        }
        else{
            verify(model, times(1)).addAttribute("message", "이메일 변경에 실패했습니다.");
            verify(model, times(1)).addAttribute("searchUrl", "/user/mypage");
        }

    }

    // 나머지 메서드에 대한 유사한 방식으로 테스트 코드를 작성할 수 있습니다.
}

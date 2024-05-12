package ldpd.suso.service;

import ldpd.suso.entity.Member;
import ldpd.suso.repository.MemberRepository;
import ldpd.suso.security.MemberCreateForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Mock 주입을 초기화합니다.
    }

    @Test
    public void testSignup() {
        // 테스트할 MemberCreateForm 생성
        MemberCreateForm memberCreateForm = new MemberCreateForm();
        memberCreateForm.setUsername("testuser");
        memberCreateForm.setPassword1("testpassword");
        memberCreateForm.setPassword2("testpassword");
        memberCreateForm.setName("Test User");
        memberCreateForm.setEmail("test@example.com");

        // save() 메서드가 호출될 때 memberRepository가 반환할 값 설정
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

        // 회원가입 메서드 호출
        memberService.signup(memberCreateForm);

        // memberRepository.save() 메서드가 1회 호출되는지 검증
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void updateUsername_Successful() {
        // Given
        String currentUsername = "oldUsername";
        String newUsername = "newUsername";
        Member member = new Member();
        member.setUsername(currentUsername);
        when(memberRepository.findByusername(currentUsername)).thenReturn(member);

        // When
        memberService.updateUsername(currentUsername, newUsername);

        // Then
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void updateName_Successful() {
        // Given
        String currentUsername = "oldUsername";
        String newName = "newName";
        Member member = new Member();
        member.setUsername(currentUsername);
        when(memberRepository.findByusername(currentUsername)).thenReturn(member);

        // When
        memberService.updateName(currentUsername, newName);

        // Then
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void updateEmail_Successful() {
        // Given
        String currentUsername = "oldUsername";
        String newEmail = "newEmail";
        Member member = new Member();
        member.setUsername(currentUsername);
        when(memberRepository.findByusername(currentUsername)).thenReturn(member);

        // When
        memberService.updateEmail(currentUsername, newEmail);

        // Then
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void updatePassword_Successful() {
        // Given
        String currentUsername = "oldUsername";
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        Member member = new Member();

        // When
        member.setPassword(passwordEncoder.encode(currentPassword)); // 현재 비밀번호를 암호화하여 저장
        when(memberRepository.findByusername(currentUsername)).thenReturn(member);
        when(passwordEncoder.matches(currentPassword, member.getPassword())).thenReturn(true); // 현재 비밀번호 일치하는 것으로 설정

        // When
        int result = memberService.updatePassword(currentUsername, currentPassword, newPassword);

        // Then
        assertEquals(1, result); // 성공적으로 비밀번호가 변경되었으므로 반환값은 1이어야 합니다.
        verify(memberRepository, times(1)).save(member); // memberRepository.save() 메서드가 호출되었는지 확인합니다.
    }

    @Test
    void userDelete_UserExists() {
        // Given
        String username = "existingUser";
        Member member = new Member();
        member.setMember_id(1); // 임의의 회원 ID 설정
        when(memberRepository.findByusername(username)).thenReturn(member);

        // When
        memberService.userDelete(username);

        // Then
        verify(memberRepository, times(1)).deleteById(member.getMember_id()); // memberRepository.deleteById() 메서드가 호출되었는지 확인합니다.
    }

}

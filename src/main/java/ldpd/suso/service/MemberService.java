package ldpd.suso.service;


import ldpd.suso.entity.Member;
import ldpd.suso.repository.MemberRepository;
import ldpd.suso.security.MemberCreateForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
@Slf4j
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder; // PasswordEncoder 필드 추가

    //Service는 db와 연관

    @Autowired
    public MemberService(PasswordEncoder passwordEncoder) { // 생성자를 통해 PasswordEncoder 주입
        this.passwordEncoder = passwordEncoder;
    }

    public Member signup(MemberCreateForm memberCreateForm) { //회원가입 시 db에 저장, 비밀번호 암호화 됨
        Member user = new Member();
        user.setUsername(memberCreateForm.getUsername());
        user.setName(memberCreateForm.getName());
        user.setEmail(memberCreateForm.getEmail());
        user.setPassword(passwordEncoder.encode(memberCreateForm.getPassword1()));
        this.memberRepository.save(user);
        return user;
    }

    public void updateUsername(String currentUsername, String newUsername) {    //아이디 수정
        Member member = memberRepository.findByusername(currentUsername);
        if (member != null) {
            member.setUsername(newUsername);
            memberRepository.save(member);
        }
    }

    public void updateEmail(String username, String newEmail) { //이메일 수정
        Member member = memberRepository.findByusername(username);
        if (member != null) {
            member.setEmail(newEmail);
            memberRepository.save(member);
        }
    }

    public void updateName(String username, String newName) {   //이름 수정
        Member member = memberRepository.findByusername(username);
        if (member != null) {
            member.setName(newName);
            memberRepository.save(member);
        }
    }

    public int updatePassword(String username, String currentPassword, String newPassword) {    //비밀번호 수정
        Member member = memberRepository.findByusername(username);
        if (member != null) {
            // 비밀번호 변경 로직 추가
            // 예를 들어, 암호화된 비밀번호 비교 후 새로운 비밀번호 설정
            if (passwordEncoder.matches(currentPassword, member.getPassword())) {
                member.setPassword(passwordEncoder.encode(newPassword));
                memberRepository.save(member);
                return 1;
            }

        }
        return -1;
    }


    public void userDelete(String username) {   //
        memberRepository.deleteById(memberRepository.findByusername(username).getMember_id());
    }

    public Member userFindUsername(String username){
        return memberRepository.findById((memberRepository.findByusername(username)).getMember_id()).get();
    }


    public Member userFindName(String name){
        return memberRepository.findById((memberRepository.findByname(name)).getMember_id()).get();
    }

    public Member userFindEmail(String email){
        return memberRepository.findById((memberRepository.findByEmail(email)).getMember_id()).get();
    }
}
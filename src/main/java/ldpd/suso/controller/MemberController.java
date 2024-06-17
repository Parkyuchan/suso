package ldpd.suso.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import ldpd.suso.entity.Member;
import ldpd.suso.repository.MemberRepository;
import ldpd.suso.security.MemberCreateForm;
import ldpd.suso.security.MemberSecurityService;
import ldpd.suso.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final MemberSecurityService memberSecurityService;

    @GetMapping("/signup")  //회원가입에 대한 Get 방식 프로토콜 메소드
    public String signup(MemberCreateForm memberCreateForm) {
        return "member/signup_form";
    }

    @PostMapping("/signup") //회원가입에 대한 Post 방식 프로토콜 메소드
    public String signup(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "member/signup_form";
        }

        if (!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "member/signup_form";
        }

        try {
            // 회원 가입 전 중복 검사
            Member existingMember = memberRepository.findByusername(memberCreateForm.getUsername());
            if (existingMember != null && existingMember.getUsername() != null) {
                bindingResult.reject("signupFailed", "이미 등록된 사용자 아이디입니다.");
                return "member/signup_form";
            }

            existingMember = memberRepository.findByname(memberCreateForm.getName());
            if (existingMember != null && existingMember.getName() != null) {
                bindingResult.reject("signupFailed", "이미 등록된 사용자 이름입니다.");
                return "member/signup_form";
            }

            // 중복이 없을 경우 회원 가입 진행
            memberService.signup(memberCreateForm);
        } catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "회원 가입에 실패하였습니다.");
            return "member/signup_form";
        }
        return "redirect:/home";
    }

    @GetMapping("/login")   //로그인에 대한 Get 방식 프로토콜 메소드, post 방식 로그인은 SecurityConfig에서 대신 수행한다.
    public String login() {
        return "member/login_form";
    }

    @GetMapping("/user/mypage") //마이페이지(정보 확인)에 대한 Get 방식 프로토콜 메소드
    public String mypage(HttpSession session, Model model) {

        //현재 인증된 사용자의 인증 객체를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 현재 사용자의 정보를 가져옴
        Object principal = authentication.getPrincipal();

        // principal이 UserDetails를 구현한 경우, 사용자의 정보에 접근할 수 있음
        String username = null;
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            // 사용자의 정보에 접근하여 필요한 값 가져오기
            username = userDetails.getUsername();
            // 사용자의 다른 정보에 접근 가능
        }

        if (memberService != null) {
            Member optionalMember = memberRepository.findByusername(username);
            //Member optionalMember = memberService.userFindUsername(username);
            if (optionalMember != null && optionalMember.getUsername()!=null) {
                Member member = optionalMember;
                model.addAttribute("member", member);
            } else {
                model.addAttribute("error", "사용자를 찾을 수 없습니다.");
            }
        } else {
            model.addAttribute("error", "데이터베이스에 접근할 수 없습니다.");
        }

        model.addAttribute("searchUrl", "/home");

        return "member/my_page";
    }

    @GetMapping("/user/update/username")    //아이디 변경에 대한 Get 방식 프로토콜 메소드
    public String updateUsername(Model model) {

        return "member/update_username";
    }

    @PostMapping("/user/update/username")   //아이디 변경에 대한 Post 방식 프로토콜 메소드
    public String updateUsername(@RequestParam("newUsername") String newUsername, Authentication authentication, Model model) {

        try {
            //아아디 수정 전 중복 검사
            Member existingMember = memberRepository.findByusername(newUsername);
            if (existingMember != null && existingMember.getUsername() != null) {
                model.addAttribute("message", "이미 사용중인 아이디입니다.");
                model.addAttribute("searchUrl", "/user/mypage");
                return "message";
            }

            // 중복이 없을 경우 회원 가입 진행
            // 현재 인증된 사용자의 정보 가져오기
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 현재 사용자의 아이디 가져오기
            String currentUsername = userDetails.getUsername();

            // 아이디 변경 로직
            memberService.updateUsername(currentUsername, newUsername);

            // 세션 정보 업데이트
            userDetails = memberSecurityService.loadUserByUsername(newUsername);
            UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuthentication);

        } catch(Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "아이디 변경에 실패했습니다.");
        }

        model.addAttribute("message", "아이디 변경에 성공했습니다.");
        model.addAttribute("searchUrl", "/user/mypage");
        return "message";
    }

    @GetMapping("/user/update/email")   //이메일 변경에 대한 Get 방식 프로토콜 메소드
    public String updateEmail(Model model) {

        return "member/update_email";
    }

    @PostMapping("/user/update/email")  //이메일 변경에 대한 Post 방식 프로토콜 메소드
    public String updateEmail(@RequestParam("newEmail") String newEmail, Authentication authentication, Model model) {

        try {
            // 이메일 수정 전 중복 검사
            Member existingMember = memberRepository.findByEmail(newEmail);
            if (existingMember != null && existingMember.getEmail().equals(newEmail)) {
                model.addAttribute("message", "이미 사용중인 이메일입니다.");
                model.addAttribute("searchUrl", "/user/mypage");
                return "message";
            }

            // 현재 인증된 사용자의 정보 가져오기
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 현재 사용자의 아이디 가져오기
            String currentUsername = userDetails.getUsername();

            // 이메일 변경 로직
            memberService.updateEmail(currentUsername, newEmail);

        } catch(Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "이메일 변경에 실패했습니다.");
        }

        model.addAttribute("message", "이메일 변경에 성공했습니다.");
        model.addAttribute("searchUrl", "/user/mypage");
        return "message";
    }

    @GetMapping("/user/update/name")    //이름 변경에 대한 Get 방식 프로토콜 메소드
    public String updateName(Model model) {

        return "member/update_name";
    }

    @PostMapping("/user/update/name")   //이름 변경에 대한 Post 방식 프로토콜 메소드
    public String updateName(@RequestParam("newName") String newName, Authentication authentication, Model model) {

        try {
            // 이름 수정 전 중복 검사
            Member existingMember = memberRepository.findByname(newName);
            if (existingMember != null && existingMember.getName() != null) {
                model.addAttribute("message", "이미 사용중인 이름입니다.");
                model.addAttribute("searchUrl", "/user/mypage");
                return "message";
            }

            // 현재 인증된 사용자의 정보 가져오기
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 현재 사용자의 아이디 가져오기
            String currentUsername = userDetails.getUsername();

            // 이메일 변경 로직
            memberService.updateName(currentUsername, newName);

        } catch(Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "이름 변경에 실패하였습니다.");
        }

        model.addAttribute("message", "이름 변경에 성공했습니다.");
        model.addAttribute("searchUrl", "/user/mypage");
        return "message";
    }

    @GetMapping("/user/update/password")    //비밀변호 변경에 대한 Get 방식 프로토콜 메소드
    public String updatePassword(Model model) {

        return "member/update_password";
    }

    @PostMapping("/user/update/password")   //비밀번호 변경에 대한 Post 방식 프로토콜 메소드
    public String updatePassword(@RequestParam("currentPassword") @Valid String currentPassword,
                                 @RequestParam("newPassword1") @Valid String newPassword1,
                                 @RequestParam("newPassword2") @Valid String newPassword2,
                                 Authentication authentication, Model model) {
        // 현재 인증된 사용자의 정보 가져오기
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // 현재 사용자의 아이디 가져오기
        String currentUsername = userDetails.getUsername();

        if (!newPassword1.equals(newPassword2)) {
            model.addAttribute("message", "새로운 비밀번호가 일치하지 않습니다.");
            model.addAttribute("searchUrl", "/user/mypage");
            return "message";
        }
        // 비밀번호 변경 로직
        int result = memberService.updatePassword(currentUsername, currentPassword, newPassword1);
        if(result==-1)
            model.addAttribute("message", "현재 비밀번호가 틀렸습니다.");
        else
            model.addAttribute("message", "비밀번호 변경이 완료됐습니다.");

        // 세션 정보 업데이트 (비밀번호 변경 시 세션 갱신은 필요하지 않을 수도 있음)
        model.addAttribute("searchUrl", "/user/mypage");
        return "message";
    }

    @GetMapping("/user/delete") //회원 정보 삭제에 대한 Get 방식 프로토콜 메소드
    public String userDelete(HttpServletRequest request, Model model, Authentication authentication){

        // 현재 인증된 사용자의 정보 가져오기
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // 현재 사용자의 아이디 가져오기
        String currentUsername = userDetails.getUsername();

        memberService.userDelete(currentUsername);

        // 세션 무효화
        request.getSession().invalidate();

        model.addAttribute("message", "회원 정보가 삭제됐습니다.");
        model.addAttribute("searchUrl", "/home");

        return "message";
    }
}

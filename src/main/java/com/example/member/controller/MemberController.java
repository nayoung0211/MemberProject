package com.example.member.controller;

import com.example.member.dto.MemberDTO;
import com.example.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class MemberController {
    //생성자 주입
    private final MemberService memberService;

    //회원가입 페이지 출력 요청
    @GetMapping("/member/save")
    public String saveForm() {
        return "save";
    }

    @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO) {
        System.out.println("MemberController.save");
        System.out.println(memberDTO);
        memberService.save(memberDTO);
        return "login";
    }
    @GetMapping("/member/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if(loginResult!=null){
            //로그인 성공
            session.setAttribute("loginEmail",loginResult.getMemberEmail());
            return "main";
        }else{
            //login 실패
            return "login";
        }
    }
    @GetMapping("/member/")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList= memberService.findAll();
        //어떠한 html로 가져갈 데이터가 있다면 model사용
        model.addAttribute("memberList",memberDTOList);
        return "list";
    }


    @GetMapping("/member/{id}")
    public String findById(@PathVariable Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "detail";
    }

    @GetMapping("/member/update")
    public String updateForm(HttpSession session,Model model) {
        String myEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.updateForm(myEmail);
        model.addAttribute("updateMember",memberDTO);
        return  "update";
    }

    @PostMapping("/member/update")
    public String update(@ModelAttribute MemberDTO memberDTO) {
       memberService.update(memberDTO);
       return "redirect:/member/"+memberDTO.getId();
    }
    @GetMapping("member/delete/{id}")
    public String delete(@PathVariable Long id) {
        memberService.delete(id);
        return "redirect:/member/";
    }
    @GetMapping("member/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }
    @PostMapping("member/email-check")
    public @ResponseBody String emailCheck(@RequestParam("memberEmail") String memberEmail) {
        System.out.println("memberEmail:"+memberEmail);
        String checkResult = memberService.emailCheck(memberEmail);
        if(checkResult != null){
            return "ok";
        }else{
            return "no";
        }
    }
}

package com.campus.myapp.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.campus.myapp.service.MemberService;
import com.campus.myapp.vo.MemberVO;
// 회원가입, 회원정보수정, 로그인, 로그아웃

@Controller
@RequestMapping("member/")
public class MemberController {
	@Inject
	MemberService service;
	
	// 회원가입폼으로 이동하는 매핑
	@GetMapping("memberForm")
	public String memberForm() {
		return "member/memberForm";  // member폴더에 memberForm.jsp 파일을 뷰로 사용한다.
	}
	
	// 회원등록
	// @RequestMapping(value="/member/memberOk", method=RequestMethod.POST)
	@PostMapping("memberOk")
	public String memberFormOk(MemberVO vo, Model model) {
		// 회원등록
		int cnt = service.memberInsert(vo);
		
		// 클라이언트 페이지로 insert 결과를 보낸다.
		model.addAttribute("cnt", cnt);
		
		return "member/memberResult";
	}
	
	// 로그인폼으로 이동
	@GetMapping("/loginForm")
	public String loginForm() {
		return "member/loginForm";
	}
	
	// 로그인
	@PostMapping("loginOk")
	public ModelAndView loginOk(MemberVO vo, HttpSession session) {
		// request userid, userpwd와 일치하는 레코드의 userid, username을 선택한다.
		MemberVO vo2 = service.loginCheck(vo);
		
		ModelAndView mav = new ModelAndView();
		
		if(vo2 != null){// 로그인성공 : session에 아이디와 이름을 저장한다. 홈으로 이동
			session.setAttribute("logId", vo2.getUserid());
			session.setAttribute("logName", vo2.getUsername());
			session.setAttribute("logStatus", "Y");
			// 컨트롤러에서 다른 컨트롤러 매핑주소를 바로 호출한다.
			mav.setViewName("redirect:/");
		} else {// 로그인실패 : 로그인폼으로 이동
			mav.setViewName("redirect:loginForm");
		}
		return mav;
	}
	
	// 로그아웃
	@GetMapping("logout")
	public ModelAndView logout(HttpSession session) {
		// 세션 객체를 지우면 세션에 저장된 로그인정보 등 모든 데이터가 삭제되고
		// 새로운 세션을 할당한다.
		session.invalidate();
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/");
	
		return mav;
	}
	
	// 회원정보수정(폼)
	@GetMapping("memberEdit")
	public ModelAndView memberEdit(HttpSession session) {
		String userid = (String)session.getAttribute("logId");
		
		MemberVO vo = service.memberSelect(userid);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("vo", vo);
		
		mav.setViewName("member/memberEdit");
		
		return mav;		
	}
	
	// 회원정보수정(DB)
	@PostMapping("memberEditOk")
	public ModelAndView memberEditOk(MemberVO vo, HttpSession session) {
		// session 로그인 아이디...
		vo.setUserid((String)session.getAttribute("logId"));
		
		service.memberUpdate(vo);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:memberEdit");
		
		return mav;
	}
	
	// 아이디 중복검사
	@PostMapping("memberIdCheck")
	@ResponseBody
	public int idCheck(String userid) {
		int cnt = service.idCheck(userid);
		return cnt;
	}

}
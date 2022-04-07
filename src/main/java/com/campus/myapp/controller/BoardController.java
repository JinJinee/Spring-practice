package com.campus.myapp.controller;

import java.nio.charset.Charset;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.function.ServerRequest.Headers;

import com.campus.myapp.service.BoardService;
import com.campus.myapp.vo.BoardVO;
import com.campus.myapp.vo.PagingVO;

@RestController  // @Controller + @ResponseBody 두가지 기능을 합친 기능이다.
@RequestMapping("/board/*")
public class BoardController {
	@Inject
	BoardService service;
	
	// 글목록
	@GetMapping("boardList")
	public ModelAndView boardList(PagingVO pVO) {
		ModelAndView mav = new ModelAndView();
		
		// 총레코드수
		pVO.setTotalRecord(service.totalRecord(pVO));
		
		// DB처리
		mav.addObject("list", service.boardList(pVO));
		mav.addObject("pVO", pVO);
		
		mav.setViewName("board/boardList");  // /WEB-INF/views/board/boardList.jsp
		return mav;
	}
	
	// 글등록 폼
	@GetMapping("boardWrite")
	public ModelAndView boardWrite() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("board/boardWrite");
		
		return mav;
	}
	
	// 글등록
	@PostMapping("boardWriteOk")
	public ResponseEntity<String> boardWriteOk(BoardVO vo, HttpServletRequest request) {
		vo.setIp(request.getRemoteAddr()); // 접속자 아이피
		// 글쓴이 -session로그인 아이디를 구한다.
		vo.setUserid((String)request.getSession().getAttribute("logId"));
		
		ResponseEntity<String> entity = null;  // 데이터와 처리상태를 가진다.
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/html; charset=utf-8");
		// headers.setContentType(new MediaType("text", "html", Charset.forName("UTF-8")));
		
		try {
			service.boardInsert(vo);
			// 정상구현
			String msg = "<script>";
			msg += "alert('글이 등록되었습니다.');";
			msg += "location.href ='/myapp/board/boardList';";
			msg += "</script>";
			entity = new ResponseEntity<String>(msg, headers, HttpStatus.OK); // 200
		} catch(Exception e) {
			e.printStackTrace();
			// 등록안됨..
			String msg = "<script>";
			msg += "alert('글등록 실패하였습니다.');";
			msg += "history.back();";
			msg += "</script>";
			entity = new ResponseEntity<String>(msg, headers, HttpStatus.BAD_REQUEST); // 
		}
		return entity;
	}
	
	// 글내용보기
	@GetMapping("boardView")
	public ModelAndView boardView(int no) {
		ModelAndView mav = new ModelAndView();
		
		service.hitCount(no); // 조회수 증가
		
		mav.addObject("vo", service.boardSelect(no));
		
		mav.setViewName("board/boardView");
		
		return mav;	
	}
	
	// 글수정 폼
	@GetMapping("boardEdit")
	public ModelAndView boardEdit(int no) {
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("vo", service.boardSelect(no));
		mav.setViewName("board/boardEdit");
		
		return mav;
	}
	
	// 글수정(DB)
	@PostMapping("boardEditOk")
	public ResponseEntity<String> boardEditOk(BoardVO vo, HttpSession session) {
		ResponseEntity<String> entity = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html", Charset.forName("UTF-8")));
		
		vo.setUserid((String)session.getAttribute("logId"));
		
		try {
			int result = service.boardUpdate(vo);
			if(result > 0) { // 수정성공
				entity = new ResponseEntity<String>(getEditSuccessMessage(vo.getNo()), headers, HttpStatus.OK);
			}else { // 수정못함
				entity = new ResponseEntity<String>(getEditFailMessage(), headers, HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e) {
			e.printStackTrace();
			// 수정실패
			entity = new ResponseEntity<String>(getEditFailMessage(), headers, HttpStatus.BAD_REQUEST);
		}
		
		return entity;
	}
	

	// 글삭제
	@GetMapping("boardDel")
	public ModelAndView boardDel(int no, HttpSession session) {
		String userid = (String)session.getAttribute("logId");
		int result = service.boardDelete(no, userid);
		
		ModelAndView mav = new ModelAndView();
	
		if(result > 0) { // 삭제
			mav.setViewName("redirect:boardList");   // list로 이동하는 컨트롤러 호출
		}else { // 삭제실패
			mav.addObject("no", no);
			mav.setViewName("redirect:boardView");
		}
		
		return mav;
	}
	
	
	// 글수정 메세지
	public String getEditFailMessage() {
		String msg = "<script>";
		msg += "alert('글수정이 실패하였습니다. \\n수정폼으로 이동합니다.');";
		msg += "history.back();";
		msg += "</script>";
		return msg;
	}
	public String getEditSuccessMessage(int no) {
		String msg = "<script>";
		msg += "alert('글수정이 되었습니다. \\n글내용보기로 이동합니다.');";
		msg += "location.href='/myapp/board/boardView?no=" + no + "';";
		msg += "</script>";
		return msg;
	}

	// 여러개 레코드 삭제
	@PostMapping("multiDel")
	public ModelAndView multiDelete(BoardVO vo, HttpSession session) {
		vo.setUserid((String)session.getAttribute("logId"));
		
		ModelAndView mav = new ModelAndView();
		
		service.boardMultiDelete(vo);
		
		mav.setViewName("redirect:boardList");
		
		return mav;
	}
	
}

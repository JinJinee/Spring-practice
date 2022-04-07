package com.campus.myapp.controller;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.campus.myapp.service.BoardService;
import com.campus.myapp.vo.BoardVO;

@Controller
public class TransactionController {
	@Inject
	BoardService service;
	
	// root-context.xml에 있는 transaction객체를 가져오기
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	@RequestMapping("/board/boardTran")
	@Transactional(rollbackFor= {Exception.class, RuntimeException.class})
	public ModelAndView transactionTest() {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		ModelAndView mav = new ModelAndView();
		
		try {
			BoardVO vo1 = new BoardVO();
			vo1.setSubject("aaaa9999");
			vo1.setContent("bbbbb9999");
			vo1.setUserid("goguma");
			vo1.setIp("127.0.0.1");
			
			service.boardInsert(vo1);
			
			BoardVO vo2 = new BoardVO();
			vo2.setSubject("aaaa8888");
			vo2.setContent("bbbbb8888");
			vo2.setUserid("goguma1234");
			vo2.setIp("127.0.0.1");
			
			service.boardInsert(vo2);
			
			transactionManager.commit(status);
			
		}catch(Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
		}
		
		mav.setViewName("redirect:boardList");
		
		return mav;
	}
	
}

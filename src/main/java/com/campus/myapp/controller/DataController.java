package com.campus.myapp.controller;


import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.campus.myapp.service.DataService;
import com.campus.myapp.vo.DataVO;

@Controller
public class DataController {
	@Autowired
	DataService service;
	
	@GetMapping("/data/dataList")
	public ModelAndView dataList() {
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("lst", service.dataSelectAll());
		
		mav.setViewName("data/dataList");
		
		return mav;
	}
	
	// 자료실 글쓰기 폼
	@GetMapping("/data/write")
	public ModelAndView dataWrite() {
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("data/dataWrite");
		
		return mav;
	}
	
	@PostMapping("/data/writeOk")
	public ResponseEntity<String> dataWriteOk(DataVO vo, HttpServletRequest request) {
		// vo : subject, content는 request가 됨.
		vo.setUserid((String)request.getSession().getAttribute("logId")); // 글쓴이
		
		ResponseEntity<String> entity = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html", Charset.forName("UTF-8")));
		
		// 파일업로드를 위한 업로드위치의 절대주소
		String path = request.getSession().getServletContext().getRealPath("/upload");	
		System.out.println("path---->" + path);
		
		try {
			// 파일업로드를 처리하기 위해서는 request객체에서 multipart객체를 구해야한다.
			MultipartHttpServletRequest mr = (MultipartHttpServletRequest)request;
			
			//mr에 파일의 수만큼 MultipartFile객체가 존재한다.
			List<MultipartFile> files = mr.getFiles("filename");
			System.out.println("업로드 파일수 = " + files.size());
			
			if(files != null) { // if 1111111
				int cnt = 1;  // 업로드 순서에 따라 filename1, filename2파일명을 대입하기 위한 변수
				
				// 첨부파일수 만큼 반복하여 업로드한다.
				for(int i=0; i<files.size(); i++) { // for 2222222
					// 1. MultipartFile객체 얻어오기
					MultipartFile mf = files.get(i);
					
					// 2. 업로드한 실제 파일명을 구하기
					String orgFileName = mf.getOriginalFilename();
					System.out.println("orgFileName->" + orgFileName);
					
					// 3. rename하기
					if(orgFileName != null && !orgFileName.equals("")) { // if 3333333
						File f = new File(path, orgFileName);
						
						// 파일이 존재하는 지 확인  true:파일존재 false: 파일없다
						if(f.exists()) { // if 44444
							for(int renameNum=1;;renameNum++) { // for 55555
								// 확장자와 파일을 분리한다.
								int point = orgFileName.lastIndexOf(".");
								String fileName = orgFileName.substring(0, point); 
								String ext = orgFileName.substring(point+1);
								
								f = new File(path, fileName+"("+renameNum+")."+ext);
								if(!f.exists()) { // 새로생성된 파일객체가 없으면 
									orgFileName = f.getName();
									break;
								}
							} // for 55555
						} // if 444444
						
						// 4. 파일업로드 함
						try {
							mf.transferTo(f);  // 실제업로드가 발생함.
						} catch(Exception ee) {
							ee.printStackTrace();
						}
						
						// 5. 업로드한(새로운파일명) vo에 셋팅
						if(cnt == 1) vo.setFilename1(orgFileName);
						if(cnt == 2) vo.setFilename2(orgFileName);
						cnt++;
					} // if 33333333
				} // for 222222
			} // if 1111111
			System.out.println(vo.toString());
			
			// DB등록
			service.dataInsert(vo);
			
			// 레코드 추가 성공
			String msg = "<script>alert('자료실 글등록이 되었습니다.');location.href='/myapp/data/dataList';</script>";
			
			entity = new ResponseEntity<String>(msg, headers, HttpStatus.OK); // 200
		}catch(Exception e) {
			e.printStackTrace();
			// 레코드추가 실패
			// 파일을 지워야한다.
			fileDelete(path, vo.getFilename1());
			fileDelete(path, vo.getFilename2());
			
			// 메세지 + 이전페이지로 보내기
			String msg = "<script>alert('자료실 글등록 실패!!!');history.back();</script>";
			
			entity = new ResponseEntity<String>(msg, headers, HttpStatus.BAD_REQUEST); // 400
		}
		return entity;
	}
	// 파일지우기
	public void fileDelete(String p, String f) {
		if(f != null) { // 파일명이 존재하면
			File file = new File(p, f);
			file.delete();
			
		}
	}
	
	// 글내용보기
	@GetMapping("/data/view")
	public ModelAndView view(int no) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("dataVO", service.dataView(no));
		mav.setViewName("data/dataView");
		
		return mav;
	}
	
	// 수정폼
	@GetMapping("/data/dataEdit")
	public ModelAndView editForm(int no) {
		ModelAndView mav = new ModelAndView();
		DataVO vo = service.dataView(no);
		// DB에 첨부된 파일의 수를 구한다.
		int fileCount = 1; // 첫번째 첨부파일은 무조건 있음.
		
		// 두번째 첨부파일 있으면 1증가
		if(vo.getFilename2() != null) {
			fileCount++;
		}
		
		mav.addObject("fileCount", fileCount);
		mav.addObject("vo", service.dataView(no));
		mav.setViewName("data/dataEdit");
		return mav;
	}
	
	// 수정(DB)
	@PostMapping("/data/editOk")
	public ResponseEntity<String> editOk(DataVO vo, HttpSession session, HttpServletRequest req) {
		vo.setUserid((String)session.getAttribute("logId"));
		String path = session.getServletContext().getRealPath("/upload");
		
		///////////////////
//		System.out.println(vo.toString());
//		if(vo.getDelFile() != null) {
//			for(int k=0; k<vo.getDelFile().length; k++) {
//				System.out.println(vo.getDelFile()[k]);
//			}
//		}
		////////////////////
		
		ResponseEntity<String> entity = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/html; charset=UTF-8");
		
		
		List<String> fileList = new ArrayList<String>(); 	// 새로 DB에 업데이트할 파일명 정리하는 컬렉션
		List<String> newUpload = new ArrayList<String>();	// 새로 업로드한 파일명
		
		try {
			// 1. DB에서 파일명 가져오기
			DataVO dbFileVO = service.getFileName(vo.getNo());
			
			fileList.add(dbFileVO.getFilename1());
			if(dbFileVO.getFilename2() != null) fileList.add(dbFileVO.getFilename2());
			
			// 2. 삭제된 파일이 있을 경우 List에서 같은 파일명을 지운다.
			if(vo.getDelFile() != null) {  // null은 삭제 파일이 없다.
				for(String delFile : vo.getDelFile()) {
					fileList.remove(delFile);
				}
			}
			
			// 3. 새로 업로드하기
			MultipartHttpServletRequest mr = (MultipartHttpServletRequest)req;
			
			// 새로 업로드된 MultipartFile객체를 얻어오기
			List<MultipartFile> newFileList = mr.getFiles("filename");
			if(newFileList != null) { // 새로 업로드된 파일이 있으면
				for(int i=0; i<newFileList.size(); i++) {
					MultipartFile newMf = newFileList.get(i);
					String newUploadFilename = newMf.getOriginalFilename();
					
					if(newUploadFilename != null && !newUploadFilename.equals("")) {
						File f = new File(path, newUploadFilename);
						if(f.exists()) {
							// rename
							for(int n=1; ; n++) {
								int point = newUploadFilename.lastIndexOf(".");
								String fileNameNoExt = newUploadFilename.substring(0, point); 
								String ext = newUploadFilename.substring(point+1);
								
								//새로운 파일명 만들어 존재유무 확인
								String nf = fileNameNoExt + "(" + n + ")." + ext;
								f = new File(path, nf);
								if(!f.exists()) {
									newUploadFilename = nf;
									break;
								}
							} // for
						}
						// 업로드
						try {
							newMf.transferTo(f);
						}catch(Exception ee) {}
						fileList.add(newUploadFilename);  // db에 등록할 파일명에 추가
						newUpload.add(newUploadFilename);// 새로 업로드 목록 추가
					}
				} // for
			} // if
			
			// fileList에 있는 DB에 등록할 파일명을 filename1 filename2에 셋팅
			for(int k=0; k<fileList.size(); k++) {
				if(k==0) vo.setFilename1(fileList.get(k));
				if(k==1) vo.setFilename2(fileList.get(k)); 
			}
			System.out.println(vo.getFilename1()+ " " + vo.getFilename2());
			// DB update
			service.dataUpdate(vo);
			
			// DB수정되었을때
			if(vo.getDelFile()!=null) {
				for(String fname : vo.getDelFile()) {
					fileDelete(path, fname);
				}
			}
			
			
			// 글내용보기로 이동
			String msg = "<script>alert('자료실글이 수정되었습니다.\\n글내용보기로 이동합니다.');location.href='/myapp/data/view?no="+vo.getNo()+"';</script>";
			entity = new ResponseEntity<String>(msg, headers, HttpStatus.OK);
			
		}catch(Exception e) {
			e.printStackTrace();
			// DB못했을때
			for(String fname : newUpload) {
				fileDelete(path, fname);
			}
			
			// 수정페이지로 이동
			String msg = "<script>alert('자료실 글수정을 실패했습니다.\\n수정폼으로 이동합니다.');history.back();</script>";
			entity = new ResponseEntity<String>(msg, headers, HttpStatus.BAD_REQUEST);	
		}
		return entity;
	}
	
	// 자료실글 삭제
	@GetMapping("/data/dataDel")
	public ResponseEntity<String> dataDel(int no, HttpSession session) {
		String userid = (String)session.getAttribute("logId");
		
		String path = session.getServletContext().getRealPath("/upload");
		
		ResponseEntity<String> entity = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/html; charset=utf-8");
		
		try {
			// 1. 삭제할 레코드의 파일명 얻어오기
			DataVO dbFileVO = service.getFileName(no);
			
			// 2. 레코드 삭제
			service.dataDelete(no, userid);
			
			// 3. 파일삭제
			fileDelete(path, dbFileVO.getFilename1());
			if(dbFileVO.getFilename2() != null) {
				fileDelete(path, dbFileVO.getFilename2());
			}
			
			String msg = "<script>alert('글이 삭제되었습니다.');location.href='/myapp/data/dataList';</script>";
			entity = new ResponseEntity<String>(msg, headers, HttpStatus.OK);
			
		}catch(Exception e) {
			e.printStackTrace();
			
			String msg = "<script>alert('글이 삭제실패하였습니다.');history.back();</script>";
			entity = new ResponseEntity<String>(msg, headers, HttpStatus.BAD_REQUEST);
			
		}
		
		return entity;
		
	}
	
}

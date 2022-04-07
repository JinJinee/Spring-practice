<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<script src="//cdn.ckeditor.com/4.17.2/full/ckeditor.js"></script>
<style>
	#subject{
		width: 99%;
	}

</style>
<script>
	$(function(){
		CKEDITOR.replace("content");
		// 파일갯수 처리하기 위한 변수생성
		var fileCount = ${fileCount};
		
		
		$("#dataFrm b").click(function(){
			// 파일명 숨기기
			$(this).parent().css("display", "none");
			
			// 삭제파일 정보 name을 delFile로 변경
			$(this).parent().next().attr("name", "delFile");
			
			// input보여주기
			$(this).parent().next().next().attr("type", "file");
			
			// 파일의 갯수 줄어들어야 한다.
			fileCount--;
		});
		
		
		$("#dataFrm").submit(function(){
			if($("#subject").val() == ''){
				alert("글제목을 입력하세요...");
				return false;
			}
			
			if(CKEDITOR.instances.content.getData() == ''){
				alert("글내용을 입력하세요...");
				return false;
			}
			
			// 첨부파일 선택갯수
			if($("#filename1").val() != ''){ // 파일을 선택했다.
			 	fileCount++;
			}
			if($("#filename2").val() != ''){
				fileCount++;
			}
			
			if(fileCount < 1){
				alert("첨부파일은 반드시 1개이상 이어야 합니다...");
				return false;
			}
			
		});
	});

</script>

<div class="container">
	<h1>자료실 글수정 폼</h1>
	<!-- 파일업로드의 기능이 있는 폼은 반드시 enctype속성을 명시하여야 한다. -->
	<form id="dataFrm" method="post" action="/myapp/data/editOk" enctype="multipart/form-data">
		<input type="hidden" name="no" value="${vo.no}"/>
		<ul>
			<li>제목</li>
			<li><input type="text" name="subject" id="subject" value="${vo.subject}"/></li>
			<li>글내용</li>
			<li><textarea name="content" id="content">${vo.content}</textarea></li>
			<li>첨부파일</li>
			<li>
				<!-- 첫번째 첨부파일 -->
				<div>${vo.filename1} &nbsp;<b>x</b></div>
				
				<!-- x를 누르면 삭제파일명이 있는 input의 name 속성값을 delFile -->
				<input type="hidden" name="" value="${vo.filename1}"/>
				
				<!-- x를 누르면 파일이 삭제되고 새로운 파일을 선택가능하도록 input 만들어 준다. -->
				<input type="hidden" name="filename" id="filename1"/>
			</li>
			<li>
				<!-- 두번째 첨부파일이 있을때 -->
				<c:if test="${vo.filename2 != null && vo.filename2 != ''}">
					<div>${vo.filename2} &nbsp; <b>x</b></div>
					<input type="hidden" name="" value="${vo.filename2}"/>
					<input type="hidden" name="filename" id="filename2"/>
				</c:if>
				<c:if test="${vo.filename2 == null || vo.filename2 == ''}">
					<input type="file" name="filename" id="filename2"/>
				</c:if>
			</li>
			<li><input type="submit" value="자료실 글수정"/></li>
		</ul>
	</form>
</div>
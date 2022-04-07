<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script>
	function dataDelCheck(){
		if(confirm("삭제하시겠습니까?")){
			location.href="/myapp/data/dataDel?no=${dataVO.no}";
		}
			
	}
</script>
<div class="container">
	<h1>자료실 글내용보기</h1>
	<ul>
		<li>번호 : ${dataVO.no }</li>
		<li>글쓴이 : ${dataVO.userid}</li>
		<li>등록일 : ${dataVO.writedate}</li>
		<li>제목 : ${dataVO.subject}</li>
		<li>글내용</li>
		<li>${dataVO.content}</li>
		<li>
			첨부파일 : <a href="/myapp/upload/${dataVO.filename1}" download>${dataVO.filename1}</a>
			<c:if test="${dataVO.filename2 != null && dataVO.filename2 != ''}">
				<a href="/myapp/upload/${dataVO.filename2}" download>${dataVO.filename2}</a>
			</c:if>
		</li>
	</ul>
	<c:if test="${dataVO.userid == logId}">
		<a href="/myapp/data/dataEdit?no=${dataVO.no}">수정</a>
		<a href="javascript:dataDelCheck()">삭제</a>
	</c:if>
</div>
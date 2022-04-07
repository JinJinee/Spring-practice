<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script>
	function delCheck(){
		// 사용자가 y(true),n(false) 선택할 수 있는 대화상자
		if(confirm("삭제하시겠습니까?")){
			// 확인버튼 선택시
			location.href = "/myapp/board/boardDel?no=${vo.no}";
		}
	}
	
	// 댓글-----------------------------------------------------
	$(function(){
		// 댓글목록을 가져오는 함수
		function replyListAll(){ // 현재글의 댓글을 모두 가져오기
			var url = "/myapp/reply/list";
			var params = "no=${vo.no}"; 	// no=32
			
			$.ajax({
				url : url,
				data : params,
				success : function(result){
					var $result = $(result);
					
					var tag = "<ul>";
					
					$result.each(function(idx, vo){
						tag += "<li><div>" + vo.userid;
						tag += "(" + vo.writedate + ")";
						
						//    'goguma' == goguma
						if(vo.userid == '${logId}'){
							tag += "<input type='button' value='Edit'/>";
							tag += "<input type='button' value='Del' title='" + vo.replyno + "'/>";
						}
						
						tag += "<br>" + vo.coment + "</div>";
						
						// 본인글일때 수정폼이 있어야 한다.
						if(vo.userid == '${logId}'){
							tag += "<div style='display:none;'><form method='post'>";
							tag += "<input type='hidden' name='replyno' value='" + vo.replyno + "'/>";
							tag += "<textarea name='coment' style='width:400px; height:50px;'>" + vo.coment + "</textarea>";
							tag += "<input type='submit' value='수정'/>";
							tag += "</form></div>";
						}
						tag += "<hr></li>";
					});
					
					tag += "</ul>";
					
					$("#replyList").html(tag);
				},
				error : function(e){
					console.log(e.responseText)
				}
			});
			
		}
		
		// 댓글등록
		$("#replyFrm").submit(function(){
			event.preventDefault(); // form 기본이벤트 제거
			
			if($("#coment").val() == ''){ // 댓글 안쓴경우
				alert("댓글을 입력후 등록하세요...");
				return;
			}else { // 댓글 입력한 경우
				var params = $("#replyFrm").serialize();
			
				$.ajax({
					url : '/myapp/reply/writeOk',
					data : params,
					type : 'POST',
					success : function(r){
						// 폼을 초기화
						$("#coment").val("");
						// 댓글목록 refresh되어야 한다.
						replyListAll();
					},
					error : function(e){
						console.log(e.responseText);
					}
				});
			}
		});
		// 댓글 수정(Edit)버튼 선택시 해당폼 보여주기
		$(document).on('click', '#replyList input[value=Edit]', function(){
			 // 숨기기
			$(this).parent().css("display", "none");
			// 보여주기
			$(this).parent().next().css("display", "block");
		});
		
		// 댓글 수정(DB)
		$(document).on('submit', '#replyList form', function(){
			event.preventDefault();
			// 데이터
			var params = $(this).serialize();
			var url = '/myapp/reply/editOk';
			
			$.ajax({
				url : url,
				data : params,
				type : 'POST',
				success : function(result){
					console.log(result);
					replyListAll();
					
				},
				error : function(){
					console.log('수정에러발생');
				}
			});
		});
		// 댓글삭제
		$(document).on('click','#replyList input[value=Del]', function(){
			if(confirm("댓글을 삭제하시겠습까?")){
				var params = "replyno="+$(this).attr("title");
				
				$.ajax({
					url : '/myapp/reply/del',
					data : params,
					success : function(result){
						console.log(result);
						replyListAll();
					},
					error : function(){
						console.log("댓글삭제에러.....");
					}
				});
			}
		});
		
		
		// 현재글의 댓글
		replyListAll();
	});
</script>
<div class="container">
	<h1>글내용보기</h1>
	<ul class="viewList">
		<li>번호 : ${vo.no}</li>
		<li>작성자 : ${vo.userid}</li>
		<li>작성일 : ${vo.writedate} | 조회수 : ${vo.hit}</li>
		<li><hr></li>
		<li>제목 : ${vo.subject}</li>
		<li><hr></li>
		<li>글내용</li>
		<li>${vo.content}</li>
	</ul>
	<div>
		<!-- 로그인 아이디와 글쓴이가 같을 경우 수정삭제 표시 -->
		<c:if test="${logId==vo.userid}">
			<a href="/myapp/board/boardEdit?no=${vo.no}">수정</a>
			<a href="javascript:delCheck()">삭제</a>
		</c:if>
	</div>
	
	<hr>
	<!-- 댓글쓰기 -->
	<c:if test="${logStatus == 'Y'}">
		<form method="POST" id="replyFrm">
			<input type="hidden" name="no" value="${vo.no}"/>
			<textarea name="coment" id="coment" style="width:500px; height:50px;"></textarea>
			<input type="submit" value="댓글등록"/>
		</form>
	</c:if>
	
	<!-- 댓글목록이 나올자리 -->
	<div id="replyList" >
	</div>
	
</div>
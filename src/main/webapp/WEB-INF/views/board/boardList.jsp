<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<style>
	/*리스트*/
	.boardList{
		overflow: auto;
	}
	
	.boardList li{
	float: left;
	height: 40px;
	line-height: 40px;
	border-bottom: 1px solid #ddd;
	width: 10%;
	}
	
	.boardList li:nth-child(6n+3){
		width: 50%;
		white-space: nowrap; /*줄안바꿈*/
		overflow: hidden;    /*넘친내용숨기기*/
		text-overflow: ellipsis; /*넘침시 ...표시*/
	}
	
	/*페이지*/
	.paging{
		margin: 30px 0px;
		overflow: auto;
	}
	
	.paging li{
		float: left;
		width: 30px;
		text-align: center;
	}
	
	/*검색*/
	#searchFrm{
		padding: 20px 200px;
		text-align: center;
	}
	
	a:link, a:visited{
		color: black;
		text-decoration: none;
	}
	
	a:hover{
		text-decoration: underline;
	}
</style>
<script>
	$(function(){
		$("#searchFrm").submit(function(){
			if($("#searchWord").val() == ""){
				alert("검색어를 입력하세요...");
				return false;
			}
		});
		
		// 리스트 체크하기
		$("#allCheck").click(function(){
			$(".chk").prop('checked', this.checked);
		});
		
		$("#multiDel").click(function(){
			var cnt = 0;
			$(".chk").each(function(i, obj){
				if(obj.checked){
					cnt++;  // checkbox가 체크 상태일 때...
				}
			});
			
			if(cnt <= 0){
				alert("목록을 선택후 삭제하세요...");
				return false;
			}
			
			$("#listFrm").submit();
		});
		
	});

</script>

<div class="container">
	<h1>게시판 목록</h1>
	<div><a href="<%=request.getContextPath()%>/board/boardWrite">글쓰기</a></div>
	<div>
		총레코드 수 : ${pVO.totalRecord}, ${pVO.totalPage} / ${pVO.pageNum}
	</div>
	<div>
		<input type="checkbox" id="allCheck"/>전체선택
		<input type="button" id="multiDel" value="선택삭제"/>
	</div>
	<form method="post" action="/myapp/board/multiDel" id="listFrm">
		<ul class="boardList">
			<li>&nbsp;</li>
			<li>번호</li>
			<li>제목</li>
			<li>글쓴이</li>
			<li>조회수</li>
			<li>등록일</li>
			
			<c:forEach var="vo" items="${list}">
				<li><input type="checkbox" name="noList" value="${vo.no}" class="chk"/></li>
				<li>${vo.no}</li>
				<li><a href="/myapp/board/boardView?no=${vo.no}">${vo.subject}</a></li>
				<li>${vo.userid}</li>
				<li>${vo.hit}</li>
				<li>${vo.writedate}</li>
			</c:forEach>
		</ul>
	</form>
	
	<!-- 페이징 -->
	<ul class="paging"> 
		<!-- 이전페이지 -->
		<c:if test="${pVO.pageNum==1}">
			<li>prev</li>
		</c:if>
		<c:if test="${pVO.pageNum>1}">
			<li><a href="/myapp/board/boardList?pageNum=${pVO.pageNum-1}<c:if test='${pVO.searchWord!=null}'>&searchKey=${pVO.searchKey}&searchWord=${pVO.searchWord}</c:if>">prev</a></li>
		</c:if>
		
		<!-- 페이지 번호 				1,5        6,10        11,15	-->
		<c:forEach var="p" begin="${pVO.startPage}" end="${pVO.startPage + pVO.onePageCount-1}">
			<!-- 총페이지수 보다 출력할 페이지번호가 작을때 -->
			<c:if test="${p<=pVO.totalPage}">
				<c:if test="${p==pVO.pageNum}">
					<li style="background-color:powderblue">
				</c:if>
				<c:if test="${p != pVO.pageNum}">
					<li>
				</c:if>
				<a href="/myapp/board/boardList?pageNum=${p}<c:if test='${pVO.searchWord!=null}'>&searchKey=${pVO.searchKey}&searchWord=${pVO.searchWord}</c:if>">${p}</a></li>
			</c:if>
		</c:forEach>
		
		<!-- 다음페이지 -->
		<c:if test="${pVO.pageNum < pVO.totalPage}">
			<li><a href="/myapp/board/boardList?pageNum=${pVO.pageNum+1}<c:if test='${pVO.searchWord!=null}'>&searchKey=${pVO.searchKey}&searchWord=${pVO.searchWord}</c:if>">next</a></li>
		</c:if>
		<c:if test="${pVO.pageNum == pVO.totalPage}">
			<li>next</li>
		</c:if>
	</ul>
	
	<!-- 검색 -->
	<div>
		<form method="get" action="/myapp/board/boardList" id="searchFrm">
			<select name="searchKey">
				<option value="subject">제목</option>
				<option value="content">글내용</option>
				<option value="userid">글쓴이</option>
			</select>
			<input type="text" name="searchWord" id="searchWord"/>
			<input type="submit" value="Search"/>
		</form>
	</div>
	
</div>
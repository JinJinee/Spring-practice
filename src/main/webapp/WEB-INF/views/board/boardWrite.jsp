<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<style>
	#subject{
		width: 100%;
	}
	
	#boardFrm li{
		padding: 10px 5px;
	}

</style>
<script src="//cdn.ckeditor.com/4.17.2/full/ckeditor.js"></script>
<script>
	$(function(){
		CKEDITOR.replace("content");
		
		$("#boardFrm").submit(function(){
			if($("#subject").val() == ''){
				alert("글제목을 입력하세요.");
				return false;
			}
			if(CKEDITOR.instances.content.getData() == ''){
				alert("글내용을 입력하세요.");
				return false;
			}
		});
	});
</script>
<div class="container">
	<h1>글등록 폼</h1>
	<form method="post" action="/myapp/board/boardWriteOk" id="boardFrm">
		<ul>
			<li>제목</li>
			<li><input type="text" name="subject" id="subject"/></li>
			<li><textarea name="content" id="content"></textarea></li>
			<li><input type="submit" value="등록"/></li>
		</ul>
	</form>
</div>
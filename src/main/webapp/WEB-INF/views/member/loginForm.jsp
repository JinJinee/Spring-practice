<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<style>
	#log{
		width:400px; 
		margin:0px auto;
		}
		
	#log>h1{
		text-align:center;
		}
	
	#logFrm>ul>li{
		float:left;
		width:30%;
		}
		
	#logFrm>ul>li:nth-child(2n){
		width:70%;
		}
		
	#logFrm>ul>li:last-of-type{
		width:100%; 
		text-align:center; 
		margin-top:30px;
		}
</style>
<script>
	function logFormCheck(){
		//아이디와 비밀번호 입력유무를 확인하고 모두 입력되면 action의 파일을 요청하고
		//하나라도 입력안되면 현재 페이지 머물러야 한다.
		//아이디가 있는지 확인
		var id = document.getElementById("userid");
		if(id.value==""){
			alert("아이디를 입력하세요.");
			id.focus(); // 커서를 해당객체에 위치시킨다.
			return false;
		}
		if(document.querySelector("#userpwd").value==""){
			alert("비밀번호를 입력하세요.");
			document.querySelector("#userpwd").focus();
			return false;
		}
		//아이디와 비밀번호가 모두 입력되었을 때
		return true;
		
	}
</script>

<div class="container">
	<div id="log">
		<h1>로그인</h1>
		<hr/>
		<form method="post" action="/myapp/member/loginOk" id="logFrm" onsubmit="return logFormCheck()">
		<ul>
			<li>아이디</li>
			<li><input type="text" name="userid" id="userid"/></li>
			<li>비밀번호</li>
			<li><input type="password" name="userpwd" id="userpwd"/></li>
			<li><input type="submit" value="로그인"/>
				<input type="reset" value="취  소"/>
			</li>
			
		</ul>
		</form>
	</div>
</div>
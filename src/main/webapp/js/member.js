// 가입하기 클릭시
function memberCheck(){
	let userid = document.getElementById("userid");
	if(userid.value == ''){
		alert("아이디를 입력하세요...");
		userid.focus();
		return false;
	}
	///////////////////////////////////////
	if(document.getElementById("idchk").value=="N"){
		alert("아이디중복검사하세요..");
		return false;
	}
	///////////////////////////////////////
	
	// 비밀번호 확인
	let userpwd = document.getElementById("userpwd");
	let userpwd2 = document.getElementById('userpwd2');
	if(userpwd.value == '' || userpwd2.value == ''){
		alert('비밀번호를 입력하세요...');
		userpwd.focus();
		return false;
	}
	if(userpwd.value != userpwd2.value){
		alert('비밀번호가 일치하지 않습니다.');
		userpwd.focus();
		return false;
	}
	
	// 이름
	let username = document.getElementById('username');
	if(username.value == ""){
		alert('이름을 입력하세요...');
		username.focus();
		return false;
	}
	
	// 전화번호
	let tel2 = document.querySelector("#tel2");
	let tel3 = document.querySelector('#tel3');
	
	let regExp = /^[0-9]{3,4}$/; // tel2
	let regExp2 = /^[0-9]{4}$/;  // tel3
	if(!regExp.test(tel2.value) || !regExp2.test(tel3.value)){
		alert('전화번호를 잘못 입력하였습니다.');
		tel2.focus();
		return false;
	}
	
}
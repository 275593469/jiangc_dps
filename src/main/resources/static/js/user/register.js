$(function(){

	clickCode();
	var emailReg = /(^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$)/;
	
	$("#toRegister").click(function(){
		var account = $("#account").val();//账号
		var nickName = $("#nickName").val();//昵称
		var password = $("#password").val();//密码
		var confirmPassword = $("#confirm-password").val();//确认密码
		var captcha = $("#captcha").val();//验证码
		
		if(checkUserName(account) && checkNickName(nickName) 
				&& checkPassword(password, confirmPassword) && checkCaptcha(captcha)){
			registerUser(account, nickName, password);//注册
		}
	});
	
	$("#account").on('keyup',function(){
		checkUserName($(this).val());
	});
	$("#nickName").on('keyup',function(){
		checkNickName($(this).val());
	});
	$("#password,#confirm-password").on('keyup',function(){
		checkPassword($("#password").val(), $("#confirm-password").val());
	});
	$("#captcha").on('keyup',function(){
		if($(this).val().length == 4){
			checkCaptcha($(this).val());
		}
	});
	
	//校验邮箱
	function checkUserName(userName){
		if(userName == ''){
			$("#accountError div span").html("用户名不能为空");
			$("#accountError").css("display","");
			return false;
		}else if(!emailReg.test(userName)){
			$("#accountError > div > span").html("邮箱格式错误");
			$("#accountError").css("display","");
			return false;
		}else{
			$("#accountError").css("display","none");
			return true;
		}
	}
	//校验昵称
	function checkNickName(nickName){
		if(nickName == ''){
			$("#nickNameError > div > span").html("昵称不能为空");
			$("#nickNameError").css("display","");
			return false;
		}else{
			$("#nickNameError").css("display","none");
			return true;
		}
	}
	//校验密码
	function checkPassword(password, confirm){
		if(password == ''){
			$("#passwordError > div > span").html("密码不能为空");
			$("#passwordError").css("display","");
			return false;
		}else if(password.length>12 || password.length<6){
			$("#passwordError > div > span").html("密码长度为6-12");
			$("#passwordError").css("display","");
			return false;
		}else{
			$("#passwordError").css("display","none");
		}
		if(confirm == ''){
			$("#confirm-passwordError > div > span").html("确认密码不能为空");
			$("#confirm-passwordError").css("display","");
			$("#passwordError").css("display","none");
			return false;
		}else if(password != confirm){
			$("#confirm-passwordError > div > span").html("两次输入的密码不一致");
			$("#confirm-passwordError").css("display","");
			return false;
		}else{
			$("#confirm-passwordError").css("display","none");
			return true;
		}
	}
	//校验验证码
	function checkCaptcha(captcha) {
		if(captcha==""){
			$("#captchaError > div > span").html("验证码不能为空");
			$("#captchaError").css("display","");
			return false;
		}else{
			if(captcha.toLowerCase() != $("#checkCode").val().toLowerCase()){
				$("#captchaError > div > span").html("验证码输入错误");
				$("#captchaError").css("display","");
				clickCode();
				return false;
			}else{
				$("#captchaError").css("display","none");
				return true;
			}
		}
	}
	//获取随机的验证码
	$("#checkCode").click(function(){
		clickCode();
	});
	function clickCode() {
		var code = ""; 
		var codeLength = 4;
		//验证码的长度 
		var checkCode = document.getElementById("checkCode"); 
		var selectChar = new Array(2,3,4,5,6,7,8,9,'A','B','C','D','E','F','G','H','I','J','K','M','N','P','Q','R','S','T','U','V','W','X','Y','Z');
		//所有候选组成验证码的字符，当然也可以用中文的 
		for(var i=0;i<codeLength;i++) {
			var charIndex = Math.floor(Math.random()*(selectChar.length)); 
			code +=selectChar[charIndex]; 
		} 
		if(checkCode) {
			checkCode.className="code"; 
			checkCode.value = code; 
		} 
	}
	
	//用户注册
	function registerUser(userName, nickName, password){
		$.ajaxFunction({
			url: getContextPath()+"/user/register",
			type: 'post',
			dataType: 'json',
			data: {
				userName: userName,
				nickName: nickName,
				password: password
			},
			success: function(data){
				if(data.code == "success"){
					$(".confirm").trigger("click");
					toActive(data.result);
				}else{
					toastr.warning(data.message);
				}
				
			}
		});
		
	}
	
	//去激活
	var toActive = function(result){
		$(".toActive").click(function(){
			location.href = result.toEmail;
		});
	}
});
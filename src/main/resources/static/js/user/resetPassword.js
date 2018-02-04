$(function(){
	clickCode();
	var emailReg = /(^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$)/;
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
	
	$("#resetPassword").click(function(){
		var account = $("#userName").val();//账号
		var captcha = $("#captcha").val();//验证码
		
		if(checkUserName(account) && checkCaptcha(captcha)){
			resetPassword(account);//注册
		}
	});
	
	$("#account").on('keyup',function(){
		checkUserName($(this).val());
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
				return false;
			}else{
				$("#captchaError").css("display","none");
				return true;
			}
		}
	}
	
	//用户注册
	function resetPassword(userName){
		$.ajaxFunction({
			url: getContextPath()+"/user/resetPasswordByUserName",
			type: 'post',
			dataType: 'json',
			data: {
				userName: userName,
			},
			success: function(data){
				if(data.code == "success"){
					$(".confirm").trigger("click");
					$(".toEmail").attr("href",data.result.toEmail);
				}else{
					toastr.warning(data.message);
				}
				
			}
		});
		
	}
});
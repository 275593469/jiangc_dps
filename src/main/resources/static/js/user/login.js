$(function(){
	
	$("#toLogin").click(function(){
		var params = {
			userName: $("#userName").val(),
			password: $("#password").val()
		}
		
		$.ajaxFunction({ 
			url: getContextPath()+"/user/login",
			type: 'post',
			dataType: 'json',
			data: params,
			success: function(data){
				var code = data.result.code;
				switch(code){
					case 0: $(".confirm").trigger("click");
						break;
					case 1:location.href = getContextPath()+"/view/index/pages";
						break;
					case 2: $("#accountError > div > span").html(data.message);
							$("#accountError").css("display","");
						break;
					case 3:	$("#passwordError > div > span").html(data.message);
							$("#passwordError").css("display","");
						break;
				}
					
			}
		});
	});
	
	$("#userName").focus(function(){
		$("#accountError").css("display","none");
	});
	$("#password").focus(function(){
		$("#passwordError").css("display","none");
	});
	
	//发送邮件激活账号
	$(".toActive").click(function(){
		$.ajaxFunction({ 
			url: getContextPath()+"/user/sendActiveMail",
			type: 'post',
			dataType: 'json',
			data: {
				userName: $("#userName").val()
			},
			success: function(data){
				if(data.code == "success"){
					toastr.success(data.message);
					location.href = data.result.toEmail;
				}else{
					toastr.warning(data.message);
				}
				
			}
		});
	});
});
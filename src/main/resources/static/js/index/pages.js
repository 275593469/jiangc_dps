//iframe嵌入页面的高度自适应
function reinitIframe(){
	var iframe = document.getElementById("main_iframe");
		try{
		var bHeight = iframe.contentWindow.document.body.scrollHeight;
		var dHeight = iframe.contentWindow.document.documentElement.scrollHeight;
		var height = Math.min(bHeight, dHeight);
		iframe.height = height;
		}catch (ex){}
	}
window.setInterval("reinitIframe()", 560);

$(function(){
	getUserInfo();
	
	//导航页的切换
	$(".mainnav li a").click(function(){
		$(".mainnav li.active").removeClass("active");
		$(this).parent().addClass("active");
		
		var href = $(this).attr("href");
		href = href.replace("#", "..");
		$("#main_iframe").attr("src", href);
	});
	
	//获取登录用户信息
	function getUserInfo(){
		$.ajaxFunction({ 
			url: getContextPath()+"/user/session",
			type: 'post',
			dataType: 'json',
			success: function(data){
				if(data.code == 'success'){
					var user = data.result[0];
					$("#account").text(" "+user.nick_name);
					$("#account").attr("userId", user.user_id);
					showResource(user.permission);
				}
					
			}
		});
	}
	
	//显示菜单栏资源
	function showResource(permission){
		for(var key in permission){
			$("a[href='"+permission[key]+"']").parent("li").removeClass("hide");
		}
	}
	
	//退出账号
	$("#logout").click(function(){
		userLogout();
	});
	function userLogout(){
		$.ajaxFunction({ 
			url: getContextPath()+"/user/logout",
			type: 'post',
			dataType: 'json',
			success: function(data){
				if(data.code == 'success'){
					location.href=getContextPath()+"/view/user/login";
				}
					
			}
		});
	}
	
});
	


$(function(){
	var contextPath = 'http://127.0.0.1:8081/DPS';//日志记录的项目路径
	var projectName = 'DPS';//项目名
	var projectId = 'e25da64929ac47e389e54138350ed2a3';//项目id
	var uuid = uuid(32);//浏览页面的UUID
	
	//进入页面时请求后台，记录浏览日志
	(function(){
		var params = {
			browseId: uuid,
			title: document.title,
			userId: getCookie('userId'),
			pageUrl: document.URL,
			projectId: projectId,
			projectName: projectName,
			isStart: true,//是否是开始浏览页面
		};
		$.ajax({
			url: contextPath+"/log/saveBrowseLog",
			type: 'post',
			dataType: 'json',
			data:{json: JSON.stringify(params)},
			success: function(data){
					
			}
		});
	})();
	
	//离开页面是触发页面
	window.onbeforeunload = function(event) {
		var params = {
				browseId: uuid,
				title: document.title,
				userId: getCookie('userId'),
				pageUrl: document.URL,
				projectId: projectId,
				projectName: projectName,
				isStart: false,//表示离开页面的标识
			};
			$.ajax({
				url: contextPath+"/log/saveBrowseLog",
				type: 'post',
				dataType: 'json',
				data:{json: JSON.stringify(params)},
				success: function(data){
						
				}
			});
	}
	
	//获取a标签的点击事件
	$("a").click(function(){
		var params = {
				userId: getCookie('userId'),
				event: 'click',
				buttonText: $(this).text(),
				projectId: projectId,
				projectName: projectName,
				href: $(this).attr('href'),
			};
			$.ajax({
				url: contextPath+"/log/saveHandleLog",
				type: 'post',
				dataType: 'json',
				data:{json: JSON.stringify(params)},
				success: function(data){
						
				}
			});
	});
	//获取input button标签的点击事件
	$("input[type=button]").click(function(){
		var params = {
				userId: getCookie('userId'),
				event: 'click',
				buttonText: $(this).val(),
				projectId: projectId,
		};
		$.ajax({
			url: contextPath+"/log/saveHandleLog",
			type: 'post',
			dataType: 'json',
			data:{json: JSON.stringify(params)},
			success: function(data){
				
			}
		});
	});
	
	//获取Cookie中的值
	function getCookie(name){
		var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
		if(arr=document.cookie.match(reg))
		return unescape(arr[2]);
		else
		return null;
	}
	
	//获取UUID,  len：长度；一般是32位
	function uuid(len) {
	    var chars = '0123456789abcdef'.split('');
	    var uuid = [], i;
	    radix = chars.length;
	 
	    if (len) {
	      // Compact form
	      for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random()*radix];
	    } else {
	      // rfc4122, version 4 form
	      var r;
	 
	      // rfc4122 requires these characters
	      uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
	      uuid[14] = '4';
	 
	      // Fill in random data.  At i==19 set the high bits of clock sequence as
	      // per rfc4122, sec. 4.1.5
	      for (i = 0; i < 36; i++) {
	        if (!uuid[i]) {
	          r = 0 | Math.random()*16;
	          uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
	        }
	      }
	    }
	    return uuid.join('');
	}
});
$(function(){
	var contextPath = '${contextPath}';
	var projectId = '${projectId}';
	var projectName = '${projectName}';
	var uuid = uuid(32);
	(function(){
		var params = {
			browseId: uuid,
			title: document.title,
			userId: getCookie('userId'),
			pageUrl: document.URL,
			projectId: projectId,
			projectName: projectName,
			isStart: true,
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
	window.onbeforeunload = function(event) {
		var params = {
				browseId: uuid,
				title: document.title,
				userId: getCookie('userId'),
				pageUrl: document.URL,
				projectId: projectId,
				projectName: projectName,
				isStart: false,
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
	function getCookie(name){
		var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
		if(arr=document.cookie.match(reg))
		return unescape(arr[2]);
		else
		return null;
	}
	function uuid(len) {
	    var chars = '0123456789abcdef'.split('');
	    var uuid = [], i;
	    radix = chars.length;
	    if (len) {
	      for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random()*radix];
	    } else {
	      var r;
	      uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
	      uuid[14] = '4';
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

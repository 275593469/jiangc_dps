$(function(){

	getUserLog();
	var maxTime;
	window.setInterval(function(){
		getUserLog();
	}, 60*1000);
	
	//获取用户日志信息
	function getUserLog(operationTime){
		$.ajaxFunction({ 
			url: getContextPath()+"/log/getUserLog",
			type: 'post',
			dataType: 'json',
			data: {time: operationTime},
			success: function(data){
				if(data.code == 'success'){
					var div = '';
					var timestampnow = new Date(data.result.nowDate).getTime();
					$.each(data.result.logs,function(i, each){
						var date = each.operation_time;
						var timestamp = new Date(date).getTime();
						var timeminute = Math.ceil((timestampnow-timestamp)/(1000*60));
						var minute = "分钟前";
						if(timeminute >= 60){
							timeminute = Math.ceil(timeminute/60);
							minute = "小时前";
						}
						if(i==0){
							maxTime = new Date(date);//最近的消息的时间
						}
						div += '<div class="alert alert-info">'
							+'<strong><font><font>消息！</font></font></strong><font><font>'+each.nick_name+' '+each.operation+'。'
							+'<span style="float:right"><font><font class="minute">'+timeminute+'</font>'+minute+'</font></span>'
							+'</font></font>'
							+'</div>';
					});
					$("#big_stats").html(div);
				}
					
			}
		});
	}
	
});
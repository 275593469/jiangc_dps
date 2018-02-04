$(function(){
	
	$("#fileUpload").click(function(){
		uploadLog();
	});
	
	//日志文件处理
	function uploadLog(){
//		var formData = new FormData($("#uploadform")[0]);
//		console.log(formData)
////		var formData = new FormData();
//		$.ajaxFunction({
//			url: getContextPath()+"/log/uploadLogs",
//			type: 'post',
//			processData: false,
//			dataType: false,
//			contentType: 'multipart/form-data',
//			data: formData,
//			success: function(data){
//				if(data.code == 'success'){
//	          		showResult(data);
//	          	}
//			}
//		});
		var startTime = new Date().getTime();
		
		$.ajaxFileUpload({
            url: getContextPath()+'/log/uploadLogs', //用于文件上传的服务器端请求地址
            secureuri: false, //是否需要安全协议，一般设置为false
            fileElementId: 'fileLogs', //文件上传域的ID
            dataType: "text/html", //返回值类型 一般设置为json, 但ie9不支持json
	        success: function (data) {
	        	var endTime = new Date().getTime();
	        	var time = (endTime-startTime)/1000+"s";
	        	$("#showTime").removeClass("hide");
	        	$("#useTime").text(time);
	        	
	          	data = eval('('+data+')');  //转为json对象    
	          	//展示处理结果
	          	if(data.code == 'success'){
	          		showResult(data);
	          	}
	        }, 
	        error:function(data){
	        }
        });
		return false;
	}
	
	//显示处理结果
	function showResult(data){
		var handles = data.result.handle;
		var browses = data.result.browse;
		var handleChart = [];
		var browseChart = [];
		for(var key in handles){
			var map = {};
			map.name = key;
			map.value = handles[key];
			map.color = getColor();
			handleChart.push(map);
		}
		for(var key in browses){
			var map = {};
			map.name = key;
			map.value = browses[key];
			map.color = getColor();
			browseChart.push(map);
		}
		$("#showReports").removeClass('hide');
		//绘制图表
		drawHandleColumn3D(handleChart);
		drawBrowseColumn3D(browseChart);
	}
	
	function drawHandleColumn3D(data){
//		var div = '<div class="span6">'
//						+'<div class="widget-header">'
//							+'<i class="icon-list-alt"></i>'
//							+'<h3>用户操作日志统计</h3>'
//						+'</div>'
//						+'<div id="handle_chart" class="chart-holder"></div>'
//					+'</div>';
//		$("#showReports").append(div);
		var line = new iChart.Column3D({
			render : 'handle_chart',
			data: data,
			title : '导入数据的用户操作记录统计',
			width : 1000,
			height : 450,
			align:'left',
			offsetx:50,
			legend : {
				enable : true
			},
			sub_option:{
				label:{
					color:'#111111'
				}
			},
			coordinate:{
				width:700,
				scale:[{
					 position:'left',	
					 start_scale:0,
					 end_scale:40,
					 scale_space:{
							parseText:function(t,x,y){
								return t/10;
							}
						},
					 listeners:{
						parseText:function(t,x,y){
							return {text:t}
						}
					}
				}]
			}
		});
	
		//开始画图
		line.draw();
	}
	
	function drawBrowseColumn3D(data){
//		var div = '<div class="span6">'
//						+'<div class="widget-header">'
//							+'<i class="icon-list-alt"></i>'
//							+'<h3>用户浏览日志统计</h3>'
//						+'</div>'
//						+'<div id="browse_chart" class="chart-holder"></div>'
//						+'</div>';
//		$("#showReports").append(div);
		var line = new iChart.Column3D({
			render : 'browse_chart',
			data: data,
			title : '导入数据的用户浏览记录统计',
			width : 1000,
			height : 450,
			align:'left',
			offsetx:50,
			legend : {
				enable : true
			},
			sub_option:{
				label:{
					color:'#111111'
				}
			},
			coordinate:{
				width:700,
				scale:[{
					 position:'left',	
					 start_scale:0,
					 end_scale:40,
					 scale_space:{
							parseText:function(t,x,y){
								return t/10;
							}
						},
					 listeners:{
						parseText:function(t,x,y){
							return {text:t}
						}
					}
				}]
			}
		});
	
		//开始画图
		line.draw();
	}
	
	
});
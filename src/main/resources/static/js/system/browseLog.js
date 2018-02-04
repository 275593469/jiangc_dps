$(function(){
	var categorys = []; 
	
	$('#userlog_table').dataTable({
		language: {
            aria: {
                sortAscending: ": activate to sort column ascending",
                sortDescending: ": activate to sort column descending"
            },
            Processing: "处理中...",
            emptyTable: "暂无数据",
            info: "总共 _TOTAL_ 条数据",
            infoEmpty: "暂无数据",
            infoFiltered: "(filtered1 from _MAX_ total records)",
            lengthMenu: "显示 _MENU_",
            search: "根据用户昵称搜索:",
            zeroRecords: "No matching records found",
            paginate: {
                previous: "上一页",
                next: "下一页",
                last: "最后一页",
                first: "第一页"
            }
        },
        "lengthMenu": [10, 20, 30],//页面显示的数据量
        "autoWidth": false, //禁用自动调整列宽 
        "processing": true, 
        "serverSide": true,//开启服务器模式
//        "bInfo": false,//启用或禁用表信息显示
        "bPaginate": true,//true 开启分页功能，如果不开启，将会全部显示   
		"bFilter": true, // true 开启过滤功能
        "bSort" : false,// 排序
        "bDeferRender": true,//延期渲染
        "aoColumns": [
             {data: 'nickName',title: '用户昵称'},
             {data: 'pageTitle',title: '页面标题'},
             {data: 'pageUrl',title: '页面路径'},
             {data: 'beginTime',title: '开始时间'},
             {data: 'endTime',title: '结束时间'},
             {data: 'projectName',title: '项目名'}
        ], //显示字段
	    "ajax": getContextPath()+'/log/getBrowseLogPage'
	   });
	
	
	getProject();
	//获取监测项目分类
	function getProject(){
		$.ajaxFunction({ 
			url: getContextPath()+"/log/getProject",
			type: 'post',
			dataType: 'json',
			success: function(data){
				if(data.code == 'success'){
					projects = data.result;
					var option = '';
					for(var i in projects){
						option += '<option value="'+projects[i].projectId+'">'+projects[i].projectName+'</option>';
					}
					$("#select_log").html(option)
					reportHandleLog(projects[0].projectId, projects[0].projectName);
				}
			}
		});
	}
	//切换日志统计的分类
	$("#select_log").change(function(){
		var operation= $(this).val();
		var projectName = $(this).text();
		if(operation != ''){
			reportHandleLog(operation, projectName);
		}else{
			reportHandleLog(operation, projectName);
		}
	});
	//用户日志统计
	function reportHandleLog(projectId, projectName){
		$.ajaxFunction({ 
			url: getContextPath()+"/log/reportHandleByTitle",
			type: 'post',
			dataType: 'json',
			data: {projectId: projectId},
			success: function(data){
				if(data.code == 'success'){
					var dataChart = [];
					var results = data.result;
					for(var i in results){
						var map = {};
						var pageTitle = results[i].pageTitle
						map.name = pageTitle;
						map.value = results[i].count;
						map.color = getColor();
						dataChart.push(map);
					}
					//绘制图表
					drawColumn3D(dataChart, projectName);
				}
			}
		});
	}
	
	function drawColumn3D(data, projectName){
		var line = new iChart.Column3D({
			render : 'log_chart',
			data: data,
			title : projectName+'项目的用户浏览记录统计',
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
					 scale_space:8,
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
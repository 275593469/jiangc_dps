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
            search: "搜索操作人:",
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
             {data: 'nickName',title: '操作人'},
             {data: 'operation',title: '操作内容'},
             {data: 'result',title: '操作结果'},
             {data: 'operationTime',title: '时间'},
             {data: 'ip',title: 'ip'}
        ], //显示字段
	    "ajax": getContextPath()+'/log/getUserLogPage'
	   });
	
	
	getUserLogCategory();
	//获取用户日志分类
	function getUserLogCategory(){
		$.ajaxFunction({ 
			url: getContextPath()+"/log/getUserLogCategory",
			type: 'post',
			dataType: 'json',
			success: function(data){
				if(data.code == 'success'){
					categorys = data.result;
					var option = '<option value="">全部</option>';
					for(var i in categorys){
						option += '<option value="'+categorys[i].operation+'">'+categorys[i].operation+'</option>';
					}
					$("#select_log").html(option)
					reportUserLog(undefined,categorys);
				}
			}
		});
	}
	//切换日志统计的分类
	$("#select_log").change(function(){
		var operation= $(this).val();
		if(operation != ''){
			var params = [{operation:operation}];
			reportUserLog(operation, params);
		}else{
			reportUserLog(operation, categorys);
		}
	});
	//用户日志统计
	function reportUserLog(operation, categorys){
		$.ajaxFunction({ 
			url: getContextPath()+"/log/reportUserLog",
			type: 'post',
			dataType: 'json',
			data: {operation: operation},
			success: function(data){
				if(data.code == 'success'){
					var dataChart = [];
					var results = data.result;
					var labels = ["0时","1时","2时","3时","4时","5时","6时","7时","8时","9时","10时","11时","12时","13时","14时","15时","16时","17时","18时","19时","20时","21时","22时","23时"]; 
					var maxCount = 0;
					for(var num in categorys){
						var name = categorys[num].operation;
						var values = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];
						for(var i=0;i<results.length;i++){
							if(name == results[i].operation){
								if(Number(results[i].count) > maxCount){
									maxCount = Number(results[i].count);
								}
								values[Number(results[i].operationTime)] = Number(results[i].count);
							}
						}
						var map = {
								name:name,
								value:values,
								color:getColor(),
								line_width:2
						};
		         		
		         		dataChart[num] =map;
					}
					drawChart(dataChart, labels, maxCount+maxCount/2);
				}
			}
		});
	}
	
	function drawChart(data, labels, maxCount){
		var line = new iChart.LineBasic2D({
			render : 'log_chart',
			data: data,
			align:'center',
			title : '用户24小时的操作日志',
			subtitle : '用户单位时间内访问次数',
			footnote : '数据来源：DPS监控',
			animation:true,//是否开启动画过渡
			width : 900,
			height : 400,
			tip:{
				enable:true,
				shadow:true
			},
			legend : {
				enable : true,
				row:1,//设置在一行上显示，与column配合使用
				column : 'max',
				valign:'top',
				sign:'bar',
				background_color:null,//设置透明背景
				offsetx:-80,//设置x轴偏移，满足位置需要
				border : true
			},
			crosshair:{
				enable:true,
				line_color:'#62bce9'
			},
			sub_option : {
				label:false,
				point_hollow : false
			},
			coordinate:{
				width:800,
				height:240,
				axis:{
					color:'#9f9f9f',
					width:[0,0,2,2]
				},
				grids:{
					vertical:{
						way:'share_alike',
				 		value:5
					}
				},
				scale:[{
					 position:'left',	
					 start_scale:0,
					 end_scale:maxCount,
					 scale_space:maxCount/10,
					 scale_size:2,
					 scale_color:'#9f9f9f'
				},{
					
					 position:'bottom',	
					 labels:labels
				}]
			}
		});
	
		//开始画图
		line.draw();
	}
});
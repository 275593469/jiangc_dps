function getContextPath(){
	var pathName = document.location.pathname; 
	var index = pathName.substr(1).indexOf("/"); 
	var result = pathName.substr(0,index+1); 
	return result;
}

$.ajaxFunction = function(json){
	$.ajax({
        url: json.url,		
        dataType: json.dataType,
        processData: json.processData,
        type: json.type,
        async: json.async,
        contentType: json.contentType,
        timeout: 40000,
        data: json.data,
        beforeSend:json.beforeSend,
        success: json.success,
        complete: json.complete,
    	error: function(data){
			toastr.error("系统错误，请稍后再试！");
		}
    });
}

var colors = ['#0d8ecf','#ef7707','#62bce9','#9f9f9f','#f68f70'];
getColor = function(i){
	if(i==undefined || i==''){
		i = Math.floor(Math.random()*colors.length);
	}
	return colors[i%(colors.length)];
}

//创建datatable表格
$.createTable = function (id, columns, data){
	$('#'+id).dataTable({
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
		},
		"destroy":true, //破坏旧表
		"autoWidth": false, //禁用自动调整列宽 
		"processing": true, 
		"serverSide": false,//开启服务器模式
		//"bInfo": false,//启用或禁用表信息显示
		"bPaginate": false,//true 开启分页功能，如果不开启，将会全部显示   
		"bFilter": false, // true 开启过滤功能
		"bSort" : false,// 排序
		"bDeferRender": true,//延期渲染
		"aoColumns": columns, //显示字段
		"data": data  //数据
	});
}

//创建datatable分页表格
$.createTablePage = function(id, columns, url){
	$('#'+id).dataTable({
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
        "destroy":true, //破坏旧表
        "lengthMenu": [10, 20, 30],//页面显示的数据量
        "autoWidth": false, //禁用自动调整列宽 
        "processing": true, 
        "serverSide": true,//开启服务器模式
//        "bInfo": false,//启用或禁用表信息显示
        "bPaginate": true,//true 开启分页功能，如果不开启，将会全部显示   
		"bFilter": true, // true 开启过滤功能
        "bSort" : false,// 排序
        "bDeferRender": true,//延期渲染
        "aoColumns": columns, //显示字段
	    "ajax": url
	   });
}

$(function(){
	
});
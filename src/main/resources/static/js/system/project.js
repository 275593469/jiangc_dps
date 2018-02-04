$(function(){
	var projectReg = /[@#\$%\^&\*]+/g;
	var ipReg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
	var portReg = /^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/;
	
	$('#projects_table').dataTable({
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
             {data: 'projectName',title: '项目名'},
             {data: 'stationIp',title: '服务器地址'},
             {data: 'createTime',title: '创建时间'},
             {data: 'updateTime',title: '修改时间'},
             {data: 'nickName',title: '项目管理员'},
             {data: 'status',title: '状态'},
             {data: 'download',title: '下载js监测脚本'}
        ], //显示字段
	    "ajax": getContextPath()+'/log/getProjectPage'
	   });
	
	
	
	//添加监测项目
	$("#addProject").click(function(){
		var projectName = $("#projectName").val();//项目名
		var serverIp = $("#serverIp").val();//服务器ip
		var port = $("#port").val();//端口号
		
		if(checkProject(projectName) && checkIp(serverIp) && checkPort(port)){
			addProject(projectName, serverIp, port);//添加监测项目名
		}
	});
	
	$(".inputbg input").focus(function(){
		$(this).parent().next(".err_tip").css("display","none");
	});
	
	function addProject(projectName, serverIp, port){
		$.ajaxFunction({
			url: getContextPath()+"/log/addProject",
			type: 'post',
			dataType: 'json',
			data: {
				projectName: projectName,
				serverIp: serverIp,
				port: port
			},
			success: function(data){
				if(data.code == "success"){
					$("#resetAdd").removeClass("hide");
					$("#resetAdd").click(function(){
						$(".inputbg input").val("");
						$(this).addClass("hide");
					});
					
					toastr.success(data.message);
				}else{
					toastr.warning(data.message);
				}
				
			}
		});
	}
	
	//校验项目名
	function checkProject(projectName){
		if(projectName == ''){
			$("#nameError div span").html("项目名不能为空");
			$("#nameError").css("display","");
			return false;
		}else if(projectName.length > 50){
			$("#nameError div span").html("项目名过长");
			$("#nameError").css("display","");
			return false;
		}else if(projectReg.test(projectName)){
			$("#nameError div span").html("项目名包含非法字符");
			$("#nameError").css("display","");
			return false;
		}else{
			return true;
		}
	}
	
	//校验ip格式
	function checkIp(serverIp){
		if(serverIp ==''){
			$("#ipError div span").html("ip不能为空");
			$("#ipError").css("display","");
			return false;
		}else if(!ipReg.test(serverIp)){
			$("#ipError div span").html("ip格式错误");
			$("#ipError").css("display","");
			return false;
		}else{
			return true;
		}
	}
	
	//校验端口格式
	function checkPort(port){
		if(port == ''){
			$("#portError div span").html("端口不能为空");
			$("#portError").css("display","");
			return false;
		}else if(!portReg.test(port)){
			$("#portError div span").html("端口格式错误");
			$("#portError").css("display","");
			return false;
		}else{
			return true;
		}
	}
	
});
$(function(){
	var roles = '';
	getRoles();
	$("a[href=#roleManage]").click(function(){
		$("#userManage .updateSpan").addClass('hide');
		getRoles();
		resetRole();
	});
	$("a[href=#userManage]").click(function(){
		getUserInfo();
	});
	
	var roleColumns = [
           {data: 'role_id',title: '角色id'},
           {data: 'role_name',title: '角色名'},
           {data: 'permission',title: '权限'},
           {data: 'update',title: '操作'},
           ];
	
	//获取角色
	function getRoles(){
		$.ajaxFunction({
			url: getContextPath()+"/role/getRolesAndPermission",
			type: 'post',
			dataType: 'json',
			success: function(data){
				var roleData = [];
				if(data.code == 'success'){
					var result = data.result;
					roles = result;
					for(var i in result){
						result[i].update = '<a class="updateRole" href="##" roleId="'+result[i].role_id+'"><i class="icon-edit"></i>修改</a>';
					}
					roleData = result;
				}
				$.createTable("role_table", roleColumns, roleData);
			}
		});
	}
	
	//获取人员信息
	function getUserInfo(){
		var userColumns = [
		                   {data: 'select',title: '<input type="checkbox" class="usercheck" value="all" />'},
		                   {data: 'user_name',title: '账号'},
		                   {data: 'nick_name',title: '昵称'},
		                   {data: 'email',title: '邮箱'},
		                   {data: 'address',title: '地址'},
		                   {data: 'create_time',title: '创建时间'},
		                   {data: 'update_time',title: '修改时间'},
		                   {data: 'role_name',title: '角色'},
		                   ];
		var url = getContextPath()+"/user/getUserPage";
		$.createTablePage("user_table", userColumns, url);
	}
	
	//获取资源信息
	$("#addRole").click(function(){
		getResources();
	});
	
	//获取资源列表
	function getResources(){
		$.ajaxFunction({
			url: getContextPath()+"/role/getResources",
			type: 'post',
			dataType: 'json',
			success: function(data){
				if(data.code == 'success'){
					categorys = data.result;
					var option = '<option value="">请选择</option>';
					option += '<option value="all">全部</option>';
					for(var i in categorys){
						option += '<option value="'+categorys[i].resource_id+'">'+categorys[i].resource_name+'</option>';
					}
					$("#resource").html(option)
				}
			}
		});
	}
	
	$("#resource").change(function(){
		var resourceId = $(this).val();
		if(resourceId == ''){
			return;
		}
		var name = $("#resource option[value="+resourceId+"]").text();
		var div = '<div class="alert alert-info" resourceId="'+resourceId+'">'
            			+'<button type="button" class="close" data-dismiss="alert"><font><font>×</font></font></button>'
            			+'<font><font>'+name+'</font></font></div>';
		if(resourceId == 'all'){
			$("#permission").html(div);
			return;
		}
		var ids = [];
		$.each($("#permission .alert"),function(i,data){
			ids.push($(data).attr("resourceId"));
		});
		if(ids[0] == 'all'){
			$("#permission").html(div);
			return;
		}
		if(ids.toString().indexOf(resourceId) == -1)
			$("#permission").append(div);
	});
	
	//保存角色
	$("#saveRole").click(function(){
		var url = '';
		var roleId = $("#roleId").val();
		if(roleId == ''){
			url = getContextPath()+"/role/addRole";
		}else{
			url = getContextPath()+"/role/updateRole";
		}
		
		var roleName = $("#roleName").val();
		var permission = '';
		$.each($("#permission .alert"),function(i,data){
			permission += $(data).attr("resourceId")+":";
		});
		permission = permission.substr(0,permission.length-1);
		$.ajaxFunction({ 
			url: url,
			type: 'post',
			dataType: 'json',
			data: {
				roleId: roleId,
				roleName: roleName,
				permission: permission
			},
			success: function(data){
				if(data.code == 'success'){
					if(roleId != ''){//修改角色
						$("a[href=#roleManage]").trigger("click");
						toastr.success(data.message);
						return;
					}
					toastr.success(data.message);
					resetRole();
				}else{
					toastr.warning(data.message);
				}
			}
		});
	});
	
	//重置
	$("#resetRole").click(function(){
		var roleId = $("#roleId").val();
		if(roleId == ''){//添加角色
			resetRole();
		}else{//修改角色  重置
			$.ajaxFunction({
				url: getContextPath()+"/role/getRoleById",
				type: 'post',
				dataType: 'json',
				data: {roleId: roleId},
				success: function(data){
					if(data.code == 'success'){
						var result = data.result[0];
						$("#roleName").val(result.role_name);
						
						var permission = result.permission;
						var div = '';
						if(permission == 'all'){
							div = '<div class="alert alert-info" resourceId="all">'
	            			+'<button type="button" class="close" data-dismiss="alert"><font><font>×</font></font></button>'
	            			+'<font><font>全部</font></font></div>';
						}else{
							for(var key in permission){
								div += '<div class="alert alert-info" resourceId="'+key+'">'
		            			+'<button type="button" class="close" data-dismiss="alert"><font><font>×</font></font></button>'
		            			+'<font><font>'+permission[key]+'</font></font></div>';
							}
						}
						$("#permission").html(div);
					}
				}
			});
		}
	});
	
	//添加角色重置
	function resetRole(){
		$("#roleId").val('');
		$("#roleName").val('');
		$("#permission").html('');
	}
	
	$(document).on('click','.updateRole',function(){
		var _this = this;
		var roleId = $(this).attr('roleId');
		$.ajaxFunction({
			url: getContextPath()+"/role/getRoleById",
			type: 'post',
			dataType: 'json',
			data: {roleId: roleId},
			success: function(data){
				if(data.code == 'success'){
					//显示修改角色页面
					$("#roleManage").removeClass("active");
					$("#addRoleTab").addClass("active");
					
					var result = data.result[0];
					$("#addRoleTab .roleTag").html("修改角色");
					$("#roleId").val(roleId);
					$("#roleName").val(result.role_name);
					
					var permission = result.permission;
					var div = '';
					if(permission == 'all'){
						div = '<div class="alert alert-info" resourceId="all">'
            			+'<button type="button" class="close" data-dismiss="alert"><font><font>×</font></font></button>'
            			+'<font><font>全部</font></font></div>';
					}else{
						for(var key in permission){
							div += '<div class="alert alert-info" resourceId="'+key+'">'
	            			+'<button type="button" class="close" data-dismiss="alert"><font><font>×</font></font></button>'
	            			+'<font><font>'+permission[key]+'</font></font></div>';
						}
					}
					$("#permission").html(div);
					
					getResources();
				}
			}
		});
	});
	
	//修改角色
	function update(roleId, roleName, permission){
		var params = {
				roleId: roleId,
				roleName: roleName,
				permission: permission
		};
		$.ajaxFunction({
			url: getContextPath()+"/role/updateRole",
			type: 'post',
			dataType: 'json',
			data: params,
			success: function(data){
				if(data.code == 'success'){
					$(".nav-tabs a[href=#roleManage]").trigger('click');
				}else{
					toastr.warning(data.message);
				}
			}
		});
	}
	
	//用户全选
	$(document).on('click', '.usercheck', function(){
		var checkValue = $(this).val();
		if(checkValue == 'all'){
			if (this.checked) {//全选
				$(".usercheck").prop("checked", true);
			}else{//全不选
				$(".usercheck").prop("checked", false);
			}
		}
	});
	
	//修改用户的角色
	$("#userManage .updateUserRole").click(function(){
		$("#userManage .updateSpan").removeClass('hide');
		var div = '<option value="">请选择</option>';
		for(var i in roles){
			div += '<option value="'+roles[i].role_id+'">'+roles[i].role_name+'</option>';
		}
		$("#userManage .role").html(div);
		
	});
	//点击保存修改
	$("#userManage .save").click(function(){
		var userIds = [];
		$(".usercheck:checked").each(function(){
			var userId = $(this).val();
			if(userId != 'all'){
				userIds.push(userId);
			}
		});
		
		if(userIds.length <= 0){
			toastr.info("请选择需要修改角色的人员");
			return;
		}
		var roleId = $("#userManage .role").val();
		if(roleId == ''){
			toastr.info("请选择角色");
			return;
		}
		var params = {
				userIds: userIds.toString(),
				roleId:roleId
		};
		$.ajaxFunction({
			url: getContextPath()+"/user/updateUserRole",
			type: 'post',
			dataType: 'json',
			data: params,
			success: function(data){
				if(data.code == 'success'){
					toastr.success(data.message);
					$(".usercheck").prop("checked",false);
					$("#userManage .updateSpan").addClass('hide');
					getUserInfo();
				}else{
					toastr.warning(data.message);
				}
			}
		});
	});
	//点击取消修改
	$("#userManage .cancel").click(function(){
		$(".usercheck").prop("checked",false);
		$("#userManage .updateSpan").addClass('hide');
	});
	
});

$(document).ready(function(){
	function getContextPath(){
		var pathName = document.location.pathname; 
		var index = pathName.substr(1).indexOf("/"); 
		var result = pathName.substr(0,index+1); 
		return result;
	}
	
	//滚动条自适应
	$('#tree-3-1,#tree-3-2').css('height',document.body.clientHeight-163);
	(function(){
		var selectorId = $.cookie("treeActive");
		if(selectorId != undefined){
			$(".tabbable-line .active").removeClass("active");
			$(".tabbable-line .nav-tabs a[href='"+selectorId+"']").parent().addClass("active");
			$(selectorId).addClass("active");
		}
		$(document).on('click', '.tabbable-line .nav-tabs li', function(){
			var selectorId = $(this).find("a").attr("href");
			$.cookie("treeActive", selectorId, { path: "/"});
		});

		
		//查询模块配置的更多元素
		$.ajax({
			url: getContextPath()+"/page/module/modules",
			type: 'get',
			dataType: 'json',
			data: {
				type:'more',
				status:1, //0,显示，1，隐藏
				time:new Date().getTime()
			},
			success: function(data){
				if(data.IOM_List.length > 0 && data.IOM_List.length <4){//隐藏元素
					var hideIds = data.IOM_List;
					for(var i in hideIds){
						var moduleId = hideIds[i].moduleId;
						switch(moduleId){
							case 'M00007': $("#Mmore li").eq(0).addClass("hide");
							break;
							case 'M00008': $("#Mmore li").eq(1).addClass("hide");
							break;
							case 'M00009': $("#Mmore li").eq(2).addClass("hide");
							break;
							case 'M000010': $("#Mmore li").eq(3).addClass("hide");
							break;
						}
					}
				}else if(data.IOM_List.length >= 4){
					$("#Mmore").addClass("hide");
				}
			}
		});
	})();
	
	//全局封装ajax
	var ajaxtate;
	var angelastate="";
	var lasturl="";
	$.ajaxFunction = function(json) {
//		if(angelastate=="1" && lasturl==json.url){ajaxstate.abort();}
		ajaxstate = $.ajax({
	        url: json.url,		
	        dataType: json.dataType,
	        type: json.type,
	        async: json.async,
	        contentType: json.contentType,
	        timeout: 40000,
	        data: json.data,
	        beforeSend:json.beforeSend,
	        success: json.success,
	        complete: json.complete,
	        error: json.error
	    });
		return ajaxstate;
	};
	//封装文件上传ajax
	$.ajaxUpload = function(json){
	    $.ajax({
	        url: json.url,		
	        dataType : json.dataType,
	        type: json.type,
	        async: json.async,
	        contentType: json.contentType,
	        processData: json.processData,
	        mimeType: json.mimeType,
	        data: json.data,
	        beforeSend:json.beforeSend,
	        success: json.success,
	        error: json.error
	    });
	};

	$(function(){
		if(!placeholderSupport()){   // 判断浏览器是否支持 placeholder
//			var placeholder = $("input[placeholder]").val();
//			$("input[placeholder]").after('<label class="placeholder" style="opacity:0.6;position:relative;z-index:10;top:-26px;left:13px;">'+placeholder+'</label> ').
			$("input[placeholder]").val('');
			$("input[placeholder]").removeAttr("placeholder");
			
//			$("input[placeholder]").focus(function(){
//				$(this).next(".placeholder").addClass("hide");
//			});
//			$("input[placeholder]").blur(function(){
//				if($(this).val() == ''){
//					$(this).next(".placeholder").removeClass("hide");
//				}
//			});
		};
		})
		function placeholderSupport() {
		    return 'placeholder' in document.createElement('input');
		}
	
	//判断是否具有打开“机构赋权”的权限
		var cloudCreator = $.cookie('IOM.cloudCreator');
		var userId = $.cookie('IOM.userKey');
		if(cloudCreator == userId && cloudCreator != '' && userId != ''){
			$(".organLimit a").attr('href', '../app/organ.html');
		}else{
			$(".organLimit").addClass('hide');
		}
	
	/*职系*/
	$(".gradeEdit").click(function(){
		$(".gradeCon").hide();
		$(".gradeHidden").show();
	});
//	$(".gradeSave").click(function(){
//		$(".gradeCon").show();
//		$(".gradeHidden").hide();
//	});
	$(".gradeDie").click(function(){
		$(".gradeCon").show();
		$(".gradeHidden").hide();
	});
	$(".gradeChildDie").click(function(){
		$("#stackgrade").hide();
	});
	$(".gradeChildSave").click(function(){
		$("#stackgrade").hide();
	});
	/*职位*/
	$(".jobEdit").click(function(){
		$(".jobHidden").show();
		$(".job").hide();
	});
	$(".jobSave").click(function(){
		$(".jobHidden").hide();
		$(".job").show();
	});
	$(".jobDie").click(function(){
		$(".jobHidden").show();
		$(".job").hide();
	});
	$(".jobChildSave").click(function(){
		$("#large").hide();
	});
	$(".jobChildDie").click(function(){
		$("#large").hide();
	});
	/*职称*/
	$(".rankEdit").click(function(){
		$(".RankHidden").show();
		$(".rank").hide();
	});
	$(".rankSave").click(function(){
		$(".RankHidden").hide();
		$(".rank").show();
	});
	$(".rankDie").click(function(){
		$(".RankHidden").show();
		$(".rank").hide();
	});
	$(".rankChildDie").click(function(){
		$("#stackRank").hide();
		$(".RankHidden").hide();
		$(".rank").show();
	});
	$(".rankChildSave").click(function(){
		$("#stackRank").hide();
		$(".RankHidden").hide();
		$(".rank").show();
	});
	/*人员*/
	$(".personRevise").click(function(){
		$(".personChange").show();
		$(".person1").hide();
		$(".personHidden").hide();
	});
	$(".personEdit").click(function(){
		$(".personChange").hide();
		$(".person1").hide();
		$(".personHidden").show();
	});
	/*$(".personSave").click(function(){
		$(".jobChange").hide();
		$(".person").show();
		$(".personHidden").hide();
	});
	$(".personDie").click(function(){
		$(".jobChange").hide();
		$(".person").show();
		$(".personHidden").hide();
	});*/
	
	//查看详情
	$(document).on('click', '#sample_1 a[userId],#sample_2 a[userId]', function(){
		
		userId = $(this).attr("userId");
		location.href = getContextPath()+"/views/v2/member/details.html?"+userId;
	});
	$(document).on('click', '.member-detail', function(){
		
		userId = $(this).attr("userId");
		location.href = getContextPath()+"/views/v2/member/details.html?"+userId;
	});
	
	//登录用户查看详情
	$(document).on('click', '.dropdown-user a', function(){
		userId = $.cookie('IOM.userKey');
		var url = location.href;
		if(url.substr(url.indexOf('?')+1) == userId){
			return;
		}
		location.href = getContextPath()+"/views/v2/member/details.html?"+userId;
	});
	
	//获取登录用户信息
	(function(){
		var userId = $.cookie('IOM.userKey');
		var data = sessionStorage.userInfo;
		if(data != undefined && data != ''){
			data = JSON.parse(data);
			$('.dropdown-user a .img-circle').attr("src", data.faceUrl);
			$('.dropdown-user a .img-circle').attr("onerror", "this.src='"+sessionStorage.path+"/IMG_FOLDER/user/headImg/default.png';this.onerror=null");
			$('.dropdown-user a span').html(data.userName);
			return;
		}
		$.ajax({
			url: getContextPath()+"/page/member/getUserInfo",
			type: 'post',
			dataType: 'json',
			data: {
				userId: userId
			},
			success: function(data){
				$('.dropdown-user a .img-circle').attr("src", data.faceUrl);
				$('.dropdown-user a .img-circle').attr("onerror", "this.src='"+sessionStorage.path+"/IMG_FOLDER/user/headImg/default.png';this.onerror=null");
				$('.dropdown-user a span').html(data.userName);
				sessionStorage.userInfo = JSON.stringify(data);
			}
		});
	})();
	
	//跳转到首页
	$(document).on('click', '.page-logo a', function(){
		$(this).attr('href','#');
		location.href = getContextPath()+"/views/templete/iom.html";
	});
	
	/*$("#changeContent").click(function(){
	$('#changeContent').parent().parent().hide();
	$("#editCon").show();
	});
	$("#modify").click(function(){
		$('#modify').parent().parent().hide();
		$("#con-two").show();
	});
	$("#editor").click(function(){
		$(this).parent().parent().hide();
		$("#con").show();
	});
	$("#add").click(function(){
		$(this).parent().parent().hide();
		$("#dis").show();
	});
	$(".two,.three").click(function(){
		$('#editCon').hide();
		$("#changeContent").parent().parent().show();
	});
	$(".two,.three").click(function(){
		$('#con-two').hide();
		$("#modify").parent().parent().show();
	});
	选中样式
	$(".circle").click(function(){
	$(this).toggleClass('circle-select');
	});
	文本显示
	$('.form-control').focus(function(){
	    $('.tips').show();
		}).blur(function(){
	    $('.tips').hide();
	});
	$("#edit").click(function(){
		$('#edit').next().hide();
		$("#dis").show();
		$(".text").hide();
	});
	$("#com").click(function(){
	$('#com').next().next().toggleClass('hidden');
	$('#com').next().toggleClass('hidden');
	});
	$(".revise").click(function(){
	$('#edit').next().hide();
	$("#dis").hide();
	$(".text").show();
	});
	$(".compile").click(function(){
	$(".card-icon").children().removeClass('del-hide');	
	$(".cloud-icon").children().removeClass('del-hide');
	$(".yellow-icon").children().removeClass('del-hide');
	$(this).parent().siblings().find(".radio-circle").addClass('del-select');
	$(this).parent().siblings().find(".radio-circle").addClass('circle-select');
	$(".centered").removeClass('hide');
	
		$(".pd5").click(function(){
			$(this).parent().parent('div').remove();
		});
	});
	$("#popup").click(function(){
	$(this).next().removeClass('text');
	});
	单选样式
	$(".radio-circle").click(function(){
		$(this).addClass('circle-select');
		$(this).parent().siblings().find("i.radio-circle").removeClass('circle-select');
	});
	$(".tab_ed").click(function(){
	$(this).parent().find(".text").addClass('show');
	$(this).siblings().addClass('text');
	});
	//获取class为caname的元素 
	$(".caname").click(function() { 
	var td = $(this); 
	var txt = td.text(); 
	var input = $("<input type='text'value='" + txt + "'/>"); 
	td.html(input); 
	input.click(function() { return false; }); 
	//获取焦点 
	input.trigger("focus"); 
	//文本框失去焦点后提交内容，重新变为文本 
	input.blur(function() { 
	var newtxt = $(this).val(); 
	//判断文本有没有修改 
	if (newtxt != txt) { 
	td.html(newtxt); 
	 
	*不需要使用数据库的这段可以不需要 
	var caid = $.trim(td.prev().text()); 
	//ajax异步更改数据库,加参数date是解决缓存问题 
	var url = "../common/Handler2.ashx?caname=" + newtxt + "&caid=" + caid + "&date=" + new Date(); 
	//使用get()方法打开一个一般处理程序，data接受返回的参数（在一般处理程序中返回参数的方法 context.Response.Write("要返回的参数");） 
	//数据库的修改就在一般处理程序中完成 
	$.get(url, function(data) { 
	if(data=="1") 
	{ 
	alert("该类别已存在！"); 
	td.html(txt); 
	return; 
	} 
	alert(data); 
	td.html(newtxt); 
	}); 
	 
	} 
	else 
	{ 
	td.html(newtxt); 
	} 
	}); 
	}); 

	$(".role-app-group .cloud-icon").click(function(){
		var _sibling = $(this).siblings(".app-con-down");
		var _parent = $(this).parents(".role-app-group");
		console.log(_sibling);
	   // console.log(_parent);

		if(_sibling.is(":hidden")){
			$(".role-app-group").removeClass("open");
			_parent.addClass("open");
			$(".tab-content").addClass("mb200");
		}
		else{
			_parent.removeClass("open");
			$(".tab-content").removeClass("mb200");
		}
	//$(this).closest(".role-app-group").removeClass("open");
	});
	配置管理 职系
	$(".gradeSave").click(function(){
		$(this).parent().removeClass("show");
		$(this).parent().prev().show();
	});
	$("#personEd").click(function(){
		$(this).parent().addClass("show");
		$(this).parent().prev().hide();
	});
	$(".tab_ed").click(function(){
		$(".gradeCon").removeClass("show");
		$(".gradeCon").hide();
	});
	配置管理 职称
	$(".rankSave").click(function(){
		$(this).parent().removeClass("show");
		$(this).parent().prev().show();
	});
	$(".tab_ed").click(function(){
		$(".GrankCon").removeClass("show");
		$(".GrankCon").hide();
	});
	
	配置管理 人员扩展信息
	$(".personRevise").click(function(){
		$(".content-one-container").css("display","none");
		$(".jobChange").css("display","block");
		$(".jobChange").removeClass("hidden");
	});
	$(".personSave").click(function(){
		$(this).parent().parent().hide();
		$(this).parent().parent().prev().show();
	});
	$(".tab_ed").click(function(){
		$(".personCon").removeClass("show");
		$(".personCon").hide();
	});
	职位
	$(".personEdit").click(function(){
		$(this).next().next().removeClass("hidden");
		$(this).next().next().css("display","block");
	});
	$(".jobSave").click(function(){
		$(this).parent().removeClass("show");
		$(this).parent().css("display","none");
		$(this).parent().prev().removeClass("hidden");
		$(this).parent().prev().css("display","block");
		
	});*/
})
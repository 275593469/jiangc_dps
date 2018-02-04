
$(function(){
	function getContextPath(){
		var pathName = document.location.pathname; 
		var index = pathName.substr(1).indexOf("/"); 
		var result = pathName.substr(0,index+1); 
		return result;
	}
	//获取资源服务器地址
	(function(){
		$.ajax({
			url: getContextPath()+"/page/iom/resourceAddress",
			type: 'get',
			dataType: 'json',
			success:function(data){
				sessionStorage.path = data.path;
				sessionStorage.publicCloudId = data.publicCloudId;
				sessionStorage.contextPath = data.contextPath;
			}
		});
	})();
	
	(function(){
		var _treeData;//用于传给jstree的json串
		$.ajax({
			url:getContextPath()+'/page/organ/getOrganTree',
			type:'post',
			data:{
				organType:0
			},
			dataType:'json',
			success:function(data){
//				console.log(data);
				$('#tree-3-1').data('jstree', false).empty();
				if(data.IOM_Head[0].result=='Success'){
					var _arrTree=[];
					var _aTree;
					$.each(data.IOM_List,function(i,_each){
						var _Dpid=_each.pId=="DR"?"#":_each.pId;
						_aTree='{"data_id":"'+_each.id+'","data_pid":"'+_each.pId+'","name":"'+_each.title+'"}';
						_arrTree.push('{"id":"'+_each.id+'","parent":"'+_Dpid+'","text":"'+_each.title+'","a_attr":'+_aTree+'}');						
					});
					_treeData='['+_arrTree.join(',')+']';
					//设置cookie
					$.cookie("treeData", _treeData, { path: "/"});
					//获取机构树
					_treeData=JSON.parse(_treeData);
					initTree(_treeData);
				};
			},
		});
	})();
		
	//初始化机构数
	function initTree(_treeData){
		$("#tree-3-1").jstree({
			'core':{
				"check_callback" : true,
				'data': _treeData
			},
			"ui":{
				"select_prev_on_delete":true,
			},
			"plugins" : [ 
			              "search",//搜索 
			              "ui",
			              "state",// state保存选择状态
			              ],
		}).on("loaded.jstree",function(e,data){
			var inst = data.instance;  
		    var obj = inst.get_node(e.target.firstChild.firstChild.lastChild);			      
		    inst.select_node(obj);  
		    //保存节点状态
		    data.instance.save_state();
		});
	}

	//获取群组树
	(function(){
		var _groupData;
			$.ajax({
				url:getContextPath()+'/page/organ/getOrganTree',
				type:'post',
				data:{
				organType:1
				},
				dataType:'json',
				success:function(data){
//					console.log(data);
					$('#tree-3-2').data('jstree', false).empty()
					if(data.IOM_Head[0].result=='Success'){
						var _arrTree=[];
						var _aTree;
						$.each(data.IOM_List,function(i,_each){
							var _Dpid=_each.pId=="TM"?"#":_each.pId;
							_aTree='{"data_id":"'+_each.id+'","data_pid":"'+_each.pId+'","name":"'+_each.title+'"}';
							_arrTree.push('{"id":"'+_each.id+'","parent":"'+_Dpid+'","text":"'+_each.title+'","a_attr":'+_aTree+'}');						
						});
						_groupData='['+_arrTree.join(',')+']';
						//设置cookie
						$.cookie("groupData", _groupData, { path: "/"});
						};
					},
				});
		
	})();
});
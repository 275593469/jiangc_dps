// ================================================================
//  author:刘保龙
//  createDate: 2015/02/02
//  description: 消息对话框
//  ===============================================================
//define(function(require, exports, module) {
$(function(require, exports, module) {
    "use strict";
    var Mustache = require("mustache");
    var toastr = require("toastr");
    
  //模版
    var template={
    		alert:['<div   class="modal fade" tabindex="-1" data-backdrop="static" data-keyboard="false">',
					'<div class="modal-body">',
					'<p>',
						 '{{message}}',
					'</p>',
					'</div>',
					'<div class="modal-footer">',
						'<button type="button" data-callback-handler="ok" data-dismiss="modal" class="btn blue">确定</button>',
					'</div>',
			        '</div>'
                   ].join(""),
             confirm:['<div   class="modal fade" tabindex="-1" data-backdrop="static" data-keyboard="false">',
					'<div class="modal-body">',
					'<p>',
						 '{{message}}',
					'</p>',
					'</div>',
					'<div class="modal-footer">',
						'<button type="button" data-callback-handler="cancel" data-dismiss="modal" class="btn btn-default">{{options.cancelText}}</button>',
						'<button type="button" data-callback-handler="ok" data-dismiss="modal" class="btn blue">{{options.okText}}</button>',
					'</div>',
			        '</div>'
                   ].join("")
    		
    };
    
    return {
    	
    	//警告对话框
    	alert:function(message){
    		var options={
    				message:message
    		};
    		var $dialog=$(Mustache.render(template.alert,options));
    		$("body").append($dialog);
    		
    		
    		$dialog.on("hidden.bs.modal", function(e) {
    	      // ensure we don't accidentally intercept hidden events triggered
    	      // by children of the current dialog. We shouldn't anymore now BS
    	      // namespaces its events; but still worth doing
    	      if (e.target === this) {
    	    	  $dialog.remove();
    	      }
    	    });
    	    
    		$dialog.modal();
    	},
    	//确认对话框
    	confirm:function(message,okCallback,cancelCallback,_options){
    		var options={
    				message:message,
    				options:$.extend({
    					okText:"确定",
    					cancelText:"取消"
    				},_options)
    		};
    		var $dialog=$(Mustache.render(template.confirm,options));
    		$("body").append($dialog);
    		
    		
    		$dialog.on("hidden.bs.modal", function(e) {
    	      // ensure we don't accidentally intercept hidden events triggered
    	      // by children of the current dialog. We shouldn't anymore now BS
    	      // namespaces its events; but still worth doing
    	      if (e.target === this) {
    	    	  $dialog.remove();
    	      }
    	    });
    		
    		$dialog.on("click", ".modal-footer button", function(e) {
    			var callbackKey = $(this).data("callback-handler");
    			if(callbackKey=="ok"){
    				okCallback();
    		    }else{
    		    	cancelCallback&&cancelCallback();
    		    }
    			
    			$(this.$el).modal("hide");
            	$(this.$el).modal("destroy");
    			
    		});
    		
    		$dialog.on("click", ".bootbox-close-button", function(e) {
    			var callbackKey = $(this).data("callback-handler");
    			$(this.$el).modal("hide");
            	$(this.$el).modal("destroy");
    		});
    		
    		$dialog.modal();
    	},
    	success:function(message){
    		if(!toastr){
    			this.alert(message);
    			return;
    		}
    		toastr.options = {
				   closeButton: true,
				   positionClass:'toast-top-right',
				   showDuration:300,
				   hideDuration:1000,
				   timeOut:3000,
				   extendedTimeOut:1000,
				   showEasing:'swing',
				   hideEasing:'linear',
				   showMethod:'fadeIn',
				   hideMethod:'fadeOut'
    		};
    		return toastr["success"](message, "提示");
    	},
    	info:function(message){
    		if(!toastr){
    				this.alert(message);
    			return;
    		}
    		toastr.options = {
 				   closeButton: true,
 				   positionClass:'toast-top-right',
 				   showDuration:300,
 				   hideDuration:1000,
 				   timeOut:3000,
 				   extendedTimeOut:1000,
 				   showEasing:'swing',
 				   hideEasing:'linear',
 				   showMethod:'fadeIn',
 				   hideMethod:'fadeOut'
     		};
    		return toastr["info"](message, "信息");
    	},
    	warning:function(message){
    		if(!toastr){
    				this.alert(message);
    			return;
    		}
    		toastr.options = {
 				   closeButton: true,
 				   positionClass:'toast-top-right',
 				   showDuration:300,
 				   hideDuration:1000,
 				   timeOut:3000,
 				   extendedTimeOut:1000,
 				   showEasing:'swing',
 				   hideEasing:'linear',
 				   showMethod:'fadeIn',
 				   hideMethod:'fadeOut'
     		};
    		return toastr["warning"](message, "警告");
    	},
   	     error:function(message){
   	    	if(!toastr){
    			this.alert(message);
    			return;
    		}
    		toastr.options = {
 				   closeButton: true,
 				   positionClass:'toast-top-right',
 				   showDuration:300,
 				   hideDuration:1000,
 				   timeOut:3000,
 				   extendedTimeOut:1000,
 				   showEasing:'swing',
 				   hideEasing:'linear',
 				   showMethod:'fadeIn',
 				   hideMethod:'fadeOut'
     		};
    		return  toastr["error"](message, "错误");
   	     },
   	     //通知
   	     notify:function(message,url){
 	    	if(!toastr){
	  			this.alert(message);
	  			return;
  		    }
  		     toastr.options = {
  			      "args":arguments,
 				  "closeButton": true,
 				  "debug": false,
 				  "newestOnTop": false,
 				  "progressBar": false,
 				  "positionClass": "toast-top-center",
 				  "preventDuplicates": false,
 				  "onclick":function(){
 					  if(this.args[1]){
 						 location.href=this.args[1];
 					  }
 				  },
 				  "showDuration": "300",
 				  "hideDuration": "1000",
 				  "timeOut": "6000",
 				  "extendedTimeOut": "1000",
 				  "showEasing": "swing",
 				  "hideEasing": "linear",
 				  "showMethod": "fadeIn",
 				  "hideMethod": "fadeOut"
 				}
 		  
  		   return toastr["warning"](message, "通知");
 	     },
 	     close:function($toastElement){
 	    	 if($($toastElement).length>0){
 	    		$toastElement.hide();
 	    		toastr["remove"]($toastElement);
 	    	 }
 	     }
    }
})
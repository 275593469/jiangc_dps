$(function(){
//      var chart = iChart.create({
//            render:"bar-chart",
//            width:800,
//            height:260,
//            background_color:"#fefefe",
//            gradient:false,
//            color_factor:0.2,
//            border:{
//                  color:"BCBCBC",
//                  width:1
//            },
//            align:"center",
//            offsetx:0,
//            offsety:0,
//            sub_option:{
//                  border:{
//                        color:"#BCBCBC",
//                        width:1
//                  },
//                  label:{
//                        fontweight:500,
//                        fontsize:11,
//                        color:"#4572a7",
//                        sign:"square",
//                        sign_size:12,
//                        border:{
//                              color:"#BCBCBC",
//                              width:1
//                        },
//                        background_color:"#fefefe"
//                  }
//            },
//            shadow:true,
//            shadow_color:"#666666",
//            shadow_blur:2,
//            showpercent:false,
//            column_width:"70%",
//            bar_height:"70%",
//            radius:"90%",
//            title:{
//                  text:"利用ichartjs制作漂亮图表",
//                  color:"#111111",
//                  fontsize:20,
//                  textAlign:"center",
//                  font:"微软雅黑",
//                  height:30,
//                  offsetx:0,
//                  offsety:0
//            },
//            subtitle:{
//                  color:"#111111",
//                  fontsize:16,
//                  textAlign:"center",
//                  font:"微软雅黑",
//                  height:20,
//                  offsetx:0,
//                  offsety:0,
//                  text:""
//            },
//            footnote:{
//                  color:"#111111",
//                  fontsize:12,
//                  textAlign:"right",
//                  font:"微软雅黑",
//                  height:20,
//                  offsetx:0,
//                  offsety:0,
//                  text:""
//            },
//            legend:{
//                  enable:false,
//                  background_color:"#fefefe",
//                  color:"#333333",
//                  fontsize:12,
//                  border:{
//                        color:"#BCBCBC",
//                        width:1
//                  },
//                  column:1,
//                  align:"right",
//                  valign:"center",
//                  offsetx:0,
//                  offsety:0
//            },
//            coordinate:{
//                  width:"80%",
//                  height:"84%",
//                  background_color:"#ffffff",
//                  axis:{
//                        color:"#a5acb8",
//                        width:[1,"",1,""]
//                  },
//                  grid_color:"#d9d9d9",
//                  label:{
//                        fontweight:500,
//                        color:"#666666",
//                        fontsize:11
//                  }
//            },
//            label:{
//                  fontweight:500,
//                  color:"#666666",
//                  fontsize:11
//            },
//            type:"column3d",
//            data:[
//                  {
//                  name:"名称A",
//                  value:20,
//                  color:"#f2f3f5"
//            },{
//                  name:"名称B",
//                  value:30,
//                  color:"#aa4643"
//            },{
//                  name:"名称C",
//                  value:40,
//                  color:"#89a54e"
//            }
//            ]
//      });
//      chart.draw();
      
      
      
		var pv=[],ip=[],t;
		for(var i=0;i<61;i++){
			t = Math.floor(Math.random()*(30+((i%12)*5)))+10;
			pv.push(t);
			t = Math.floor(t*0.5);
			t = t-Math.floor((Math.random()*t)/2);
			ip.push(t);
		}
		
		var data = [{name:"用户登录",value:[5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,6,3,1,4,1],color:"#0d8ecf",line_width:2},{name:"退出登录",value:[2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0],color:"#0d8ecf",line_width:2}];
         
		var labels = ["2012-08-01","2012-08-02","2012-08-03","2012-08-04","2012-08-05","2012-08-06"];
		var line1 = new iChart.LineBasic2D({
			render : 'bar-chart',
			data: data,
			align:'center',
			title : 'ichartjs官方网站最近5天流量趋势',
			subtitle : '平均每个人访问2-3个页面(访问量单位：万)',
			footnote : '数据来源：模拟数据',
			width : 800,
			height : 200,
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
				width:640,
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
					 end_scale:100,
					 scale_space:10,
					 scale_size:2,
					 scale_color:'#9f9f9f'
				},{
					 position:'bottom',	
					 labels:labels
				}]
			}
		});
	
	//开始画图
	line1.draw();
});
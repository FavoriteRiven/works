//获取系统时间
var newDate = '';
getLangDate();
//值小于10时，在前面补0
function dateFilter(date){
    if(date < 10){return "0"+date;}
    return date;
}
function getLangDate(){
    var dateObj = new Date(); //表示当前系统时间的Date对象
    var year = dateObj.getFullYear(); //当前系统时间的完整年份值
    var month = dateObj.getMonth()+1; //当前系统时间的月份值
    var date = dateObj.getDate(); //当前系统时间的月份中的日
    var day = dateObj.getDay(); //当前系统时间中的星期值
    var weeks = ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"];
    var week = weeks[day]; //根据星期值，从数组中获取对应的星期字符串
    var hour = dateObj.getHours(); //当前系统时间的小时值
    var minute = dateObj.getMinutes(); //当前系统时间的分钟值
    var second = dateObj.getSeconds(); //当前系统时间的秒钟值
    var timeValue = "" +((hour >= 12) ? (hour >= 18) ? "晚上" : "下午" : "上午" ); //当前时间属于上午、晚上还是下午
    newDate = dateFilter(year)+"年"+dateFilter(month)+"月"+dateFilter(date)+"日 "+" "+dateFilter(hour)+":"+dateFilter(minute)+":"+dateFilter(second);
    document.getElementById("nowTime").innerHTML = "管理员，"+timeValue+"好！ 当前时间为： "+newDate+"　"+week;
    setTimeout("getLangDate()",1000);
}

layui.use(['layer','table','laydate'],function(){
    $ = layui.jquery;
    var layer = layui.layer ,table = layui.table,laydate = layui.laydate;
    //第一个实例
	table.render({
	    elem: '#demo'
	    ,id:'tabuser'
	    ,height: "auto"
	    ,method:"post"
	    ,url: baseUrl+'/admin/userinfo' //数据接口
	    ,request: {
		  pageName: 'pageNum' //页码的参数名称，默认：page
		  ,limitName: 'pageSize' //每页数据量的参数名，默认：limit
		} 
	    ,page: true //开启分页
	    ,cols: [[ //表头
	      	{field: 'userName', title: '姓名',align:'center', event: 'setUserName',templet:function(d){
                return '<span style="color:#16c2c2;">'+d.userName+'</span>'
            }}
	      	,{field: 'phone', title: '手机号',align:'center', event: 'setPhone',templet:function(d){
                return '<span style="color:#16c2c2;">'+d.phone+'</span>'
            }}
	      	,{field: 'userType', title: '用户状态',align:'center', templet:function(d){
	      		if(d.userType){
	      			return '<span style="color:#16c2c2;">已登录</span>'
	      		}else{
	      			return '<span style="color:#f66;">未登录</span>'
	      		}
            }}
	      	,{field: 'ismember', title: '用户类型',align:'center', templet:function(d){
	      		var timestamp=new Date().getTime();
	      		var a = d.expireTime - timestamp;
	      		if(d.ismember!=2){
	      			if(a>0){
		      			return '<span style="color:#16c2c2;">会员</span>'
		      		}else{
		      			return '<span style="color:#f66;">非会员</span>'
		      		}
	      		}else{
	      			return '<span style="color:#ccc311;">永久会员</span>'
	      		}
            }}
	      	,{field: 'maxlogin', title: '最大登录数（已登录 ）',align:'center',event:"setMaxlogin",templet:function(d){
                    return '<span style="color:#16c2c2;">'+d.maxlogin+'(<span style="color:#ffb800">'+d.currentConnection+'</span>)</span>'
                }}
	      	,{field: 'islogin', title: '连接状态',align:'center', templet:function(d){
	      		if(d.islogin){
	      			return '<span style="color:#16c2c2;">已连接</span>'
	      		}else{
	      			return '<span style="color:#f66;">未连接</span>'
	      		}}}
	      	,{field: 'createTime', title: '创建时间',align:'center',sort:true, toolbar: '#createTime'}
	      	,{field: 'rechargeTime', title: '充值时间',align:'center',sort:true, toolbar: '#rechargeTime'}
	      	,{field: 'expireTime', title: '过期时间',align:'center',event:'setExpireTime', toolbar: '#expireTime'}
	        ,{fixed: 'right',field: 'userType', title: '操作', width:150, align:'left', toolbar: '#barDemo'} //这里的toolbar值是模板元素的选择器
	    ]]
	});
	
	
	$(".addUser_btn").click(function(){
        lineEdit();
    })
	function lineEdit(edit){
        var index = layui.layer.open({
            title : "用户编辑",
            type : 2,
            content : "user_edit.html",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                if(edit){
                	console.log(edit)
                    body.find(".lineId").val(edit.id);
                	
                    body.find(".lineName").val(edit.name);
                    body.find(".lineServer").val(edit.server);
//                  body.find(".lineType").val(edit.type);
					if(edit.type == 0){
						body.find(".lineType").val('0');
					}else{
						body.find(".lineType").val('1');
					}
                    body.find(".lineIp").val(edit.ip);
                    body.find("#city").empty().append('<option value="'+edit.cityId+'">'+edit.cityName+'</option>');
                    form.render('select');
                }
                setTimeout(function(){
                    layui.layer.tips('点击此处返回', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            }
        })
        layui.layer.full(index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(index);
        })
    }
    //监听工具条
	table.on('tool(test)', function(obj){ 
	  var data = obj.data;
	  var layEvent = obj.event;
	  var tr = obj.tr; //获得当前行 tr 的DOM对象
	 if(layEvent === 'del'){ //删除
	    layer.confirm('真的删除行么', function(index){
	      obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
	      layer.close(index);
	      //向服务端发送删除指令
	       $.ajax({
			  type: 'post',
			  url: baseUrl+"/admin/delete/"+data.id,
			  dataType: 'json',
			  success: function(res){
			  	if(res.status == 0){
			  		layer.msg(''+res.msg+'',{icon:1,time:1500});
			  	}else {
			  		layer.msg(''+res.msg+'',{icon:2,time:1500});
			  		return;
			  	}
			  }
			});
	    });
	  } else if(layEvent === 'setUserName'){//修改用户名
	      	layer.prompt({
	        	formType: 2
	        	,shadeClose:true
	        	,title: '修改 ['+ data.userName +'] 的用户名'
	        	,value: data.userName
	      	}, function(value, index){
	        	layer.close(index);
	        	var uinfo = {'id': data.id , "userName" : value }
	        	//这里一般是发送修改的Ajax请求
				editUserInfo(uinfo)
	        	//同步更新表格和缓存对应的值
	        	obj.update({
	          		userName: value
	        	});
	      	});
	    }else if(layEvent === 'setPhone'){//修改手机号
	      	layer.prompt({
	        	formType: 2
	        	,shadeClose:true
	        	,title: '修改 ['+ data.userName +'] 的手机号'
	        	,value: data.phone
	      	}, function(value, index){
	      		var myreg=/^[1][3,4,5,7,8][0-9]{9}$/;
				if(!myreg.test(value)){
					layer.msg('请输入正确的手机号', {
						icon: 2,
						time: 1500
					});
					return 
				}else{
					layer.close(index);
		        	var uinfo = {'id': data.id , "phone" : value }
		        	//这里一般是发送修改的Ajax请求
					var aaa = editUserInfo(uinfo);
		        	if (aaa==1) {
		        		obj.update({
			          		phone: value
			        	});
					} else {
						return;
					}
		        	//同步更新表格和缓存对应的值
				}
	        	
	      	});
	    }else if(layEvent === 'setMaxlogin'){//修改最大登录数
	    	var re = /^[1-9]+[0-9]*]*$/
	      	layer.prompt({
	        	formType: 2
	        	,shadeClose:true
	        	,title: '修改 ['+ data.userName +'] 的最大登录数'
	        	,value: data.maxlogin
	      	}, function(value, index){
	        	if (!re.test(value)) {
	        			layer.msg("请输入正整数");
				  return ;
				}else{
		        	layer.close(index);
		        	var uinfo = {'id': data.id , "maxlogin" : value }
		        	//这里一般是发送修改的Ajax请求
					editUserInfo(uinfo)
		        	//同步更新表格和缓存对应的值
		        	setTimeout(function(){
		        		location.reload();
		        	},1500)
				}
	      	});
	    }else if(layEvent === 'offLine'){ //下线
	    	layer.confirm('确定要执行此操作吗?', function(index){
		      	layer.close(index);
		      	var uinfo = {"id": data.id , "islogin" : 0 }
		      	editUserInfo(uinfo)
		      	setTimeout(function(){
		      		location.reload();
		      	},1500)
		    });
	    }else if(layEvent === 'setExpireTime'){//更改过期时间
				var index = layui.layer.open({
		            title : "编辑线路",
		            type : 2,
				  	shadeClose: true,
		            area: ['380px', '500px'],
		            content : "../page/select_date.html",
		            success : function(layero, index){
		                	var body = layui.layer.getChildFrame('body', index);
			                    body.find("#id").val(data.id);
			                    body.find("#test1").val(timestampToTime(data.expireTime));
			                    laydate.render();
		            }
		        })
	    }
	});
	var i = 0;
    function editUserInfo(obj){
    	$.ajax({
		  	type: 'post',
		  	url: baseUrl+"/admin/update",
		  	data: obj,
		  	async:false,
		  	dataType: 'json',
		  	success: function(res){
		  		if(res.status == 0){
		  			layer.msg(''+res.msg+'',{icon:1,time:1500});
		  	    	i = 1;
			  	}else {
			  		layer.msg(''+res.msg+'',{icon:2,time:1500});
			  		i = 0;
			  	}
		  	}
		});
    	return i;
    }
    
    var $ = layui.$, active = {
    	    reload: function(){
    	      var demoReload = $('#demoReload');
    	      
    	      //执行重载
    	      table.reload('tabuser', {
    	        page: {
    	          curr: 1 //重新从第 1 页开始
    	        }
    	        ,where: {
    	            phone: demoReload.val()
    	        }
    	      });
    	    }
    	  };
    	  
    	  $('.demoTable .layui-btn').on('click', function(){
    	    var type = $(this).data('type');
    	    active[type] ? active[type].call(this) : '';
    	  });
    	  
})

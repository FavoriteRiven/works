layui.use(['layer','table','form'],function(){
    $ = layui.jquery;
    var layer = layui.layer ,table = layui.table , form = layui.form;
    //第一个实例
	var tableIns = table.render({
	    elem: '#lineCon'
	    ,height: "auto"
	    ,method:"post"
	    ,url: baseUrl+'/consoleline/listinfo' //数据接口
	    ,request: {
		  pageName: 'pageNum' //页码的参数名称，默认：page
		  ,limitName: 'pageSize'//每页数据量的参数名，默认：limit
		}
	    ,where:{
	    	id:$("#lineID").val()
	    }
	    ,page: true //开启分页
	    ,cols: [[ //表头
	      	 {field: 'vlinename', title: '名称',width:200,align:'center'}
	      	,{field: 'ipaddress', title: 'IP地址',width:400,align:'center'} 
	      	,{field: 'state', title: '状态',width:200,align:'center',toolbar:'#lineStatus'}
	        ,{fixed: 'right', title: '操作',width:500, align:'center', toolbar: '#barDemo'}
	    ]]
	});
	//    监听工具条
	table.on('tool(lineTable)', function(obj){ 
	  var data = obj.data;
	  var layEvent = obj.event;
	  var tr = obj.tr; //获得当前行 tr 的DOM对象
	 if(layEvent === 'del'){ //删除
	    layer.confirm('真的删除行么', function(index){
	    	layer.close(index);
	      	//向服务端发送删除指令
	      	$.ajax({
			  type: 'get',
			  url: baseUrl+"/consoleline/deletelist/"+data.id,
			  dataType: 'json',
			  success: function(res){
			  	if(res.status == 0){
			  		obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
			  		layer.msg(''+res.msg+'',{icon:1,time:1500});
			  	}else {
			  		layer.msg(''+res.msg+'',{icon:2,time:1500});
			  		return;
			  	}
			  }
			});
	    });
	  }else if(layEvent === "lineout"){
		  layer.confirm('确定执行此操作？', function(index){
		    	layer.close(index);
		      	//向服务端发送删除指令
		      	$.ajax({
				  type: 'post',
				  url: baseUrl+"/consoleline/setlinelist/",
				  dataType: 'json',
				  data:{id:data.id},
				  success: function(res){
				  	if(res.status == 0){
				  		layer.msg(''+res.msg+'',{icon:1,time:1500});
				  		setTimeout(function(){
				  			location.reload();
				  		},1000)
				  	}else {
				  		layer.msg(''+res.msg+'',{icon:2,time:1500});
				  		return;
				  	}
				  }
				});
		    });
	  }
	});
	$(".addLine_btn").click(function(){
		addlinecon($("#lineID").val())
	})
	function addlinecon(id){
		var index =	layer.open({
			  type: 2,
			  title: '添加线路配置',
			  shadeClose: true,
			  shade: 0.3,
			  area: ['400px', '500px'],
			  content: 'addLineConfig.html',
			  success: function(layero,index){
			  	var body = layui.layer.getChildFrame('body', index);
                if(id){
                	console.log(id)
                    body.find("#lineID").val(id);
                }
                setTimeout(function(){
                    layui.layer.tips('点击此处返回', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
			  }
			})
//		layui.layer.full(index);
//      //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
//      $(window).on("resize",function(){
//          layui.layer.full(index);
//      })
	}
})
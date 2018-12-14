layui.use(['layer','table','form'],function(){
    $ = layui.jquery;
    var layer = layui.layer ,table = layui.table , form = layui.form;
    //第一个实例
	table.render({
	    elem: '#line'
	    ,height: "auto"
	    ,method:"post"
	    ,url: baseUrl+'/consoleline/info' //数据接口
	    ,request: {
		  pageName: 'pageNum' //页码的参数名称，默认：page
		  ,limitName: 'pageSize' //每页数据量的参数名，默认：limit
		}
	    ,page: true //开启分页
	    ,cols: [[ //表头
	      	{field: 'name', title: '线路名称',event: 'setName',align:'center'}
	      	,{field: 'server', title: '运营商',event: 'setServer',align:'center'} 
	      	,{field: 'type', title: '线路类型',event: 'setType',align:'center',toolbar:"#lineType"}
	      	,{field: 'ip', title: 'IP地址',align:'center'}
	      	,{field: 'cityName', title: '城市',event: 'setCityName',align:'center'}
	      ,{fixed: 'right', title: '操作', width:250, align:'center', toolbar: '#barDemo'} //这里的toolbar值是模板元素的选择器
	    ]]
	});
	$(".addLine_btn").click(function(){
        lineEdit();
    })
	function lineEdit(edit){
        var index = layui.layer.open({
            title : "编辑线路",
            type : 2,
            content : "line_edit.html",
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
	function lineConfig(edit){
        var index = layui.layer.open({
            title : "线路配置",
            type : 2,
            content : "lineconfig.html",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                if(edit){
                    body.find("#lineID").val(edit);
                    table.render()
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
			  type: 'post',
			  url: baseUrl+"/consoleline/delete/"+data.id,
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
	  } else if(layEvent === 'edit'){ //编辑线路
	    	lineEdit(data)
	  } else if(layEvent === 'lineconfig'){ //线路配置
	    //do something
	    	lineConfig(data.id)
	  }
	});
    
})

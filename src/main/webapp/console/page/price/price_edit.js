layui.use(['form','layer'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
	//  监听购买单位选择
    form.on('select(company)', function(data){
	  	console.log(data.value); //得到被选中的值
	});   
	//	提交
	form.on("submit(addPrice)",function(data){
		var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
		$.post(baseUrl+"/consolemember/update",data.field,function(res){
      		if(res){
      			if(res.status == 0){
      				setTimeout(function(){
						top.layer.close(index);
			            top.layer.msg(res.msg);
			            layer.closeAll("iframe");
			            //刷新父页面
			            parent.location.reload();
			        },500);
      			}
      		}else{layer.msg("网络连接错误")}
       	})
        return false;
    })

})
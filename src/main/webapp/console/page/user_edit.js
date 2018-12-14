layui.use(['form','layer'],function(){
	var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    
	//  监听线路类型选择
   /* form.on('select(lineType)', function(data){
	  	console.log(data.value); //得到被选中的值
	  	if(data.value!=1){
	  		$('#lineType-ip').slideUp();
	  	}else{
	  		$('#lineType-ip').slideDown();
	  		$('#lineType-ip input').focus()
	  	}
	});*/ 
/*	if($('.lineType').val()==1){
		$('#lineType-ip').slideDown();
  	}else{
	  	$('#lineType-ip').slideUp();
  	}
    form.on('select(province)', function(data){
	  	//得到select原始DOM对象console.log(data.elem); 
	  	//得到被选中的值console.log(data.value); 
	  	//得到美化后的DOM对象console.log(data.othis); 
		//	  监听省选择
		getCity(data.value)
	}); */   

	//	提交
	function submitBtn(){
		 
		/*let val = $('.lineType').find("option:selected").val();
		if(val){
			$('.lineIp').val('')
		}*/
	}
	form.on("submit(addUser)",function(data){
		/*let val = $('.lineType').find("option:selected").val();
		if(val!=1){
			$('.lineIp').val('')
			data.field.ip='';
		}*/
		var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
		
		if($('.id').val()){
	        
		}else{
			
			delete data.field.id;
			
	       	$.post(baseUrl+"/user/register",data.field,function(res){
	      		if(res){
	      			if(res.status == 0){
	      				setTimeout(function(){
							top.layer.close(index);
				            top.layer.msg("添加成功！");
				            layer.closeAll("iframe");
				            //刷新父页面
				            parent.location.reload();
				        },500);
	      			}else if(res.status == 1){
	      				setTimeout(function(){
							top.layer.close(index);
				            top.layer.msg("手机号已存在！");
				            layer.closeAll("iframe");
				            //刷新父页面
				            /*parent.location.reload();*/
				        },500);
	      			}
	      		}else{layer.msg("网络连接错误")}
	       	})
		}
        return false;
    })

})
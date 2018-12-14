layui.use(['layer','form','upload'],function(){
    $ = layui.jquery;
    var layer = layui.layer ,
    	upload = layui.upload, 
    	form = layui.form;
    //第一个实例
    console.log($('#lineID').val())
	//创建一个上传组件
	upload.render({
	  elem: '#test1'
	  ,auto: false //选择文件后不自动上传
	  ,bindAction: '#testListAction'
	  ,url: baseUrl+'/consoleline/insert/linelist'
	  ,data: {vlineid:$('#lineID').val()}
	  ,done: function(res, index, upload){
	    //获取当前触发上传的元素，一般用于 elem 绑定 class 的情况，注意：此乃 layui 2.1.0 新增
	    var item = this.item;
	    if(res){
	    	if(res.status == 0){
	    		layer.close(index)
	    		layer.msg(res.msg,{icon:1,time:1000});
	    		setTimeout(function(){
	    			parent.location.reload()
	    		},1000)
	    	}
	    }
	  }
	  ,accept: 'file' //允许上传的文件类型
	  //,size: 50 //最大允许上传的文件大小
	  //,……
	})
	
})

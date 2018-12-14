layui.use(['layer','table','form'],function(){
	$ = layui.jquery;
    var layer = layui.layer ,table = layui.table,form = layui.form;
     
    $.ajax({
	  type: 'get',
	  url: baseUrl+"/consolemember/info",
	  data: {},
	  dataType: 'json',
	  success: function(res){
	  	if(res.status == 0){
	  		table.render({
			    elem: '#price'
			    ,height: "auto"
			    ,data:res.data
			    ,cols: [[ //表头
			      	{field: 'title', title: '购买方式',align:'center'}
			      	,{field: 'price', title: '价格',align:'center'} 
			      	,{field: 'costprice', title: '原价',align:'center'}
			      	,{field: 'company', title: '购买方式',align:'center',toolbar:"#barcompany"}
			      	,{field: 'maxlogin', title: '最大登录数',align:'center'}
			      	,{field: 'describe1', title: '介绍',align:'center'}
			      	,{field: 'describe2', title: '介绍',align:'center'}
			      	,{field: 'describe3', title: '介绍',align:'center'}
			      ,{fixed: 'right', title: '操作', width:150, align:'center', toolbar: '#barDemo'} //这里的toolbar值是模板元素的选择器
			    ]]
			});
	  	}else {
	  		layer.msg(''+res.msg+'',{icon:2,time:1500});
	  		return;
	  	}
	  }
	});
	table.on('tool(priceTable)', function(obj){ 
	  	var data = obj.data;
	  	var layEvent = obj.event;
	  	var tr = obj.tr; //获得当前行 tr 的DOM对象
	 	if(layEvent === 'edit'){ //编辑
		    lineEdit(data)
		} 
	});
	$(".addLine_btn").click(function(){
        lineEdit();
    })
	function lineEdit(edit){
        var index = layui.layer.open({
            title : "编辑价格",
            type : 2,
            content : "price_edit.html",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                if(edit){
                    body.find(".lineId").val(edit.id);
                	
                    body.find(".pricetitle").val(edit.title);
                    body.find(".price").val(edit.price);
                    body.find(".maxlogin").val(edit.maxlogin);
                    body.find(".costprice").val(edit.costprice);
                    body.find(".company").val(edit.company);
                    body.find(".describe1").val(edit.describe1);
                    body.find(".describe2").val(edit.describe2);
                    body.find(".describe3").val(edit.describe3);
                    form.render();
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
})

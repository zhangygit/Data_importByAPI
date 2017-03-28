var ztreeObj = null;
var form = null;
var param_tr_one = "<tr>"+
		"<td><input name='params_name' lay-verify='title' autocomplete='off' placeholder='参数名称' class='layui-input' type='text'></td>"+
		"<td><input name='params_detail' lay-verify='title' autocomplete='off' placeholder='参数说明' class='layui-input' type='text'></td>"+
		"<td><input type='checkbox' name='params_required' lay-skin='primary'  checked></td>"+
		"<td><input name='params_defaultValue' lay-verify='title' autocomplete='off' placeholder='默认值' class='layui-input' type='text'></td>"+
		"<td><input name='params_remarks' lay-verify='title' autocomplete='off' placeholder='备注' class='layui-input' type='text'></td>"+
		"<td>"+
		    "<div class='layui-btn-group'>"+
			  "<a class='layui-btn layui-btn-primary layui-btn-small add_tr'>"+
			    "<i class='layui-icon'>&#xe654;</i>"+
			  "</a>"+
			"</div>"+
		"</td>"+
		"</tr>";
var param_tr = "<tr>"+
	      "<td><input name='params_name' lay-verify='title' autocomplete='off' placeholder='参数名称' class='layui-input' type='text'></td>"+
	      "<td><input name='params_detail' lay-verify='title' autocomplete='off' placeholder='参数说明' class='layui-input' type='text'></td>"+
	      "<td><input type='checkbox' name='params_required' lay-skin='primary'  checked></td>"+
	      "<td><input name='params_defaultValue' lay-verify='title' autocomplete='off' placeholder='默认值' class='layui-input' type='text'></td>"+
	      "<td><input name='params_remarks' lay-verify='title' autocomplete='off' placeholder='备注' class='layui-input' type='text'></td>"+
	      "<td>"+
		      "<div class='layui-btn-group'>"+
			  "<a class='layui-btn layui-btn-primary layui-btn-small del_tr'>"+
			    "<i class='layui-icon'>&#xe640;</i>"+
			  "</a>"+
			  "<a class='layui-btn layui-btn-primary layui-btn-small add_tr'>"+
			    "<i class='layui-icon'>&#xe654;</i>"+
			  "</a>"+
			"</div>"+
		 "</td>"+
	    "</tr>";
layui.use(['layer', 'laytpl','form'], function(){
		var $ = layui.jquery,
		layer = layui.layer,
		 laytpl = layui.laytpl,
		form = layui.form();
		initMenuTree(form,laytpl);
		
		form.on('submit(go1)', function(res) {
			$.ajax({
				  url: "/menu/project-save.htm",
				  data: res.field,
				  type:"post",
				  dataType: "json",
				  success: function(data){
					  if(data.status =='500'){
						  layer.msg(data.msg, {icon: 5});
					  }else{
						  layer.msg(data.msg, {icon: 1});
						  initMenuTree(form,laytpl);
					  }
					 
				  }
			});
			return false;
	  });
		form.on('submit(go2)', function(res) {
		
			var paramList =new Array();
			if($(".table-params").is(':visible')){
				var paramObj = new Object();
				$('.table-params input').each(function(index,element){
					switch($(element).attr("name")){
						case 'params_name':
							paramObj = new Object();
							paramObj.name = $(element).val();
						break;
						case 'params_detail':
							paramObj.detail = $(element).val();
							break;
						case 'params_required':
							if($(element).next().hasClass("layui-form-checked")){
								paramObj.required = 1;
							}else{
								paramObj.required = 0;
							}
							break;
						case 'params_defaultValue':
							paramObj.defaultValue = $(element).val();
							break;
						case 'params_remarks':
							paramObj.remarks = $(element).val();
							paramList.push(paramObj);
							break;
					};
				});
			}
			res.field.paramList = paramList;
			$.ajax({
				  url: "/menu/api-save.htm",
				  data: JSON.stringify(res.field),
				  type:"post",
				  dataType: "json",
				  contentType:"application/json",
				  success: function(data){
					  if(data.status =='500'){
						  layer.msg(data.msg, {icon: 5});
					  }else{
						  layer.msg(data.msg, {icon: 1});
						  initMenuTree(form,laytpl);
					  }
					 
				  }
			});
			return false;
	  });
	  	//console.log($("#ztree").length);
	form.on('radio(apiType)', function(data){
		if($(data.elem).attr("title") == "验证码"){
			$(".table-params tbody").empty();
			$(".table-params").hide();
		}else{
			$(".table-params tbody").empty().append(param_tr_one);
			form.render('checkbox');
			$(".table-params").show();
	}
			
	 }); 
	 $(".del_tr").live("click",function(){
		 $(this).parent().parent().parent().remove();
	 });
	 $(".add_tr").live("click",function(){
		 $(".table-params tbody").append(param_tr);
		 form.render('checkbox');
	 });
});  

function initMenuTree(form,laytpl){
	 $.get("/menu/tree.htm",  function(trees){
		 ZtreeUtil.initTree("ztree",trees,{
				onClick:function(event, treeId, treeNode){
					if(treeNode.type == "project"){
						$(".project-div fieldset legend").text("编辑项目");
						$(".project-div input[name='id']").val(treeNode.id);
						AppUtils.clearForm($(".project-div .layui-form"));
						$.ajax({url: "/menu/project-get.htm",data:{id:treeNode.id},type:"post",dataType: "json",async:false,
							  success: function(data){
								  AppUtils.setForm($(".project-div .layui-form"),data);
								  form.render('radio');
							  }
						});
	                	$(".project-div").show();
	                	$(".api-div").hide();
	                	
	                	
					}else if(treeNode.type=="api"){
						$(".api-div fieldset legend").text("编辑接口");
						$(".api-div input[name='projectId']").val("");
						$(".api-div input[name='projectId']").val(treeNode.id);
						AppUtils.clearForm($(".api-div .layui-form"));
						$.ajax({url: "/menu/api-get.htm",data:{id:treeNode.id},type:"post",dataType: "json",async:false,
							  success: function(data){
								  console.log(data);
								  AppUtils.setForm($(".api-div .layui-form"),data);
								  if(data.apiType == 2){
									  $(".table-params tbody").empty();
									  $(".table-params").hide();
								  }else{
									  if(data.paramList.length!=0){
										  var getTpl = paramList.innerHTML;
										  laytpl(getTpl).render(data, function(html){
										      $(".table-params tbody").empty().append(html);
										      $(".table-params").show();
										     form.render('checkbox');
										  });
									  }
								  }
								  form.render();
							  }
						});
	                	$(".api-div").show();
	                	$(".project-div").hide();
					}
				},
				beforeClick:function(treeId, treeNode, clickFlag){
					if(treeNode.type == "project" || treeNode.type=="api"){
						return true;
					}else{
						return false;
					}
				}
				
			},{
				data:{
					key:{
						name: "text",
		    			url: null
					}
				}
			},{
				context:[{
	                id: "addProject",
	                label: "新增项目",
	                visible:function(NODE,TREE_OBJ){
	                	if(NODE.type=="base"){
	                		return true;
	                	}else{
	                		return false;
	                	}
	                },
	                action: function(NODE, TREE_OBJ){
	                	$(".project-div fieldset legend").text("新增项目");
	                	AppUtils.clearForm($(".project-div .layui-form"));
	                	$(".project-div").show();
	                	$(".api-div").hide();
	                	form.render('radio');
	                }
	            }, {
	                id: "addApi",
	                label: "新增接口",
	                visible: function(NODE,TREE_OBJ){
	                	if(NODE.type=="project"){
	                		return true;
	                	}else{
	                		return false;
	                	}
	                },
	                action: function(NODE, TREE_OBJ){
	                	$(".project-div").hide();
	                	$(".api-div fieldset legend").text("新增接口");
	                	AppUtils.clearForm($(".api-div .layui-form"));
	                	$("#askType_div input").eq(0).prop('checked', true);
	                	$("#apiType_div input").eq(2).prop('checked', true);
	                	form.render('radio');
	                	$(".table-params tbody").empty().append(param_tr_one);
	                	$(".table-params").show();
	        			form.render('checkbox');
	                	$(".api-div input[name='projectId']").val(NODE.id);
	                	$(".api-div").show();
	                	
	                }
	            },{
	                id: "delProject",
	                label: "删除项目",
	                visible: function(NODE,TREE_OBJ){
	                	if(NODE.type=="project"){
	                		return true;
	                	}else{
	                		return false;
	                	}
	                },
	                action: function(NODE, TREE_OBJ){
	                	
//	                	layer.confirm(msg, {icon: 3});
	                	var message = "";
	                	if(NODE.isParent){
	                		message ="该项目存在接口<br>"
	                	}
	                	layer.confirm(message+'确认删除项目【'+NODE.text+'】吗？', {icon: 3},function(index){
	                		$.get("/menu/project-del.htm", {id:NODE.id}, function(data){
		                		if(data.status =='500'){
		  						  layer.msg(data.msg, {icon: 5});
			  					  }else{
			  						  layer.msg(data.msg, {icon: 1});
			  						initMenuTree(form,laytpl);
			  					  }
		                	},"json");
	                	});
	                }
	            },{
	                id: "delApi",
	                label: "删除接口",
	                visible: function(NODE,TREE_OBJ){
	                	if(NODE.type=="api"){
	                		return true;
	                	}else{
	                		return false;
	                	}
	                },
	                action: function(NODE, TREE_OBJ){
	                	
	                	layer.confirm('确认删除接口【'+NODE.text+'】吗？', {icon: 3},function(index){
	                		$.get("/menu/api-del.htm", {id:NODE.id}, function(data){
		                		if(data.status =='500'){
		  						  layer.msg(data.msg, {icon: 5});
			  					  }else{
			  						  layer.msg(data.msg, {icon: 1});
			  						initMenuTree(form,laytpl);
			  					  }
		                	},"json");
	                	});
	                }
	            }]
			});
			ztreeObj = ZtreeUtil.getZtreeObj('ztree');
	 },"json");
	
}
function showMenu() {
    var cityObj = $("#pointName");
    var cityOffset = $("#pointName").offset();
    $("#menuContent").css({left:155 + "px", top:275 + "px"}).slideDown("fast");
    $("body").bind("mousedown", onBodyDown);
}

function validatetable(){
	var starttime = $("input[name='startTime']").val().replace(/-/g, "/");
	var endTime = $("input[name='endTime']").val().replace(/-/g, "/");
	if(starttime!='' && endTime != '' && starttime>=endTime){
        swal("查询失败!", "结束日期需大于开始日期", "error");
		return;
	}
	var tmp = $('input[name="pointId"]').val();
	var l = $( '.ladda-button' ).ladda();
	l.ladda( 'start' );
	if(tmp && tmp.lastIndexOf(",") > 0){//大于1个站点的需要检查是否能多选
		$.ajax({
			url: '/checkStaions',
			type: 'POST',
			data: "pointIds="+tmp,
			dataType: 'json',
			success: function(res){
				if(!res.unpass || res.unpass.length <= 0){
					$('input[name="pointId"]').val(tmp);
				/*	$('#searchForm').submit();*/
				} else {
					var stations = new Array();
					for(var i = 0; i < res.unpass.length; i++ ){
						stations[i] = $('#pointIds option[value="' + res.unpass[i] + '"]').text();
					}
					l.ladda('stop');
					showErrorInfo(stations);
				}
			},
			error: function(e){
				l.ladda('stop');
			}
		});
	} else{
		//l.ladda('stop');
		init();
	}

};

//datatable表头
var dataTable;
$(function(){
    //查询按钮                
    $("#searchBtn").click(function(){
        debugger;
             /* validatetable();*/
        init();
    });
})
function init(){
    var pointId= $("input:hidden[name='pointId']").val(),//input type为hidden时的取值方式
        pointName= $("input[name='pointName']").val();//input为text时的取值
    var startTime = $("input[name='startTime']").val();
    var endTime = $("input[name='endTime']").val();;
   /* var param = {
            id: id,
            name: name
            };*/
    $.ajax({
        url  : '/historyDataAjax',
        type : 'POST',
        data : "pointId="+pointId+"&pointName="+pointName+"&startTime="+startTime+"&endTime="+endTime,
        success : function(resdata) {
                    //请求成功后 如果存在datatable结构销毁
                    if(dataTable){
                    dataTable.destroy();
                    }
                    //清空table数据
                    $("#colTb").html("");

                    var colsDef = [];//定义表头列名

                    var obj = resdata.params;//获取表头列名List
                    var tableData = resdata.datas;//获取table数据
                    var cols = obj.length;
                    var datasize = tableData.length;
            if(cols>0){//构建jsp的table表头显示
                $("#colTr").html((function (cols){
                    var html = "";
                    var paramName;
                     html += $("<th></th>").append("单位").prop("outerHTML");//构建table表头的html
                     html += $("<th></th>").append("测点名称").prop("outerHTML");//构建table表头的html
                     html += $("<th></th>").append("时间").prop("outerHTML");//构建table表头的html
                    for(var i=0;i<cols;i++){ //这里遍历后台返回的字段信息集合
                         //alert(obj[i].column_cn);
                         if(obj[i].display==1){
                            paramName=obj[i].paramName;//取32路参数的标准名称
                             html += $("<th></th>").append(paramName).prop("outerHTML");//构建table表头的html
                         }

                         /*colsNameCN=obj[i].view_name_cn;//这里取的字段信息的中文名
                         var html1=""
                         if(colsNameCN.length>15){//字段中文名过长截取显示
                             html1 += "<span title='"+colsNameCN+"'>"+ colsNameCN.substr(0, 14) + "...</span>";//title鼠标悬浮显示全部字段中文名
                             html += $("<th></th>").append(html1).prop("outerHTML");//构建table表头的html
                             }else{
                             html += $("<th></th>").append(colsNameCN).prop("outerHTML");//构建table表头的html

                         }*/
                    }
                     return html;
                })(cols));
            }

           if(cols>0){
           var companyName = "companyName";
               colsDef.push((function (companyName){
                       var colItem = {
                               data:companyName,
                               render: function( data, type, full, meta ) {
                                   if(data){
                                        return data;
                                   }else{
                                        return "";
                                   }
                               }
                       }
                       return colItem;
                })(companyName));
                var point = "point";
               colsDef.push((function (point){
                       var colItem = {
                               data:point,
                               render: function( data, type, full, meta ) {
                                   if(data){
                                        return data;
                                   }else{
                                        return "";
                                   }
                               }
                       }
                       return colItem;
                })(point));
                var time ="time";
               colsDef.push((function (time){
                       var colItem = {
                               data:time,
                               render: function( data, type, full, meta ) {
                                   if(data){
                                        var date = new Date(data);
                                        var year = date.getFullYear();
                                        var month = date.getMonth() + 1;
                                        (month <10) ? month="0" +month: month=month;
                                        var day = date.getDate();
                                        (date.getDate() <10) ? day="0" +date.getDate(): day=date.getDate();
                                        var hour = date.getHours();
                                        (date.getHours() <10) ? hour="0" +date.getHours(): hour=date.getHours();
                                        var minute = date.getMinutes();
                                        (date.getMinutes() <10) ? minute="0" +date.getMinutes(): minute=date.getMinutes();
                                        var second;
                                        (date.getSeconds() <10) ? second="0" +date.getSeconds(): second=date.getSeconds();

                                        return year + '-' + month + '-' + day+ ' ' + hour+':' + minute + ':' + second;
                                   }else{
                                        return "";
                                   }
                               }
                       }
                       return colItem;
                })(time));
                for(var i=0;i<cols;i++){
                    var paramDef;
                    if(obj[i].display==1){
                        paramDef=obj[i].param;
                        colsDef.push((function (paramDef){
                            var colItem = {
                                    data:paramDef,
                                    render: function( data, type, full, meta ) {
                                        if(data){
                                             return data;
                                        }else{
                                             return "";
                                        }
                                    }
                            }
                            return colItem;
                        })(paramDef));
                    }

            }
        }
                //datatable初始化
                dataTable = $('#dataTable').DataTable({
                    "ordering": false,
                    "info": true,
                    "bLengthChange": false,
                    "iDisplayLength":10,
                    "bFilter": true,
                    "retrieve": true,
                    "processing": true,
                    "scrollX": true,
                    "fixedColumns": true,
                    "bScrollAutoCss": true,
                    "language": {
                        "search": "过滤：",
                        "lengthMenu": "每页 _MENU_ 条记录",
                        "loadingRecords": "请等待，数据正在加载中......",
                        "zeroRecords": "没有找到记录",
                        "info": "从 _START_ 到  _END_ 条记录 总记录数为 _TOTAL_ 条，第 _PAGE_ 页 ( 总共 _PAGES_ 页 )",
                        "infoEmpty": "没有数据",
                        "infoFiltered": "(从 _MAX_ 条数据中检索)",
                        "oPaginate": {
                            "processing": "正在查询中...",
                            "sFirst": "首页",
                            "sPrevious": "前一页",
                            "sNext": "后一页",
                            "sLast": "尾页"
                       }
                     },
                     data:tableData,
                     columns: colsDef

                });

                //全选
                $('#checkAll').click(function() {
                    $('[name=id]:checkbox').prop('checked', this.checked);
                    });
        },
         error: function () {

            alert("请求失败")
     }
    });
}


function showErrorInfo(stations){
	toastr.options = {
		  "closeButton": true,
		  "debug": false,
		  "progressBar": false,
		  "preventDuplicates": false,
		  "positionClass": "toast-top-right",
		  "onclick": null,
		  "showDuration": "400",
		  "hideDuration": "1000",
		  "timeOut": "2000",
		  "extendedTimeOut": "1000",
		  "showEasing": "swing",
		  "hideEasing": "linear",
		  "showMethod": "fadeIn",
		  "hideMethod": "fadeOut"
    };
	var sta = '';
	for(var index in stations){
		sta += '<br/>' + stations[index];
	}
    toastr.error('以下站点只能单选：' + sta, '查询失败');
}
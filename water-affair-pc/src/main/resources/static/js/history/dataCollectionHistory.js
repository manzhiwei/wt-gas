$(document).ready(function(){
      init();
});
   /* var isInitFilterSelect=false;*/
    function initFilterSelect(){
        var str='<label >显示/隐藏列：</label>';
        var tableTh=$("#dataTable").find("thead").find("th");

        for(var i=0;i<tableTh.length;i++){//<i class="Hui-iconfont"></i>
            // str+=' <a class="toggle-vis" data-column="'+i+'">'+$("#dataTable").find("thead").find("th").eq(i).text()+'</a>--'
           str+=' <a class="toggle-vis" data-column="'+i+'">'+$("#dataTable").find("thead").find("th").eq(i).text()+'</a>--'

           /* str+='<span class="toggle-vis" data-column="'+i+'"><input type="checkbox" checked="checked"  />'+tableTh.eq(i).text()+'</span>'*/
           // <div class="checkbox-inline i-checks"><label> <input type="checkbox" th:checked="${queryDto.firstLoad or queryDto.showMin}" name="showMin"/> <i></i>最小值</label></div>
        }

        $("#tableSelect").html(str);

       /* isInitFilterSelect=true;*/
    }
function showMenu() {
    var cityObj = $("#pointName");
    var cityOffset = $("#pointName").offset();
    $("#menuContent").css({left:155 + "px", top:275 + "px"}).slideDown("fast");
    $("body").bind("mousedown", onBodyDown);
}

//datatable表头
var dataTable;
$(function(){
    //查询按钮                
    $("#searchBtn").click(function(){
        init();
           });
})
function init(){
    var pointId= $("input:hidden[name='pointId']").val(),//input type为hidden时的取值方式
        pointName= $("input[name='pointName']").val();//input为text时的取值
    var startTime = $("input[name='startTime']").val();
    var endTime = $("input[name='endTime']").val();
    var queryType= $("#queryType").val();
    console.log(queryType);
   /* var param = {
            id: id,
            name: name
            };*/
    $.ajax({
        url  : '/dataCollectionHistoryDataAjax',
        type : 'POST',
        data : "pointId="+pointId+"&pointName="+pointName+"&startTime="+startTime+"&endTime="+endTime+"&queryType="+queryType,
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
                     html += $("<th></th>").append("测点名称").prop("outerHTML");//构建table表头的html
                     html += $("<th></th>").append("时间").prop("outerHTML");//构建table表头的html
                    for(var i=0;i<cols;i++){ //这里遍历后台返回的字段信息集合
                         //alert(obj[i].column_cn);
                        paramName=obj[i].paramName+obj[i].unit;//取32路参数的标准名称
                        html += $("<th></th>").append(paramName).prop("outerHTML");//构建table表头的html

                    }
                     return html;
                })(cols));
            }

           if(cols>0){
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
                //datatable初始化
                dataTable = $('#dataTable').DataTable({
                    dom: '<"html5buttons"B>lTfgtip',
                    "ordering": false,
                    "info": true,
                    "bLengthChange": true,
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
                       },
                     },
                      buttons: [
                          'copyHtml5',
                          'excelHtml5',
                          'csvHtml5',
                          'pdfHtml5'
                      ],
                     data:tableData,
                     columns: colsDef

                });

                /*//全选
                $('#checkAll').click(function() {
                    $('[name=id]:checkbox').prop('checked', this.checked);
                    });*/
                initFilterSelect();
                $('.toggle-vis').on( 'click', function (e) {
                        e.preventDefault();

                        // Get the column API object
                        var column = dataTable.column( $(this).attr('data-column') );

                        // Toggle the visibility
                        column.visible( ! column.visible() );
                        }
                   );

        },
         error: function () {

            alert("请求失败")
     }
    });
}

$(document).ready(function(){
      init();
});
   /* var isInitFilterSelect=false;*/
    function initFilterSelect(){
        var str='<div class="checkbox checkbox-success checkbox-inline" ><input id = "selectAll" style="cursor:pointer;" type="checkbox" /><label for="selectAll"><b>全选</label></div>';
        var tableTh=$("#dataTable").find("thead").find("th");

        for(var i=0;i<tableTh.length;i++){//<i class="Hui-iconfont"></i>
            str+='<div class="checkbox checkbox-success checkbox-inline ceshi" ><input style="cursor:pointer;" class="toggle-vis"  data-column="'+i+'" type="checkbox" id="'+i+'"  /><label for="'+i+'"><b>'+$("#dataTable").find("thead").find("th").eq(i).text()+'</label></div>'
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
                     html += $("<th></th>").append("非甲烷总烃排放量kg").prop("outerHTML");//构建table表头的html
                    for(var i=0;i<cols;i++){ //这里遍历后台返回的字段信息集合
                         //alert(obj[i].column_cn);
                        paramName=obj[i].paramName+obj[i].unit;//取32路参数的标准名称
                        if(i==0){
                            html += $("<th></th>").append(paramName).prop("outerHTML");//构建table表头的html
                        }else if(i>=20&&i<=24){
                            var html1 = $("<th></th>").append(paramName).prop("outerHTML");//构建table表头的html
                            //调整非甲烷总烃，到第4列
                            var reg  = /(.{43})(.*)/;
                            if(i==21){
                                reg = /(.{65})(.*)/;
                            }else if(i==22){
                                reg = /(.{87})(.*)/;
                            }else if(i==23){
                                reg = /(.{109})(.*)/;
                            }else if(i==24){
                                reg = /(.{128})(.*)/;
                            }

                            html = html.replace(reg, "$1"+html1+"$2");
                        }else{
                            html += $("<th></th>").append(paramName).prop("outerHTML");//构建table表头的html
                        }
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
              var point = "totalCount";//非甲烷总烃排放量 = 非甲烷平均浓度 * 标态烟气累计值/1000000
              colsDef.push((function (point){
                      var colItem = {
                        data:point,
                        render: function( data, type, full, meta ) {
                            var avg = full.p11Avg;
                            var cou = full.p14Cou;
                            if(cou!=null && avg!=null){
                                return (avg*cou/1000000).toFixed(3);
                            }else{
                                console.log("平均值:"+avg);
                                console.log("累计值:"+cou);
                                return "";
                            }

                        }
                }
                return colItem;
             })(point));
                var newobj = new Array();
                for(var i=0;i<cols;i++){
                    if(i<5){
                        newobj[i]=obj[20+i];
                    }else if(i>=5&&i<=24){
                        newobj[i]= obj[i-5];
                    }else {
                        newobj[i]=obj[i];
                    }
                }
                for(var i=0;i<cols;i++){
                    var paramDef;
                    paramDef=newobj[i].param;
                    colsDef.push((function (paramDef){
                        var colItem = {
                                data:paramDef,
                                render: function( data, type, full, meta ) {

                                        return data;
                                }
                        }
                        return colItem;
                    })(paramDef));


            }
        }
                //datatable初始化
                dataTable = $('#dataTable').DataTable({
                    dom: '<"html5buttons"B>lTfgtip',
                    "ordering": true,
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
                    //2.全选第二步，如果有复选框没有被选中，那么全选不能被选中
                    // 若所有的复选框都被选中了，那么全选要被选中
                   if($(".ceshi input:checkbox").length=== $(".ceshi input:checked").length) {
                          $("#selectAll").prop("checked", true);
                      } else {
                          $("#selectAll").prop("checked", false);
                      }
                    // Get the column API object
                    var column = dataTable.column( $(this).attr('data-column') );

                    // Toggle the visibility
                    column.visible( ! column.visible() );
                    }
               );
               //全选
                $('#selectAll').on( 'click', function (e) {
                     /*  e.preventDefault();*/
                     //1.给所有的复选框选中  对应的各列显示应该为，选中就不显示，不选中就显示
                      $(".toggle-vis").prop("checked", $(this).prop('checked'));

                       for(var i =1 ;i<$("input:checkbox").length;i++){
                            var column = dataTable.column( $("input:checkbox:eq('"+i+"')").attr('data-column') );

                                 console.log($("input:checkbox:eq('"+i+"')").prop("checked"));
                                 if(($("input:checkbox:eq('"+i+"')").prop("checked")&&column.visible())||(!$("input:checkbox:eq('"+i+"')").prop("checked")&&column.visible()===false)){
                                         column.visible( ! column.visible() );
                                 }
                       }
                       }
                  );

        },
         error: function () {

            alert("请求失败")
     }
    });
}

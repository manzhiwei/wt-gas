$(document).ready(function(){
      	init();
});
    function initFilterSelect(){
        var str='<label >显示/隐藏列:  </label><div class="checkbox checkbox-success checkbox-inline" ><input id = "selectAll" style="cursor:pointer;" type="checkbox" /><label for="selectAll"><b>全选</label></div>';
        var tableTh=$("#dataTable").find("thead").find("th");

        for(var i=0;i<tableTh.length;i++){
           str+='<div class="checkbox checkbox-success checkbox-inline ceshi" ><input style="cursor:pointer;" class="toggle-vis"  data-column="'+i+'" type="checkbox" id="'+i+'"  /><label for="'+i+'"><b>'+$("#dataTable").find("thead").find("th").eq(i).text()+'</label></div>'

        }

        $("#tableSelect").html(str);

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
   /* var param = {
            id: id,
            name: name
            };*/
    $.ajax({
        url  : '/airHistoryDataAjax',
        type : 'POST',
        data : "pointId="+pointId+"&pointName="+pointName+"&startTime="+startTime+"&endTime="+endTime,
        success : function(resdata) {
                    //请求成功后 如果存在datatable结构销毁
                    if(dataTable){
                        dataTable.destroy();
                    }
                    //清空table数据
                    $("#dataTable").find("tbody").html("");
                    var columns=[ {
                    		"mData" : "point"
                    	},{
                          	"mData" : "time"
                        },{
                            "mData" : "humidity"
                        },{
                    		"mData" : "temp"
                    	}, {
                    		"mData" : "currentI"
                    	}, {
                    		"mData" : "currentControlStat"
                    	}, {
                    		"mData" : "currentRunStat"
                    	}, {
                    		"mData" : "currentForbidden"
                    	}, {
                            "mData" : "ventilateMode"
                        },{
                            "mData" : "ventilateStatus"
                        }];
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
                     data:resdata,
                     aoColumns: columns,
                     aoColumnDefs : [
                    {
                        targets : 1,
                        mRender : function(data, type, full, meta) {
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
                    },{
                          targets : 5,
                          mRender : function(data, type, full, meta) {
                              if(data != null ){
                                if(data==0){ return "自动";}else{ return "收到";}
                              }
                          }
                      },{
                          targets : 6,
                          mRender : function(data, type, full, meta) {
                               if(data != null ){
                                if(data == 3){ return "停止"; }else{ return data;}
                               }
                          }
                      },
                      {
                          targets : 7,
                          mRender : function(data, type, full, meta) {
                              if(data != null ){
                                if(data==0){ return "未禁用"; }else{ return "禁用"; }
                              }
                          }
                      },{
                            targets : 8,
                            mRender : function(data, type, full, meta) {
                                if(data != null ){
                                    if(data==0){  return "手动"; }
                                    else{  return "自动"; }
                               }
                            }
                        },
                        {
                            targets : 9,
                            mRender : function(data, type, full, meta) {
                               if(data != null ){
                                    if(data==0){  return "停止"; }
                                    else{  return "运行"; }
                               }
                            }
                        },
                      {
                        "defaultContent" : "-",
                        "targets" : "_all"
                    } ]

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


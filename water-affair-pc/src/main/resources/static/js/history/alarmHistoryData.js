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
   /* var param = {
            id: id,
            name: name
            };*/
    $.ajax({
        url  : '/alarmStatHistoryDataAjax',
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
                            "mData" : "systemStatComment"
                        },{
                    		"mData" : "colorbookStatComment"
                    	}, {
                    		"mData" : "machineStatComment"
                    	}, {
                    		"mData" : "autoRunStat"
                    	},{
                    		"mData" : "systemMaintainStat"
                    	}, {
                    		"mData" : "systemBreakdownStat"
                    	}, {
                    		"mData" : "gaowenheTempAlarm"
                    	},{
                    		"mData" : "sampleTempAlarm"
                    	}, {
                    		"mData" : "banreguanTempAlarm"
                    	}, {
                    		"mData" : "fanchuifaOpenStat"
                    	}, {
                    		"mData" : "caiyangbengOpenStat"
                    	}, {
                    		"mData" : "kongzhifaOpenStat"
                    	}, {
                    		"mData" : "sample2tempAlarm"
                    	}, {
                    		"mData" : "banreguan2tempAlarm"
                    	}, {
                    		"mData" : "fanchuifa2openStat"
                    	}, {
                    		"mData" : "caiyangbeng2openStat"
                    	}, {
                    		"mData" : "kongzhifa2openStat"
                    	}, {
                    		"mData" : "innerCommExcp"
                    	}, {
                    		"mData" : "fid1ignitionFalse"
                    	}, {
                    		"mData" : "fid1fireBreak"
                    	}, {
                    		"mData" : "fid1fireTempExcp"
                    	}, {
                    		"mData" : "fid1boxTempExcp"
                    	}, {
                    		"mData" : "shitongfaTempExcp"
                    	}, {
                    		"mData" : "zhuxiangTempExcp"
                    	}, {
                    		"mData" : "jixiangTempExcp"
                    	}, {
                    		"mData" : "hydrogen1flowExcp"
                    	}, {
                    		"mData" : "hydrogen2flowExcp"
                    	}, {
                    		"mData" : "air1flowExcp"
                    	}, {
                    		"mData" : "air2flowExcp"
                    	}, {
                    		"mData" : "zhuqianya1pressExcp"
                    	}, {
                    		"mData" : "zhuqianya2pressExcp"
                    	}];
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
                          targets : [5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32],
                          mRender : function(data, type, full, meta) {
                              if(data==0){
                                return "正常";
                              }else{
                                return "不正常";
                              }
                          }
                      },{
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


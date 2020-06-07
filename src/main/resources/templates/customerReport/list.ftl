<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>潜在客户报表查询</title>
    <#include "../common/link.ftl" >
    <script>
        $(function () {

            $('.btn-chart').click(function () {
                //清楚响应的数据
                // 每次隐藏时，清除数据，确保不会和主页dom元素冲突。确保点击时，重新加载。
                $("#chartModal").on("hidden.bs.modal", function() {
                    // 这个#showModal是模态框的id
                    $(this).removeData("bs.modal");
                    $(this).find(".modal-content").children().remove();
                });
                //获取url
                var url = $(this).data('url');
                //指定相应的url 模态框的加载
                $('#chartModal').modal({
                    remote:url + "?" + $('#searchForm').serialize()
                })
                //弹出模态框
                $('#chartModal').modal('show')
            });
        })
    </script>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <#include "../common/navbar.ftl" >
    <#assign currentMenu="customerReport"/>
    <#include "../common/menu.ftl" >
    <div class="content-wrapper">
        <section class="content-header">
            <h1>潜在客户报表查询</h1>
        </section>
        <section class="content">
            <div class="box">
                <div style="margin: 10px;">
                    <!--高级查询--->
                    <form class="form-inline" id="searchForm" action="/customerReport/list.do" method="post">
                        <input type="hidden" name="currentPage" id="currentPage" value="1">
                        <div class="form-group">
                            <label for="keyword">员工姓名:</label>
                            <input type="text" class="form-control" id="keyword" name="keyword" value="${qo.keyword!}">
                        </div>
                        <div class="form-group">
                            <label>时间段查询:</label>
                            <div class="input-daterange input-group" id="datepicker">
                                <input type="text" class="input-sm form-control" name="startDate"
                                       value="${(qo.startDate?string('yyyy-MM-dd'))!}"/>
                                <span class="input-group-addon">to</span>
                                <input type="text" class="input-sm form-control" name="endDate"
                                       value="${(qo.endDate?string('yyyy-MM-dd'))!}"/>
                            </div>
                            <script>
                                $('#datepicker').datepicker({
                                    clearBtn: true,
                                    language: "zh-CN",
                                    autoclose: true,
                                    todayHighlight: true
                                });
                            </script>
                        </div>
                        <div class="form-group">
                            <label for="status">分组类型:</label>
                            <select class="form-control" id="groupType" name="groupType">
                                <option value="e.name">员工</option>
                                <option value="DATE_FORMAT(c.input_time, '%Y')">年</option>
                                <option value="DATE_FORMAT(c.input_time, '%Y-%m')">月</option>
                                <option value="DATE_FORMAT(c.input_time, '%Y-%m-%d')">日</option>
                            </select>
                            <script>
                                $('#groupType').val("${qo.groupType!}")
                            </script>
                        </div>
                        <button id="btn_query" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span> 查询</button>
                        <button type="button" class="btn btn-info btn-chart" data-url="/customerReport/listByBar.do">
                            <span class="glyphicon glyphicon-stats"></span> 柱状图
                        </button>
                        <button type="button" class="btn btn-warning btn-chart"  data-url="/customerReport/listByPar.do">
                            <span class="glyphicon glyphicon-dashboard"></span> 饼状图
                        </button>
                    </form>
                </div>
                <div class="box-body table-responsive no-padding ">
                    <table class="table table-hover table-bordered">

                        <tr>
                            <th>分组类型</th>
                            <th>潜在客户新增数</th>
                        </tr>
                            <#list pageInfo.list as map>
                        <tr>
                                <td>${map.groupTypes!}</td>
                                <td>${map.number!}</td>
                        </tr>
                            </#list>
                    </table>
                    <#include "../common/page.ftl">
                </div>
            </div>
        </section>
    </div>
    <#include "../common/footer.ftl">
</div>
<!-- Modal模态框 -->
<div class="modal fade" id="chartModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
        </div>
    </div>
</div>
</body>
</html>
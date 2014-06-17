<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="fixflow/css/css.css" />
<title>Insert title here</title>
</head>
<body>
	<div class="content">
			<table width="100%" class="fix-table" cellspacing="0">
				<thead>
					<th width="50">编号</th>
					<th width="30"></th>
					<th>流程名称</th>
					<th>业务主键</th>
					<th>任务描述</th>
					<th>当前处理人</th>
					<th>开始时间</th>
					<th>当前节点名称</th>
				</thead>
				<c:forEach items="${taskList}" var="dataList"
					varStatus="index">
					<tr>
						<td style="text-align: center;">${(index.index+1)+pageInfo.pageSize*(pageInfo.pageIndex-1)}</td>

						<td></td>
						<td>${dataList.processDefinitionName}</td>
						<td>${dataList.bizKey}</td>
						<td><a href="doTask.action?taskId=${dataList.taskInstanceId}&bizKey=${dataList.bizKey}">${dataList.description}</a>
						</td>
						<td>${dataList.assignee}</td>
						<td>${dataList.createTime}</td>
						<td>${dataList.nodeName}</td>
						
					</tr>
				</c:forEach>
			</table>

		</div>
	</div>
</body>
</html>
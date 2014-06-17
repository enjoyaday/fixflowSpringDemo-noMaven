<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="fixflow/js/jquery.js"></script>
<script type="text/javascript" src="fixflow/js/flowcommand.js"></script>
<script type="text/javascript" src="fixflow/js/flowautoassemble.js"></script>
<link rel="stylesheet" type="text/css" href="fixflow/css/form.css" />
<title>处理任务</title>
</head>
<body>
<form action="completeTask.action" id="form1">
	<table align="center">
		<tr>
			<td>请假编号：</td>
			<td><input type="text" name="qjbh" value="${leave.qjbh}"></td>
		</tr>
		<tr>
			<td>请假人：</td>
			<td><input type="text" name="qjr" value="${leave.qjr}"></td>
		</tr>
		<tr>
			<td>请假时间：</td>
			<td><input type="text" name="qjsj" value="${leave.qjsj}"></td>
		</tr>
	</table>
	<div class="toolbar">
		<c:forEach items="${commandList}" var="row"
			varStatus="status">
			<div class="btn-normal" id="btn_${status.index+1}"
				commandId="${row.id}" commandName="${row.name}"
				commandType="${row.type}" isAdmin="${row.isAdmin}"
				isVerification="${row.isVerification}"
				isSaveData="${row.isSaveData}"
				isSimulationRun="${row.isSimulationRun}" nodeId="${row.nodeId}"
				nodeName="${row.nodeName}">
				<a>${row.name}</a>
			</div>
		</c:forEach>
	</div>
</form>
</body>
</html>
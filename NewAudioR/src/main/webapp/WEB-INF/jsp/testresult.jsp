<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>	
</head>
<body>

<div class="shadow-z-1">
<table id="table" class="table-table-hover">
 <thead>
	<tr><th>编号</th><th>时长</th><th>噪音频率</th><th>测试样例数</th><th>测试准确率1</th><th>测试准确率2</th></tr>
 </thead>
 <tbody id="tbody">
		<c:forEach items="${audios }" var="audio">
		<tr>
			<td>${audio.idmusicinfo }</td>
			<td>${audio.title }</td>
			<td>${audio.artist }</td>
			<td>${audio.album }</td>
			<td>${audio.filedir }</td>
			<td>${audio.infodir }</td>
			<td> </td>
		</tr>
		</c:forEach>
</tbody>
</table>
</div>
</body>
</html>
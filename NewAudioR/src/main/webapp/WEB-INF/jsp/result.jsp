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

<table border="1">
	<tr><td>编号</td><td>名称</td><td>歌手</td><td>专辑</td><td>文件路径</td><td>相似度值</td></tr>
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
</table>

</body>
</html>
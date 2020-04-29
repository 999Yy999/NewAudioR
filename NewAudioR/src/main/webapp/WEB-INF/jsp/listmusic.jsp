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
	<tr><td>id</td><td>title</td><td>artist</td><td>album</td><td>filedir</td><td>infodir</td><td>caozuo</td></tr>
	<c:forEach items="${audios }" var="audio">
		<tr>
			<td>${audio.idmusicinfo }</td>
			<td>${audio.title }</td>
			<td>${audio.artist }</td>
			<td>${audio.album }</td>
			<td>${audio.filedir }</td>
			<td>${audio.infodir }</td>
			<td>
				<a href="deleteAudio?id=${audio.idmusicinfo }">delete</a>
			</td>
		</tr>
	</c:forEach>
	
</table>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
	
	<link rel="stylesheet" href="${css }/commom.css">
	<link rel="stylesheet" href="${css }/table.css">
	<link rel="stylesheet" href="${css }/inputs.css">
	<link rel="stylesheet" href="${css }/filter.css">
	<script src="${js }/jquery.js"></script>
	<script defer src="${fonts }/js/fontawesome-all.js"></script>
</head>
<body>
<h1><span class="icon"><i class="fa fa-filter"></i></span> 音乐库音乐</h1>
<fieldset>
	<legend>Music-List</legend>
	<div class="shadow-z-1">
	  <table id="table" class="table-table-hover">
		<thead>
			<tr>
			 <th width="5%">编号</th>
			 <th>歌名</th>
			 <th>歌手</th>
			 <th>专辑</th>
			 <th>文件路径</th>
			 <th>操作</th>
			</tr>
		</thead>
		<tbody id="tbody">
			<!-- 
			<c:forEach items="${audios }" var="audio">
				<tr>
				 <td>${audio.idmusicinfo }</td>
				 <td>${audio.title }</td>
				 <td>${audio.artist }</td>
				 <td>${audio.album }</td>
				 <td>${audio.filedir }</td>
				 <td>
					<a href="deleteAudio?id=${audio.idmusicinfo }">delete</a>
				 </td>
				</tr>
			</c:forEach>
			-->
			 
			<c:forEach items="${pb.beanList }" var="audio">
				<tr>
				 <td>${audio.idmusicinfo }</td>
				 <td>${audio.title }</td>
				 <td>${audio.artist }</td>
				 <td>${audio.album }</td>
				 <td>${audio.filedir }</td>
				 <td>
					<a href="deleteMusicByID?id=${audio.idmusicinfo }">delete</a>
				 </td>
				</tr>
			</c:forEach>
			
		</tbody>
	   </table>
   
	   <!-- 底部页码 -->
	   <table>
	   	<div id="fenye" class="pagination" style="margin-top: 30px;">
			<!-- 首页：当前的页码为1 -->
		    <a href="listmusic?pc=1" class="">首页</a>
		    <!-- pb.pc为当前的页码，大于1时，才能点击 上一页 -->
		    <c:if test="${pb.pc > 1 }">
		    	<a href="listmusic?pc=${pb.pc-1}" class="">
		    		上一页
		   		</a>
		    </c:if>
		    <!-- pb.pc为当前的页码，小于等于1时，不能点击 上一页 -->
		    <c:if test="${pb.pc <= 1 }">
		    	<a href="javascript:;" class="">
		    		上一页
		   		</a>
		    </c:if>
		   	<!-- pb.pc为当前的页码， pb.tp为总页数，当前的页码小于总页数时，才能点击下一页 -->
		    <c:if test="${pb.pc < pb.tp }">
		    	<a href="listmusic?pc=${pb.pc+1}" class="">下一页</a>
		    </c:if>
		    <!-- pb.pc为当前的页码， pb.tp为总页数，当前的页码大于等于总页数时，不能点击下一页 -->
		    <c:if test="${pb.pc >= pb.tp }">
		    	<a href="javascript:;" class="">下一页</a>
		    </c:if>
		    <!-- 尾页：当前的页码为总页数 -->
		    <a href="listmusic?pc=${pb.tp}" class="">尾页</a>共${pb.tp }页
		  </div>
		</table>
		<a href="admin">添加音乐</a>
	</div>
</fieldset>
	
</body>
</html>
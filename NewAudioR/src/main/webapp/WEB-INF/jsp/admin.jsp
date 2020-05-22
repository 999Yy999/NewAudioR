<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<!-- 加Recorder.js的时候新加的 -->
 <meta name="viewport" content="width=device-width, initial-scale=1.0">
 <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
 <link rel="stylesheet" type="text/css" href="${css }/style.css">
 <!--  -->   
</head>

<body>
	<form action="insert" method="post">
		inser file/Dir<input type="text" name="filedir"/>
		<input type="submit" value="insert"/>
	</form>
	<form action="search" method="post">
		search file<input type="text" name="filename"/>
		<input type="submit" value="search"/>
	</form>
	<form action="search" enctype="multipart/form-data" method="post">
    	<input type="file" name="filename"><input type="submit" value="搜索"></input> 
	</form>
	
	<a href="listmusic">音乐库音乐信息</a>
	<a href="search?filename=D:\\Z_毕设\\音频素材\\test\\[C@1abae621.wav">搜索</a>
	<a href="main">新界面哟</a>
	<div id="controls">
  	 <button id="recordButton">Record</button>
  	 <button id="pauseButton" disabled>Pause</button>
  	 <button id="stopButton" disabled>Stop</button>
    </div>
    <div id="formats">Format: start recording to see sample rate</div>
  	<p><strong>Recordings:</strong></p>
  	<ol id="recordingsList"></ol>
  	<div id="results">results</div>
    <!-- inserting these scripts at the end to be able to use all the elements in the DOM -->
  	<script src="https://cdn.rawgit.com/mattdiamond/Recorderjs/08e7abd9/dist/recorder.js"></script>
  	<script src="${js }/app.js"></script>
	<script type="text/javascript" src="${js }/jquery-1.8.2.js"></script>
	
	
	<!--  
	<script type="text/javascript" src="${js }/jquery-1.8.2.js"></script>
	<script type="text/javascript">
		$(function(){
			alert("wuhu");
			$("#start_record").click(function(){
				$.ajax({
					   type: "POST",//请求的方式
					   url: "startrecord",//请求的路径
					   data:{"goodsname":uval,"brand":bval},//传递参数
					   success: function(msg){//请求成功后返回的信息json--msg为json对象hashmap(key,value)
						   alert("wuhu");
					   }
				});
			});
		});
	</script>
	-->

</body>
</html>
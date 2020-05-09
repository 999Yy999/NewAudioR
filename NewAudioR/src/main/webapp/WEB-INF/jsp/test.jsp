<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <title>search</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta http-equiv="cache-control" content="max-age=0" />
  <meta http-equiv="cache-control" content="no-cache" />
  <meta http-equiv="expires" content="0" />
  <meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
  <meta http-equiv="pragma" content="no-cache" />
  <link rel="shortcut icon" href="${imgs }/ico.ico" />
  <link rel="stylesheet" href="${css }/commom.css">
  <link rel="stylesheet" href="${css }/table.css">
  <link rel="stylesheet" href="${css }/inputs.css">
  <link rel="stylesheet" href="${css }/filter.css">
  <script defer src="${fonts }/js/fontawesome-all.js"></script>
  <script src="${js }/jquery.js"></script>
  <!-- inserting these scripts at the end to be able to use all the elements in the DOM -->
  <script src="https://cdn.rawgit.com/mattdiamond/Recorderjs/08e7abd9/dist/recorder.js"></script>
  <script src="${js }/testresult.js"></script>
  <script type="text/javascript" src="${js }/jquery-1.8.2.js"></script>
  <!-- 
  <script>
  	function DoTable(){
  		var uval = $("#username").val();
		$.ajax({
			   type: "POST",//请求的方式
			   url: "test",//请求的路径
			   data:{"goodsname":uval,"brand":bval},//传递参数
			   success: function(msg){//请求成功后返回的信息json--msg为json对象hashmap(key,value)
				   alert("wuhu");
			   }
		});
  	}
  </script>
   -->
</head>
<body>
  <div id="content">
    <h1><span class="icon"><i class="fa fa-filter"></i></span> Test</h1>
    <p>
      <form action="test" method="POST">
      <fieldset>
        <fieldset id="filter" class="ft">
          <legend>Search word</legend>
          <input type="text" name="searchword" id="searchword" placeholder="Ex.: Column generation">
        </fieldset>
        <fieldset class="ft">
          <legend>时长</legend>
          <input id="tl4" type="radio" name="time" value="mode1"><label for="r1960"><span><span id="p1960">小于4s</span></span></label>
          <input id="t410" type="radio" name="time" value="mode2" checked="checked"><label for="r1970"><span><span id="p1970">大于等于4秒&小于10秒</span></span></label>
          <input id="th10" type="radio" name="time" value="mode3"><label for="r1980"><span><span id="p1980">大于等于10秒</span></span></label>
        </fieldset>
        <fieldset class="ft">
          <legend>白噪音强度</legend>
          <input id="f2" type="radio" name="fre" value="2"><label for="f2"><span><span id="f2">2Hz</span></span></label>
          <input id="f10" type="radio" name="fre" value="10"><label for="f10"><span><span id="f10">10Hz</span></span></label>
          <input id="f20" type="radio" name="fre" value="20" checked="checked"><label for="f20"><span><span id="f20">20Hz</span></span></label>
          <input id="f30" type="radio" name="fre" value="30"><label for="f30"><span><span id="f30">30Hz</span></span></label>
        </fieldset>
        <fieldset class="ft">
          <legend>测试样例数</legend>
          <input id="s3" type="radio" name="sample" value="3" checked="checked"><label for="s3"><span><span id="s3">3</span></span></label>
          <input id="s10" type="radio" name="sample" value="10"><label for="s10"><span><span id="s10">10</span></span></label>
          <input id="s100" type="radio" name="sample" value="100"><label for="s100"><span><span id="s100">100</span></span></label>
          <input id="s200" type="radio" name="sample" value="200"><label for="s200"><span><span id="s200">200</span></span></label>
          <input id="s500" type="radio" name="sample" value="500"><label for="s500"><span><span id="s500">500</span></span></label>
        </fieldset>
        <p>
        <div class="filter" id="testing">
        	<i class="fa fa-search"></i>
        </div>
        <input class="filter" id="formsubmit" type="submit" value="Test" style="width: 1028px;" />
        </p>
      </fieldset>
      </form>
    </p>
    
    <h1><span class="icon"><i class="fa fa-filter"></i></span> Results </h1>
    <div class="shadow-z-1">
      <table id="table" class="table table-hover">
        <thead>
          <tr>
            <th>编号</th>
            <th>时长</th>
            <th width="40%">噪音频率</th>
            <th>测试样例数</th>
            <th>测试准确率1</th>
            <th>测试准确率2</th>
          </tr>
        </thead>
        <tbody id="tbody"></tbody>
      </table>
      </div>
  </div>
</body>
</html>
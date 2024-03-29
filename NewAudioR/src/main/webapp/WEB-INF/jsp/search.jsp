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
  <script src="${js }/app.js"></script>
  <script type="text/javascript" src="${js }/jquery-1.8.2.js"></script>
</head>
<body>
  <div id="content">
    <h1><span class="icon"><i class="fa fa-filter"></i></span> Search</h1>
    
    <div id="controls">
     <fieldset>
	  	 <div id="recordButton" class="filter">录制</button></div>
	 </fieldset>
	 <fieldset>
	  	 <div id="pauseButton" disabled class="filter">暂停</button></div>
	 </fieldset>
	 <fieldset>
	  	 <div id="stopButton" disabled class="filter">停止</button>
	 </fieldset>
	  	 <div id="formats">点击录制可看到采样率</div>
    </div>
     <p>
      <fieldset>
        <p><div class="filter" onclick="DoTable();"><i class="fa fa-search"></i></div></p>
      </fieldset>
    </p>
  	<p><strong>录音:</strong></p>
  	<ol id="recordingsList"></ol>
  	<!--<div id="results">结果</div>-->
    <!-- inserting these scripts at the end to be able to use all the elements in the DOM -->
  	<script src="https://cdn.rawgit.com/mattdiamond/Recorderjs/08e7abd9/dist/recorder.js"></script>
  	<script src="${js }/app.js"></script>
	<script type="text/javascript" src="${js }/jquery-1.8.2.js"></script>
    
    
    <div class="shadow-z-1">
      <table id="table" class="table table-hover">
        <thead>
          <tr>
            <th>录音编号</th>
            <th>时长</th>
            <th width="40%">噪音</th>
            <th>url</th>
            <th>播放</th>
            <th>搜索</th>
          </tr>
        </thead>
        <tbody id="tbody">
	       	<tr>
	       		<td id=rid></td>
	       		<td id=dur></td>
	       		<td id=noise></td>
	       		<td id=url></td>
	       		<td id=play></td>
	       		<td id=search>
	       			<a href="search?filename=">search</a>
	       			<!-- $("#name").val(); -->
	       		</td>
	       	</tr>
        </tbody>
      </table>
    </div>
  </div>
</body>
</html>
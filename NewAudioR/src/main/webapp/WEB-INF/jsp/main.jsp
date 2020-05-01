<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <title>NewAudioR</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta http-equiv="cache-control" content="max-age=0" />
  <meta http-equiv="cache-control" content="no-cache" />
  <meta http-equiv="expires" content="0" />
  <meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
  <meta http-equiv="pragma" content="no-cache" />
  <!-- 加Recorder.js的时候新加的 -->
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <!-- 加界面模板时加的 -->
  <link rel="shortcut icon" href="${imgs }/ico.ico" />
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
  <link rel="stylesheet" type="text/css" href="${css }/style.css">
  <link rel="stylesheet" href="${css }/index.css?version=1.00">
  <script defer src="${fonts }/js/fontawesome-all.js"></script>
</head>
<body>
  <div>
    <div id="leftPanel">
      <div class="menu">
        <div class="icon"><a href="${html }/home.html" target="homeFrame"><i class="fa fa-home"></i></a></div>
        <div class="subMenu">
          <div class="icon"><a href="${html }/review.html" target="homeFrame" class="field-tip"><i class="fa fa-database"></i><span class="tip-content">音乐库显示1</span></a></div>
          <div class="icon"><a href="searchpage" target="homeFrame" class="field-tip"><i class="fa fa-filter"></i><span class="tip-content">搜索</span></a></div>
          <div class="icon"><a href="${html }/datset.html" target="homeFrame" class="field-tip"><i class="fa fa-puzzle-piece"></i><span class="tip-content">结果列表1</span></a></div>
          <div class="icon"><a href="https://oscar-oliveira.github.io/Instance-2D-Viewer/" target="homeFrame" class="field-tip"><i class="fa fa-object-group"></i><span class="tip-content">Audio Recognizition</span></a></div>
          <div class="icon"><a href="https://oscar-oliveira.github.io/CSP-2D-Viewer/" target="homeFrame" class="field-tip"><i class="fa fa-square-full"></i><span class="tip-content">2D cutting plan viewer</span></a></div>
          <div class="icon"><a href="https://oscar-oliveira.github.io/BPP-3D-Viewer/" target="homeFrame"class="field-tip"><i class="fa fa-cube"></i><span class="tip-content">3D cutting plan viewer</span></a></a></div>
        </div>
      </div>
    </div>
    <div id="rightPanel">
    <iframe id="homeFrame" name="homeFrame" src="${html }/home.html" scrolling="auto">
    
    </iframe></div>
    <div class="clear"></div>
  </div>

</body>
</html>
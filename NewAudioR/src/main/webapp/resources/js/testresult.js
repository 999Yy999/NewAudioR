
var testing = document.getElementById("testing");

//add events to those 2 buttons
testing.addEventListener("click", formsubmit);

function formsubmit() {
	console.log("formsubmit clicked");
	
	//新建一行
	add();
}

var num=0;
var req;
function add(){
	num++;
	var tr=document.createElement("tr");
	// 1.新建一项
	var id=document.createElement("td");
	var dur=document.createElement("td");
	var noise=document.createElement("td");
	var sample=document.createElement("td");
	var zql1=document.createElement("td");
	var zql2=document.createElement("td");
	var exec=document.createElement("td");
	
	id.innerHTML=num;
	
	var time = $('input[name=time]:checked').val();
	var fre = $('input[name=fre]:checked').val();
	var samplenum = $('input[name=sample]:checked').val();
	
	// 2.填内容
	dur.innerHTML=time;
	noise.innerHTML=fre;
	sample.innerHTML=samplenum;
	
	var tab=document.getElementById("table");
	var tbody=document.getElementById("tbody");
	tbody.appendChild(tr);
	// 3.id加到表里
	tr.appendChild(id);
	tr.appendChild(dur);
	tr.appendChild(noise);
	tr.appendChild(sample);

	req="test?time="+time+"&fre="+fre+"&sample="+samplenum;
	alert(req);
	var exectest = document.createElement('a');
	//upload.href="#";
	exectest.innerHTML = "开始测试";
	exectest.addEventListener("click", function(event){
		  var xhr=new XMLHttpRequest();
		  alert("1:"+xhr.status);
		  //设置响应返回的数据格式
		  //xhr.responseType = "document";
		  //注册相关事件回调处理函数，onload当请求成功完成时触发，此时xhr.readystate=4
		  xhr.onload=function(e) {
			  alert("2:"+xhr.status);
		      if(this.readyState === 4) {
		    	  alert("3:"+xhr.status);
		          console.log("Server returned: ",e.target.responseText);
		          //document.getElementById("results").innerHTML=e.target.responseText;
		          console.log("allheaders: ",xhr.getAllResponseHeaders());
		          
		          $.get("test", function(data) {
		      		alert("zql1:" + data.zql + "\nzql2:" + data.zql);
		      	  });
		      }
		  };
		  alert("4:"+xhr.status);
		  //打开连接
		  xhr.open("POST",req,true);  //异步
		  //发送请求 必须有send
		  xhr.send();
	});
	tr.appendChild(exec);
	exec.appendChild(exectest);
	
}
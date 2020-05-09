
var testing = document.getElementById("testing");
var stopButton = document.getElementById("stopButton");
var pauseButton = document.getElementById("pauseButton");

//add events to those 2 buttons
testing.addEventListener("click", testing);

function formsubmit() {
	console.log("formsubmit clicked");
	//新建一行
	add();
}


var num=0;
function add(blob){
	num++;
	var tr=document.createElement("tr");
	// 新建一项
	var id=document.createElement("td");
	var dur=document.createElement("td");
	var noise=document.createElement("td");
	var sample=document.createElement("td");
	var zql1=document.createElement("td");
	var zql2=document.createElement("td");
	
	id.innerHTML=num;
	dur.innerHTML=document.getElementByName("time");
	noise.innerHTML=document.getElementByName("fre");
	sample.innerHTML=document.getElementByName("sample");
	
	var tab=document.getElementById("table");
	var tbody=document.getElementById("tbody");
	tbody.appendChild(tr);
	//id加到表里
	tr.appendChild(id);
	tr.appendChild(dur);
	tr.appendChild(noise);
	tr.appendChild(sample);
	
	
	var test = getElementById("testing");
	//var upload = document.createElement('a');
	//upload.href="#";
	//upload.innerHTML = "Search";
	test.addEventListener("click", function(event){
		  var xhr=new XMLHttpRequest();
		  //设置响应返回的数据格式
		  //xhr.responseType = "document";
		  //注册相关事件回调处理函数，onload当请求成功完成时触发，此时xhr.readystate=4
		  xhr.onload=function(e) {
		      if(this.readyState === 4) {
		          console.log("Server returned: ",e.target.responseText);
		          //document.getElementById("results").innerHTML=e.target.responseText;
		      }
		  };
		  xhr.open("POST","test",true);  //异步
	})
}
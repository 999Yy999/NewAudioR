
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
var xhr;
var vzql1,vzql2;
var time = $('input[name=time]:checked').val();
var fre = $('input[name=fre]:checked').val();
var samplenum = $('input[name=sample]:checked').val();
req="test?time="+time+"&fre="+fre+"&sample="+samplenum;

function add(){
	//发送请求
	xhr=new XMLHttpRequest();
	// 绑定事件，在xhs的readystate属性发生改变时做出处理
	xhr.onreadystatechange = readyStateChange;
	//打开连接
	xhr.open("POST",req,true);  //异步
	//发送请求 必须有send
	xhr.send();
}


function GetQueryString(name,url) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");    //i:大小写不敏感，g:全局匹配
    console.log("reg: ",reg);
    //var r = window.location.search.substr(1).match(reg);
    var r = url.match(reg);     //去掉第一个字母http://localhost:8080/NewAudioR/test?zqls=8.0&zqls=9.0
    //console.log("substr(1): ",url.substr(1));
    console.log("r: ",r);
    //console.log("r[0]: ",r[0]);
    //console.log("r[1]: ",r[1]);
    //console.log("r[2]: ",r[2]);
    if (r != null)
      return unescape(r[2]);
   return null;
} 


var sendDate,receiveDate,responseTimeMs;
// 事件处理函数，当xhs的readystate属性改变时调用
function readyStateChange(){
    switch(xhr.readyState){
        case 0:
            // 此时对象尚未初始化，也没有调用open方法
            console.log("创建请求...");
            console.log("0Date: ",(new Date()).getTime());
            break;
        case 1:
            // 此时对象已经调用了open方法，当没有调用send方法
            console.log("请求创建成功，准备发送请求...");
            sendDate = (new Date()).getTime();
            console.log("2sendDate: ",sendDate);
            console.log("1Date: ",(new Date()).getTime());
            break;
        case 2:
            // 此时调用了send方法，但服务器还没有给出响应
            console.log("请求发送完毕，等待接收响应...");
            break;
        case 3:
            // 此时正在接收服务器的请求，当还没有结束，一般这里不做处理
        	console.log("3Date: ",(new Date()).getTime());
            break;
        case 4:
            // 此时已经得到了服务器放回的数据，可以开始处理
            console.log("接收响应成功，开始处理...");

            // 调用自定义的函数处理结果
            //handleResult();
            receiveDate = (new Date()).getTime();
              console.log("4sendDate: ",sendDate);
	    	  console.log("4receiveDate: ",receiveDate);
	    	  
	    	  responseTimeMs = receiveDate - sendDate;
	    	  console.log("responseTimeMs: ",responseTimeMs);
	    	  
	    	  //alert("3:"+xhr.readyState);
	          //console.log("Server returned: ",e.target.responseText);
	          //document.getElementById("results").innerHTML=e.target.responseText;
	          console.log("allheaders: ",xhr.getAllResponseHeaders());
	          console.log("responseUrl: ",xhr.responseURL);
	          
	          //处理动作：
	          var rpseurl=xhr.responseURL;
	          //vzql1.toFixed(4);
	          //vzql2.toFixed(4);
	          
	          vzql2=GetQueryString('zqls',rpseurl);
	          //console.log("substr36: ","http://localhost:8080/NewAudioR/test?zqls=8.0000&zqls=9.0000".substr(37,12));
	          vzql1=GetQueryString('zqls',rpseurl.substr(37,12));
	         
	          //alert(vzql1+","+vzql2);
	          
	        //动态生成一行表格
	      	num++;
	      	var tr=document.createElement("tr");
	      	// 1.新建一项
	      	var id=document.createElement("td");
	      	var dur=document.createElement("td");
	      	var noise=document.createElement("td");
	      	var sample=document.createElement("td");
	      	var zql1=document.createElement("td");
	      	var zql2=document.createElement("td");
	      	var rtime=document.createElement("td");    //识别时长
	      	//var exec=document.createElement("td");
	      	
	      	id.innerHTML=num;
	      	
	      	switch(fre){
		      	case "2": fre=-80; break;
		      	case "10": fre=-70;break;
		      	case "20": fre=-60;break;
		      	case "30": fre=-50;break;
		      	case "40": fre=-40;break;
	      	}
	      	var durtime;
	      	switch(time){
	      	case "mode1": durtime="时长<4s"; break;
	      	case "mode2": durtime="4s<=时长<10s"; break;
	      	case "mode3": durtime="时长>=10s"; break;
	      	}
	      	
	      	// 2.填内容
	      	dur.innerHTML=durtime;
	      	noise.innerHTML=fre;
	      	sample.innerHTML=samplenum;
	      	zql1.innerHTML=vzql1;
	      	zql2.innerHTML=vzql2;
	      	rtime.innerHTML=responseTimeMs;
	      	
	      	var tab=document.getElementById("table");
	      	var tbody=document.getElementById("tbody");
	      	tbody.appendChild(tr);
	      	// 3.id加到表里
	      	tr.appendChild(id);
	      	tr.appendChild(dur);
	      	tr.appendChild(noise);
	      	tr.appendChild(sample);
	      	tr.appendChild(zql1);
	      	tr.appendChild(zql2);
	      	tr.appendChild(rtime);
	      	
	      	//alert(req);
	      	//var exectest = document.createElement('a');
	      	//upload.href="#";
	      	//exectest.innerHTML = "开始测试";

	      	//tr.appendChild(exec);
	      	//exec.appendChild(exectest);
	          //double zqls[2]=request.getAttribute("zqls");
		  //alert("zql1:" + zql[0] + "\nzql2:" + zql[1]);
		  //zql1.innerHTML=zql[0];
	          //zql2.innerHTML=zq[1];
            console.log("响应处理完毕，本次AJAX结束。");
            break;
    }
}


/*exectest.addEventListener("click", function(event){
var xhr=new XMLHttpRequest();
//alert("1:"+xhr.status);
//设置响应返回的数据格式
//xhr.responseType = "document";
//注册相关事件回调处理函数，onload当请求成功完成时触发，此时xhr.readystate=4
xhr.onload=function(e) {
	 // alert("2:"+xhr.status);
    if(this.readyState === 4) {
  	  //alert("3:"+xhr.status);
        console.log("Server returned: ",e.target.responseText);
        //document.getElementById("results").innerHTML=e.target.responseText;
        console.log("allheaders: ",xhr.getAllResponseHeaders());
       
        alert(req+" 请求结束");
        
        //处理动作：
        alert(GetQueryString('zqls'));
        alert(GetQueryString('zqls'));
        //double zqls[2]=request.getAttribute("zqls");
		  //alert("zql1:" + zql[0] + "\nzql2:" + zql[1]);
		  //zql1.innerHTML=zql[0];
        //zql2.innerHTML=zq[1];
    }
};
//alert("4:"+xhr.status);
//打开连接
xhr.open("POST",req,true);  //异步
//发送请求 必须有send
xhr.send();
});*/




/*alert("1:"+xhr.readyState);
//设置响应返回的数据格式
//xhr.responseType = "document";
//注册相关事件回调处理函数，onload当请求成功完成时触发，此时xhr.readystate=4
if(xhr.readyState==2){
	  alert("4:"+xhr.readyState);
	  sendDate = (new Date()).getTime();
	  console.log("sendDate: ",sendDate);
}*/
/*xhr.onload=function(e) {
	  alert("2:"+xhr.readyState);
	  /*if(this.readyState==2){
		  sendDate = (new Date()).getTime();
		  console.log("sendDate: ",sendDate);
	  }
    if(this.readyState === 4) {
  	  //计算用时
  	  receiveDate = (new Date()).getTime();
  	  console.log("receiveDate: ",receiveDate);
  	  responseTimeMs = receiveDate - sendDate;
  	  console.log("responseTimeMs: ",responseTimeMs);
  	  
  	  alert("3:"+xhr.readyState);
        console.log("Server returned: ",e.target.responseText);
        //document.getElementById("results").innerHTML=e.target.responseText;
        console.log("allheaders: ",xhr.getAllResponseHeaders());
        console.log("responseUrl: ",xhr.responseURL);
        
        //处理动作：
        var rpseurl=xhr.responseURL;
        //vzql1.toFixed(4);
        //vzql2.toFixed(4);
        
        vzql2=GetQueryString('zqls',rpseurl);
        //console.log("substr36: ","http://localhost:8080/NewAudioR/test?zqls=8.0000&zqls=9.0000".substr(37,12));
        vzql1=GetQueryString('zqls',rpseurl.substr(37,12));
       
        //alert(vzql1+","+vzql2);
        
      //动态生成一行表格
    	num++;
    	var tr=document.createElement("tr");
    	// 1.新建一项
    	var id=document.createElement("td");
    	var dur=document.createElement("td");
    	var noise=document.createElement("td");
    	var sample=document.createElement("td");
    	var zql1=document.createElement("td");
    	var zql2=document.createElement("td");
    	var rtime=document.createElement("td");    //识别时长
    	//var exec=document.createElement("td");
    	
    	id.innerHTML=num;
    	
    	
    	
    	// 2.填内容
    	dur.innerHTML=time;
    	noise.innerHTML=fre;
    	sample.innerHTML=samplenum;
    	zql1.innerHTML=vzql1;
    	zql2.innerHTML=vzql2;
    	rtime.innerHTML=responseTimeMs;
    	
    	var tab=document.getElementById("table");
    	var tbody=document.getElementById("tbody");
    	tbody.appendChild(tr);
    	// 3.id加到表里
    	tr.appendChild(id);
    	tr.appendChild(dur);
    	tr.appendChild(noise);
    	tr.appendChild(sample);
    	tr.appendChild(zql1);
    	tr.appendChild(zql2);
    	tr.appendChild(rtime);
    	
    	//alert(req);
    	//var exectest = document.createElement('a');
    	//upload.href="#";
    	//exectest.innerHTML = "开始测试";

    	//tr.appendChild(exec);
    	//exec.appendChild(exectest);
        //double zqls[2]=request.getAttribute("zqls");
	  //alert("zql1:" + zql[0] + "\nzql2:" + zql[1]);
	  //zql1.innerHTML=zql[0];
        //zql2.innerHTML=zq[1];
    }
};*/
//alert("4:"+xhr.status);
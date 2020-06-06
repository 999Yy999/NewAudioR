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
	var vzql1,vzql2;
	var sendDate,receiveDate,responseTimeMs;
	var showfzsearch;
	var fzsearch=$('input[name=fzsearch]:checked').val();
	var time = $('input[name=time]:checked').val();
  	var fre = $('input[name=fre]:checked').val();
  	var samplenum = $('input[name=sample]:checked').val();
	req="test?fzsearch="+fzsearch+"&time="+time+"&fre="+fre+"&sample="+samplenum;
	//发送请求
	var xhr=new XMLHttpRequest();
	  //alert("1:"+xhr.readyState);
	  //设置响应返回的数据格式
	  //xhr.responseType = "document";
	  //注册相关事件回调处理函数，onload当请求成功完成时触发，此时xhr.readystate=4
	  if(xhr.readyState==0){
		  sendDate = (new Date()).getTime();
		  console.log("sendDate: ",sendDate);
	  }
	  xhr.onload=function(e) {
		  //alert("2:"+xhr.readyState);
		  if(this.readyState==2){
			  sendDate = (new Date()).getTime();
			  console.log("sendDate: ",sendDate);
		  }
	      if(this.readyState === 4) {
	    	  //计算用时
	    	  receiveDate = (new Date()).getTime();
	    	  console.log("receiveDate: ",receiveDate);
	    	  responseTimeMs = receiveDate - sendDate;
	    	  console.log("responseTimeMs: ",responseTimeMs);
	    	  
	    	  //alert("3:"+xhr.readyState);
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
	      	var fzsearchtd=document.createElement("td");
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
	      	switch(time){
	    	case "mode1": durtime="时长<4s"; break;
	    	case "mode2": durtime="4s<=时长<10s"; break;
	    	case "mode3": durtime="时长>=10s"; break;
	    	}
	      	switch(fzsearch){
	      	case "1": showfzsearch="是"; break;
	      	case "0": showfzsearch="否"; break;
	      	}
	      	// 2.填内容
	      	fzsearchtd.innerHTML=showfzsearch;
	      	dur.innerHTML=durtime;
	      	noise.innerHTML=fre;
	      	sample.innerHTML=samplenum;
	      	zql1.innerHTML=vzql1;
	      	zql2.innerHTML=vzql2;
	      	rtime.innerHTML=responseTimeMs;
	      	
	      	var tab=document.getElementById("table");
	      	var tbody=document.getElementById("tbody");
	      	tbody.appendChild(tr);
	      	// 3.td加到表里
	      	tr.appendChild(id);
	      	tr.appendChild(fzsearchtd);
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
	  };
	  //alert("4:"+xhr.status);
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
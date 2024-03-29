//webkitURL is deprecated but nevertheless
URL = window.URL || window.webkitURL;

var gumStream; 						//stream from getUserMedia()
var rec; 							//Recorder.js object
var input; 							//MediaStreamAudioSourceNode we'll be recording

// shim for AudioContext when it's not avb. 
var AudioContext = window.AudioContext || window.webkitAudioContext;
var audioContext //audio context to help us record

var recordButton = document.getElementById("recordButton");
var stopButton = document.getElementById("stopButton");
var pauseButton = document.getElementById("pauseButton");

//add events to those 2 buttons
recordButton.addEventListener("click", startRecording);
stopButton.addEventListener("click", stopRecording);
pauseButton.addEventListener("click", pauseRecording);

function startRecording() {
	console.log("recordButton clicked");

	/*
		Simple constraints object, for more advanced audio features see
		https://addpipe.com/blog/audio-constraints-getusermedia/
	*/
    
    var constraints = { audio: true, video:false }

 	/*
    	Disable the record button until we get a success or fail from getUserMedia() 
	*/

	recordButton.disabled = true;
	stopButton.disabled = false;
	pauseButton.disabled = false

	/*
    	We're using the standard promise based getUserMedia() 
    	https://developer.mozilla.org/en-US/docs/Web/API/MediaDevices/getUserMedia
	*/

	navigator.mediaDevices.getUserMedia(constraints).then(function(stream) {
		console.log("getUserMedia() success, stream created, initializing Recorder.js ...");

		/*
			create an audio context after getUserMedia is called
			sampleRate might change after getUserMedia is called, like it does on macOS when recording through AirPods
			the sampleRate defaults to the one set in your OS for your playback device

		*/
		audioContext = new AudioContext();

		//update the format 
		document.getElementById("formats").innerHTML="Format: 2 channel pcm @ "+audioContext.sampleRate/1000+"kHz"
		
		/*  assign to gumStream for later use  */
		gumStream = stream;
		
		/* use the stream */
		input = audioContext.createMediaStreamSource(stream);

		/* 
			Create the Recorder object and configure to record mono sound (1 channel)
			Recording 2 channels  will double the file size
		*/
		rec = new Recorder(input,{numChannels:2})      //记得修改为双声道

		//start the recording process
		rec.record()

		console.log("Recording started");

	}).catch(function(err) {
	  	//enable the record button if getUserMedia() fails   //recorder.js加载失败时
    	recordButton.disabled = false;
    	stopButton.disabled = true;
    	pauseButton.disabled = true
	});
}

function pauseRecording(){
	console.log("pauseButton clicked rec.recording=",rec.recording );
	if (rec.recording){
		//pause
		rec.stop();
		pauseButton.innerHTML="继续录制";
	}else{
		//resume
		rec.record()
		pauseButton.innerHTML="暂停录制";

	}
}

function stopRecording() {
	console.log("stopButton clicked");

	//disable the stop button, enable the record too allow for new recordings
	stopButton.disabled = true;
	recordButton.disabled = false;
	pauseButton.disabled = true;

	//reset button just in case the recording is stopped while paused
	pauseButton.innerHTML="暂停录制";
	
	//tell the recorder to stop the recording
	rec.stop();

	//stop microphone access
	gumStream.getAudioTracks()[0].stop();
	
	//create the wav blob and pass it on to createDownloadLink
	rec.exportWAV(createDownloadLink);
}
var au;
function createDownloadLink(blob) {
	
	var url = URL.createObjectURL(blob);
	au = document.createElement('audio');
	
	var li = document.createElement('li');
	var link = document.createElement('a');

	//name of .wav file to use during upload and download (without extendion)
	var filename = new Date().toISOString();

	//add controls to the <audio> element这些都可以删掉
	au.controls = true;
	au.src = url;
	
	//save to disk link
	link.href = url;
	link.download = filename+".wav"; //download forces the browser to donwload the file using the  filename
	link.innerHTML = "Save to disk";

	//add the new audio element to li
	li.appendChild(au);
	
	//add the filename to the li
	li.appendChild(document.createTextNode(filename+".wav "))

	//add the save to disk link to li
	li.appendChild(link);
	
	add(blob);
	/*//
	document.getElementById("rid").innerHTML = li.value;
	alert(li.value);
	
	//
	document.getElementById("url").innerHTML = au.src;
	alert(au.src);
	
	//
	document.getElementById("dur").innerHTML = au.duration;
	alert(au.duration);*/
	
	//print blob from me
	console.log(blob);
	
	//upload link
	/*var upload = document.createElement('a');
	upload.href="#";
	upload.innerHTML = "Upload";
	upload.addEventListener("click", function(event){
		  var xhr=new XMLHttpRequest();
		  xhr.onload=function(e) {
		      if(this.readyState === 4) {
		          console.log("Server returned: ",e.target.responseText);
		      }
		  };
		  var fd=new FormData();
		  fd.append("audio_data",blob, filename);
		  xhr.open("POST","record2local",true);
		  xhr.send(fd);
	})*/
	/*var upload = document.createElement('button');
	//upload.href="#";
	upload.innerHTML = "Upload";
	upload.addEventListener("click", function(event){
		  var xhr=new XMLHttpRequest();
		  //设置响应返回的数据格式
		  //xhr.responseType = "document";
		  //注册相关事件回调处理函数，onload当请求成功完成时触发，此时xhr.readystate=4
		  xhr.onload=function(e) {
		      if(this.readyState === 4) {
		          //console.log("Server returned: ",e.target.responseText);
		          document.getElementById("results").innerHTML=e.target.responseText;
		      }
		  };
		  var fd=new FormData();
		  fd.append("audio_data",blob, filename);
		  xhr.open("POST","record2local",true);  //异步
		  xhr.send(fd);
	})
	li.appendChild(document.createTextNode (" "))//add a space in between
	li.appendChild(upload)//add the upload link to li
	
	//add the li element to the ol
	recordingsList.appendChild(li);*/
}

var num=0;
function add(blob){
	num++;
	var tr=document.createElement("tr");
	// 新建一项
	var id=document.createElement("td");
	//var dur=document.createElement("td");
	//var noise=document.createElement("td");
	//var url=document.createElement("td");
	var play=document.createElement("td");
	var search=document.createElement("td");
	//var a=document.createElement("a");
	//var aplay=document.createElement("a");
	var tdinputartist=document.createElement("td");
	var tdinputalbum=document.createElement("td");
	
	var filename = new Date().toISOString();
	var vartist=document.getElementById("artist").value;
	var valbum=document.getElementById("album").value;
	var invartist,invalbum;
	if (vartist==""){
		invartist="无";
	}
	if (valbum==""){
		invalbum="无";
	}
	//a.href=au.src;
	///a.download=filename+".wav";
	//a.innerHTML="下载";
	
	//aplay.href=au.src;
	//aplay.innerHTML="跳转播放";
	
	//play.href=au.src;
	//play.innerHTML='跳转播放';
	
	//id内容
	id.innerHTML=num;
	
	//dur.innerHTML=au.duration;
	//url.innerHTML=au.src;
	if (vartist==""){
		tdinputartist.innerHTML=invartist;
	}else{
		tdinputartist.innerHTML=vartist;
	}
	if(valbum==""){
		tdinputalbum.innerHTML=invalbum;
	}else{
		tdinputalbum.innerHTML=valbum;
	}
	
	//xm.innerHTML="第"+num+"学生";
	//var search=document.createElement("td");
	//search.innerHTML="<a href='javascript:;' onclick='del(this)' >删除</a>";
	var tab=document.getElementById("table");
	var tbody=document.getElementById("tbody");
	tbody.appendChild(tr);
	//id加到表里
	tr.appendChild(id);
	//tr.appendChild(dur);
	//tr.appendChild(noise);
	//tr.appendChild(url);
	tr.appendChild(play);
	//url.appendChild(a);
	play.appendChild(au);
	tr.appendChild(tdinputartist);
	tr.appendChild(tdinputalbum);
	
	var req="?artist="+vartist+"&album="+valbum;
	var upload = document.createElement('a');
	//upload.href="#";
	upload.innerHTML = "识别";
	upload.addEventListener("click", function(event){
		  var xhr=new XMLHttpRequest();
		  //设置响应返回的数据格式
		  //xhr.responseType = "document";
		  //注册相关事件回调处理函数，onload当请求成功完成时触发，此时xhr.readystate=4
		  xhr.onload=function(e) {
		      if(this.readyState === 4) {
		          //console.log("Server returned: ",e.target.responseText);
		          document.getElementById("results").innerHTML=e.target.responseText;
		      }
		  };
		  var fd=new FormData();
		  fd.append("audio_data",blob, filename);
		  xhr.open("POST","record2local"+req,true);  //异步
		  xhr.send(fd);
	})
	tr.appendChild(search);
	search.appendChild(upload);
}
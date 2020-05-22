package com.audio.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.audio.entity.Music;
import com.audio.entity.PageBean;
import com.audio.service.AudioService;

@Controller
public class AudioController {
	@Autowired
	private AudioService audioService;
	//跳转到默认界面
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String index(){
		return "index";
	}
	
	//管理员添加音乐
	@RequestMapping(value="/admin", method=RequestMethod.GET)
	public String admin(){
		return "admin";
	}
	
	//跳转到显示音乐库界面
	/*@RequestMapping(value="/showmusics", method=RequestMethod.GET)
	public String showmusics(){
		return "showmusics";
	}*/
	
	//显示音乐库音乐功能
	
	//@RequestMapping(value="/showmusics", method=RequestMethod.GET)
	@RequestMapping(value="/listmusic", method=RequestMethod.GET)
	public String showmusics(@RequestParam(defaultValue="1") int pc,Model model){
	//public String showmusics(Model model){
		List<HashMap<String,Object>> musics = audioService.listAll();
		model.addAttribute("musics", musics);
		
		// 给定ps的值，每页显示的行数,默认值PageBean给的是每页显示10条
		int ps = 30;
		//将查询的数据封装到分页控件 pc tr  ps beanList
		PageBean<HashMap<String,Object>> pb = audioService.listMusicByPage(pc,ps);
		model.addAttribute("pb", pb);
		
		return "listmusic";
		//return "showmusics";
	}
	
	//显示音乐列表
	/*@RequestMapping(value="/listmusic", method=RequestMethod.GET)
	public String listmusic(Model model){
		List<HashMap<String,Object>> audios = audioService.listAll();
		model.addAttribute("audios", audios);
		
		return "listmusic";
	}*/
	
	//跳转到新界面
	@RequestMapping(value="/main", method=RequestMethod.GET)
	public String main(){
		return "main";
	}
	
	//跳转到新界面
	@RequestMapping(value="/searchpage", method=RequestMethod.GET)
	public String searchpage(){
		return "searchpage";
	}
	
	//跳转到新界面
	@RequestMapping(value="/testpage", method=RequestMethod.GET)
	public String testpage(){
		//System.out.println("--------zqls:"+zqls);
		//System.out.println("testget, zql1:"+zqls.get("zql1")+"zql2:"+zqls.get("zql2"));
		//System.out.println("testget, zql1:"+zqls[0]+"zql2:"+zqls[1]);
		return "test";
	}
	
	@RequestMapping(value="/test", method=RequestMethod.GET)
	public String test(String[] zqls, Model model){
		System.out.println("--------zqls:"+zqls);
		//System.out.println("testget, zql1:"+zqls.get("zql1")+"zql2:"+zqls.get("zql2"));
		System.out.println("testget, zql1:"+zqls[0]+"zql2:"+zqls[1]);
		model.addAttribute("zqls", zqls);
		return "test";
	}
		
	//测试
	@RequestMapping(value="/test", method=RequestMethod.POST)
	public String test(String time, int fre, int sample, Model model){
		//完成搜索功能
		//List<HashMap<String, Object>> audios=audioService.search(filename);
		//double[] zql=audioService.test(time, fre, sample);
		String[] zqls=audioService.test(time, fre, sample);
		System.out.println("testpost, zql1:"+zqls[0]+"zql2:"+zqls[1]);
		model.addAttribute("zqls", zqls);
		
		//return "test";
		return "redirect:test"; 
	}
	
	//deleteAudio
	@RequestMapping(value="/deleteMusicByID", method=RequestMethod.GET)
	public String deleteMusicByID(Integer id, Model model){
		List<HashMap<String,Object>> audios = audioService.deleteMusicById(id);
		model.addAttribute("audios", audios);
		
		//添加成功后执行listmusic方法获取最新数据跳转至listmusic界面
		return "redirect:listmusic";
	}
	
	//插入到音乐库
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public String insert(String filedir, Model model){
		//完成添加功能
		List<HashMap<String,Object>> audios=audioService.insert(filedir);
		//数据返回前端
		model.addAttribute("audios", audios);
		//跳转页面i
		return "listmusic";
	}
	
	//检索样本音乐
	@RequestMapping(value="/search", method=RequestMethod.POST)
	public String search(String filename, Model model){
		//完成搜索功能
		List<HashMap<String, Object>> audios=audioService.search(filename,null);
		model.addAttribute("audios", audios);
		
		return "result";
	}
	
	//检索样本音乐 支持重定向
	@RequestMapping(value="/search", method=RequestMethod.GET)
	public String searchr(String filename, Model model){
		//完成搜索功能
		List<HashMap<String, Object>> audios=audioService.search(filename,null);
		model.addAttribute("audios", audios);
		
		return "result";
	}
	
	/*
	@RequestMapping(value="/search", method=RequestMethod.GET)
	public String search1(String filename, Model model){
		//完成搜索功能
		List<HashMap<String, Object>> audios=audioService.search(filename);
		model.addAttribute("audios", audios);
		
		return "result";
	}*/
	
	//通过Spring的autowired注解获取spring默认配置的request 
	@Autowired
	private HttpServletRequest request; 
	  
	/*** 
	 * 上传文件 用@RequestParam注解来指定表单上的file为MultipartFile 
	 * 
	 * @param file 
	 * @return 
	 */
	@RequestMapping("record2local") 
	public String record2local(@RequestParam("audio_data") MultipartFile audio_data,  Model model) { 
		// 判断文件是否为空 
		String filename=audio_data.getOriginalFilename();
		char[] fname=filename.toCharArray();
		for (int i=0; i<filename.length(); i++){
			if (fname[i]==':'){
				fname[i]='.';
			}
		}
		filename=fname.toString();
		if (!audio_data.isEmpty()) { 
			try { 
		        // 文件保存路径  upload  request.getSession().getServletContext().getRealPath("/") + "fileUpload/"+audio_data.getOriginalFilename();
		        String filePath = request.getSession().getServletContext().getRealPath("/") + "fileUpload/"
		            + filename; 
		        // 转存文件 
		        File file = new File(filePath);
		        audio_data.transferTo(file); 
		        char[] realfname=file.getName().toCharArray();
		        File realfile= new File("D:\\Z_graduation\\"+file.getName()+".wav");
		        file.renameTo(realfile);
		        System.out.println("filename:"+filename+",file:"+file.getPath()+",realfilename:"+realfile.getPath());
		        //resampling
		        AudioInputStream sourceStream = null;
				try {
					sourceStream = AudioSystem.getAudioInputStream(realfile);
				} catch (UnsupportedAudioFileException | IOException e1) {
					e1.printStackTrace();
				}
				final AudioFormat sourceFormat = sourceStream.getFormat();
				System.out.println("fs:"+sourceFormat.getSampleRate()+"channels:"+sourceFormat.getChannels()+"endian:"+sourceFormat.isBigEndian());
				final AudioFormat targetFormat = new AudioFormat(
			        sourceFormat.getEncoding(),
			        8000f, // target sample rate
			        sourceFormat.getSampleSizeInBits(),
			        sourceFormat.getChannels(),
			        sourceFormat.getFrameSize(),
			        8000f, // target frame rate
			        sourceFormat.isBigEndian()
				);
				System.out.println("fs:"+targetFormat.getSampleRate()+"channels:"+targetFormat.getChannels()+"endian:"+targetFormat.isBigEndian());
				filename="D:\\Z_graduation\\2\\"+filename+".wav";
				final AudioInputStream resampledStream = AudioSystem.getAudioInputStream(targetFormat, sourceStream);
				//保存文件
				try {
					AudioSystem.write(resampledStream, AudioFileFormat.Type.WAVE, new File(filename));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			} catch (Exception e) { 
				e.printStackTrace(); 
			} 
	    } 
		
		model.addAttribute("filename", filename);
		
		// 重定向 
	    return "redirect:search"; 
	  } 
	  
	  /*** 
	   * 读取上传文件中得所有文件并返回 
	   * 
	   * @return 
	   */
	  /*@RequestMapping("list") 
	  public ModelAndView list() { 
	    String filePath = request.getSession().getServletContext().getRealPath("/") + "upload/"; 
	    ModelAndView mav = new ModelAndView("list"); 
	    File uploadDest = new File(filePath); 
	    String[] fileNames = uploadDest.list(); 
	    for (int i = 0; i < fileNames.length; i++) { 
	      //打印出文件名 
	      System.out.println(fileNames[i]); 
	    } 
	    return mav; 
	  } */
	
}

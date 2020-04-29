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
import com.audio.service.AudioService;

@Controller
public class AudioController {
	@Autowired
	private AudioService audioService;
	//��ת��Ĭ�Ͻ���
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String index(){
		return "index";
	}
	
	//��ת���½���
	@RequestMapping(value="/main", method=RequestMethod.GET)
	public String main(){
		return "main";
	}
	
	//��ת���½���
	@RequestMapping(value="/search", method=RequestMethod.GET)
	public String search(){
		return "search";
	}
	
	//��ʾ�����б�
	@RequestMapping(value="/listmusic", method=RequestMethod.GET)
	public String listmusic(Model model){
		List<HashMap<String,Object>> audios = audioService.listAll();
		model.addAttribute("audios", audios);
		
		return "listmusic";
	}
	
	//���뵽���ֿ�
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public String insert(String filedir, Model model){
		//������ӹ���
		List<HashMap<String,Object>> audios=audioService.insert(filedir);
		//���ݷ���ǰ��
		model.addAttribute("audios", audios);
		//��תҳ��i
		return "listmusic";
	}
	
	//������������
	@RequestMapping(value="/search", method=RequestMethod.POST)
	public String search(String filename, Model model){
		//�����������
		List<HashMap<String, Object>> audios=audioService.search(filename);
		model.addAttribute("audios", audios);
		
		return "result";
	}
	
	/*
	@RequestMapping(value="/search", method=RequestMethod.GET)
	public String search(String filename, Model model){
		//�����������
		List<HashMap<String, Object>> audios=audioService.search(filename);
		model.addAttribute("audios", audios);
		
		return "result";
	}*/
	
	//ͨ��Spring��autowiredע���ȡspringĬ�����õ�request 
	@Autowired
	private HttpServletRequest request; 
	  
	/*** 
	 * �ϴ��ļ� ��@RequestParamע����ָ�������ϵ�fileΪMultipartFile 
	 * 
	 * @param file 
	 * @return 
	 */
	@RequestMapping("record2local") 
	public String record2local(@RequestParam("audio_data") MultipartFile audio_data,  Model model) { 
		// �ж��ļ��Ƿ�Ϊ�� 
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
		        // �ļ�����·��  upload  request.getSession().getServletContext().getRealPath("/") + "fileUpload/"+audio_data.getOriginalFilename();
		        String filePath = request.getSession().getServletContext().getRealPath("/") + "fileUpload/"
		            + filename; 
		        // ת���ļ� 
		        File file = new File(filePath);
		        audio_data.transferTo(file); 
		        char[] realfname=file.getName().toCharArray();
		        File realfile= new File("D:\\Z_����\\��Ƶ�ز�\\test\\"+file.getName()+".wav");
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
				filename="C:\\Users\\yy\\Desktop\\"+filename+".wav";
				final AudioInputStream resampledStream = AudioSystem.getAudioInputStream(targetFormat, sourceStream);
				//�����ļ�
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
		
	    // �ض��� 
	    return "redirect:search"; 
	  } 
	  
	  /*** 
	   * ��ȡ�ϴ��ļ��е������ļ������� 
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
	      //��ӡ���ļ��� 
	      System.out.println(fileNames[i]); 
	    } 
	    return mav; 
	  } */
	
}
package com.audio.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class CommonListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		ServletContext application =  arg0.getServletContext();
		
		//工程的根路径
		String root = application.getContextPath();
		application.setAttribute("audioR", root);
		application.setAttribute("js", root+"/resources/js"); //NewAudioR/resources/js
		application.setAttribute("css", root+"/resources/css");
		application.setAttribute("html", root+"/resources/html");
		application.setAttribute("fonts", root+"/resources/fonts");
		application.setAttribute("imgs", root+"/resources/imgs");
		application.setAttribute("songs", root+"/resources/songs");
	}
	
}

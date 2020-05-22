package com.audio.entity;

import java.util.List;
/**
 * 分页实体类
 *
 *
 * @param <T>
 */
public class PageBean<T> {

	private int pc;//当前页码page code
	private int tr;//总记录数total record
	private int ps = 10;//每页记录数page size
	private List<T> beanList;//当前页的记录
	
	public int getPc() {
		return pc;
	}
	public void setPc(int pc) {
		this.pc = pc;
	}
	
	//获得总页数
	public int getTp(){
		int tp = tr / ps;
		return tr%ps == 0 ? tp : tp+1;
	}
	
	public int getTr() {
		return tr;
	}
	public void setTr(int tr) {
		this.tr = tr;
	}
	public int getPs() {
		return ps;
	}
	public void setPs(int ps) {
		this.ps = ps;
	}
	public List<T> getBeanList() {
		return beanList;
	}
	public void setBeanList(List<T> beanList) {
		this.beanList = beanList;
	}
}

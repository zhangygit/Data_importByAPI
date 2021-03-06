package com.luculent.data.model;

/**
 * 
* @ClassName: SysMenuChild 
* @Description: 下拉菜单
* @author zhangy
* @date 2017年3月28日 下午3:13:59
 */
public class SysMenuChild {

	private String id;

	private String title;
	
	private String icon="&#xe641;";
	
	private String href;
	
	
	public SysMenuChild() {
		// TODO Auto-generated constructor stub
	}
	
	public SysMenuChild(String id,String title,String href) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.title = title;
		this.href = href;
	}
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
	
}

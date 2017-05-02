package com.luculent.data.constant;

/**
 * 系统常量
 */
public class DataConstant {
	
	/**
	 * 防止类被实例化
	 */
	private DataConstant() {
		// TODO Auto-generated constructor stub
		throw new AssertionError();
	}

	/** 菜单树根节点. */
	public static final String VirtualBaseTree ="VirtualBaseTree";
	
	/** 菜单路径. */
	public static final String MENU_HREF ="/api/index.htm?apiId=";
	
	
	/** 验证码. */
	public static final String CODE_NAME="验证码";
	
	
	public static final String RES_CODE_SUCCESS="000000";
	
	public static final String RES_CODE_NOTLOGIN="900003";
	
	public static final String URL_AND ="&";
	
	public static final String URL_EQUAL ="=";
	
	public static final int CONNECTION_TIMEOUT = 3;
	
	public static final String PATH = System.getProperty("user.dir") +"\\src\\main";
	
	public static final String SCHEDULER_PATH = DataConstant.PATH+"\\java\\com\\luculent\\data\\scheduler\\";
	
	public static final String SCHEDULER_PACKAGE_PATH="com.luculent.data";
	/**ocr地址*/
	public static final String OCR_PATH =PATH+"\\resources\\";
	/**图片输出地址*/
	public static final String TEMP_PATH =PATH+"\\webapp\\temp\\";
	/**图片扩展名 */
	public static final String IMG_TYPE =".gif";
	
	public static final ThreadLocal<Integer> PAGENUM = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			return 1;
		}
	};
	
	/**出错线程等待.*/
	public static final int WAIT_RUNTASK_NUM = 5;
	
	/**自动登陆重试次数(从1开始 值减1).*/
	public static final int AUTO_LOGIN_NUM =4;
	
	public static final ThreadLocal<Integer> WAITNUM = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			return 0;
		}
	};

}

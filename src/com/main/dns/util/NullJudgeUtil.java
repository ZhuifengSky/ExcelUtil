package com.main.dns.util;

/**
 * 字符空判断工具类
 * @author pc-zw
 *
 */
public class NullJudgeUtil {

	public static boolean isNull(Object object){
		if (object==null || object.equals("")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isNotNull(Object object){
		if (object!=null && !object.equals("")) {
			return true;
		} else {
			return false;
		}
	}
}

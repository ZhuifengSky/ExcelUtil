package com.main.dns.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.support.RequestContext;

public class Utility {

	private static Log logger = LogFactory.getLog(Utility.class);
	// 代理设置(HttpConnection)
	public static final Boolean PROXY = false;
	public static final String PROXY_HOSTNAME = "proxy.cmcc";
	public static final Integer PROXY_PORT = 8080;

	public static final String appId = Init.getProperty("appId"); 
	public static final String appSecret = Init.getProperty("appSecret"); 
	public static final String IS_UP_OWER_SYSOP = Init.getProperty("is_up_ower_sysop");
	public static final String DNS_INFO_URL = Init.getProperty("dns_info_url");
	public static final String DNS_UP_URL = Init.getProperty("dns_up_url");
	/** 资源服务器 */
	public static final String RESOURCE_URL_UPLOAD = "http://123.57.163.126:8080/refs";

	public static final String DATE_FROMAT_Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";
	// 添加by Gaoqs
	public static final String DATE_FROMAT_Y_M_D = "yyyy-MM-dd";

	public final static SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
	public final static SimpleDateFormat fmt2 = new SimpleDateFormat("HH:mm");
	public final static SimpleDateFormat fmt3 = new SimpleDateFormat("MM月dd日 HH:mm");
	public final static SimpleDateFormat fmt4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final static SimpleDateFormat fmt5 = new SimpleDateFormat("yyyy-MM-dd");
	public final static SimpleDateFormat fmt6 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public final static SimpleDateFormat fmt7 = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	public final static SimpleDateFormat fmt8 = new SimpleDateFormat("yyyy年MM月");
	public final static SimpleDateFormat fmt9 = new SimpleDateFormat("yyyy年MM月dd日");

	// 微信公众号
	/*public static final String appid = "wxaa1b0953262f808e";
	public static final String secret = "ecb7ee4c8cb55940fed84fb50c93c065";
	public static final String key = "Sinotsing20150525haoxuehaozhiEcc";
	public static final String mchID = "1240527102";
	public static final String ip = "123.56.110.135";*/

	// 注册消息队列
	public static final String MQ_ADDR = "101.200.80.193:9876";
	public static final String MQ_REGISTER_GROUP_QUEUE = "GROUPTEST";
	public static final String MQ_REGISTER_QUEUE = "TEST";
	public static final Integer MQ_REGISTER_THREAD = 32;


	public static final Integer pageSize = 10;

	// 验证经纬度
	public static boolean verifyLatitudeLongitude(String latitude,
			String longitude,
			String utilitylatitude,
			String utilitylongitude) {
		double latitudebg = new BigDecimal(latitude).doubleValue();
		double longitudebg = new BigDecimal(longitude).doubleValue();
		BigDecimal utilitylatitudebg = new BigDecimal(utilitylatitude);
		BigDecimal utilitylongitudebg = new BigDecimal(utilitylongitude);

		BigDecimal delta = new BigDecimal(0.015);

		double maxutilitylatitude = utilitylatitudebg.add(delta).multiply(new BigDecimal(1000000)).doubleValue();
		double minutilitylatitude = utilitylatitudebg.subtract(delta).multiply(new BigDecimal(1000000)).doubleValue();
		double maxutilitylongitude = utilitylongitudebg.add(delta).multiply(new BigDecimal(1000000)).doubleValue();
		double minutilitylongitude = utilitylongitudebg.subtract(delta).multiply(new BigDecimal(1000000)).doubleValue();

		if (longitudebg < maxutilitylongitude && longitudebg > minutilitylongitude) {
			if (latitudebg < maxutilitylatitude && latitudebg > minutilitylatitude) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取单一账单号
	 * 
	 * @return
	 */
	public static String createSingleOrderNumber() {
		StringBuffer s = new StringBuffer();
		String format = new SimpleDateFormat("HHmmssSSS").format(new Date());
		Random rd = new Random();
		int a = rd.nextInt(9) + 1;
		s.append(a);
		s.append(format);
		return s.toString();
	}

	public static String createJson(List<Map<String, Object>> jsonList) {

		if (jsonList == null)
			return null;

		JSONArray json = new JSONArray();

		int i = 0;
		for (; i < jsonList.size(); i++)
			json.add(i, jsonList.get(i));
		String ret = json.toString();
		json.clear();
		json = null;
		return ret;
	}

	public static String createJsonByListObj(List<Object> jsonList) {

		if (jsonList == null)
			return null;

		JSONArray json = new JSONArray();

		int i = 0;
		for (; i < jsonList.size(); i++)
			json.add(i, jsonList.get(i));
		String ret = json.toString();
		json.clear();
		json = null;
		return ret;
	}

	public static String createJsonStr(Map<String, Object> jsonMap) {
		if (jsonMap == null) {
			return null;
		}
		JSONObject json = new JSONObject();
		json.putAll(jsonMap);
		String ret = json.toString();
		json.clear();
		json = null;
		return ret;
	}

	public static <T> String createJsonStrByList(List<T> objList) {
		if (objList == null)
			return null;

		JSONArray json = new JSONArray();
		for (T t : objList) {
			json.add(JSONObject.fromObject(t));
		}
		String ret = json.toString();
		json.clear();
		json = null;
		return ret;
	}

	public static <T> String createJsonStrByList(List<T> objList,
			String dateFormat) {
		if (objList == null)
			return null;

		JSONArray json = new JSONArray();
		for (T t : objList) {
			json.add(buildJson(t, dateFormat));
		}
		String ret = json.toString();
		json.clear();
		json = null;
		return ret;
	}

	/**
	 * @方法名：createJsonStr
	 * @描述：生成json字符串
	 * @param jsonList
	 * @return
	 * @输出：String
	 * @作者：lixf
	 *
	 */
	public static String createJsonStr(List<String> jsonList) {

		if (jsonList == null)
			return null;

		JSONArray json = new JSONArray();

		int i = 0;
		for (; i < jsonList.size(); i++)
			json.add(i, jsonList.get(i));
		String ret = json.toString();
		json.clear();
		json = null;
		return ret;
	}

	/**
	 * 
	 * @方法名：parseJsonObj
	 * @描述：将json串转化成json对象
	 * @param jsonStr
	 *            json串
	 * @输出：JSONObject
	 * @作者：lixf
	 *
	 */
	public static JSONObject parseJsonObj(String jsonStr) {
		if (jsonStr == null) {
			return null;
		}
		JSONObject json = new JSONObject();
		json = JSONObject.fromObject(jsonStr);
		return json;
	}

	/**
	 * 
	 * @方法名：parseJsonToObject
	 * @描述：将json串转化成实体对象
	 * @param jsonString
	 *            json字符串
	 * @param clzz
	 *            类的calss
	 * @return
	 * @输出：Object
	 * @作者：lixf
	 *
	 */
	public static Object parseJsonToObject(String jsonString,
			Class<?> clzz) {
		JsonConfig jsonConfig = new JsonConfig(); // JsonConfig是net.sf.json.JsonConfig中的这个，为固定写法
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(jsonString, jsonConfig);
		return JSONObject.toBean(json, clzz);
	}

	/**
	 * 
	 * @方法名：getEntityJson
	 * @描述：获取实体json串
	 * @param t
	 * @return
	 * @输出：String
	 * @作者：lixf
	 *
	 */
	public static String getEntityJson(Object t) {

		Map<String, Object> map = new HashMap<String, Object>();

		try {

			Field[] fields = t.getClass().getDeclaredFields();

			for (Field field : fields) {
				String name = field.getName();

				if (field.getType() == Integer.class) {
					map.put(name, (Integer) field.get(t));
				}
				if (field.getType() == Long.class) {
					map.put(name, (Long) field.get(t));
				}
				if (field.getType() == Double.class) {
					map.put(name, (Double) field.get((t)));
				}
				if (field.getType() == Short.class) {
					map.put(name, (Short) field.get(t));
				}
				if (field.getType() == BigDecimal.class) {
					map.put(name, (BigDecimal) field.get(t));
				}
				if (field.getType() == String.class) {
					map.put(name, (String) field.get(t));
				}
				if (field.getType() == Date.class) {
					map.put(name, (Date) field.get(t));
				}
			}

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return Utility.createJsonStr(map);

	}

	/**
	 * 
	 * @方法名：isNumber
	 * @描述：判断是否为纯数字
	 * @param str
	 *            数字字符串
	 * @return
	 * @输出：boolean
	 * @作者：lixf
	 *
	 */
	public static boolean isNumber(String str) {
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[0-9]*");
		java.util.regex.Matcher match = pattern.matcher(str);
		if (match.matches() == false) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 
	 * @方法名：data2JsonResut
	 * @描述：对象转成json数据
	 * @param obj
	 *            需要转换的对象
	 * @return json字符串
	 * @作者：cjl
	 *
	 */
	public static String data2JsonResut(Boolean flag,
			Object obj) {
		JsonConfig jsonConfig = new JsonConfig(); // JsonConfig是net.sf.json.JsonConfig中的这个，为固定写法
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(obj, jsonConfig);
		// JSONObject json = new JSONObject();
		json.put("flag", flag);
		json.put("info", obj);
		return json.toString();
	}

	/**
	 * 
	 * @描述：构建json数据
	 * @param json
	 *            key json value
	 * @return JSONObject
	 * @作者：cjl
	 *
	 */
	public static JSONObject buildJson(String key,
			Object obj) {
		JsonConfig jsonConfig = new JsonConfig(); // JsonConfig是net.sf.json.JsonConfig中的这个，为固定写法
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(obj, jsonConfig);
		JSONObject ret = new JSONObject();
		ret.put(key, json.toString());

		return ret;
	}

	/**
	 * @描述：构建json数据
	 * @param json
	 *            key json value
	 * @return JSONObject
	 * @作者：cjl
	 *
	 */
	public static JSONObject buildJson(Object obj) {
		JsonConfig jsonConfig = new JsonConfig(); // JsonConfig是net.sf.json.JsonConfig中的这个，为固定写法
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(obj, jsonConfig);
		return json;
	}

	/**
	 * 
	 * @描述：构建json数据 格式化时间
	 * @param json
	 *            key json value
	 * @return JSONObject
	 * @作者：cjl
	 */
	public static JSONObject buildJson(Object obj,
			String dateFormat) {
		JsonConfig jsonConfig = new JsonConfig(); // JsonConfig是net.sf.json.JsonConfig中的这个，为固定写法
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor(dateFormat));
		JSONObject json = JSONObject.fromObject(obj, jsonConfig);
		return json;
	}

	/**
	 * 读取流
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String convertStreamToString(InputStream is) throws IOException {

		if (is != null) {

			StringBuilder sb = new StringBuilder();

			String line;

			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

				while ((line = reader.readLine()) != null) {

					sb.append(line).append("\n");
				}
			} catch (Exception e) {

			} finally {

				is.close();
			}
			return sb.toString();
		}

		return "";
	}

	/**
	 * 
	 * @方法名：createDate
	 * @描述： 设置延长时间
	 * @param date
	 *            开始计算时间
	 * @param addType
	 *            增加类型 - 年 月 日 时 分 秒
	 * @param count
	 *            增加数量
	 * @输出：Date
	 * @作者：lixf
	 *
	 */
	public static Date createDate(Date date,
			Integer addType,
			Integer count) {
		Calendar cdCalendar = new GregorianCalendar();
		cdCalendar.setTime(date);
		cdCalendar.add(addType, count);
		return cdCalendar.getTime();
	}

	static StringBuilder time = new StringBuilder();

	public static String secondToHours(int sec) {
		if (sec <= 0) {
			return "0分钟";
		}
		sec = Math.round(sec / 60);
		String res;
		if (sec > 60) {
			int hour = (int) Math.floor(sec / 60);
			int min = sec % 60;
			time.append(hour).append("小时:");
			if (min != 0) {
				time.append(min).append("分钟");
			}
		} else {
			time.append(sec).append("分钟");
		}
		res = time.toString();
		time.delete(0, time.length());
		return res;
	}

	/**
	 *
	 * @方法名：createPage
	 * @描述：创建分页信息
	 * @param page
	 * @param rows
	 * @return
	 * @输出：Integer
	 * @作者：lixf
	 *
	 */
	public static Integer createPage(Integer page,
			Integer rows) {

		if (page == null) {
			page = 1;
		}

		if (rows == null) {
			rows = 15;
		}

		return (page - 1) * rows;
	}

	/**
	 * 
	 * @描述：构建json数据
	 * @param json
	 *            key json value
	 * @return JSONObject
	 * @作者：cjl
	 *
	 */
	public static JSONObject buildJsonNotFormarDate(Object obj) {
		JSONObject json = JSONObject.fromObject(obj);
		return json;
	}

	/**
	 * 
	 * @方法名： getRandomString
	 * @描述： 获取随机数据
	 * @param @param length 传入随机数的位数
	 * @param @return
	 * @throws
	 * @输出：String
	 * @作者：Gaoqs
	 *
	 */
	public static String getRandomString(int length) {
		String base = "0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 
	 * @方法名： getNextYearDateForInactive
	 * @描述： 获取下一年的到期时间
	 * @param @return
	 * @throws
	 * @输出：Date
	 * @作者：Gaoqs
	 *
	 */
	public static Date getNextYearDateForInactive() {
		Calendar curr = Calendar.getInstance();
		curr.set(Calendar.YEAR, curr.get(Calendar.YEAR) + 1);
		Date date = curr.getTime();
		return date;
	}

	/**
	 * 
	 * @方法名：getAccountNo
	 * @描述：获取账户唯一编码
	 * @return
	 * @throws Exception
	 * @输出：String
	 * @作者：lixf
	 *
	 */
	public static String getAccountNo() throws Exception {
		return getRandomString(3) + createTimeStamp();
	}

	/**
	 * 
	 * @方法名：getBullPositionCode
	 * @描述：获取买家唯一编号
	 * @param provinceCode
	 *            省码
	 * @throws Exception
	 * @输出：String
	 * @作者：lixf
	 *
	 */
	public static String getBullPositionCode(String provinceCode) throws Exception {
		return provinceCode + 2 + createTimeStamp();
	}

	/**
	 * 
	 * @方法名：getDistributorCode
	 * @描述：获取分销商唯一编号
	 * @param provinceCode
	 *            省码
	 * @return
	 * @throws Exception
	 * @输出：String
	 * @作者：lixf
	 *
	 */
	public static String getDistributorCode(String provinceCode) throws Exception {
		return provinceCode + 0 + createTimeStamp();
	}

	/**
	 * 
	 * @方法名：getMerchantCode
	 * @描述：获取商户唯一编号
	 * @param provinceCode
	 *            省码
	 * @throws Exception
	 * @输出：String
	 * @作者：lixf
	 *
	 */
	public static String getMerchantCode(String provinceCode) throws Exception {
		return provinceCode + 1 + createTimeStamp();
	}

	/**
	 * 
	 * @方法名：createTimeStamp
	 * @描述：获取时间戳
	 * @return
	 * @输出：String 时间戳 格式:yyyyMMddHHmmss
	 * @作者：lixf
	 *
	 */
	public static String createTimeStamp() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	/**
	 * Create object by class name
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings({ "finally", "rawtypes" })
	public static Object createInstance(String name) {
		Object object = null;
		try {
			Class clazz = Class.forName(name);
			object = clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return object;
		}
	}

	/**
	 * 拼接rest的返回JSON
	 * 
	 * @param flag
	 *            成功200,失败500
	 * @param msg
	 *            内容
	 * @return JSON字符串
	 */
	public static String respJsonRest(boolean flag,
			String msg) {
		JSONObject json = new JSONObject();
		if (flag) {
			json.put("code", 200);
		} else {
			json.put("code", 500);
		}
		json.put("info", msg);
		if (logger.isDebugEnabled())
			logger.info(json.toString());
		return json.toString();
	}

	/**
	 * 返回JSON数据
	 * 
	 * @param flag
	 *            成功true,失败false
	 * @param obj
	 *            对象
	 * @param dateFormat
	 *            日期格式,null默认格式
	 * @return JSON字符串
	 */
	public static <T> String respJson(boolean flag,
			T obj,
			String dateFormat) {
		String jsonObj = JsonUtil.toJson(obj, dateFormat);
		JSONObject json = new JSONObject();
		json.put("flag", flag);
		json.put("info", jsonObj);
		if (logger.isDebugEnabled())
			logger.info(json.toString());
		return json.toString();
	}

	/**
	 * 返回JSON数据
	 * 
	 * @param flag
	 *            成功true,失败false
	 * @param msg
	 *            String
	 * @return JSON字符串
	 */
	public static String respJson(boolean flag,
			String msg) {
		JSONObject json = new JSONObject();
		json.put("flag", flag);
		json.put("info", msg);
		if (logger.isDebugEnabled())
			logger.info(json.toString());
		return json.toString();
	}

	public static String toJson(String status,
			String msg) {
		JSONObject json = new JSONObject();
		json.put("status", status);
		json.put("info", msg);
		if (logger.isDebugEnabled())
			logger.info(json.toString());
		return json.toString();
	}

	/**
	 * 返回JSON数据（国际化）
	 * 
	 * @param flag
	 *            成功true,失败false
	 * @param msg
	 *            String
	 * @param request
	 * @return JSON字符串
	 */
	public static String respJsonI18N(boolean flag,
			String msg,
			HttpServletRequest request) {
		String msgStr = getInternatioNalization(request, msg);
		JSONObject json = new JSONObject();
		json.put("flag", flag);
		json.put("info", msgStr);
		if (logger.isDebugEnabled())
			logger.info(json.toString());
		return json.toString();
	}

	/**
	 * @方法名：getInternatioNalization
	 * @描述：获取国际化配置中的值
	 * @param request
	 *            HttpServletRequest对象
	 * @param key
	 *            国际化配置文件中的key
	 * @return 国际化值
	 * @作者：lixf
	 */
	public static String getInternatioNalization(HttpServletRequest request,
			String key) {
		RequestContext requestContext = new RequestContext(request);
		String message = "";
		try {
			// String country = requestContext.getLocale().getCountry();
			message = requestContext.getMessage(key);
			message = new String(message.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * 设置 HttpServletRequest 与 HttpServletResponse 对象编码
	 * 
	 * @param request
	 * @param response
	 * @param characterEncoding
	 *            default UTF-8
	 * @param contentType
	 *            default text/html; charset=UTF-8
	 */
	public static void setCharacterEncoding(HttpServletRequest request,
			HttpServletResponse response,
			String characterEncoding,
			String contentType) {
		try {
			if (characterEncoding == null || characterEncoding.isEmpty()) {
				request.setCharacterEncoding("UTF-8");
			} else {
				request.setCharacterEncoding(characterEncoding);
			}

			if (contentType == null || contentType.isEmpty()) {
				response.setContentType("text/html; charset=UTF-8");
			} else {
				response.setContentType(contentType);
			}
		} catch (UnsupportedEncodingException e) {
		}
	}
	
	/**
	 * 验证手机号合法性
	 * @param matcherStr
	 * @return
	 */
	public static  boolean isMobileNumber(String matcherStr) {
		Pattern r = Pattern.compile("/^1\\d{10}$/");
	      // 现在创建 matcher 对象
	    Matcher m = r.matcher(matcherStr);
	    if(m.find()){
	    	return true;
	    }
		return false;
	}
	/**
	 * 日期格式是：0000-00-00  或者 00-00-00
	 * @param matcherStr
	 * @return
	 */
	public static  boolean isDate(String matcherStr) {
		Pattern r = Pattern.compile("/^(\\d{4}|\\d{2})-((0?([1-9]))|(1[0-2]))-((0?[1-9])|([12]([0-9]))|(3[0|1]))$/");
		// 现在创建 matcher 对象
		Matcher m = r.matcher(matcherStr);
		if(m.find()){
			return true;
		}
		return false;
	}
	public static  boolean isQQ(String matcherStr) {
		Pattern r = Pattern.compile("/^[1-9]\\d{4,}$/");
		// 现在创建 matcher 对象
		Matcher m = r.matcher(matcherStr);
		if(m.find()){
			return true;
		}
		return false;
	}

	/**
	 * 设置默认的页码和每页条数
	 * 
	 * @param pageNum
	 * @param pageSize
	 */
	public static Integer setDefaultVaule(Integer num,
			int defaultV) {
		if (num == null || (int) num == 0) {
			num = defaultV == 0 ? num :defaultV;
		}
		return num;
	}

	public static void setCharacterEncoding(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.setContentType("text/html;charset=UTF-8");
	}
 
	/**Wap网关Via头信息中特有的描述信息*/
    private static String mobileGateWayHeaders[]=new String[]{
    "ZXWAP",//中兴提供的wap网关的via信息，例如：Via=ZXWAP GateWayZTE Technologies，
    "chinamobile.com",//中国移动的诺基亚wap网关，例如：Via=WTP/1.1 GDSZ-PB-GW003-WAP07.gd.chinamobile.com (Nokia WAP Gateway 4.1 CD1/ECD13_D/4.1.04)
    "monternet.com",//移动梦网的网关，例如：Via=WTP/1.1 BJBJ-PS-WAP1-GW08.bj1.monternet.com. (Nokia WAP Gateway 4.1 CD1/ECD13_E/4.1.05)
    "infoX",//华为提供的wap网关，例如：Via=HTTP/1.1 GDGZ-PS-GW011-WAP2 (infoX-WISG Huawei Technologies)，或Via=infoX WAP Gateway V300R001 Huawei Technologies
    "XMS 724Solutions HTG",//国外电信运营商的wap网关，不知道是哪一家
    "Bytemobile",//貌似是一个给移动互联网提供解决方案提高网络运行效率的，例如：Via=1.1 Bytemobile OSN WebProxy/5.1
    };
    
    /**电脑上的IE或Firefox浏览器等的User-Agent关键词*/
    private static String[] pcHeaders=new String[]{
    "Windows 98",
    "Windows ME",
    "Windows 2000",
    "Windows XP",
    "Windows NT",
    "Ubuntu"
    };
    
    /**手机浏览器的User-Agent里的关键词*/
    private static String[] mobileUserAgents=new String[]{
    "Nokia",//诺基亚，有山寨机也写这个的，总还算是手机，Mozilla/5.0 (Nokia5800 XpressMusic)UC AppleWebkit(like Gecko) Safari/530
    "SAMSUNG",//三星手机 SAMSUNG-GT-B7722/1.0+SHP/VPP/R5+Dolfin/1.5+Nextreaming+SMM-MMS/1.2.0+profile/MIDP-2.1+configuration/CLDC-1.1
    "MIDP-2",//j2me2.0，Mozilla/5.0 (SymbianOS/9.3; U; Series60/3.2 NokiaE75-1 /110.48.125 Profile/MIDP-2.1 Configuration/CLDC-1.1 ) AppleWebKit/413 (KHTML like Gecko) Safari/413
    "CLDC1.1",//M600/MIDP2.0/CLDC1.1/Screen-240X320
    "SymbianOS",//塞班系统的，
    "MAUI",//MTK山寨机默认ua
    "UNTRUSTED/1.0",//疑似山寨机的ua，基本可以确定还是手机
    "Windows CE",//Windows CE，Mozilla/4.0 (compatible; MSIE 6.0; Windows CE; IEMobile 7.11)
    "iPhone",//iPhone是否也转wap？不管它，先区分出来再说。Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_1 like Mac OS X; zh-cn) AppleWebKit/532.9 (KHTML like Gecko) Mobile/8B117
    "iPad",//iPad的ua，Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X; zh-cn) AppleWebKit/531.21.10 (KHTML like Gecko) Version/4.0.4 Mobile/7B367 Safari/531.21.10
    "Android",//Android是否也转wap？Mozilla/5.0 (Linux; U; Android 2.1-update1; zh-cn; XT800 Build/TITA_M2_16.22.7) AppleWebKit/530.17 (KHTML like Gecko) Version/4.0 Mobile Safari/530.17
    "BlackBerry",//BlackBerry8310/2.7.0.106-4.5.0.182
    "UCWEB",//ucweb是否只给wap页面？ Nokia5800 XpressMusic/UCWEB7.5.0.66/50/999
    "ucweb",//小写的ucweb貌似是uc的代理服务器Mozilla/6.0 (compatible; MSIE 6.0;) Opera ucweb-squid
    "BREW",//很奇怪的ua，例如：REW-Applet/0x20068888 (BREW/3.1.5.20; DeviceId: 40105; Lang: zhcn) ucweb-squid
    "J2ME",//很奇怪的ua，只有J2ME四个字母
    "YULONG",//宇龙手机，YULONG-CoolpadN68/10.14 IPANEL/2.0 CTC/1.0
    "YuLong",//还是宇龙
    "COOLPAD",//宇龙酷派YL-COOLPADS100/08.10.S100 POLARIS/2.9 CTC/1.0
    "TIANYU",//天语手机TIANYU-KTOUCH/V209/MIDP2.0/CLDC1.1/Screen-240X320
    "TY-",//天语，TY-F6229/701116_6215_V0230 JUPITOR/2.2 CTC/1.0
    "K-Touch",//还是天语K-Touch_N2200_CMCC/TBG110022_1223_V0801 MTK/6223 Release/30.07.2008 Browser/WAP2.0
    "Haier",//海尔手机，Haier-HG-M217_CMCC/3.0 Release/12.1.2007 Browser/WAP2.0
    "DOPOD",//多普达手机
    "Lenovo",// 联想手机，Lenovo-P650WG/S100 LMP/LML Release/2010.02.22 Profile/MIDP2.0 Configuration/CLDC1.1
    "LENOVO",// 联想手机，比如：LENOVO-P780/176A
    "HUAQIN",//华勤手机
    "AIGO-",//爱国者居然也出过手机，AIGO-800C/2.04 TMSS-BROWSER/1.0.0 CTC/1.0
    "CTC/1.0",//含义不明
    "CTC/2.0",//含义不明
    "CMCC",//移动定制手机，K-Touch_N2200_CMCC/TBG110022_1223_V0801 MTK/6223 Release/30.07.2008 Browser/WAP2.0
    "DAXIAN",//大显手机DAXIAN X180 UP.Browser/6.2.3.2(GUI) MMP/2.0
    "MOT-",//摩托罗拉，MOT-MOTOROKRE6/1.0 LinuxOS/2.4.20 Release/8.4.2006 Browser/Opera8.00 Profile/MIDP2.0 Configuration/CLDC1.1 Software/R533_G_11.10.54R
    "SonyEricsson",// 索爱手机，SonyEricssonP990i/R100 Mozilla/4.0 (compatible; MSIE 6.0; Symbian OS; 405) Opera 8.65 [zh-CN]
    "GIONEE",//金立手机
    "HTC",//HTC手机
    "ZTE",//中兴手机，ZTE-A211/P109A2V1.0.0/WAP2.0 Profile
    "HUAWEI",//华为手机，
    "webOS",//palm手机，Mozilla/5.0 (webOS/1.4.5; U; zh-CN) AppleWebKit/532.2 (KHTML like Gecko) Version/1.0 Safari/532.2 Pre/1.0
    "GoBrowser",//3g GoBrowser.User-Agent=Nokia5230/GoBrowser/2.0.290 Safari
    "IEMobile",//Windows CE手机自带浏览器，
    "WAP2.0"//支持wap 2.0的
    };
    
    /**
    * 根据当前请求的特征，判断该请求是否来自手机终端，主要检测特殊的头信息，以及user-Agent这个header
    * @param request http请求
    * @return 如果命中手机特征规则，则返回对应的特征字符串
    */
    public static boolean isMobileDevice(HttpServletRequest request){
        boolean b = false;
        boolean pcFlag = false;
        boolean mobileFlag = false;
        String via = request.getHeader("Via");
        String userAgent = request.getHeader("user-agent");
        if(via != null && !via.trim().equals("")){
        	for (int i = 0; i < mobileGateWayHeaders.length; i++) {
        		if(via.contains(mobileGateWayHeaders[i])){
        			mobileFlag = true;
        			break;
        		}
        	}
        }
        if(userAgent != null && !userAgent.trim().equals("")){
        	for (int i = 0; !mobileFlag && i < mobileUserAgents.length; i++) {
        		if(userAgent.contains(mobileUserAgents[i])){
        			mobileFlag = true;
        			break;
        		}
        	}
        	
        	for (int i = 0; i < pcHeaders.length; i++) {
        		if(userAgent.contains(pcHeaders[i])){
        			pcFlag = true;
        			break;
        		}
        	}
        }
        if(mobileFlag == true && pcFlag == false){
            b=true;
        }
        return b;//false pc  true shouji
    
    }
}

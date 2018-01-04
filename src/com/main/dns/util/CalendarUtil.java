package com.main.dns.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {

	/**
	 * 根据起始日期和间隔时间计算结束日期
	 * 
	 * @param sDate 开始时间
	 * @param days 间隔时间
	 *            （天）
	 * @return 结束时间
	 * */
	public static Date calculateEndDate(Date sDate, int days) {
		// 将开始时间赋给日历实例
		Calendar sCalendar = Calendar.getInstance();
		sCalendar.setTime(sDate);
		// 计算结束时间
		sCalendar.add(Calendar.DATE, days);
		// 返回Date类型结束时间
		return sCalendar.getTime();
	}

	/**
	 * 根据起始日期和间隔时间计算结束日期
	 * 
	 * @param sDate 开始时间
	 * @param days 间隔时间
	 *            （天）
	 * @return 结束时间
	 * */
	public static long calculateEndTime(Date sDate, int days) {
		// 将开始时间赋给日历实例
		Calendar sCalendar = Calendar.getInstance();
		sCalendar.setTime(sDate);
		// 计算结束时间
		sCalendar.add(Calendar.DATE, days);
		// 返回Date类型结束时间
		return sCalendar.getTimeInMillis();
	}

	/**
	 * 根据起始日期和间隔时间计算结束日期
	 * 
	 * @param sDate 开始时间
	 * @param hours
	 *            间隔时间(小时)
	 * @return 结束时间
	 * */
	public static Date calculateEndHour(Date sDate, int hours) {
		// 将开始时间赋给日历实例
		Calendar sCalendar = Calendar.getInstance();
		sCalendar.setTime(sDate);
		// 计算结束时间
		sCalendar.add(Calendar.HOUR, hours);
		// 返回Date类型结束时间
		return sCalendar.getTime();
	}

	/**
	 * 根据起始日期和间隔时间计算结束日期
	 * 
	 * @param sDate 开始时间
	 * @param minutes
	 *            间隔时间(分钟)
	 * @return 结束时间
	 * */
	public static Date calculateEndMinute(Date sDate, int minutes) {
		// 将开始时间赋给日历实例
		Calendar sCalendar = Calendar.getInstance();
		sCalendar.setTime(sDate);
		// 计算结束时间
		sCalendar.add(Calendar.MINUTE, minutes);
		// 返回Date类型结束时间
		return sCalendar.getTime();
	}

	/**
	 * 根据起始日期和间隔时间计算结束日期
	 * 
	 * @param sDate 开始时间
	 * @param SECOND
	 *            间隔时间(秒)
	 * @return 结束时间
	 * */
	public static Date calculateEndSecond(Date sDate, int SECOND) {
		// 将开始时间赋给日历实例
		Calendar sCalendar = Calendar.getInstance();
		sCalendar.setTime(sDate);
		// 计算结束时间
		sCalendar.add(Calendar.SECOND, SECOND);
		// 返回Date类型结束时间
		return sCalendar.getTime();
	}

	/**
	 * 字符串去除两头空格，如果为空，则返回""，如果不空，则返回该字符串去掉前后空格
	 * 
	 * @param tStr 输入字符串
	 * @return 如果为空，则返回""，如果不空，则返回该字符串去掉前后空格
	 */
	public static String cTrim(String tStr) {
		String ttStr = "";
		if (tStr == null) {
		} else {
			ttStr = tStr.trim();
		}
		return ttStr;
	}

	/**
	 * 计算两个日期的时间间隔
	 * 
	 * @param sDate 开始时间
	 * @param eDate 结束时间
	 * @param type 间隔类型
	 *            ("Y/y"--年 "M/m"--月 "D/d"--日)
	 * @return interval时间间隔
	 * */
	public static int calInterval(Date sDate, Date eDate, String type) {
		// 时间间隔，初始为0
		int interval = 0;
		/* 比较两个日期的大小，如果开始日期更大，则交换两个日期 */
		// 标志两个日期是否交换过
		boolean reversed = false;
		if (compareDate(sDate, eDate) > 0) {
			Date dTest = sDate;
			sDate = eDate;
			eDate = dTest;
			// 修改交换标志
			reversed = true;
		}

		/* 将两个日期赋给日历实例，并获取年、月、日相关字段值 */
		Calendar sCalendar = Calendar.getInstance();
		sCalendar.setTime(sDate);
		int sYears = sCalendar.get(Calendar.YEAR);
		int sMonths = sCalendar.get(Calendar.MONTH);
		int sDays = sCalendar.get(Calendar.DAY_OF_YEAR);

		Calendar eCalendar = Calendar.getInstance();
		eCalendar.setTime(eDate);
		int eYears = eCalendar.get(Calendar.YEAR);
		int eMonths = eCalendar.get(Calendar.MONTH);
		int eDays = eCalendar.get(Calendar.DAY_OF_YEAR);

		// 年
		if (cTrim(type).equals("Y") || cTrim(type).equals("y")) {
			interval = eYears - sYears;
			if (eMonths < sMonths) {
				--interval;
			}
		}
		// 月
		else if (cTrim(type).equals("M") || cTrim(type).equals("m")) {
			interval = 12 * (eYears - sYears);
			interval += (eMonths - sMonths);
		}
		// 日
		else if (cTrim(type).equals("D") || cTrim(type).equals("d")) {
			interval = 365 * (eYears - sYears);
			interval += (eDays - sDays);
			// 除去闰年天数
			while (sYears < eYears) {
				if (isLeapYear(sYears)) {
					--interval;
				}
				++sYears;
			}
		}
		// 如果开始日期更大，则返回负值
		if (reversed) {
			interval = -interval;
		}
		// 返回计算结果
		return interval;
	}

	/**
	 * 输出日历相关字段（当前日期）
	 * 
	 * @param cNow 当前时间
	 * @return null 各个字段值都可以用get(field)得到，也可以用set(field, value)函数修改
	 * */
	public static void printFields(Calendar cNow) {
		// 先用Date类型输出验证
		SimpleDateFormat df = (SimpleDateFormat) DateFormat.getInstance();
		df.applyPattern("yyyy-MM-dd HH:mm:ss");
		System.out.println("标准日期:" + df.format(new Date()));
		// 逐个输出相关字段值
		System.out.print("年份:" + cNow.get(Calendar.YEAR) + "\t");
		// 月份有问题(这里的月份开始计数为0)
		System.out.print("月份:" + cNow.get(Calendar.MONTH) + "\t");
		System.out.print("日期:" + cNow.get(Calendar.DATE) + "\t");
		System.out.print("小时:" + cNow.get(Calendar.HOUR) + "\t");
		System.out.print("分钟:" + cNow.get(Calendar.MINUTE) + "\t");
		System.out.println("秒钟:" + cNow.get(Calendar.SECOND));
		// 本年的第几天,在计算时间间隔的时候有用
		System.out.println("一年中的天数:" + cNow.get(Calendar.DAY_OF_YEAR));
		System.out.println("一年中的周数:" + cNow.get(Calendar.WEEK_OF_YEAR));
		// 即本月的第几周
		System.out.println("一月中的周数:" + cNow.get(Calendar.WEEK_OF_MONTH));
		// 即一周中的第几天(这里是以周日为第一天的)
		System.out.println("一周中的天数:" + cNow.get(Calendar.DAY_OF_WEEK));
	}

	/**
	 * 判定某个年份是否是闰年
	 * 
	 * @param year 待判定的年份
	 * @return 判定结果
	 * */
	private static boolean isLeapYear(int year) {
		return (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0));
	}

	/**
	 * 比较两个Date类型的日期大小
	 * 
	 * @param sDate 开始时间
	 * @param eDate 结束时间
	 * @return result返回结果(0--相同 1--前者大 -1--后者大)
	 * */
	public static int compareDate(Date sDate, Date eDate) {
		int result = 0;
		// 将开始时间赋给日历实例
		Calendar sC = Calendar.getInstance();
		sC.setTime(sDate);
		// 将结束时间赋给日历实例
		Calendar eC = Calendar.getInstance();
		eC.setTime(eDate);
		// 比较
		result = sC.compareTo(eC);
		// 返回结果
		return result;
	}

	public static Date intToDate(int time) {
		String temp = String.valueOf(time) + "000";
		Date date = new Date(Long.valueOf(temp));
		return date;
	}

	public static String intToDate(int time, String type) {
		String temp = String.valueOf(time) + "000";
		Date date = new Date(Long.valueOf(temp));
		SimpleDateFormat sfd = new SimpleDateFormat(type);
		return sfd.format(date);
	}
	
	public static String longToDate(long time, String type) {
 		Date date = new Date(Long.valueOf(time));
		SimpleDateFormat sfd = new SimpleDateFormat(type);
		return sfd.format(date);
	}

	public static int longToInt(long time) {
		return Integer.valueOf(String.valueOf(String.valueOf(time).substring(0,10)));
	}
	
	public static long intToLong(int time){
		String temp = String.valueOf(time) + "000";
 		return Long.valueOf(temp);
	}

	/**
	 * 将 yyyy-MM-dd 格式日期转换成时间戳
	 * @return
	 * @throws ParseException 
	 */
	public static long StringToTimestamp(String strTime) throws ParseException{
		 SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");  
		 Date date = format.parse(strTime);
		 return date.getTime();
	}
	
	/**
	 * 比较两个time类型的日期大小
	 * 
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @return result返回结果(0--相同 1--前者大 -1--后者大)
	 * */
	public static int compareTime(int sTime, int eTime) {
		int result = 0;
		if (sTime > eTime) {
			result = 1;
		} else if (sTime == eTime) {
			result = 0;
		} else {
			result = -1;
		}
		return result;
	}

	// MINUTE SECOND
	public static int SubtractTime(int sTime, int eTime, String type) {
		int jetLagTime = eTime - sTime;
		int jetLag = 0;
		if ("MINUTE".equals(type)) {
			jetLag = jetLagTime / (1000 * 60);
		} else if ("SECOND".equals(type)) {
			jetLag = jetLagTime / 1000;
		} else if ("HOUR".equals(type)) {
			jetLag = jetLagTime / (1000 * 60 * 60);
		}
		return jetLag;
	}

	/**
	 * 计算time和当前时间之间的时间间隔，超过一个月按年月日显示，超过24小时以天计算显示，否则按小时计算显示
	 * 
	 * @param time
	 * @return
	 */
	public static String TimeInterval(long time) {
		String timeInterval = "";
  		long jetLagTime = new Date().getTime()-intToLong((int) time);
 		if (jetLagTime / 1000 < 60) {
			timeInterval = jetLagTime / 1000 + "秒前";
		} else if (jetLagTime / (1000 * 60) < 60) {
			timeInterval = jetLagTime / (1000 * 60) + "分前";
		} else if (jetLagTime / (1000 * 60 * 60) < 24) {
			timeInterval = jetLagTime / (1000 * 60 * 60) + "小时前";
		} else if (jetLagTime / (1000 * 60 * 60 * 24) > 0) {
			if (jetLagTime / (1000 * 60 * 60 * 24) < 30) {
				BigDecimal days = new BigDecimal(jetLagTime / (1000 * 60 * 60 * 24)).setScale(0,BigDecimal.ROUND_HALF_UP);
				timeInterval = days + "天前";
			} else {
				timeInterval = longToDate(intToLong((int) time), "yyyy-MM-dd");
			}
		}
		 
		return timeInterval;
	}
	/**
	 * 获取系统当前年份
	 * @return 当前年份
	 */
	public static int getYear(){
		Calendar calendar=Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}
	/**
	 * 获取系统当前月份
	 * @return 当前月份
	 */
	public static int getMonth(){
		Calendar calendar=Calendar.getInstance();
		return calendar.get(Calendar.MONTH)+1;
	}
	/**
	 * 获取系统当前所在月份的日期
	 * @return 当前所在月份的日期
	 */
	public static int getDate(){
		Calendar calendar=Calendar.getInstance();
		return calendar.get(Calendar.DATE);
	}

	public static void main(String agrs[]) {
 		System.out.println(new Date().getTime());
 		
  		System.out.println(CalendarUtil.getDate());
//		Date date = new Date();
//		int now = CalendarUtil.longToInt(CalendarUtil.calculateEndTime(date, 1));
//		
//		System.out.println(now);
//		
//		System.out.println(longToInt(CalendarUtil.calculateEndDate(date, 31).getTime()));
//
//		System.out.println(CalendarUtil.intToDate(1456482443, "yyyy-MM-dd HH:mm:ss"));
//
//		System.out.println(TimeInterval(new Date().getTime(),intToLong(1456571638)));
 
		
	//	Calendar sCalendar = Calendar.getInstance();
	//	printFields(sCalendar);

		/*
		 * // 获取日历实例并赋予当前时间 Calendar cNow = Calendar.getInstance();
		 * cNow.setTime(new Date()); // 输出日历相关字段 printFields(cNow);
		 * 
		 * 计算两个日期的时间间隔 // 开始时间 Date sDate = new Date(); // 结束时间 try { Date eDate
		 * = (new SimpleDateFormat("yyyy-MM-dd")).parse("2012-05-29"); //
		 * 计算结果并输出 System.out.println("今天与2020-05-29相隔:" + calInterval(sDate,
		 * eDate, "d") + "天"); } catch (ParseException e) { e.printStackTrace();
		 * }
		 * 
		 * 计算结束日期并输出 SimpleDateFormat df = (SimpleDateFormat)
		 * DateFormat.getInstance(); df.applyPattern("yyyy-MM-dd");
		 * System.out.println("从几天开始计算，2天后是:" +
		 * df.format(calculateEndDate(sDate, 2)));
		 */
		
	}

}

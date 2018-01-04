package com.main.dns.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class Init {
	private static HashMap<String, String> tjProperty;

	public Init() {
		tjProperty = new HashMap<String, String>();
	}

	static {
		try {
			reloadAll();
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	private static void reloadAll() {
		if (tjProperty == null)
			tjProperty = new HashMap<String, String>();
		strageMsg();
	}

	private static void strageMsg() {
		HashMap<String, String> map = new HashMap<String, String>();
		Properties p = new Properties();
		try {
			p.load((Init.class).getClassLoader().getResourceAsStream("com/config/config.properties"));
			String key;
			String value;
			for (Enumeration<?> enu = p.propertyNames(); enu.hasMoreElements(); map.put(key, value)) {
				key = (String) enu.nextElement();
				value = p.getProperty(key);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e.toString());
		} catch (IOException io) {
			throw new RuntimeException(io.toString());
		}
		tjProperty = map;
	}

	public static int getPageSize(String key) {
		int str = 10;
		if (tjProperty != null) {
			if (tjProperty.containsKey(key)) {
				str = Integer.valueOf(tjProperty.get(key)).intValue();
			}
		}
		return str;
	}

	public static String getProperty(String key) {
		String str = "";
		if (tjProperty != null) {
			if (tjProperty.containsKey(key)) {
				str = tjProperty.get(key);
			}
		}
		return str;
	}

	public static void println(String message) {
		String console = Init.getProperty("CONSOLE");
		if (console != null && console.trim().equalsIgnoreCase("TRUE")) {
			System.out.println(message);
		}
	}

	public static void println(Exception e) {
		String console = Init.getProperty("CONSOLE");
		if (console != null && console.trim().equalsIgnoreCase("TRUE")) {
			System.out.println(e.getLocalizedMessage());
		}
	}
}

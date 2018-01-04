package com.test;

import java.math.BigDecimal;

public class TestTransTime {

	public static void main(String[] args) {
		 long length = 333;
		 BigDecimal a = new BigDecimal(length);
		 BigDecimal b = new BigDecimal(60);
		 String result = "0";
		 if (length>0) {
			 BigDecimal temp = a.divide(b,2,BigDecimal.ROUND_HALF_UP);
			 result =  temp.toString();
		}
		System.out.println(result);
	}
}

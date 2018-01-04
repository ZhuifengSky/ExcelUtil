package com.main.dns.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
  
public class JsonDateValueProcessor implements JsonValueProcessor {  
  
    private String format ="yyyy-MM-dd";  
    
    public JsonDateValueProcessor(){
    	
    }
      
    public JsonDateValueProcessor(String format){
    	this.format = format;
    }
    
    public Object processArrayValue(Object value, JsonConfig config) {  
        return process(value);  
    }  
  
    public Object processObjectValue(String key, Object value, JsonConfig config) {  
        return process(value);  
    }  
    
    private Object process(Object value){  
          
        if(value instanceof Date ||value instanceof Timestamp ){  
            SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.UK);  
            return sdf.format(value);  
        }  
        return value == null ? "" : value.toString();  
    }  
}

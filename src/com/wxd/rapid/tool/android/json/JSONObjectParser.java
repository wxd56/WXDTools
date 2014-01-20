package com.wxd.rapid.tool.android.json;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.util.Log;

/**
 * 将给定的json字符串解析成一个类（通过反射技术）
 * 
 * @author 王旭东
 *
 */
public class JSONObjectParser {
	
	public static <T> T parseObject(String jsonStr, Class<T> clazz) throws Exception{
		//得到所有属性列表
		List<Field> props = getClassProps(clazz);
		
		//组装成Json对象
		JSONObject jsonObj = new JSONObject(jsonStr);
		
		//从Json对象中取出数据
		T result = clazz.newInstance();
		for(Field field : props){
			
			Object value = null;
			
			if(field.getType().isAssignableFrom(String.class)){
				value = jsonObj.getString(field.getName());
			}else if(field.getType().isAssignableFrom(Integer.class)){
				value = jsonObj.getInt(field.getName());
			}
			
			
			if(value != null){				
				String firstChar = field.getName().substring(0,1).toUpperCase();
				String methodName = "set"+ firstChar + field.getName().substring(1);
				
				Log.d("wxd test", "为属性["+field.getName() +"]赋值," +"调用方法："+methodName);				
				clazz.getMethod(methodName, field.getType()).invoke(result, value);
			}			
		}
		
		//返回结果		
		return result;
	}
	
	private static <T> List<Field> getClassProps(Class<T> clazz){
		List<Field> props = new ArrayList<Field>();
		Field[] fields = clazz.getDeclaredFields();
	
		for(Field f:fields){
			props.add(f);
		}
		
		Log.d("wxd test", "解析class属性结果为："+props);
		return props;
	}
}

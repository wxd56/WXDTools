package com.wxd.rapid.tool.android.json;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.util.Log;

/**
 * 将给定的json字符串解析成一个类（通过反射技术）。<br>
 * 支持的自动转换的类型为：String，Integer，Boolean，Long，Double。如果都不匹配则返回Object类型。
 * @author 王旭东
 *
 */
public class JSONObjectParser {
	
	/**
	 * 将给定的json字符串解析成一个类（通过反射技术）。<br>
	 * 支持的自动转换的类型为：String，Integer，Boolean，Long，Double。如果都不匹配则返回Object类型。
	 * 
	 *@param jsonStr Json字符串
	 *@param clazz   需要被转换的类型
	 *@param return  如果参数jsonStr为空，则直接返回Null。
	 *
	 */
	public static <T> T parseObject(String jsonStr, Class<T> clazz) throws Exception{
		//得到所有属性列表
		List<Field> props = getClassProps(clazz);
		
		//组装成Json对象
		Log.d("wxd test", "解析服务器端返回的Json字符串为："+jsonStr);
		if(jsonStr == null){
			return null;
		}
			
		JSONObject jsonObj = new JSONObject(jsonStr);
		
		//从Json对象中取出数据
		T result = clazz.newInstance();
		for(Field field : props){
			
			Object value = null;
			
			if(field.getType().isAssignableFrom(String.class)){
				value = jsonObj.getString(field.getName());
			}else if(field.getType().isAssignableFrom(Integer.class)){
				value = jsonObj.getInt(field.getName());
			}else if(field.getType().isAssignableFrom(Boolean.class)){
				value = jsonObj.getBoolean(field.getName());
			}else if(field.getType().isAssignableFrom(Long.class)){
				value = jsonObj.getLong(field.getName());
			}else if(field.getType().isAssignableFrom(Double.class)){
				value = jsonObj.getDouble(field.getName());
			}else{
				value = jsonObj.get(field.getName());
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

package com.wxd.rapid.tool.android.json;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.util.Log;

/**
 * ��������json�ַ���������һ���ࣨͨ�����似������<br>
 * ֧�ֵ��Զ�ת��������Ϊ��String��Integer��Boolean��Long��Double���������ƥ���򷵻�Object���͡�
 * @author ����
 *
 */
public class JSONObjectParser {
	
	/**
	 * ��������json�ַ���������һ���ࣨͨ�����似������<br>
	 * ֧�ֵ��Զ�ת��������Ϊ��String��Integer��Boolean��Long��Double���������ƥ���򷵻�Object���͡�
	 * 
	 *@param jsonStr Json�ַ���
	 *@param clazz   ��Ҫ��ת��������
	 *@param return  �������jsonStrΪ�գ���ֱ�ӷ���Null��
	 *
	 */
	public static <T> T parseObject(String jsonStr, Class<T> clazz) throws Exception{
		//�õ����������б�
		List<Field> props = getClassProps(clazz);
		
		//��װ��Json����
		Log.d("wxd test", "�����������˷��ص�Json�ַ���Ϊ��"+jsonStr);
		if(jsonStr == null){
			return null;
		}
			
		JSONObject jsonObj = new JSONObject(jsonStr);
		
		//��Json������ȡ������
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
				
				Log.d("wxd test", "Ϊ����["+field.getName() +"]��ֵ," +"���÷�����"+methodName);				
				clazz.getMethod(methodName, field.getType()).invoke(result, value);
			}			
		}
		
		//���ؽ��		
		return result;
	}
	
	private static <T> List<Field> getClassProps(Class<T> clazz){
		List<Field> props = new ArrayList<Field>();
		Field[] fields = clazz.getDeclaredFields();
	
		for(Field f:fields){
			props.add(f);
		}
		
		Log.d("wxd test", "����class���Խ��Ϊ��"+props);
		return props;
	}
}

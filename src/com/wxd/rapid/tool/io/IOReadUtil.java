package com.wxd.rapid.tool.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * ���������Ĺ�����
 * @author admin
 *
 */
public class IOReadUtil {
	
	/**
	 * ��ȡ������������ȡ��������Ϊ�ַ�������
	 * @param is
	 * @param encode ����
	 * @throws IOException 
	 */
	public static  String read(InputStream is,String encode) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024*4];
		
		int readCount = is.read(buffer,0,buffer.length);
		while(readCount > 0){
			baos.write(buffer,0,readCount);
			readCount = is.read(buffer,0,buffer.length);
		}
		
		return new String(baos.toByteArray(),encode);
	}
}

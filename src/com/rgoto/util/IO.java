/**
 * IO类
 */
package com.rgoto.util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class IO {
	//String url = String.class.getResource("/test/sql.txt").getFile();
	
	/**
	 * 	持久化对象
	 * @param filename 文件名
	 * @param object 要保存的对象
	 */
	public static void setObject(String filename,Object object){
			try {
				String filedir = System.getProperty("user.dir") + "/object/";
				File f=new File(filedir);
				if (!f.exists()) {
					f.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(filedir + filename);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(object);
				oos.close();
				fos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	/**
	 * 	读取持久化对象
	 * @param filename 文件名
	 */
		public static Object getObject(String filename){
			Object o = null;
			try {
				String filedir = System.getProperty("user.dir") + "/object/";
				File f=new File(filedir);
				if (!f.exists()) {
					f.mkdirs();
				}
				f=new File(filedir + filename);
				if (!f.exists()) {
					return null;
				}
				FileInputStream fis = new FileInputStream(filedir + filename);
				ObjectInputStream ois = new ObjectInputStream(fis);
				o = ois.readObject();
				ois.close();
				fis.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return o;
		}

		/**
		 * 读取文本或zip，zip只能包含一个文件
		 * @param filename 文件名
		 * @return BufferedReader
		 */
		@SuppressWarnings("resource")
		public static BufferedReader getBuffer(String filename){
			BufferedReader br = null;
			if(filename.endsWith(".zip")){
				try {
					ZipFile zf = new ZipFile(filename);
					InputStream in = new BufferedInputStream(new FileInputStream(filename));
					ZipInputStream zin = new ZipInputStream(in);
					ZipEntry ze;
					while ((ze = zin.getNextEntry()) != null) {
						if (!ze.isDirectory()){
							 br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
							 break;
						}	 
					}		 
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				try {
					FileReader fr = new FileReader(filename);
					br = new BufferedReader(fr);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			return br;
	}
	
	/**
	 * 字符串写入文件
	 * @param str 字符串
	 * @param filename 文件
	 */
	public static boolean writer(String filename, String str){
		return writer(filename,str,"utf-8");
	}
	/**
	 * 字符串写入文件
	 * @param filename 文件
	 * @param str 字符串
	 * @param encode 编码
	 * @return
	 */
	public static boolean writer(String filename, String str,String encode){
		boolean bo = false;
		FileOutputStream fos;
		File f = new File(filename);
		if(!f.getParentFile().exists()) f.getParentFile().mkdirs();
		try {
			fos = new FileOutputStream(filename);
			fos.write(str.getBytes(encode));
			fos.close();
			bo = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bo;
	}
	
	/**
	 * 读取文本文件
	 * @param filename 文件名
	 * @param encode 编码
	 * @return 字符串
	 */
	public static String reader(String filename,String encode){
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(filename), encode);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			StringBuilder sb = new StringBuilder();
			while((line=br.readLine())!=null){
				sb.append(line + "\r\n");
			}
			br.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String reader(String filename){
		return reader(filename,"utf-8");
	}
	
	/**
	 * 复制文件
	 * @param source_file 源文件
	 * @param target_file 目标文件
	 * @return
	 */
	public static boolean copy(String source_file, String target_file){
		boolean bo = false;
	    try {
	    	File s = new File(source_file);
			if(!s.exists()) return bo;
			File f = new File(target_file);
			if(!f.getParentFile().exists()) f.getParentFile().mkdirs();
	    	InputStream input = new FileInputStream(new File(source_file));
	    	OutputStream output = new FileOutputStream(new File(target_file));        
	    	byte[] buf = new byte[1024];        
	    	int bytesRead;        
	    	while ((bytesRead = input.read(buf)) > 0) {
	    		output.write(buf, 0, bytesRead);
	    	}
	    	input.close();
	    	output.close();
	    	bo = true;
	    }catch (IOException e) {
			e.printStackTrace();
		}
	    return bo;
	}
	
	/**
	 * 获取文件行数，异常返回-1
	 * @param filename 文件全路径
	 * @return 行数
	 */
	public static long getFileLineCount(String filename){
		int lineNo = -1;
		try {
			LineNumberReader lnr = new LineNumberReader(new FileReader(filename));
	        lnr.skip(Long.MAX_VALUE);
	        lineNo = lnr.getLineNumber() + 1;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lineNo;
	}
		
		
	public static void main(String[] args) {
		String str = "abc";
		IO.writer("/Users/remind/Downloads/aaa/a.txt", str);
	}

}

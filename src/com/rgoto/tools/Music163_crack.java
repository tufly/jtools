package com.rgoto.tools;

import com.rgoto.util.IO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//网易云音乐：音乐缓存转mp3
public class Music163_crack {
	
	public static void main(String[] args) throws IOException, SQLException {
		//输出路径
		String out_path = "/Users/remind/Downloads/163music/";
		//歌单ID，只下载这个歌单的文件.留空则转换所有缓存
		String loveid = "14264582";
		
		Long a = System.currentTimeMillis();
		//缓存路径
		String path = System.getProperty("user.home") +"/Library/Containers/com.netease.163music/Data/Library/Caches/online_play_cache/";
		if(!new File(path).exists()) path = System.getProperty("user.home") + "/Library/Containers/com.netease.163music/Data/Caches/online_play_cache/";
		if(!new File(path).exists()){
			System.out.println("找不到缓存目录");
			return;
		}
		File f=new File(path);
        String[] str=f.list();
        int add = 0;
        int cache = 0;
        
        File o = new File(out_path);
		if(!o.exists()) o.mkdirs();
        
      //只需要自己喜欢的
        List<String> loves = new ArrayList<String>();
		if(loveid != null && loveid.length() > 0){
			String love_url = "http://music.163.com/playlist?id=" + loveid;
    		Document doc = Jsoup.connect(love_url).get();
    		Elements es = doc.select(".n-songtb").get(0).select(".f-hide");
    		if(es.size() > 0){
    			Element ul = es.get(0);
    			Elements lis = ul.select("li");
    			for(Element li:lis){
    				String id = li.select("a").get(0).attr("href").replace("/song?id=", "");
    				loves.add(id+"");
//    				System.out.println(li + "");
    			}
    		}
		}
		
//		System.out.println("love=" + loves);
        
        for (int i = 0; i < str.length; i++) {
        	String filename = str[i];
        	if(filename.contains(".uc!")){
        		cache++;
        		String id = filename.split("-_-_")[0];
        		
        		//只需要自己喜欢的
        		if(loveid != null && loveid.length() > 0){
            		if(!loves.contains(id)) {
//            			System.out.println("no love,id=" + id);
            			continue;
            		}
        		}
        		
//        		System.out.println(filename + ",id=" + id);
        		String url = "https://music.163.com/song?id=" + id;
        		Document r = Jsoup.connect(url).get();
        		String title = r.title();
        		String[] ts = title.split(" - ");
        		String real_name = title;
        		if(ts.length > 1) real_name = ts[0] + " - " + ts[1];
    			real_name=real_name.replace("/", "_").replace("\\", "_");
    			//扩展名
    			String ext = "mp3";
    			String info_file = path + filename.replace(".uc!", ".info");
    			if(new File(info_file).exists()){
    				String json = IO.reader(info_file);
        			ext = json.substring(json.indexOf("\"format\":\"")+10, json.lastIndexOf("\""));
    			}
    			String target_file = out_path + real_name + "." + ext;
    			String source_file = path + filename;
    			if(!new File(target_file).exists()){
    				System.out.println("source=" + source_file);
        			System.out.println("target=" + target_file);
    				File inFile = new File(source_file);  
    		         File outFile = new File(target_file);  
    		         DataInputStream dis = new DataInputStream( new FileInputStream(inFile));  
    		         DataOutputStream dos = new DataOutputStream( new FileOutputStream(outFile));  
    		         byte[] by = new byte[1000];  
    		         int len;  
    		         while((len=dis.read(by))!=-1){  
    		             for(int ii=0;ii<len;ii++){  
    		                 by[ii]^=0xa3;  //云音乐的缓存文件加密算法十分简单，只是对每一个字进行了异或操作。
    		             }  
    		             dos.write(by,0,len);  
    		         }  
    		         dis.close();  
    		         dos.close();
    		         add++;
    			}
        	} 
        }
        
        Long b = System.currentTimeMillis();
        System.out.println("完成!缓存" + cache + "个文件,新下载" + add + "个文件。[费时:" + (b-a) + "ms]");
	}
}

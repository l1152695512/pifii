package com.yinfu.business.freemarker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.util.PropertyUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class AudioMarker {

    private Configuration cfg;
    private static AudioMarker m_instance = null;
    private Object shopId = "0";
    private boolean bool = false;
    private String rootPath = PathKit.getWebRootPath()+File.separator+"file"+File.separator+"freemarker"+File.separator;
    private String savePath = rootPath+"html"+File.separator;
    
    
	public AudioMarker() {
		try {
        	cfg = new Configuration();
            cfg.setDefaultEncoding("UTF-8");
			cfg.setDirectoryForTemplateLoading(new File(rootPath+File.separator+"ftl"));
		} catch (IOException e) {
		}
	}
	
    public synchronized static AudioMarker getInstance() {
		if (m_instance == null) {
			m_instance = new AudioMarker();
		}
		return m_instance;
	}
    
    /**
     * 根据shopId创建电台html
     * @param shopId
     * @return
     */
    public boolean createHtml(Object shopId){
		try {
			this.shopId = shopId;
			Map root = new HashMap();
			root.put("audiolist", getAudio());

			Template t = cfg.getTemplate("audio.ftl");
			String dir = savePath + shopId + File.separator + "mb"+ File.separator + "audio";
			File htmlFile = new File(dir);
			htmlFile.mkdirs();
			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(dir + File.separator + "audio.html"),
					"UTF-8"));
			t.process(root, out);
			out.flush();
			out.close();
			bool = true;
			System.out.println("生成完毕");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bool;
    }
   
    
    private List<Record> getAudio(){
    	String idType = PropertyUtils.getProperty("route.upload.type.audio", "audio");
    	String imgDir = PropertyUtils.getProperty("downdir.audioImg", "/storageroot/Data/mb/audio/logo");
    	String[] img = imgDir.split("/");
    	imgDir = img[img.length-1];
    	
    	StringBuffer sql = new StringBuffer("select ");
    	sql.append("concat(?,'_',id) as id,name,");
    	sql.append("concat(?,'/',ifnull(reverse(left(reverse(icon),locate('/',reverse(icon))-1)),'')) as icon,");
    	sql.append("ifnull(reverse(left(reverse(link),locate('/',reverse(link))-1)),'') as link ");
    	sql.append("from bp_audio ");
    	sql.append("where status=1 and delete_date is null ");
    	List<Record> list = Db.find(sql.toString(), new Object[]{idType,imgDir});
    	return list;
    }
    
    
    public static void main(String[] args)throws Exception{
    	
    }
}

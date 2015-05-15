package com.yinfu.jbase.util.packageData;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import com.jfinal.kit.PathKit;

/**
 * 监听视频、音乐、app、小说、游戏的添加删除操作，然后更新到Data包中打包
 * @author l
 *
 */
public class ListenDataFileChange {
    private static final String DATA_PATH = "file"+File.separator+"init"+File.separator+"Data";  
    public static void listenFile() {
    	String dataAbsolutePath = PathKit.getWebRootPath() + File.separator + DATA_PATH;
        File DataDir = FileUtils.getFile(dataAbsolutePath);  
        FileAlterationObserver observer = new FileAlterationObserver(DataDir);  
        observer.addListener(new FileAlterationListenerAdaptor() {  
                @Override  
                public void onFileCreate(File file) {  
                    System.out.println("File created: " + file.getName());  
                }
                @Override  
                public void onFileDelete(File file) {  
                    System.out.println("File deleted: " + file.getName());  
                }  
                @Override
                public void onFileChange(File file) {
	                
                }
                @Override  
                public void onDirectoryCreate(File dir) {  
                    System.out.println("Directory created: " + dir.getName());  
                }  
                @Override  
                public void onDirectoryDelete(File dir) {  
                    System.out.println("Directory deleted: " + dir.getName());  
                }  
                @Override
                public void onDirectoryChange(File directory) {
	                
                }
        });  
        FileAlterationMonitor monitor = new FileAlterationMonitor(500, observer);  
        try {  
            monitor.start();  
//            monitor.stop();  
        } catch (IOException e) {  
            e.printStackTrace();  
            throw new RuntimeException("start listen file server error!");
        } catch (InterruptedException e) {  
            e.printStackTrace();  
            throw new RuntimeException("start listen file server error!");
        } catch (Exception e) {  
            e.printStackTrace();  
            throw new RuntimeException("start listen file server error!");
        }  
    } 
}

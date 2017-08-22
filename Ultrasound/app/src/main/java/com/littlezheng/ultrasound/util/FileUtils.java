package com.littlezheng.ultrasound.util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxp on 2017/7/22.
 */

public class FileUtils {

    private static final String TAG = "FileUtils";


    /**
     * 列举出所有的快照文件夹
     * @param path 路径
     * @return
     */
    public static List<String> listFolders(String path){
        List<String> folders = new ArrayList<>();
        File pathFile = new File(Environment.getExternalStorageDirectory(), path);
        File[] files = pathFile.listFiles();
        if(files != null){
            for(File file : files){
                if(!file.isFile()){
                    folders.add(file.getName());
                }
            }
        }
        return folders;
    }

    /**
     * 列举出指定文件夹下的所有文件
     * @param folderName
     * @return
     */
    public static List<File> listFilesInFolder(String folderName){
        List<File> files = new ArrayList<>();
        File path = new File(Environment.getExternalStorageDirectory(),folderName);
        File[] allFiles = path.listFiles();
        if(allFiles != null){
            for(File file : allFiles){
                if(file.isFile()) files.add(file);
            }
        }
        return files;
    }


    /**
     * 关闭流
     * @param br
     * @param ir
     * @param in
     */
    public static void closeQuietly(BufferedReader br, InputStreamReader ir, InputStream in){
        if(br != null){
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(ir != null){
            try {
                ir.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(in != null){
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 删除指定文件
     * @param pathName 路径与文件名
     * @return
     */
    public static boolean delete(String pathName) {
        File file = new File(pathName);
        return file.delete();
    }

}

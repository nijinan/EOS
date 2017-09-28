package cn.edu.pku.EOS.util;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList; 
import java.util.Enumeration; 
import java.util.List; 
import java.util.zip.CRC32; 
import java.util.zip.CheckedOutputStream; 
import java.util.zip.Deflater; 
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile; 
import org.apache.tools.zip.ZipOutputStream;
import org.eclipse.core.runtime.Path;

/**
  * @Description: 
  *     压缩和解压工具
 */ 
public class ZipUtil {
    public static String zipAll(List<String> sourcePath ,String zipPath){
        File sourceFile = new File(sourcePath.get(0));
        if (!sourceFile.exists() || (sourceFile.isDirectory() && sourceFile.list().length == 0)) {
            return "";
        }
        String sourcePathName = "";
        // 设置压缩文件路径，默认为将要压缩的路径的父目录为压缩文件的父目录
        if (zipPath == null || "".equals(zipPath)) {
            sourcePathName = sourceFile.getParentFile().getAbsolutePath();
            zipPath = sourceFile.getParentFile().getAbsolutePath() + Path.SEPARATOR + sourceFile.getParentFile().getName()+".zip";
            //int index = sourcePathName.lastIndexOf("\\");
            //zipPath = (index > -1 ? sourcePathName.substring(0, index) : sourcePathName) + ".zip";
        }
        List<String> paths = sourcePath;
        compressFilesZip(paths.toArray(new String[paths.size()]),zipPath,sourcePathName    );
        return zipPath;
    }
    /**
      * @Description: 
      *     压缩文件
      * @param sourcePath 将要压缩的文件或目录的路径，请使用绝对路径
      * @param zipPath 生成压缩文件的路径，请使用绝对路径。如果该路径以“.zip”为结尾，
      *         则压缩文件的名称为此路径；如果该路径不以“.zip”为结尾，则压缩文件的名称
      *         为该路径加上将要压缩的文件或目录的名称，再加上以“.zip”结尾
      * @param encoding 压缩编码
      * @param comment 压缩注释
     */
    public static String zip(String sourcePath ,String zipPath){
        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists() || (sourceFile.isDirectory() && sourceFile.list().length == 0)) {
            return "";
        }
        // 设置压缩文件路径，默认为将要压缩的路径的父目录为压缩文件的父目录
        if (zipPath == null || "".equals(zipPath)) {
            String sourcePathName = sourceFile.getAbsolutePath();
            zipPath = sourceFile.getAbsolutePath()+".zip";
            //int index = sourcePathName.lastIndexOf("\\");
            //zipPath = (index > -1 ? sourcePathName.substring(0, index) : sourcePathName) + ".zip";
        } else {
            // 如果压缩路径为目录，则将要压缩的文件或目录名做为压缩文件的名字，这里压缩路径不以“.zip”为结尾则认为压缩路径为目录
            if(!zipPath.endsWith(".zip")){
                // 如果将要压缩的路径为目录，则以此目录名为压缩文件名；如果将要压缩的路径为文件，则以此文件名（去除扩展名）为压缩文件名
                String fileName = sourceFile.getName();
                int index = fileName.lastIndexOf(".");
                zipPath = zipPath + File.separator + (index > -1 ? fileName.substring(0, index) : fileName) + ".zip";
            }
        }
        List<String> paths = getFiles(sourcePath);
        File zipFile = new File(zipPath);
        if (!zipFile.exists())
        compressFilesZip(paths.toArray(new String[paths.size()]),zipPath,sourcePath    );
        return zipPath;
    }
    /**
     * 递归取到当前目录所有文件
     * @param dir
     * @return
     */
    public static List<String> getFiles(String dir){
        List<String> lstFiles = null;
        if(lstFiles == null){
            lstFiles = new ArrayList<String>();
        }
        File file = new File(dir);
        File [] files = file.listFiles();
        for(File f : files){
            if(f.isDirectory()){
                lstFiles.add(f.getAbsolutePath());
                lstFiles.addAll(getFiles(f.getAbsolutePath()));
            }else{
                String str =f.getAbsolutePath();
                lstFiles.add(str);
            }
        }
        return lstFiles;
    }

    /**
     * 文件名处理
     * @param dir
     * @param path
     * @return
     */
    public static String getFilePathName(String dir,String path){
        String p = path.replace(dir+File.separator, "");
        p = p.replace("\\", "/");
        return p;
    }
    /**
     * 把文件压缩成zip格式
     * @param files         需要压缩的文件
     * @param zipFilePath 压缩后的zip文件路径   ,如"D:/test/aa.zip";
     */
    public static void compressFilesZip(String[] files,String zipFilePath,String dir) {
        if(files == null || files.length <= 0) {
            return ;
        }
        ZipArchiveOutputStream zaos = null;
        try {
            File zipFile = new File(zipFilePath);
            zaos = new ZipArchiveOutputStream(zipFile);
            zaos.setUseZip64(Zip64Mode.AsNeeded);
            //将每个文件用ZipArchiveEntry封装
            //再用ZipArchiveOutputStream写到压缩文件中
            for(String strfile : files) {
                File file = new File(strfile);
                if(file != null) {
                    String name = getFilePathName(dir,strfile);
                    ZipArchiveEntry zipArchiveEntry  = new ZipArchiveEntry(file,name);
                    zaos.putArchiveEntry(zipArchiveEntry);
                    if(file.isDirectory()){
                        continue;
                    }
                    InputStream is = null;
                    try {
                        is = new BufferedInputStream(new FileInputStream(file));
                        byte[] buffer = new byte[1024 ];
                        int len = -1;
                        while((len = is.read(buffer)) != -1) {
                            //把缓冲区的字节写入到ZipArchiveEntry
                            zaos.write(buffer, 0, len);
                        }
                        zaos.closeArchiveEntry();
                    }catch(Exception e) {
                        throw new RuntimeException(e);
                    }finally {
                        if(is != null)
                            is.close();
                    }

                }
            }
            zaos.finish();
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally {
            try {
                if(zaos != null) {
                    zaos.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}  
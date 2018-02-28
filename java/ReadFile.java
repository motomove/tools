package com.whaty.platform.common.util;

import com.whaty.core.framework.util.FileUtils;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import org.apache.commons.collections.CollectionUtils;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadFile {


    public ReadFile() {
    }

    public static void main(String[] args) {
        try {
//			readAllFiles("E:\\www\\webapps");
//			readfile("E:\\www\\webapps");
//			readfile("/www/webapps");

            readAllFiles("E:\\www\\xnkj2\\backup");
//			System.out.println(ReadFile.getMimeType("E:\\www\\xnkj2\\backup\\网梯资源平台.docx"));  
//		     output :  text/plain

//			 MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");  
//		        File f = new File ("E:\\www\\xnkj2\\backup\\Wildlife.mp4");  
//		        Collection<?> mimeTypes = MimeUtil.getMimeTypes(f);  
//		        System.out.println(mimeTypes);  

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("ok");
    }

    public static Map<String, List<Object[]>> findAllFilesToMap(List<Object[]> list) {

        Map<String, List<Object[]>> dirTree = new HashMap<String, List<Object[]>>();

        try {
            if (CollectionUtils.isNotEmpty(list)) {
                for (Object[] objects : list) {
                    String pid = objects[4].toString();
                    if (dirTree.containsKey(pid)) {
                        List<Object[]> listo = dirTree.get(pid);
                        listo.add(objects);
                    } else {
                        List<Object[]> listo = new ArrayList<Object[]>();
                        listo.add(objects);
                        dirTree.put(pid, listo);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }

        return dirTree;
    }

    /**
     * 遍历目录下文件并保存
     *
     * @param filePath   目录
     * @param result 存储结果集:key 文件路径，value List<fileName>
     */
    public static Map<String, List<String>> ergoFiles(String filePath, Map<String, List<String>> result) {
        File f = new File(filePath);
        if (f.isDirectory()) {
            File[] fileArray = f.listFiles();
            for (File file : fileArray) {
                String fileName = file.getAbsolutePath();
                File fl = new File(fileName);
                if (fl.isDirectory()) {
                    ergoFiles(fileName, result);
                } else {
                    if (result != null && result.containsKey(filePath)) {
                        List<String> li = result.get(filePath);
                        li.add(fileName);
                    } else {
                        List<String> li = new ArrayList<String>();
                        li.add(fileName);
                        result.put(filePath, li);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 遍历目录下文件并保存
     *
     * @param filePath   目录
     * @param result 存储结果集:key 文件路径，value List<fileName>
     */
    public static List<String> ergoFiles(String filePath, List<String> result) {
        File f = new File(filePath);
        if (f.isDirectory()) {
            File[] fileArray = f.listFiles();
            for (File file : fileArray) {
                String fileName = file.getAbsolutePath();
                File fl = new File(fileName);
                if (fl.isDirectory()) {
                    ergoFiles(fileName, result);
                } else {
                    result.add(fileName);
                }
            }
        }
        return result;
    }

    /**
     * 读取目录下所有文件
     */
    public static List<String> readFilesByPath(String filepath) {
        List<String> filesList = new ArrayList<String>();
        File file = new File(filepath);
        if (file.isDirectory()) {
            filesList = ergoFiles(filepath, filesList);
        }
        return filesList;
    }

    /**
     * 读取目录下所有文件
     */
    public static Map<String, List<String>> readAllFilesByPath(String filepath) {
        Map<String, List<String>> filesMap = new HashMap<String, List<String>>();
        File file = new File(filepath);
        if (file.isDirectory()) {
            filesMap = ergoFiles(filepath, filesMap);
        }
        return filesMap;
    }

    public static Map<String, List<Object[]>> readAllFiles(String filepath) {

        Map<String, List<Object[]>> dirTree = new HashMap<String, List<Object[]>>();

        try {
            File file = new File(filepath);
            if (file.isDirectory()) {
                List<Object[]> resultList = new ArrayList<Object[]>();
                List<Object[]> list = ergoResource(filepath, resultList);

                if (CollectionUtils.isNotEmpty(list)) {
                    //开始---------组装根目录
                    Object[] dirArrays = new Object[8];
                    dirArrays[0] = "根目录文件";
                    dirArrays[1] = file.getPath();
                    dirArrays[2] = file.getAbsolutePath();
                    dirArrays[3] = file.getName();
                    dirArrays[4] = "root";
                    dirArrays[5] = "0";
                    dirArrays[6] = "0";
                    dirArrays[7] = "2";
                    list.add(0, dirArrays);
                    //结束---------组装根目录
                    for (Object[] objects : list) {
                        System.out.println(objects[0] + "|" + objects[1] + "|" + objects[2] + "|" + objects[3] + "|" + objects[4] + "|" + objects[5] + "|" + objects[6] + "|" + objects[7]);
                        String pid = objects[4].toString();
                        if (dirTree.containsKey(pid)) {
                            List<Object[]> listo = dirTree.get(pid);
                            listo.add(objects);
                        } else {
                            List<Object[]> listo = new ArrayList<Object[]>();
                            listo.add(objects);
                            dirTree.put(pid, listo);
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }

        return dirTree;
    }

    public static List<Object[]> ergoResource(String filepath, List<Object[]> resultFileName) {
        File file = new File(filepath);
        String[] files = file.list();
        if (files == null) return resultFileName;// 判断目录下是不是空的
        for (int i = 0; i < files.length; i++) {
            File readfile = new File(filepath + File.separator + files[i]);
            if (readfile.isDirectory()) {// 判断是否文件夹
                Object[] dirArrays = new Object[8];
                dirArrays[0] = "文件夹";
                dirArrays[1] = readfile.getPath();
                dirArrays[2] = readfile.getAbsolutePath();
                dirArrays[3] = readfile.getName();
                dirArrays[4] = readfile.getParentFile().getAbsolutePath();
                dirArrays[5] = "0";
                dirArrays[6] = i + 1;
                dirArrays[7] = "2";
                resultFileName.add(dirArrays);
                ergoResource(filepath + File.separator + files[i], resultFileName);// 调用自身,查找子目录

            } else if (!readfile.isDirectory()) {
                Object[] dirArrays = new Object[8];
                dirArrays[0] = "文件";
                dirArrays[1] = readfile.getPath();
                dirArrays[2] = readfile.getAbsolutePath();
                dirArrays[3] = readfile.getName();
                dirArrays[4] = readfile.getParentFile().getAbsolutePath();
                dirArrays[5] = "1";
                dirArrays[6] = i + 1;
                dirArrays[7] = "2";
                resultFileName.add(dirArrays);

            }
        }
        return resultFileName;
    }


    public static MultimediaInfo createVideoJpg(File video, String videoPath) {
        MultimediaInfo info = null;
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            Encoder encoder = new Encoder();
            String v = FileUtils.getFileSaveRealPath(videoPath);
            v = v.substring(0, v.lastIndexOf(".")) + ".jpg";
            info = encoder.getInfo(video, new File(v));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static String getMimeType(String fileUrl) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(fileUrl);
        return type;
    }

    /**
     * 递归获取目录树
     *
     * @param filepath
     * @param resultFileName
     * @return List<Object[]>
     */
    public static List<Object[]> ergodic(String filepath, List<Object[]> resultFileName) {
        File file = new File(filepath);
        String[] files = file.list();
        if (files == null) return resultFileName;// 判断目录下是不是空的
        for (int i = 0; i < files.length; i++) {
            File readfile = new File(filepath + File.separator + files[i]);
            if (readfile.isDirectory()) {// 判断是否文件夹
                Object[] dirArrays = new Object[8];
                dirArrays[0] = "文件夹";
                dirArrays[1] = readfile.getPath();
                dirArrays[2] = readfile.getAbsolutePath();//该文件唯一路径标示
                dirArrays[3] = readfile.getName();
                dirArrays[4] = readfile.getParentFile().getAbsolutePath(); //标示父类ID
                dirArrays[5] = "0";
                dirArrays[6] = i + 1;
                dirArrays[7] = "2";
                resultFileName.add(dirArrays);
                ergodic(filepath + File.separator + files[i], resultFileName);// 调用自身,查找子目录
            }
        }
        return resultFileName;
    }


    /**
     * 组装目录树结构
     *
     * @param filepath
     * @return Map<String, List<Object[]>>
     */
    public static Map<String, List<Object[]>> readAllDirs(String filepath) {
        Map<String, List<Object[]>> dirTree = new HashMap<String, List<Object[]>>();

        try {
            File file = new File(filepath);
            if (file.isDirectory()) {
                String rootName = file.getName();
                List<Object[]> resultList = new ArrayList<Object[]>();
                //Object[]
                List<Object[]> list = ergodic(filepath, resultList);

                if (CollectionUtils.isNotEmpty(list)) {
                    //开始---------组装根目录
                    Object[] dirArrays = new Object[8];
                    dirArrays[0] = "根目录文件";
                    dirArrays[1] = file.getPath();
                    dirArrays[2] = file.getAbsolutePath();
                    dirArrays[3] = rootName;
                    dirArrays[4] = "root";
                    dirArrays[5] = "0";
                    dirArrays[6] = "0";
                    dirArrays[7] = "2";
                    list.add(0, dirArrays);
                    //结束---------组装根目录
                    for (Object[] objects : list) {
                        System.out.println(objects[0] + "|" + objects[1] + "|" + objects[2] + "|" + objects[3] + "|" + objects[4] + "|" + objects[5] + "|" + objects[6] + "|" + objects[7]);
                        String pid = objects[4].toString();
                        if (dirTree.containsKey(pid)) {
                            List<Object[]> listo = dirTree.get(pid);
                            listo.add(objects);
                        } else {
                            List<Object[]> listo = new ArrayList<Object[]>();
                            listo.add(objects);
                            dirTree.put(pid, listo);
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }

        return dirTree;
    }


}

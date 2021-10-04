package com.wjh.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件相关的工具类
 */
public class FileUtil {
    /**
     * 删除文件或文件夹
     *
     * @param path 例如：d:/tmp   d:/    d:/x.txt
     */
    public static void delete(String path) {
        File file = new File(path);
        if (file.isFile()) {//文件可直接删除
            file.delete();
        } else if (file.isDirectory()) {//空文件夹才能直接删除
            String[] subFileNames = file.list();//文件夹file中的文件、文件夹
            if (subFileNames == null) {//文件不存在或其它错误
                return;
            }
            //空文件夹可直接删除
            if (subFileNames.length == 0) {
                file.delete();
            }
            //清空文件夹中的内容
            for (String subFileName : subFileNames) {
                String absolutePath = file.getAbsolutePath();
                absolutePath = absolutePath.endsWith("/") || absolutePath.endsWith("\\") ? absolutePath : absolutePath + "/";

                String subFilePath = absolutePath + subFileName;
                delete(subFilePath);
            }
            //删除空文件夹
            file.delete();
        }
    }


    /**
     * 将bytes数据写入filePath文件中
     */
    public static boolean write(byte[] bytes, String filePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(bytes);
        fos.close();
        return true;
    }
}
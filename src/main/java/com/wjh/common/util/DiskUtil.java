package com.wjh.common.util;

import java.io.File;
import java.util.Date;

public class DiskUtil {
    /**
     * 获取当前电脑中所有的磁盘名
     *
     * @return 例如：{C,D,E,F}
     */
    public static String[] allDisks() {
        File[] files = File.listRoots();
        String[] disks = new String[files.length];

        for (int i = 0; i < files.length; i++) {
            // file.toString() 返回 C:\
            String plateName = files[i].toString().substring(0, 1);
            disks[i] = plateName;
        }
        return disks;
    }
}

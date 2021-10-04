package com.wjh.common.util;

import com.wjh.basic.date.DateUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LogUtil {
    public static void log(String msg) {
        try {
            FileOutputStream fos = new FileOutputStream("./disk-safe-clear.log", true);

            String nowTime = DateUtil.nowDateString(DateUtil.DEFAULT_MASK_DATE_TIME);
            String log = String.format("%s===%s\n", nowTime, msg);
            System.out.print(log);
            if (fos != null) fos.write(log.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

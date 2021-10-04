package com.wjh.filler;

import com.wjh.UIWindow;
import com.wjh.basic.date.DateUtil;
import com.wjh.common.Constant;
import com.wjh.common.DiskStatusEnum;
import com.wjh.common.util.FileUtil;
import com.wjh.common.util.LogUtil;

import java.io.IOException;

public class DefaultDiskFillerImpl implements DiskFiller {
    // 16MB的块 B=byte
    private static final byte[] block16MB = new byte[16 * 1024 * 1024];
    // 1MB的块
    private static final byte[] block1MB = new byte[1 * 1024 * 1024];
    // 1kB的块
    private static final byte[] block1KB = new byte[1 * 1024];
    // 1B的块,为尽可能做到100%的数据安全，所以精确到1byte
    private static final byte[] block1B = new byte[1];


    /**
     * 清理磁盘
     *
     * @param diskName 磁盘 例如 D
     */
    @Override
    public Thread fill(String diskName) {
        Thread thread = new Thread(() -> {
            String timeMask = "yyyyMMddHHmmss";
            try {
                LogUtil.log(String.format("使用16M块填充%s盘", diskName));
                while (true) {
                    FileUtil.write(block16MB, diskName + ":/" + DateUtil.nowDateString(timeMask) + Constant.TMP_FILE_SUFFIX);
                }
            } catch (Exception e) {// 剩余空间少于16MB
                if (!isIOException(e)) {
                    e.printStackTrace();
                    return;
                }
                try {
                    LogUtil.log(String.format("使用1M块填充%s盘", diskName));
                    while (true) {
                        FileUtil.write(block1MB, diskName + ":/" + DateUtil.nowDateString(timeMask) + Constant.TMP_FILE_SUFFIX);
                    }
                } catch (Exception e2) {// 剩余空间少于1MB
                    if (!isIOException(e)) {
                        e.printStackTrace();
                        return;
                    }
                    try {
                        LogUtil.log(String.format("使用1KB块填充%s盘", diskName));
                        while (true) {
                            FileUtil.write(block1KB, diskName + ":/" + DateUtil.nowDateString(timeMask) + Constant.TMP_FILE_SUFFIX);
                        }
                    } catch (Exception e3) {//  剩余空间少于1KB
                        if (!isIOException(e)) {
                            e.printStackTrace();
                            return;
                        }
                        try {
                            LogUtil.log(String.format("使用1B块填充%s盘", diskName));
                            while (true) {
                                FileUtil.write(block1B, diskName + ":/" + DateUtil.nowDateString(timeMask) + Constant.TMP_FILE_SUFFIX);
                            }
                        } catch (Exception e4) {
                            if (!isIOException(e)) {
                                e.printStackTrace();
                                return;
                            }

                            LogUtil.log(String.format("%s盘已填满", diskName));

                            // 清理填充文件
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                            UIWindow.diskNameAndProgressLabelMap.get(diskName).setText("完成");
                            UIWindow.diskNameAndClearThreadMap.remove(diskName);
                            UIWindow.diskStatusMap.put(diskName, DiskStatusEnum.RELEASING);
                            FileUtil.delete(diskName + ":/");
                            LogUtil.log(String.format("清理完成%s盘", diskName));
                        }
                    }
                }
            }
        });
        thread.start();
        return thread;
    }


    private boolean isIOException(Exception e) {
        if (e == null) return false;
        return e.getClass().equals(IOException.class);
    }
}

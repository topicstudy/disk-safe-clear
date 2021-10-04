package com.wjh.reporter;

import com.wjh.UIWindow;
import com.wjh.basic.collection.CollectionUtil;
import com.wjh.basic.number.DoubleUtil;
import com.wjh.common.util.LogUtil;

import javax.swing.*;
import java.io.File;
import java.util.Map;
import java.util.Set;

/**
 * 用1个线程报告所有进度
 */
public class DefaultProgressReporter implements ProgressReporter {
    private static boolean hasOpenReportThread = false;

    @Override
    public synchronized void report() {
        if (hasOpenReportThread) return;

        Map<String, JLabel> diskNameAndProgressLabelMap = UIWindow.diskNameAndProgressLabelMap;
        if (diskNameAndProgressLabelMap == null) return;

        Map<String, Thread> diskNameAndClearThreadMap = UIWindow.diskNameAndClearThreadMap;
        if (diskNameAndClearThreadMap == null) return;

        Thread thread = new Thread(() -> {
            hasOpenReportThread = true;
            while (true) {
                // 每100ms报告一次进度
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 需要报告进度的磁盘
                Set<String> needReportProgressDiskNames = diskNameAndClearThreadMap.keySet();
                if (CollectionUtil.isEmpty(needReportProgressDiskNames)) continue;
                for (String diskName : needReportProgressDiskNames) {
                    JLabel progressLabel = diskNameAndProgressLabelMap.get(diskName);
                    if (progressLabel == null) continue;

                    Thread clearThread = diskNameAndClearThreadMap.get(diskName);
                    if (clearThread == null) continue;

                    File diskFile = new File(diskName + ":/");
                    Long usableSpace = diskFile.getUsableSpace();
                    Long totalSpace = diskFile.getTotalSpace();
                    if (usableSpace.equals(0)) {// 已填充满
                        LogUtil.log(diskName + "盘已填充满");
                        progressLabel.setText("100%");
                        diskNameAndClearThreadMap.remove(diskName);
                        diskNameAndClearThreadMap.remove(diskName);
                        continue;
                    }

                    // 报告进度
                    double d = 1 - (usableSpace.doubleValue()) / (totalSpace.doubleValue());//totalSpace可能超过int最大值(21亿)
                    String clearProgress = DoubleUtil.percentage(d, 2);
                    progressLabel.setText(clearProgress);
                    if (clearProgress.length() > 8) {
                        LogUtil.log(String.format("clearProgress is %s, and usableSpace is %s, totalSpace is %s",
                                clearProgress, usableSpace, totalSpace));
                    }
                }
            }
        });

        thread.setDaemon(true);
        thread.setName("clearProgressThread");
        thread.start();
    }
}

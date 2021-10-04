package com.wjh;

import com.wjh.basic.collection.CollectionUtil;
import com.wjh.basic.file.FileUtil;
import com.wjh.basic.text.StringUtil;
import com.wjh.common.Constant;
import com.wjh.common.DiskStatusEnum;
import com.wjh.common.util.DiskUtil;
import com.wjh.common.util.SwingUtil;
import com.wjh.common.util.LogUtil;
import com.wjh.filler.DefaultDiskFillerImpl;
import com.wjh.filler.DiskFiller;
import com.wjh.reporter.DefaultProgressReporter;
import com.wjh.reporter.ProgressReporter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.*;

/**
 * 使用页面（只写展示和交互代码，不要在这里做逻辑处理！）
 */
public class UIWindow {
    public static DiskFiller diskFiller = new DefaultDiskFillerImpl();
    public static ProgressReporter progressReporter = new DefaultProgressReporter();
    // 磁盘对应的进度标签，k:磁盘名称,v:清理进度的标签
    public static Map<String, JLabel> diskNameAndProgressLabelMap = new HashMap();
    // 磁盘对应的清理线程，k:磁盘名称,v:线程
    public static Map<String, Thread> diskNameAndClearThreadMap = new HashMap();
    // 磁盘状态,k:磁盘名称，v:磁盘状态
    public static Map<String, DiskStatusEnum> diskStatusMap = new HashMap<>();

    private static JFrame jFrame = new JFrame();
    private static Container contentPane = jFrame.getContentPane();

    static {
        initWindow();
    }

    private static void initWindow() {
        Rectangle screenInfo = SwingUtil.screenInfo();
        int screenWidth = screenInfo.width;
        int screenHeight = screenInfo.height;

        int w = screenWidth / 2;
        int h = (int) (0.618 * w);// 使window是黄金矩形

        jFrame.setVisible(true);
        jFrame.setSize(w, h);
        jFrame.setLocation(screenWidth / 2 - w / 2, screenHeight / 2 - h / 2);// 使window居中
        jFrame.setResizable(false);
        jFrame.setTitle(Constant.APP_NAME);
        jFrame.setLayout(new FlowLayout(FlowLayout.LEFT));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        URL url = UIWindow.class.getClassLoader().getResource("logo.png");//TODO 静态资源路径
        LogUtil.log("logo.png's url is " + url);// jar:file:/D:/code/IdeaProjects/disk-safe-clear/target/disk-safe-clear-ga.jar!/logo.png
        jFrame.setIconImage(new ImageIcon(url).getImage());
    }

    /**
     * 整个项目的入口
     *
     * @param args
     */
    public static void main(String[] args) {
        String[] allPlateNames = DiskUtil.allDisks();
        for (final String diskName : allPlateNames) {
            // 清理按钮
            JButton startClearButton = new JButton(String.format("开始清理【%s】盘", diskName));
            contentPane.add(startClearButton);

            // 清理进度
            JLabel progressLabel = new JLabel("-");
            contentPane.add(progressLabel);
            diskNameAndProgressLabelMap.put(diskName, progressLabel);

            nextLine(contentPane);

            // 磁盘状态
            diskStatusMap.put(diskName, DiskStatusEnum.INIT);

            startClearButton.addActionListener((ActionEvent e) -> {
                // 判断磁盘是否允许被清理
                if (isBlockedDiskName(diskName)) return;

                diskStatusMap.put(diskName, DiskStatusEnum.FILLING);

                // 清理磁盘
                progressLabel.setText("0%, 马上开始清理...");
                LogUtil.log(String.format("开始清理%s盘", diskName));
                FileUtil.delete(diskName + ":/");
                Thread fillThread = diskFiller.fill(diskName);
                diskNameAndClearThreadMap.put(diskName, fillThread);
            });
        }

        // 展示该磁盘的清理进度
        progressReporter.report();

        // 刷新swing页面
        contentPane.validate();
    }


    /**
     * 判断磁盘是否正在被清理
     *
     * @param diskName
     * @return
     */
    private synchronized static boolean isClearing(String diskName) {
        if (StringUtil.isBlank(diskName)) throw new RuntimeException("disName不可为空");
        Set<String> clearingDiskNames = diskNameAndClearThreadMap.keySet();
        return CollectionUtil.isNotEmpty(clearingDiskNames) && clearingDiskNames.contains(diskName);
    }

    /**
     * 判断磁盘是否可以被清理
     *
     * @param diskName
     * @return
     */
    private static boolean isBlockedDiskName(String diskName) {
        if (StringUtil.isBlank(diskName)) throw new RuntimeException("disName不可为空");

        // 该磁盘正在清理，就不开清理线程了
        if (isClearing(diskName)) {
            JOptionPane.showMessageDialog(null, diskName + "盘已在清理中",
                    "提示", JOptionPane.ERROR_MESSAGE);
            return true;
        }

        // 永远不允许清除C盘数据
        if ("C".equalsIgnoreCase(diskName)) {
            JOptionPane.showMessageDialog(null, "不允许C盘数据，因为C盘通常是系统盘",
                    "提示", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        // 测试不允许清理C盘和D盘，防止测试误操作，造成灾难
        if (Constant.TEST_MODE) {
            if ("C".equalsIgnoreCase(diskName) || "D".equalsIgnoreCase(diskName)) {
                JOptionPane.showMessageDialog(null, "测试时不允许清理C、D盘",
                        "提示", JOptionPane.ERROR_MESSAGE);
                return true;
            }
        }
        return false;
    }


    /**
     * 独占一行，实现换行效果
     */
    private static void nextLine(Container container) {
        String longBlankString = "";
        for (int i = 0; i < 50; i++) longBlankString += "                      ";
        container.add(new JLabel(longBlankString));
    }
}

package com.wjh.common.util;

import java.awt.*;

public class SwingUtil {
    public static Rectangle screenInfo(){
        GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle rectangle=ge.getMaximumWindowBounds();
       return rectangle;
    }
}

package com.wjh.common;

/**
 * init==click button==>filling====>releasing====>finished
 */
public enum DiskStatusEnum {
    INIT("初始态"),
    FILLING("正在填充"),
    RELEASING("正在释放无用文件"),
    FINISHED("已完成");

    private String desc;

    DiskStatusEnum(String desc) {
        this.desc = desc;
    }
}

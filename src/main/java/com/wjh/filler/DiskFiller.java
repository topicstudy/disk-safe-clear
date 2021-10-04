package com.wjh.filler;

/**
 * 填充U盘
 */
public interface DiskFiller {
    /**
     * 填充磁盘
     *
     * @param diskName 磁盘 例如 D
     */
    Thread fill(String diskName);
}

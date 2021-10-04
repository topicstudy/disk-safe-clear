# 介绍
该应用用于清除电脑磁盘，清除后无法恢复，使用电脑自带的文件删除功能删除的文件是可以被恢复回来的。换新电脑、离职交电脑前可以该软件清除电脑中的数据。

该应用不允许清除C盘的数据（因为一般C盘装了操作系统，重要文件一般不存C盘）

# 仓库

* github   https://github.com/luotuoshamo/disk-safe-clear.git

* gitee https://gitee.com/smlt_1_wjh_q/disk-safe-clear.git

  

## 原理
* 直接删除磁盘上的文件是可以被恢复回来的的
* 格式化磁盘后，文件也可恢复
* 我的做法：删除磁盘中的文件=>用无用文件覆盖磁盘空间=>删除无永文件，这样就恢复出来的也是无用文件
* 验证：删完后可用数据恢复软件测试下（我用的恢复软件是 
[易极数据恢复 DRInstaller_1.0.2.3_tx.exe ](https://gitee.com/smlt_1_wjh_q/release/blob/master/apps/%E6%98%93%E6%9E%81%E6%95%B0%E6%8D%AE%E6%81%A2%E5%A4%8DDRInstaller_1.0.2.3_tx.exe)

## 运行
1. 环境要求

   配置好Java和Maven的环境变量

| 环境  | 版本 |
| ----- | ---- |
| Java  | 1.8  |
| Maven | 3    |

2. 双击bin文件夹中的start.bat运行程序

> 如果发现老文件无法删除，需要以管理员权限运行start.bat






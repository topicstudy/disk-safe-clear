# 介绍
该应用用于清除电脑磁盘，清除后无法恢复，使用电脑自带的文件删除功能删除的文件是可以被恢复回来的。换新电脑、离职交电脑前可以该软件清除电脑中的数据。

该软件不能清除C盘的数据（因为一般C盘装了操作系统，重要文件不存在C盘）

## 原理
* 直接删除（ctrl+d或右键文件->删除）磁盘上的文件是可以被恢复的
* 格式化磁盘后，文件也可恢复
* 我的做法：删除磁盘中的文件=>用无用的数据覆盖磁盘空间=>删除无用文件
* 删完后可用数据恢复软件测试下是否可以恢复出来东西（我用的恢复软件是易极数据恢复 DRInstaller_1.0.2.3_tx.exe）

## 运行
环境要求：

| 环境  | 版本 |
| ----- | ---- |
| Java  | 1.8  |
| Maven | 3    |

## 方式一:直接运行源码

运行`com.wjh.UIWindow#main`

## 方式二：打包运行

在项目根目录(pom.xml)运行maven打包

```shell
git clone ghttps://gitee.com/open_projects/cleardisk.git
cd ./cleardisk
mvn package
```

打包完成后进入target文件夹，运行jar包：

```sh
cd ./target
java -classpath cleardisk-xxx.jar com.wjh.UIWindow
```








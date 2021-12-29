# NewAudioR 
audio recognition

# 网页版听歌识曲

一、音乐库
目前包含1k+音乐


二、功能包括
###### 1. 音乐库管理（增删改查）
![image](https://user-images.githubusercontent.com/49663646/147631824-8b1ecbb9-9dfa-4833-982e-de6091021d3a.png)



###### 2. 录音识曲和在线试听
![image](https://user-images.githubusercontent.com/49663646/147631889-1225c9cf-f98c-49ab-b19a-332392ce285a.png)
![image](https://user-images.githubusercontent.com/49663646/147631896-84450a61-22c6-4f72-9815-f1e86a675e20.png)
![image](https://user-images.githubusercontent.com/49663646/147631907-fdbfd7dd-83d6-458f-b567-8d4f15fcc7db.png)



###### 3. 测试算法识别准确率（模拟加噪环境下）
![image](https://user-images.githubusercontent.com/49663646/147631953-7045eeb8-d51e-4170-a67b-f76c96cafa40.png)
![image](https://user-images.githubusercontent.com/49663646/147631960-f77d417c-d461-4ecf-bfe5-2a0c2eb701aa.png)



三、算法
###### 基于Shazam公司的Landmark算法


四、算法效果
###### 1、在音频截取时长不低于4秒时，检索成功率很高。
###### 2、加入辅助检索后，识别准确率增加。
###### 3、系统抗噪能力强，音乐识别速度较快，符合性能需求。
###### 4、系统的最佳参数是：音乐片段时长10秒以上，几乎可以确保音乐识别成功率。



五、 展望
###### 1、 在识别准确率方面，音频截取时长低于4秒时，测试准确率比较低。
###### 2、 在时长方面，特别是添加音乐到音乐库时的时长有待提高，识别时长也有提升空间。
###### 3、 同时音乐库的体量也有待增大。
###### 4、 音乐可以使用爬虫爬取







六、 遇到的问题
###### #遇到的问题1：数据库语句运行太慢了

###### #数据库配置优化了 闲云野鹤大佬帮忙改的 2020-5-4

###### 1. engine改成了MyISAM，适合读多的
CREATE TABLE `hashtable` (
  `idhashtable` int(11) NOT NULL AUTO_INCREMENT,
  `hash` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  `time` int(11) NOT NULL,
  PRIMARY KEY (`idhashtable`),
  KEY `hash` (`hash`) KEY_BLOCK_SIZE=1024
) ENGINE=MyISAM AUTO_INCREMENT=3901013 DEFAULT CHARSET=utf8 ROW_FORMAT=FIXED

###### 2.改了Mysql服务器参数配置
##闲云野鹤加的
key_buffer_size=1G
##先改成了16M，又改成了32M
read_buffer_size=32M



###### #大佬的修改步骤 （7s -> 3~4s）
1.数据库瓶颈语句Select * from hashtable where in(.......)
2.用desc看执行计划
3.查看参数 'innodb_read_io_threads', '4'
4.问我电脑的硬件、CPU、内存配置。主频:1.6GHz,最大频率:2.1GHz,缓存2M,硬件核心数:2,虚拟线程数:2,出厂最大内存:4GB,固态硬盘128GB
5.改了hashtable表的engine为MyISAM
5.配置MySQL的my.ini。key_buffer_size=1G, read_buffer_size=32M


###### #大佬的建议
in太多了，尝试一半in，然后union all。


# NewAudioR
audio recognition


#遇到的问题1：数据库语句运行太慢了

#数据库配置优化了 闲云野鹤大佬帮忙改的 2020-5-4

1. engine改成了MyISAM，适合读多的
CREATE TABLE `hashtable` (
  `idhashtable` int(11) NOT NULL AUTO_INCREMENT,
  `hash` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  `time` int(11) NOT NULL,
  PRIMARY KEY (`idhashtable`),
  KEY `hash` (`hash`) KEY_BLOCK_SIZE=1024
) ENGINE=MyISAM AUTO_INCREMENT=3901013 DEFAULT CHARSET=utf8 ROW_FORMAT=FIXED

2.改了Mysql服务器参数配置
##闲云野鹤加的
key_buffer_size=1G
##先改成了16M，又改成了32M
read_buffer_size=32M



#大佬的修改步骤 （7s -> 3~4s）
1.数据库瓶颈语句Select * from hashtable where in(.......)
2.用desc看执行计划
3.查看参数 'innodb_read_io_threads', '4'
4.问我电脑的硬件、CPU、内存配置。主频:1.6GHz,最大频率:2.1GHz,缓存2M,硬件核心数:2,虚拟线程数:2,出厂最大内存:4GB,固态硬盘128GB
5.改了hashtable表的engine为MyISAM
5.配置MySQL的my.ini。key_buffer_size=1G, read_buffer_size=32M


#大佬的建议
in太多了，尝试一半in，然后union all。


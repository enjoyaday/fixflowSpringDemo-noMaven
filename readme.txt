首先声明：
这不是官方示例，是我一天时间赶出来的，日志，异常等都没时间完善，只关注spring的集成。细节问题慢慢完善

此示例适合对fixflow有基础了解的用户，因为毕竟没有涉及设计器和流程图的画法问题。

本项目解决了几个用户比较关心的问题：
1.fixflow-expand打成jar包
2.将表单上的数据，作为变量传给引擎问题
3.spring的配置问题。


安装过程：
1.下载fixflow5.2设计器
2.将conf.properties复制到fixflowDesigner根目录
2.导入此项目，不能改名，否则请修改conf.properties和fixflowconfig.xml文件中的项目名称。
3.执行数据库脚本Demosql_mysql.sql，新建一张请假表，其他数据库请修改sql语句。
4.修改fixflowconfig.xml文件中的数据库地址，使设计器能够连上数据库。
5.打开webContent/DemoBPMN/QJLC.bpmn文件，发布到数据库
6.修改jdbc.properties文件中的数据库连接，此处为运行时数据库连接
7.将项目发布到tomcat下，并启动tomcat
8.访问http://127.0.0.1:8080/fixflowSpringDemo-noMaven/fixflow
9.点击启动流程，输入相关信息，此处的下一步处理人必须为au_userinfo中存在的用户
10.点击启动，查看待办，处理任务

整个示例结束，应该还存在不少bug，毕竟时间太紧了。


关于集成的过程，稍后我会在博客中详细说明，并附简单的代码讲解，希望能对大家有所帮助。
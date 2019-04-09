# hive-udf

编译源码并打包：
    mvn package
UDF四种加载方式
第一种：
hive命令行中添加jar包并创建临时函数：
1. 将上传的jar包导入到classpath变量里
    add jar <本地路径>  //本地路径需是hiveServer2所在机器，且用户能访问到的目录
    add jar <hdfs路径>  //hdfs上传时，需使用测试要用的用户上传，否则影响文件属主
2. 查看导入的jar包
    list jars;
3. 创建临时函数，关联添加的jar包
    create temporary function hello as 'com.hive.udf.GenericUDFUserEncrypt'; //as后加全路径
4. 查看创建的临时函数
    show functions like "hello";
    desc function hello;

add jar并创建临时函数
    create temporary function en as 'com.hive.udf.GenericUDFUserEncrypt' using jar 'hdfs:///user/udfjar/hive-udf-1.0-SNAPSHOT.jar';

第二种：
修改hive-site.xml文件
修改参数hive.aux.jars.path的值指向UDF文件所在的路径；
    <property>
        <name>hive.aux.jars.path</name>
        <value>file:///jarpath/all_new1.jar,file:///jarpath/all_new2.jar</value>
    </property>

第三种：
在${HIVE_HOME}下创建auxlib目录，将UDF文件放到该目录中，这样hive在启动时会将其中的jar文件加载到classpath中。

第四种：
设置HIVE_AUX_JARS_PATH环境变量，变量的值为放置jar文件的目录，可以修改${HIVE_HOME}/conf中的hive-env.sh文件的export HIVE_AUX_JARS_PATH=jar文件目录来实现，
或者在系统中直接添加HIVE_AUX_JARS_PATH环境变量。

调试UDF，打开DEBUG日志；
    cp hive-udf-1.0-SNAPSHOT.jar /home/p46_u27_gonghuanglv/
    sudo -u p46_u27_gonghuanglv hive --hiveconf hive.root.logger=DEBUG,console;
    hive> create temporary function hello as 'com.hive.udf.GenericUDFUserEncrypt' using jar '/home/p46_u27_gonghuanglv/hive-udf-1.0-SNAPSHOT.jar'；
    hive> desc function hello;
    hive> select hello(<入参>);
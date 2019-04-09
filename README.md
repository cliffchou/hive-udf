# hive-udf

平台使用：1.mvn package  打包源码
        2.在Pather/查询设计/UDF jar中添加UDF jar包
          注意，查询类型为Hive QL，类名称为全限定名，如com.hive.udf.GenericUDFDBOutput，函数名称自定义，用于后续hive sql，这里为DB_OutPut
        3.在Pather/查询设计/HiveQL中选择对应数据库以及UDF jar包，正确书写hive sql语句，点击执行即可，hive sql如下
          select rdcrecom.DB_OutPut('INSERT INTO push_sohutv_info(vid,category_id) VALUES (?,?)',vid,10) from rdcrecom.dw_video_common where dt='20190226' limit 3
          其中，rdcrecom.DB_OutPut为自定义函数，本质为GenericUDFDBOutput类中evaluate方法，第一个参数为待执行的MySQL语句，后面两个参数是hive表中待查询的字段，rdcrecom.dw_video_common为待查询hive表
          注意：hive表中字段类型一定要和MySQL中字段类型一致，否则不能插入，还有就是设计MySQL表时，确保不为空的字段一定要有数据插入进来，否则不能插入

不使用平台：一般步骤如下
          1.add jar /usr/share/java/mysql-connector-java-5.1.17.jar;
          2.add jar /usr/share/java/hive-udf-1.0-SNAPSHOT.jar
          3.CREATE TEMPORARY FUNCTION dboutput AS 'GenericUDFDBOutput';
          4.select dboutput('jdbc:mysql://localhost/result','root','123456','INSERT INTO dc(code,size) VALUES (?,?)',code,size) from accesslog limit 10;
          5.从hive表accesslog中查找字段code,size的10条记录插入到mysql表dc中code,size字段，


补充：这里的MySQL数据库连接使用的是DBCP连接池，连接配置写死在DBManager类中，因此如果需要修改数据库表连接配置，只需要在这里面修改即可～
     这就很不方便了，原来的代码中是将数据库连接驱动等配置作为参数传递给GenericUDFDBOutput类中evaluate方法，因此只需要在写hive sql时配置即可，更加灵活～

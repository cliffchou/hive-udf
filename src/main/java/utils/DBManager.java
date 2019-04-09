package utils;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DBManager {
    private static final Log log = LogFactory.getLog(DBManager.class);

    private static DataSource dataSource;

    static{
        Properties dbProperties = new Properties();
        try{

            //线上库
            /*log.info("url=" + ConfigLoader.mysqlJdbcApollo());
            log.info("db=" + ConfigLoader.mysqlDbApollo());
            log.info("user=" + ConfigLoader.mysqlUsernameApollo());
            log.info("passwd=" + ConfigLoader.mysqlPasswordApollo());

            dbProperties.setProperty("driverClassName", "com.mysql.jdbc.Driver");
            dbProperties.setProperty("url", ConfigLoader.mysqlJdbcApollo() + ConfigLoader.mysqlDbApollo()
                    + "?connectTimeout=30000&socketTimeout=30000&rewriteBatchedStatements=true");
            dbProperties.setProperty("username", ConfigLoader.mysqlUsernameApollo());
            dbProperties.setProperty("password", ConfigLoader.mysqlPasswordApollo());*/

            //测试库
            log.info("url=" + "jdbc:mysql://vrrec-testrw.db2.sohuno.com:3306/vrrec_test?useUnicode=true&characterEncoding=gb2312");
            log.info("db=" + "vrrec_test");
            log.info("user=" + "vrrec_test_rw");
            log.info("passwd=" + "8756HZ1f5S9m5Y9");

            dbProperties.setProperty("driverClassName", "com.mysql.jdbc.Driver");
            dbProperties.setProperty("url", "jdbc:mysql://vrrec-testrw.db2.sohuno.com:3306/vrrec_test?useUnicode=true&characterEncoding=gb2312");
            dbProperties.setProperty("username", "vrrec_test_rw");
            dbProperties.setProperty("password", "8756HZ1f5S9m5Y9");

            // initial size
            dbProperties.setProperty("initialSize", "8");
            // maxActive Connection
            dbProperties.setProperty("maxActive", "20");
            // max idle connection
            dbProperties.setProperty("maxIdle", "6");
            // min idle connection
            dbProperties.setProperty("minIdle", "2");
            // auto Commit
            dbProperties.setProperty("defaultAutoCommit", "true");
            // max timewait for getting conncetion form pool
            dbProperties.setProperty("maxWait", "120000");

            dataSource = BasicDataSourceFactory.createDataSource(dbProperties);

            Connection conn = getConn();

            closeConn(conn);
        }catch(Exception e){
            System.out.println("初始化DBCP连接池失败：" + e);
        }
    }

    private DBManager() {
    }

    public static void closeConn(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("DBCP 关闭数据库连接失败：" + e);
        }
    }

    public static final Connection getConn() {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            System.out.println("DBCP 获取数据库连接失败：" + e);
        }
        return conn;
    }
}

package com.hive.udf;

/**
 * Created by Administrator on 2018/9/3.
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.UDFType;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde.Constants;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import org.apache.hadoop.io.IntWritable;
import utils.DBManager;

/**
 * GenericUDFDBOutput is designed to output data directly from Hive to a JDBC
 * datastore. This UDF is useful for exporting small to medium summaries that
 * have a unique key.
 *
 * Due to the nature of hadoop, individual mappers, reducers or entire jobs can
 * fail. If a failure occurs a mapper or reducer may be retried. This UDF has no
 * way of detecting failures or rolling back a transaction. Consequently, you
 * should only use this to export to a table with a unique key. The unique
 * key should safeguard against duplicate data.
 *
 * Use hive's ADD JAR feature to add your JDBC Driver to the distributed cache,
 * otherwise GenericUDFDBoutput will fail.
 * com.hive.udf.GenericUDFDBOutput
 */
@Description(name = "dboutput",
        value = "_FUNC_(jdbcstring,username,password,preparedstatement,[arguments])"
                + " - sends data to a jdbc driver",
        extended = "argument 0 is the JDBC connection string\n"
                + "argument 1 is the user name\n"
                + "argument 2 is the password\n"
                + "argument 3 is an SQL query to be used in the PreparedStatement\n"
                + "argument (4-n) The remaining arguments must be primitive and are "
                + "passed to the PreparedStatement object\n")
@UDFType(deterministic = false)
public class GenericUDFDBOutput extends GenericUDF {
    private static final Log LOG = LogFactory
            .getLog(GenericUDFDBOutput.class.getName());

    private transient ObjectInspector[] argumentOI;
    private transient Connection connection = null;
    private String url;
    private String user;
    private String pass;
    private String sql;
    private final IntWritable result = new IntWritable(-1);

    /**
     * @param arguments
     * argument 0 is the JDBC connection string argument 1 is the user
     * name argument 2 is the password argument 3 is an SQL query to be
     * used in the PreparedStatement argument (4-n) The remaining
     * arguments must be primitive and are passed to the
     * PreparedStatement object
     */
    //数据库已经连接好了，因此不需要再进行另外的参数获取了，只需要获取sql即可
    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentTypeException {
        argumentOI = arguments;

        // this should be connection url,username,password,query,column1[,columnn]*
        /*for (int i = 0; i < 4; i++) {
            if (arguments[i].getCategory() == ObjectInspector.Category.PRIMITIVE) {
                PrimitiveObjectInspector poi = ((PrimitiveObjectInspector) arguments[i]);

                if (!(poi.getPrimitiveCategory() == PrimitiveObjectInspector.PrimitiveCategory.STRING)) {
                    throw new UDFArgumentTypeException(i,
                            "The argument of function should be \""
                                    + Constants.STRING_TYPE_NAME + "\", but \""
                                    + arguments[i].getTypeName() + "\" is found");
                }
            }
        }
        for (int i = 4; i < arguments.length; i++) {
            if (arguments[i].getCategory() != ObjectInspector.Category.PRIMITIVE) {
                throw new UDFArgumentTypeException(i,
                        "The argument of function should be primative" + ", but \""
                                + arguments[i].getTypeName() + "\" is found");
            }
        }*/

        //参数0是要在PreparedStatement参数中使用的SQL查询(0)
        if (arguments[0].getCategory() == ObjectInspector.Category.PRIMITIVE) {
            PrimitiveObjectInspector poi = ((PrimitiveObjectInspector) arguments[0]);

            if (!(poi.getPrimitiveCategory() == PrimitiveObjectInspector.PrimitiveCategory.STRING)) {
                throw new UDFArgumentTypeException(0,
                        "The argument of function should be \""
                                + Constants.STRING_TYPE_NAME + "\", but \""
                                + arguments[0].getTypeName() + "\" is found");
            }
        }

        //其余参数必须是原始的，并传递给PreparedStatement对象(1...)
        for (int i = 1; i < arguments.length; i++) {
            if (arguments[i].getCategory() != ObjectInspector.Category.PRIMITIVE) {
                throw new UDFArgumentTypeException(i,
                        "The argument of function should be primative" + ", but \""
                                + arguments[i].getTypeName() + "\" is found");
            }
        }

        return PrimitiveObjectInspectorFactory.writableIntObjectInspector;
    }

    /**
     * @return 0 on success -1 on failure
     */
    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {
        /*url = ((StringObjectInspector) argumentOI[0]).getPrimitiveJavaObject(arguments[0].get());
        user = ((StringObjectInspector) argumentOI[1]).getPrimitiveJavaObject(arguments[1].get());
        pass = ((StringObjectInspector) argumentOI[2]).getPrimitiveJavaObject(arguments[2].get());
        sql = ((StringObjectInspector) argumentOI[3]).getPrimitiveJavaObject(arguments[3].get());*/

        //获取原始Java对象
        LOG.info("start to execute evaluate .........");
        sql = ((StringObjectInspector) argumentOI[0]).getPrimitiveJavaObject(arguments[0].get());

        /*try {
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException ex) {
            LOG.error("Driver loading or connection issue", ex);
            result.set(2);
        }*/

        //获取数据库连接
        try{
            connection = DBManager.getConn();
        }catch(Exception e){
            result.set(2);
            LOG.error("Driver loading or connection issue", e);
        }

        if (connection != null) {
            try {
                PreparedStatement ps = connection.prepareStatement(sql);
                /*for (int i = 4; i < arguments.length; ++i) {
                    PrimitiveObjectInspector poi = ((PrimitiveObjectInspector) argumentOI[i]);
                    ps.setObject(i - 3, poi.getPrimitiveJavaObject(arguments[i].get()));
                }*/
                for (int i = 1; i < arguments.length; ++i) {
                    PrimitiveObjectInspector poi = ((PrimitiveObjectInspector) argumentOI[i]);
                    ps.setObject(i, poi.getPrimitiveJavaObject(arguments[i].get()));
                }
                ps.execute();
                ps.close();
                result.set(0);
            } catch (SQLException e) {
                LOG.error("Underlying SQL exception", e);
                result.set(1);
                LOG.info(e.getMessage()+"========================================");
            } finally {
                try {
                    DBManager.closeConn(connection);
                } catch (Exception ex) {
                    LOG.error("Underlying SQL exception during close", ex);
                }
            }
        }

        return result;
    }

    @Override
    public String getDisplayString(String[] children) {
        StringBuilder sb = new StringBuilder();
        sb.append("dboutput(");
        if (children.length > 0) {
            sb.append(children[0]);
            for (int i = 1; i < children.length; i++) {
                sb.append(",");
                sb.append(children[i]);
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public static void main(String[] args) {
        //add jar /usr/share/java/mysql-connector-java-5.1.17.jar;
        //add jar /usr/share/java/hive-udf-1.0-SNAPSHOT.jar
        //CREATE TEMPORARY FUNCTION dboutput AS 'GenericUDFDBOutput';
        //select dboutput('jdbc:mysql://localhost/result','root','123456','INSERT INTO dc(code,size) VALUES (?,?)',code,size) from accesslog limit 10;
        //从hive表accesslog中查找字段code,size的10条记录插入到mysql表dc中code,size字段，
    }
}

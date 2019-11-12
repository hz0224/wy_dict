package utils;

import bean.ColumnRemark;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class ReadMysqlUtils {

    private static QueryRunner queryRunner = new QueryRunner();
    private static DataSource dataSource = null;

    static {

        Properties pros = new Properties();


        InputStream in = ReadMysqlUtils.class.getClassLoader().getResourceAsStream("readDB.properties");

        if(in==null){
            System.out.println("readDB.properties读取不到啊");
        }

        try {
            pros.load(in);

            dataSource = BasicDataSourceFactory.createDataSource(pros);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        System.out.println(dataSource);
    }





    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void release(Connection connection){
        if(connection !=null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //将备注批量插入数据库
    public static void writeRemark(ArrayList<ColumnRemark> columnRemarks) {

        Connection connection = null;

        try{
            connection = ReadMysqlUtils.getConnection();
            String sql = "insert into ods.table_remark (db_table_column,remark) values(?,?) " +
                    "on duplicate key update remark = values(remark)";

            Object[][] params = new Object[columnRemarks.size()][];

            for(int i = 0 ;i<columnRemarks.size() ; i++){
                Object[] objects = new Object[2];
                objects[0] = columnRemarks.get(i).getDbTableColumn();
                objects[1] = columnRemarks.get(i).getRemark();
                params[i] = objects;
            }

            queryRunner.batch(connection,sql,params);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            ReadMysqlUtils.release(connection);
        }
    }


}

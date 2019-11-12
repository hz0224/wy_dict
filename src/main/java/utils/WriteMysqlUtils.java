package utils;

import org.apache.commons.dbcp2.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class WriteMysqlUtils {

    private static DataSource dataSource = null;

    static {

        Properties pros = new Properties();

        InputStream in = ReadMysqlUtils.class.getClassLoader().getResourceAsStream("WriteDB.properties");

        if(in==null){
            System.out.println("WriteDB.properties读取不到啊");
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
}

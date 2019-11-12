package showdoc;

import utils.ReadMysqlUtils;

import java.sql.*;

public class Test {

    public static void main(String[] args) throws SQLException {

        //String result = getTableCreateStr("ods.school");

        String result = "CREATE TABLE `planbook_compare_history` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',\n" +
                "  `insured_age` int(6) DEFAULT NULL COMMENT '被保人年龄',\n" +
                "  `insured_sex` int(6) DEFAULT NULL COMMENT '被保人性别',\n" +
                "  `applicant_age` int(6) DEFAULT NULL COMMENT '投保人年龄',\n" +
                "  `applicant_sex` int(6) DEFAULT NULL COMMENT '投保人性别',\n" +
                "  `results` varchar(500) DEFAULT NULL COMMENT '计划书UUID集合',\n" +
                "  `is_deleted` bit(1) DEFAULT b'0',\n" +
                "  `create_datetime` datetime DEFAULT NULL,\n" +
                "  `modify_datetime` datetime DEFAULT NULL,\n" +
                "  `record_update_datetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  KEY `index` (`user_id`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=354537 DEFAULT CHARSET=utf8mb4;\n";
        System.out.println(mysqlToHive(result));

        //System.out.println(getAllColumn(result));

    }


    //获取表的创建语句
    public static String getTableCreateStr(String tableName){

        Connection connection = null;
        String createTableStr = null;
        try{

            connection = ReadMysqlUtils.getConnection();
            String sql = "show create table " + tableName;

            Statement st = connection.createStatement();

            ResultSet rs = st.executeQuery(sql);

            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();

            if(rs.next()){
                for(int i = 0 ; i<columnCount ; i++){
                    if(i == 1){
                        createTableStr = (String)rs.getObject(i + 1);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return createTableStr;
    }

    //将mysql表转换为hive表
    public static String mysqlToHive(String str){

        String[] split = str.split("\n");

        StringBuilder sb = new StringBuilder();
        sb.append(split[0] + "\n");

        for(int i = 1 ; i<split.length - 1 ; i++){
            String line = split[i];
            String[] data = line.split(" ");
            String column = data[2];
            String comment = ",";
            String mysqlType = data[3];

            //找注释
            for(int j = 0 ; j<data.length ; j++){
                if(data[j].equalsIgnoreCase("COMMENT")){
                    StringBuilder builder = new StringBuilder();
                    for(int m = j + 1 ; m<data.length ; m++){
                        builder.append(data[m]);
                    }

                    comment = builder.toString();
                }
            }

            //找到对应hive中的类型
            String hiveType = "";
            if(mysqlType.startsWith("varchar") || mysqlType.startsWith("char") || mysqlType.startsWith("VARCHAR") || mysqlType.startsWith("CHAR")){
                hiveType = "STRING";
            }else if(mysqlType.startsWith("int") || mysqlType.startsWith("INT")){
                hiveType = "INT";
            }else if(mysqlType.startsWith("bit") || mysqlType.startsWith("BIT")){
                hiveType = "BOOLEAN";
            }else if(mysqlType.startsWith("bigint") || mysqlType.startsWith("BIGINT")){
                hiveType = "BIGINT";
            }else if(mysqlType.startsWith("timestamp") || mysqlType.startsWith("TIMESTAMP")){
                hiveType = "TIMESTAMP";
            }else if(mysqlType.startsWith("datetime") || mysqlType.startsWith("DATETIME")){
                hiveType = "STRING";
            }else if(mysqlType.startsWith("double") || mysqlType.startsWith("DOUBLE") || mysqlType.startsWith("decimal") || mysqlType.startsWith("DECIMAL")){
                hiveType = "DOUBLE";
            }else {
                hiveType = "error";
            }

            if(",".equals(comment)){
                sb.append(column + " " + hiveType + comment + "\n");
            }else{
                sb.append(column + " " + hiveType + " " + "COMMENT" + " " + comment + "\n");
            }

        }
        sb.append(")");
        return sb.toString();
    }

    //获取建表语句中所有的列
    public static String getAllColumn(String str){

        String[] split = str.split("\n");

        StringBuilder sb = new StringBuilder();
        for(int i = 1 ; i<split.length - 1 ; i++){
            String line = split[i];
            String[] data = line.split(" ");
            sb.append("- " + data[2] + "\n");
        }

        return sb.toString().replaceAll("`","");
    }

}

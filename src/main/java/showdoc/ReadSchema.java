package showdoc;

import bean.TableMsg;
import bean.TotalTable;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import utils.ReadMysqlUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReadSchema {

    private QueryRunner queryRunner = null;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ReadSchema() {
        queryRunner = new QueryRunner();
    }

    //加载要同步的数据库
    public Properties getSyncDataBases(){
        Properties pros = new Properties();

        InputStream in = ReadSchema.class.getClassLoader().getResourceAsStream("sync_db.properties");

        if(in==null){
            System.out.println("sync_db.properties读取不到啊");
        }


        try {
            pros.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pros;
    }

    //查询指定库下所有的表
    public List<TotalTable> getAllTables(String db, String sql){

        Connection connection = null;
        try{
            connection = ReadMysqlUtils.getConnection();
            return queryRunner.query(connection,sql,new BeanListHandler<TotalTable>(TotalTable.class),db);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            ReadMysqlUtils.release(connection);
        }

        return null;
    }

    //查询出一张表结构
    public  List<TableMsg> getTableMsgForList(String sql, Object... args){
        Connection connection = null;
        try {
            connection = ReadMysqlUtils.getConnection();

            return queryRunner.query(connection,sql,new BeanListHandler<TableMsg>(TableMsg.class),args);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            ReadMysqlUtils.release(connection);
        }
        return null;
    }


    //将一张表结构使用 markDown语法封装
    public  String getStringWithMarkDown(List<TableMsg> list){

        String tableName = list.listIterator().next().getTableName();

        StringBuilder sb = new StringBuilder("-  " + tableName  + "表\n\n");

        sb.append("|字段|类型|空|注释|备注|\n");
        sb.append("|:----    |:-------    |:--- |-- -|------      |\n");

        for (TableMsg tableMsg : list) {
            String isNullable = tableMsg.getIsNullable();
            String isNull = "YES".equals(isNullable) ? "是" : "否";

            String columnComment = tableMsg.getColumnComment() == null || "".equals(tableMsg.getColumnComment()) || "\t".equals(tableMsg.getColumnComment()) ? "\t" : tableMsg.getColumnComment().replaceAll("\r\n", " ");
            String columnRemark = tableMsg.getColumnRemark() == null || "".equals(tableMsg.getColumnRemark()) || "\t".equals(tableMsg.getColumnRemark()) ? "\t" : tableMsg.getColumnRemark().replaceAll("\n", " ");

            String line = "|" + tableMsg.getColumnName() + "|" + tableMsg.getColumnType() + "|"
                    + isNull + "|" + columnComment + "|" + columnRemark + "|" + "\n";
            sb.append(line);
        }


        String dateStr = this.format.format(new Date());
        sb.append("\t最近更新时间: "+dateStr);

        return sb.toString();
    }

    //填充tableMsgForList里每张表的 remark属性.
    public void fillRemark(String db, List<TableMsg> tableMsgForList, HashMap<String, String> dbTableColumn2Remark) {

        for (TableMsg tableMsg : tableMsgForList) {
            String tableName = tableMsg.getTableName();
            String columnName = tableMsg.getColumnName();
            String key = db + "_" + tableName + "_" + columnName;
            String value = dbTableColumn2Remark.get(key);
            value = value == null ? "" : value;
            tableMsg.setColumnRemark(value);
        }
    }

    //过滤表   即一张表的分表只保留一个
    public void filterTables(List<TotalTable> tableList) {

        //分表
        ArrayList<String> submeterTables = new ArrayList<String>();

        Iterator<TotalTable> it = tableList.iterator();

        while (it.hasNext()){
            String tableName = it.next().getTableName();

            boolean flag = true;


            if(tableName.contains("_")){
                String[] tableNameSplit = tableName.split("_");
                String endStr = tableNameSplit[tableNameSplit.length - 1];
                try {
                    int i = Integer.parseInt(endStr);

                    if(i>100){
                        it.remove();
                    }else{
                        if(flag){
                            submeterTables.add(tableName);
                            it.remove();
                        }
                    }
                }catch (NumberFormatException e){
                    flag = false;
                }

            }

        }


        //submeterTables去重
        HashSet<String> set = new HashSet<String>();

        for (String table : submeterTables) {
            //sales_user_account_course_28
            String[] split = table.split("_");
            String endStr = split[split.length - 1];
            String newTableName = table.replaceAll("_"+endStr,"");
            set.add(newTableName+"_00");
        }

        for (String s : set) {
            tableList.add(new TotalTable(s));
        }

    }
}

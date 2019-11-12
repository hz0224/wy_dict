package showdoc;

import bean.Page;
import bean.TableMsg;
import bean.TotalTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        ReadSchema readSchema = new ReadSchema();
        WriteSchema writeSchema = new WriteSchema();
        Properties dataBases = readSchema.getSyncDataBases();

        //获取要同步的所有库
        String[] dbs = dataBases.getProperty("dbs").split(",");


        //获取更新sql语句
        String sql = dataBases.getProperty("getTablesSql");

        //获取showdoc上数据字典这个项目的 item_id
        int item_id = Integer.parseInt(dataBases.getProperty("item_id"));

        for (String db : dbs) {
            //获取该库下要更新的表.
            List<TotalTable> tableList = readSchema.getAllTables(db,sql);
            if(tableList.size() ==0 ) continue;

            //过滤分表
            readSchema.filterTables(tableList);

            //存储备注信息,便于进行join
            HashMap<String, String> dbTableColumn2Remark  = new HashMap<String, String>();

            //保存备注到 dbTableColumn2Remark中。
            writeSchema.parseRemark(db,tableList,dbTableColumn2Remark);

            //存储的是 catName与catId的对应关系
            HashMap<String, Integer> catName2CatId = new HashMap<String, Integer>();

            //清空showdoc上该同名数据库目录下指定的页面
            writeSchema.cleanShowdocDir(item_id,db,catName2CatId,tableList);

            //pageId 自增
            int pageId  = writeSchema.getMaxPageId();

            ArrayList<Page> pages = new ArrayList<Page>();

            //同步该库下要更新的表
            for (TotalTable table : tableList) {
                //查询出这张表的结构
                String sql1 = "select  c.table_name tableName,\n" +
                        "c.column_name columnName,\n" +
                        "c.is_nullable isNullable,\n" +
                        "c.column_type columnType,\n" +
                        "c.column_comment columnComment\n" +
                        "FROM\n" +
                        "information_schema.columns c\n" +
                        "where table_schema = ? and table_name = ?";

                List<TableMsg> tableMsgForList = readSchema.getTableMsgForList(sql1, db, table.getTableName());
                if(tableMsgForList.size()==0) continue;


                //填充tableMsgForList里每张表的 remark属性.
                readSchema.fillRemark(db,tableMsgForList,dbTableColumn2Remark);

                //将表结构封装成markDown语法.
                String stringWithMarkDown = readSchema.getStringWithMarkDown(tableMsgForList);

                pageId++;
                Page page = new Page(pageId,1,"showdoc",item_id,catName2CatId.get(db),table.getTableName()+"表",
                        "",stringWithMarkDown,99,System.currentTimeMillis());
                pages.add(page);
            }

            //批量插入
            writeSchema.insertBatchToShowDoc(pages);
        }

    }
}

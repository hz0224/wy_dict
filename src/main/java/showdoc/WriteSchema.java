package showdoc;

import bean.Page;
import bean.PageMsg;
import bean.TotalTable;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import utils.WriteMysqlUtils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WriteSchema {

    private QueryRunner queryRunner = null;

    public WriteSchema() {
        queryRunner = new QueryRunner();
    }

    //插入数据
    public int insertToShowDoc(String sql , Object... args){

        Connection connection = null;
        try{
            connection = WriteMysqlUtils.getConnection();
            return queryRunner.update(connection,sql,args);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            WriteMysqlUtils.release(connection);
        }

        return 0;
    }

    //清空showdoc上的目录及目录下的所有页面
    public  void cleanShowdocDir(int item_id, String db, HashMap<String, Integer> catName2CatId, List<TotalTable> tableList) {

        Connection connection = null;

        try{
            connection = WriteMysqlUtils.getConnection();
            String sql1 = "select cat_id from catalog where item_id = ? and cat_name = ?";

            //查询出与数据库同名的目录的cat_id
            Integer catId = queryRunner.query(connection, sql1, new ScalarHandler<Integer>(), item_id, db);
            catName2CatId.put(db,catId);

            String sql2 = "delete from page where item_id = ? and cat_id = ? and page_title = ?";

            //批量删除
            Object[][] params = new Object[tableList.size()][];
            //填充params
            for(int i = 0 ; i < tableList.size() ; i++){
                Object[] objects = new Object[3];
                objects[0] = item_id;
                objects[1] = catId;
                objects[2] = tableList.get(i).getTableName() + "表";
                params[i] = objects;
            }

            queryRunner.batch(connection, sql2, params);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            WriteMysqlUtils.release(connection);
        }


    }

    //获取page表中 page_id的最大值
    public int getMaxPageId(){

        Connection connection = null;
        try{

            connection = WriteMysqlUtils.getConnection();

            String sql = "select max(page_id) from page";

            Integer pageIdMax = queryRunner.query(connection, sql, new ScalarHandler<Integer>());
            return pageIdMax == null? 0 : pageIdMax;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            WriteMysqlUtils.release(connection);
        }

        return 0;
    }


    //解析备注填充map集合
    public void parseRemark(String db, List<TotalTable> tableList, HashMap<String, String> dbTableColumn2Remark){

        Connection connection = null;

        try{
            connection = WriteMysqlUtils.getConnection();
            String sql = "select c.cat_name db,p.page_title tableName,p.page_content pageContent " +
                    "from page p,catalog c " +
                    "where c.cat_name =? and p.cat_id = c.cat_id and page_title = ?";

            //一个集合存储了一个数据库里的多张表信息
            List<PageMsg> msgList = new ArrayList<PageMsg>();

            for (TotalTable table : tableList) {
                PageMsg msg = queryRunner.query(connection, sql, new BeanHandler<PageMsg>(PageMsg.class), db, table.getTableName() + "表");
                if(msg !=null) msgList.add(msg);
            }

        if(msgList.size() == 0) return;

        //解析表信息，填充map集合
        for (PageMsg pageMsg : msgList) {
            String tableName = pageMsg.getTableName();
            String pageContent = pageMsg.getPageContent();

            String[] split = pageContent.split("\n");

            for(int i = 4 ; i<split.length - 1 ; i++){
                String remark = "";
                String[] strings = split[i].split("\\|");
                String column = strings[1];
                if(strings.length == 6){
                    remark = strings[5];
                }
                String key = db + "_" + tableName + "_" + column;
                String newKey = key.replaceAll("表", "");
                dbTableColumn2Remark.put(newKey,remark);
            }
        }

    }catch (Exception e){
        e.printStackTrace();
    }finally {
        WriteMysqlUtils.release(connection);
    }
    }


    //批量插入 showdoc
    public void insertBatchToShowDoc(ArrayList<Page> pages) {


        Connection connection = null;
        try{
            connection = WriteMysqlUtils.getConnection();
            String sql = "insert into page values(?,?,?,?,?,?,?,?,?,?)";

            Object[][] params = new Object[pages.size()][];

            //填充params
            for(int i = 0 ; i<pages.size() ; i++){
                Object[] objects = new Object[10];
                objects[0] = pages.get(i).getPageId();
                objects[1] = pages.get(i).getAuthorUid();
                objects[2] = pages.get(i).getAuthorUsername();
                objects[3] = pages.get(i).getItemId();
                objects[4] = pages.get(i).getCatId();
                objects[5] = pages.get(i).getPageTitle();
                objects[6] = pages.get(i).getPageComments();
                objects[7] = pages.get(i).getPageContent();
                objects[8] = pages.get(i).getsNumber();
                objects[9] = pages.get(i).getAddTime();
                params[i] = objects;
            }

            int[] batch = queryRunner.batch(connection, sql, params);
            System.out.println("向showdoc中插入了" + batch.length + " 条数据");

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            WriteMysqlUtils.release(connection);
        }
    }
}

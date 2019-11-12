package bean;

public class PageMsg {
    private String db;
    private String tableName;
    private String pageContent;

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPageContent() {
        return pageContent;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }

    @Override
    public String toString() {
        return "PageMsg{" +
                "db='" + db + '\'' +
                ", tableName='" + tableName + '\'' +
                ", pageContent='" + pageContent + '\'' +
                '}';
    }
}

package bean;

public class TotalTable {

    private String tableName;

    public TotalTable(String tableName) {
        this.tableName = tableName;
    }

    public TotalTable() {
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "TotalTable{" +
                "tableName='" + tableName + '\'' +
                '}';
    }
}

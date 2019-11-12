package bean;

public class ColumnRemark {
    private String dbTableColumn;
    private String remark;

    public ColumnRemark(String dbTableColumn, String remark) {
        this.dbTableColumn = dbTableColumn;
        this.remark = remark;
    }

    public ColumnRemark() {
    }

    public String getDbTableColumn() {
        return dbTableColumn;
    }

    public void setDbTableColumn(String dbTableColumn) {
        this.dbTableColumn = dbTableColumn;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "ColumnRemark{" +
                "dbTableColumn='" + dbTableColumn + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}

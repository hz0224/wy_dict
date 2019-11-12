package bean;

//封装表的结构信息

public class TableMsg {

    private String tableName;       //表名
    private String columnName;      //列名
    private String isNullable;      //是否可以为空
    private String columnType;   //列的类型
    private String columnComment;    //列的注释
    private String columnRemark;    //列的备注

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getColumnRemark() {
        return columnRemark;
    }

    public void setColumnRemark(String columnRemark) {
        this.columnRemark = columnRemark;
    }

    @Override
    public String toString() {
        return "TableMsg{" +
                "tableName='" + tableName + '\'' +
                ", columnName='" + columnName + '\'' +
                ", isNullable='" + isNullable + '\'' +
                ", columnType='" + columnType + '\'' +
                ", columnComment='" + columnComment + '\'' +
                ", columnRemark='" + columnRemark + '\'' +
                '}';
    }
}

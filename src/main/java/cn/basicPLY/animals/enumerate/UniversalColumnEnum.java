package cn.basicPLY.animals.enumerate;

/**
 * purpose:
 *
 * @author Pan Liuyang
 * 2022/4/23 15:41
 */
public enum UniversalColumnEnum {
    /**
     * 删除标识：1、未删除；0：已删除
     */
    DELETE_MARK("delete_mark", "删除标识：1、未删除；0：已删除"),

    /**
     * 创建人
     */
    CREATE_BY("create_by", "创建人"),

    /**
     * 创建时间
     */
    CREATE_DATE("create_date", "创建时间"),

    /**
     * 更新人
     */
    UPDATE_BY("update_by", "更新人"),

    /**
     * 更新时间
     */
    UPDATE_DATE("update_date", "更新时间");

    /**
     * 列名
     */
    private final String column;
    /**
     * 列名备注含义
     */
    private final String remark;

    UniversalColumnEnum(String column, String remark) {
        this.column = column;
        this.remark = remark;
    }

    public String getColumn() {
        return column;
    }

    public String getRemark() {
        return remark;
    }
}

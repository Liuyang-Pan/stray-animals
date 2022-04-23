package cn.basicPLY.animals.enumerate;

/**
 * purpose:
 *
 * @author Pan Liuyang
 * 2022/4/23 10:47
 */
public enum UserEnum {
    /**
     * 超级管理员
     */
    USER_ADMIN("超级管理员", "ADMIN", "超级管理员标识");

    /**
     * 名称
     */
    private final String name;
    /**
     * 值/CODE
     */
    private final String value;
    /**
     * 备注
     */
    private final String remark;

    UserEnum(String name, String value, String remark) {
        this.name = name;
        this.value = value;
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getRemark() {
        return remark;
    }
}

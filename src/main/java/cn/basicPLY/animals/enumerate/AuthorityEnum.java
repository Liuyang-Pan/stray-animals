package cn.basicPLY.animals.enumerate;

/**
 * purpose:
 *
 * @author Pan Liuyang
 * 2022/4/23 12:17
 */
public enum AuthorityEnum {
    /**
     * 超级管理员
     */
    ROLE_ADMIN("ROLE_admin", "超级管理员", "具备所有权限"),

    /**
     * 普通用户
     */
    ROLE_USER("ROLE_user", "普通用户", "具备基础权限");

    /**
     * 角色权限CODE
     */
    private final String authority;
    /**
     * 角色权限中文名称
     */
    private final String authorityZh;
    /**
     * 角色权限用途备注
     */
    private final String remark;


    AuthorityEnum(String authority, String authorityZh, String remark) {
        this.authority = authority;
        this.authorityZh = authorityZh;
        this.remark = remark;
    }

    public String getAuthority() {
        return authority;
    }

    public String getAuthorityZh() {
        return authorityZh;
    }

    public String getRemark() {
        return remark;
    }
}

package cn.basicPLY.animals.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 流浪动物救助网：用户角色权限中间表
 * @TableName stray_animals_user_authority
 */
@TableName(value ="stray_animals_user_authority")
@Data
public class StrayAnimalsUserAuthority implements Serializable {
    /**
     * 主键
     */
    @TableId
    private String keyId;

    /**
     * 用户表主键
     */
    private String userKeyId;

    /**
     * 权限表主键
     */
    private String authorityId;

    /**
     * 权限CODE
     */
    private String authority;

    /**
     * 权限中文名
     */
    private String authorityZh;

    /**
     * 是否删除（1：未删除；0：已删除）
     */
    private Integer deleteMark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新人
     */
    private Date updateDate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
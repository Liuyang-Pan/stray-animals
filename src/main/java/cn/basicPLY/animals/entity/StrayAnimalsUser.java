package cn.basicPLY.animals.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName stray_animals_user
 */
@TableName(value = "stray_animals_user")
@Data
public class StrayAnimalsUser implements Serializable {
    /**
     * 主键
     */
    @TableId
    private String keyId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 邮箱
     */
    private String emailAddress;

    /**
     * 是否启用（1：已启用；0：未启用）
     */
    private Integer enabled;

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
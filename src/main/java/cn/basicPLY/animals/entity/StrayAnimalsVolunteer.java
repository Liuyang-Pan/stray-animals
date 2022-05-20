package cn.basicPLY.animals.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 流浪动物救助网：志愿者信息
 * @TableName stray_animals_volunteer
 */
@TableName(value ="stray_animals_volunteer")
@Data
public class StrayAnimalsVolunteer implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private String keyId;

    /**
     * 关联用户ID
     */
    private String userId;

    /**
     * 志愿者姓名
     */
    private String volunteerName;

    /**
     * 籍贯
     */
    private String hometown;

    /**
     * 联系电话
     */
    private String contactNumber;

    /**
     * 身份证号码
     */
    private String volunteerIdCode;

    /**
     * 志愿者城市
     */
    private String volunteerCity;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date stopDate;

    /**
     * 学历/教育经历
     */
    private String education;

    /**
     * 性别
     */
    private String sex;

    /**
     * 个人简介
     */
    private String personalProfile;

    /**
     * 期望工作
     */
    private String expectedJob;

    /**
     * 是否删除（1：已认证；0：未认证）
     */
    private Integer certificationMark;

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
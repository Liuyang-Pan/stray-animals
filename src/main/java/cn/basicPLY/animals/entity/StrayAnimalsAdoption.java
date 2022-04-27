package cn.basicPLY.animals.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 流浪动物救助网：领养信息表
 * @TableName stray_animals_adoption
 */
@TableName(value ="stray_animals_adoption")
@Data
public class StrayAnimalsAdoption implements Serializable {
    /**
     * 主键
     */
    @TableId
    private String keyId;

    /**
     * 领养标题
     */
    private String adoptionTitle;

    /**
     * 领养内容描述
     */
    private String adoptionContent;

    /**
     * 领养状态：1、待领养；2、有确认；3、已领养
     */
    private Integer adoptionState;

    /**
     * 领养地点
     */
    private String adoptionAddress;

    /**
     * 领养电话
     */
    private String adoptionPhoneNumber;

    /**
     * 发布人（关联用户表ID）
     */
    private String foreignKeyPublisher;

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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createDate;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新人
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateDate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
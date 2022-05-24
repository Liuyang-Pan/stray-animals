package cn.basicPLY.animals.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 流浪动物救助信息网：信息评论表
 * @TableName stray_animals_comment
 */
@TableName(value ="stray_animals_comment")
@Data
public class StrayAnimalsComment implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private String keyId;

    /**
     * 归属数据ID
     */
    private String dataId;

    /**
     * 数据类型（1 基地 2 资源 3 领养信息）
     */
    private Integer dataType;

    /**
     * 评论人ID
     */
    private String commentatorId;

    /**
     * 评论人名称
     */
    private String commentatorName;

    /**
     * 评论内容
     */
    private String commentContent;

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
package cn.basicPLY.animals.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 流浪动物救助网：资源信息文件关联表
 * @TableName stray_animals_resource_file
 */
@TableName(value ="stray_animals_resource_file")
@Data
public class StrayAnimalsResourceFile implements Serializable {
    /**
     * 主键
     */
    @TableId
    private String keyId;

    /**
     * 关联的资源信息ID
     */
    private String resourceId;

    /**
     * 关联文件ID
     */
    private String fileId;

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
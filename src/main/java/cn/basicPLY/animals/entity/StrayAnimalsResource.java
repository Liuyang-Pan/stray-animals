package cn.basicPLY.animals.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 流浪动物救助网：资源需求相关信息表
 * @TableName stray_animals_resource
 */
@TableName(value ="stray_animals_resource")
@Data
public class StrayAnimalsResource implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private String keyId;

    /**
     * 发布人（关联用户表ID）
     */
    private String foreignKeyPublisher;

    /**
     * 资源类型(需求/供应)
     */
    private String resourceType;

    /**
     * 需求供应类型(资金/物资/场地/其他)
     */
    private String demandSupplyType;

    /**
     * 需求供应主体(企业/个人)
     */
    private String demandSupplyBody;

    /**
     * 需求供应主体机构名称
     */
    private String demandSupplyOrganization;

    /**
     * 联系电话
     */
    private String contactNumber;

    /**
     * 资源标题
     */
    private String resourceTitle;

    /**
     * 资源地址
     */
    private String resourceAddress;

    /**
     * 资源内容描述
     */
    private String resourceContent;

    /**
     * 是否救助站发布(1 救助站发布 2 普通用户发布)
     */
    private Integer aidStationMark;

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
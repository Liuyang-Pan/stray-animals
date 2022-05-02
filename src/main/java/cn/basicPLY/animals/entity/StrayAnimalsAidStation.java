package cn.basicPLY.animals.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 流浪动物救助信息网：救助站/基地相关信息表
 * @TableName stray_animals_aid_station
 */
@TableName(value ="stray_animals_aid_station")
@Data
public class StrayAnimalsAidStation implements Serializable {
    /**
     * 主键
     */
    @TableId
    private String keyId;

    /**
     * 关联的用户ID
     */
    private String userId;

    /**
     * 救助站名称
     */
    private String stationName;

    /**
     * 救助站类型
     */
    private String stationType;

    /**
     * 社会统一信用代码
     */
    private String creditCode;

    /**
     * 负责人名称
     */
    private String principalName;

    /**
     * 负责人身份证号码
     */
    private String principalIdCode;

    /**
     * 联系电话(客服电话)
     */
    private String contactNumber;

    /**
     * 联系地址(救助站地址)
     */
    private String stationAddress;

    /**
     * 救助站logo图片地址
     */
    private String stationLogo;

    /**
     * 救助站营业执照图片地址
     */
    private String stationBusinessLicense;

    /**
     * 身份证照片人像面图片地址
     */
    private String principalIdPortrait;

    /**
     * 身份证照片国徽面图片地址
     */
    private String principalIdNationalEmblem;

    /**
     * 认证状态(1待审核 已审核)
     */
    private Integer certificationStatus;

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
package cn.basicPLY.animals.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 流浪动物救助站：领养信息与领养人关系状态表
 *
 * @TableName stray_animals_adopter
 */
@TableName(value = "stray_animals_adopter")
@Data
public class StrayAnimalsAdopter implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private String keyId;

    /**
     * 领养信息ID
     */
    private String adoptionId;

    /**
     * 领养人ID（用户ID）
     */
    private String adopterId;

    /**
     * 领养人领养状态（1已申请 2可查看联系方式）
     */
    private Integer adopterState;

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
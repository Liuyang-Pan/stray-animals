package cn.basicPLY.animals.entity.DTO;

import cn.basicPLY.animals.entity.StrayAnimalsVolunteer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * purpose:志愿者列表DTO
 *
 * @author Pan Liuyang
 * 2022/5/20 22:48
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("志愿者列表DTO")
@Data
public class VolunteerManagementDTO extends StrayAnimalsVolunteer implements Serializable {
    private static final long serialVersionUID = 4457551409786166973L;

    /**
     * 当前页
     */
    @ApiModelProperty("当前页")
    private long current = 1;
    /**
     * 每页最大数
     */
    @ApiModelProperty("每页最大数")
    private long size = 10;
}

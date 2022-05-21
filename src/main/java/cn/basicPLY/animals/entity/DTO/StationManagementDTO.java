package cn.basicPLY.animals.entity.DTO;

import cn.basicPLY.animals.entity.StrayAnimalsAidStation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * purpose:救助站列表DTO
 *
 * @author Pan Liuyang
 * 2022/5/21 8:50
 */
@ApiModel("救助站列表DTO")
@EqualsAndHashCode(callSuper = true)
@Data
public class StationManagementDTO extends StrayAnimalsAidStation implements Serializable {
    private static final long serialVersionUID = -5855682124992507298L;

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

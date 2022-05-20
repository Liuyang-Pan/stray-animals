package cn.basicPLY.animals.entity.DTO;

import cn.basicPLY.animals.entity.StrayAnimalsUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * purpose:用户列表DTO
 *
 * @author Pan Liuyang
 * 2022/5/20 20:05
 */
@ApiModel("用户列表DTO")
@EqualsAndHashCode(callSuper = true)
@Data
public class UserManagementDTO extends StrayAnimalsUser implements Serializable {
    private static final long serialVersionUID = 1380160142198980538L;

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

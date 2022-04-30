package cn.basicPLY.animals.entity.VO;

import cn.basicPLY.animals.entity.StrayAnimalsAdopter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * purpose:领养人返回VO
 *
 * @author Pan Liuyang
 * 2022/4/29 23:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("领养人返回VO")
public class StrayAnimalsAdopterVO extends StrayAnimalsAdopter implements Serializable {
    private static final long serialVersionUID = -3225423347311324160L;

    /**
     * 领养人名称
     */
    @ApiModelProperty("领养人名称")
    private String nickName;

    /**
     * 领养人电话
     */
    @ApiModelProperty("领养人电话")
    private String adopterPhoneNumber;
}

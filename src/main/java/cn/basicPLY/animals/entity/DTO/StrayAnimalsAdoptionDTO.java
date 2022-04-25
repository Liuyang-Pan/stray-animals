package cn.basicPLY.animals.entity.DTO;

import cn.basicPLY.animals.entity.StrayAnimalsAdoption;
import cn.basicPLY.animals.entity.StrayAnimalsAdoptionFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * purpose:领养信息发布入参DTO
 *
 * @author Pan Liuyang
 * 2022/4/25 21:17
 */
@ApiModel("领养信息发布入参DTO")
@Data
@EqualsAndHashCode(callSuper = true)
public class StrayAnimalsAdoptionDTO extends StrayAnimalsAdoption implements Serializable {
    private static final long serialVersionUID = -2611216654268565159L;

    /**
     * 领养图片关联中间表
     */
    @ApiModelProperty("领养图片关联中间表数据")
    List<StrayAnimalsAdoptionFile> strayAnimalsAdoptionFile;
}

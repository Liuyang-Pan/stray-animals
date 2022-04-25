package cn.basicPLY.animals.entity.VO;

import cn.basicPLY.animals.entity.StrayAnimalsAdoption;
import cn.basicPLY.animals.entity.StrayAnimalsFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * purpose:领养信息返回实体VO
 *
 * @author Pan Liuyang
 * 2022/4/25 21:42
 */
@ApiModel("领养信息返回实体VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class StrayAnimalsAdoptionVO extends StrayAnimalsAdoption implements Serializable {
    private static final long serialVersionUID = -6826841916810560705L;

    /**
     * 领养关联文件列表
     */
    @ApiModelProperty("领养关联文件列表")
    private List<StrayAnimalsFile> strayAnimalsFiles;
}

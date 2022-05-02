package cn.basicPLY.animals.entity.VO;

import cn.basicPLY.animals.entity.StrayAnimalsFile;
import cn.basicPLY.animals.entity.StrayAnimalsResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * purpose:
 *
 * @author Pan Liuyang
 * 2022/5/2 16:13
 */
@ApiModel("资源信息返回实体类")
@Data
@EqualsAndHashCode(callSuper = true)
public class StrayAnimalsResourceVO extends StrayAnimalsResource implements Serializable {
    private static final long serialVersionUID = 7294127483618083544L;

    /**
     * 资源信息关联文件列表
     */
    @ApiModelProperty("资源信息关联文件列表")
    private List<StrayAnimalsFile> strayAnimalsFiles;
}

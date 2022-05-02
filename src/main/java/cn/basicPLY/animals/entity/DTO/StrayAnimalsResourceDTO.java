package cn.basicPLY.animals.entity.DTO;

import cn.basicPLY.animals.entity.StrayAnimalsResource;
import cn.basicPLY.animals.entity.StrayAnimalsResourceFile;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * purpose:
 *
 * @author Pan Liuyang
 * 2022/5/2 14:42
 */
@ApiModel("资源发布入参信息DTO")
@Data
@EqualsAndHashCode(callSuper = true)
public class StrayAnimalsResourceDTO extends StrayAnimalsResource implements Serializable {
    private static final long serialVersionUID = 5822020256836592316L;

    /**
     * 资源信息文件关联表
     */
    List<StrayAnimalsResourceFile> strayAnimalsResourceFiles;
}

package cn.basicPLY.animals.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * purpose:上传文件成功返回VO
 *
 * @author Pan Liuyang
 * 2022/4/25 19:57
 */
@Data
@ApiModel("文件上传返回VO")
public class FileReturnParameterVO implements Serializable {
    private static final long serialVersionUID = 2945614782089782845L;

    /**
     * 上传文件ID
     */
    @ApiModelProperty("上传文件ID")
    private String fileId;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 相对路径
     */
    @ApiModelProperty("相对路径")
    private String relativePath;
}

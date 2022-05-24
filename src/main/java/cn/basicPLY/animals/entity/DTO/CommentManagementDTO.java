package cn.basicPLY.animals.entity.DTO;

import cn.basicPLY.animals.entity.StrayAnimalsComment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * purpose:
 *
 * @author Pan Liuyang
 * 2022/5/25 0:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("评论管理查询DTO")
public class CommentManagementDTO extends StrayAnimalsComment implements Serializable {
    private static final long serialVersionUID = -2895884330591315654L;

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

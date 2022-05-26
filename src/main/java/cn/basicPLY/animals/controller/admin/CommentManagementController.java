package cn.basicPLY.animals.controller.admin;

import cn.basicPLY.animals.entity.DTO.CommentManagementDTO;
import cn.basicPLY.animals.entity.StrayAnimalsComment;
import cn.basicPLY.animals.enumerate.Constants;
import cn.basicPLY.animals.service.StrayAnimalsCommentService;
import cn.basicPLY.animals.utils.AjaxResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * purpose:
 *
 * @author Pan Liuyang
 * 2022/5/24 22:43
 */
@Api(tags = "评论管理相关接口")
@Slf4j
@RestController
@RequestMapping("/comment-management")
public class CommentManagementController {

    @Autowired
    private StrayAnimalsCommentService commentService;

    /**
     * 删除评论信息接口
     *
     * @param keyId 评论ID
     * @return 删除是否成功结果
     */
    @ApiOperation("删除评论信息接口")
    @DeleteMapping("/delete/{keyId}")
    public ResponseEntity<AjaxResult> delete(@PathVariable("keyId") String keyId) {
        log.info("开始执行删除评论信息接口接口");
        StrayAnimalsComment deleteComment = new StrayAnimalsComment();
        deleteComment.setDeleteMark(Constants.DELETED);
        deleteComment.setKeyId(keyId);
        int result = commentService.getBaseMapper().updateById(deleteComment);
        if (result > 0) {
            return ResponseEntity.ok(AjaxResult.success("删除成功"));
        }
        return ResponseEntity.ok(AjaxResult.error("删除失败"));
    }

    /**
     * 获取管理评论列表接口
     *
     * @param comment 查询DTO
     * @return 返回分页数据
     */
    @ApiOperation("获取管理评论列表接口")
    @GetMapping("/getCommentManagementList")
    public ResponseEntity<AjaxResult> getComment(CommentManagementDTO comment) {
        log.info("开始获取管理评论列表接口接口");
        LambdaQueryWrapper<StrayAnimalsComment> commentQueryWrapper = new LambdaQueryWrapper<>();
        commentQueryWrapper.eq(StrayAnimalsComment::getDeleteMark, Constants.NOT_DELETED)
                .like(StringUtils.isNotBlank(comment.getCommentContent()), StrayAnimalsComment::getCommentContent, comment.getCommentContent())
                .like(StringUtils.isNotBlank(comment.getCommentatorName()), StrayAnimalsComment::getCommentatorName, comment.getCommentatorName())
                .eq(null != comment.getDataType(), StrayAnimalsComment::getDataType, comment.getDataType())
                .orderByDesc(StrayAnimalsComment::getCreateBy);
        IPage<StrayAnimalsComment> strayAnimalsCommentIPage = commentService.getBaseMapper().selectPage(new Page<>(comment.getCurrent(), comment.getSize()), commentQueryWrapper);
        return ResponseEntity.ok(AjaxResult.success("获取成功", strayAnimalsCommentIPage));
    }
}

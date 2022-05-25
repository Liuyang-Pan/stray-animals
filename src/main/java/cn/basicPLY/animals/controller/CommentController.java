package cn.basicPLY.animals.controller;

import cn.basicPLY.animals.entity.StrayAnimalsComment;
import cn.basicPLY.animals.enumerate.Constants;
import cn.basicPLY.animals.enumerate.UniversalColumnEnum;
import cn.basicPLY.animals.service.StrayAnimalsCommentService;
import cn.basicPLY.animals.utils.AjaxResult;
import cn.basicPLY.animals.utils.UserUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * purpose:评论相关接口
 *
 * @author Pan Liuyang
 * 2022/5/24 22:04
 */
@Api(tags = "评论相关接口")
@Slf4j
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private StrayAnimalsCommentService commentService;

    @ApiOperation("发布评论接口")
    @PostMapping("/release")
    public ResponseEntity<AjaxResult> release(@RequestBody StrayAnimalsComment comment) {
        log.info("开始执行发布评论接口");
        //判断是否登录用户
        if (ObjectUtils.isEmpty(UserUtils.getUserDetails()) || StringUtils.isBlank(UserUtils.getUserDetails().getKeyId())) {
            return ResponseEntity.ok(AjaxResult.error("请登录用户"));
        }
        //填充发布人和发布时间等信息
        comment.setCommentatorId(UserUtils.getUserDetails().getKeyId());
        if (StringUtils.isNotEmpty(UserUtils.getUserDetails().getNickName())) {
            String nickName = UserUtils.getUserDetails().getNickName();
            comment.setCommentatorName(nickName);
            comment.setCreateBy(nickName);
        }
        comment.setCreateDate(new Date());
        int result = commentService.getBaseMapper().insert(comment);
        //写入数量大于1则返回成功
        if (result > 0) {
            return ResponseEntity.ok(AjaxResult.success("评论成功"));
        }
        return ResponseEntity.ok(AjaxResult.error("评论失败"));
    }

    @ApiOperation("获取某个数据的评论信息接口")
    @GetMapping("/getComment")
    public ResponseEntity<AjaxResult> getComment(@RequestParam("current") long current,
                                                 @RequestParam("size") long size,
                                                 @RequestParam("dataType") Integer dataType,
                                                 @RequestParam("keyId") String keyId) {
        log.info("开始执行获取某个数据的评论信息接口");
        QueryWrapper<StrayAnimalsComment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq(UniversalColumnEnum.UPDATE_DATE.getColumn(), Constants.NOT_DELETED)
                .eq("data_type", dataType)
                .eq("data_id", keyId);
        IPage<StrayAnimalsComment> strayAnimalsCommentIPage = commentService.getBaseMapper().selectPage(new Page<>(current, size), commentQueryWrapper);
        return ResponseEntity.ok(AjaxResult.success("获取成功", strayAnimalsCommentIPage));
    }
}

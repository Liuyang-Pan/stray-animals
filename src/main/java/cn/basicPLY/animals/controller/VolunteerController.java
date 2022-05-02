package cn.basicPLY.animals.controller;

import cn.basicPLY.animals.entity.StrayAnimalsVolunteer;
import cn.basicPLY.animals.service.StrayAnimalsVolunteerService;
import cn.basicPLY.animals.utils.AjaxResult;
import cn.basicPLY.animals.utils.UserUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * purpose:
 *
 * @author Pan Liuyang
 * 2022/5/2 10:05
 */
@Api(tags = "志愿者信息相关接口")
@Slf4j
@RestController
@RequestMapping("/volunteer")
public class VolunteerController {

    /**
     * 志愿者Service
     */
    @Autowired
    private StrayAnimalsVolunteerService volunteerService;

    @ApiOperation("申请认证志愿者接口")
    @PostMapping("/applyToVolunteer")
    public ResponseEntity<AjaxResult> applyToVolunteer(@RequestBody StrayAnimalsVolunteer volunteer) {
        log.info("开始执行申请认证志愿者接口");
        //判断是否登录用户
        if (ObjectUtils.isEmpty(UserUtils.getUserDetails()) || StringUtils.isBlank(UserUtils.getUserDetails().getKeyId())) {
            return new ResponseEntity<>(AjaxResult.error("请登录用户"), HttpStatus.OK);
        }
        //判断是否已经申请
        QueryWrapper<StrayAnimalsVolunteer> volunteerQueryWrapper = new QueryWrapper<>();
        volunteerQueryWrapper.eq("user_id", UserUtils.getUserDetails().getKeyId()).eq("delete_mark", 1);
        Long result = volunteerService.getBaseMapper().selectCount(volunteerQueryWrapper);
        if (ObjectUtils.isNotEmpty(result) && result > 0) {
            return new ResponseEntity<>(AjaxResult.error("您已申请过了"), HttpStatus.OK);
        }
        //设置关联用户ID和创建人和时间
        volunteer.setUserId(UserUtils.getUserDetails().getKeyId());
        volunteer.setCreateBy(UserUtils.getUserDetails().getNickName());
        volunteer.setCreateDate(new Date());
        volunteerService.getBaseMapper().insert(volunteer);
        return new ResponseEntity<>(AjaxResult.success("申请志愿者成功"), HttpStatus.OK);
    }
}

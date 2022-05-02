package cn.basicPLY.animals.controller;

import cn.basicPLY.animals.entity.StrayAnimalsAidStation;
import cn.basicPLY.animals.service.StrayAnimalsAidStationService;
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
 * 2022/5/1 17:18
 */
@Api(tags = "救助站认证相关接口")
@Slf4j
@RestController
@RequestMapping("/aidStation")
public class AidStationController {

    /**
     * 救助站/基地Service
     */
    @Autowired
    private StrayAnimalsAidStationService aidStationService;

    @ApiOperation("申请认证救助站/基地接口")
    @PostMapping("/applyForCertification")
    public ResponseEntity<AjaxResult> applyForCertification(@RequestBody StrayAnimalsAidStation aidStation) {
        log.info("开始执行申请认证救助站/基地接口");
        //判断是否登录用户
        if (ObjectUtils.isEmpty(UserUtils.getUserDetails()) || StringUtils.isBlank(UserUtils.getUserDetails().getKeyId())) {
            return new ResponseEntity<>(AjaxResult.error("请登录用户"), HttpStatus.OK);
        }
        //判断是否已经申请
        QueryWrapper<StrayAnimalsAidStation> checkAidStation = new QueryWrapper<>();
        checkAidStation.eq("delete_mark", 1)
                .eq("user_id", UserUtils.getUserDetails().getKeyId());
        Long aLong = aidStationService.getBaseMapper().selectCount(checkAidStation);
        if (ObjectUtils.isNotEmpty(aLong) && aLong > 0) {
            return new ResponseEntity<>(AjaxResult.error("您已申请过了"), HttpStatus.OK);
        }
        //设置认证的用户
        aidStation.setUserId(UserUtils.getUserDetails().getKeyId());
        aidStation.setCreateBy(UserUtils.getUserDetails().getNickName());
        aidStation.setCreateDate(new Date());
        if (aidStationService.getBaseMapper().insert(aidStation) > 0) {
            return new ResponseEntity<>(AjaxResult.success("申请认证成功"), HttpStatus.OK);
        }
        return new ResponseEntity<>(AjaxResult.error("申请认证失败"), HttpStatus.OK);
    }
}

package cn.basicPLY.animals.controller;

import cn.basicPLY.animals.entity.StrayAnimalsAidStation;
import cn.basicPLY.animals.entity.StrayAnimalsUserAuthority;
import cn.basicPLY.animals.entity.VO.CertificationUserDetails;
import cn.basicPLY.animals.enumerate.Constants;
import cn.basicPLY.animals.enumerate.UniversalColumnEnum;
import cn.basicPLY.animals.service.StrayAnimalsAidStationService;
import cn.basicPLY.animals.utils.AjaxResult;
import cn.basicPLY.animals.utils.UserUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.stream.Collectors;

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

    @ApiOperation("获取救助站/基地列表接口")
    @GetMapping("/getAidStationList")
    public ResponseEntity<AjaxResult> getAidStationList(Page<StrayAnimalsAidStation> page,
                                                        @RequestParam(required = false) String stationName,
                                                        @RequestParam(required = false) String principal,
                                                        @RequestParam(required = false) String address) {
        QueryWrapper<StrayAnimalsAidStation> aidStationQueryWrapper = new QueryWrapper<>();
        aidStationQueryWrapper.eq(UniversalColumnEnum.DELETE_MARK.getColumn(), Constants.NOT_DELETED);
        //判断是否登录用户
        if (ObjectUtils.isNotEmpty(UserUtils.getUserDetails())) {
            //判断非超级管理员则只显示认证通过信息
            CertificationUserDetails userDetails = UserUtils.getUserDetails();
            if (userDetails.getUserAuthorities().isEmpty() || (!userDetails.getUserAuthorities().stream().map(StrayAnimalsUserAuthority::getAuthority).collect(Collectors.toList()).contains("ROLE_admin"))) {
                aidStationQueryWrapper.eq("certification_status", Constants.VOLUNTEER_CERTIFICATION_PASSED);
            }
        }
        if (StringUtils.isNotBlank(stationName)) {
            aidStationQueryWrapper.like("station_name", stationName);
        }
        if (StringUtils.isNotBlank(principal)) {
            aidStationQueryWrapper.like("principal_name", principal);
        }
        if (StringUtils.isNotBlank(address)) {
            aidStationQueryWrapper.like("station_address", address);
        }
        Page<StrayAnimalsAidStation> strayAnimalsAidStationPage = aidStationService.getBaseMapper().selectPage(page, aidStationQueryWrapper);
        return new ResponseEntity<>(AjaxResult.success("获取数据成功", strayAnimalsAidStationPage), HttpStatus.OK);
    }

    @ApiOperation("获取救助站/基地详情接口")
    @GetMapping("/getAidStationInfo/{keyId}")
    public ResponseEntity<AjaxResult> getAidStationList(@PathVariable String keyId) {
        return new ResponseEntity<>(AjaxResult.success("获取数据成功", aidStationService.getBaseMapper().selectById(keyId)), HttpStatus.OK);
    }
}

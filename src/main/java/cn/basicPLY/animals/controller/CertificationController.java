package cn.basicPLY.animals.controller;

import cn.basicPLY.animals.entity.StrayAnimalsUser;
import cn.basicPLY.animals.service.CertificationService;
import cn.basicPLY.animals.utils.AjaxResult;
import cn.basicPLY.animals.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * purpose:登录注册认证Controller层
 *
 * @author Pan Liuyang
 * 2022/4/5 22:37
 */
@Api(tags = "用户相关接口")
@Slf4j
@RestController
@RequestMapping("/certification")
public class CertificationController {

    @Autowired
    private CertificationService certificationService;

    /**
     * 注册用户接口
     *
     * @param strayAnimalsUser 用户实体
     * @return 注册成功与否标识
     */
    @ApiOperation("用户注册接口")
    @PostMapping("/registered")
    public ResponseEntity<AjaxResult> registeredUser(@RequestBody StrayAnimalsUser strayAnimalsUser) {
        log.info("开始执行用户注册接口");
        ResponseEntity<AjaxResult> resultResponseEntity = certificationService.checkParameters(strayAnimalsUser);
        //无异常信息则进入注册流程
        if (ObjectUtils.isEmpty(resultResponseEntity)) {
            int result = certificationService.registeredUser(strayAnimalsUser);
            if (result > 0) {
                return new ResponseEntity<>(AjaxResult.success("注册成功"), HttpStatus.OK);
            }
            return new ResponseEntity<>(AjaxResult.error("注册失败"), HttpStatus.BAD_REQUEST);
        }
        return resultResponseEntity;
    }

    /**
     * 修改用户接口
     *
     * @param strayAnimalsUser 用户实体
     * @return 修改成功与否结果
     */
    @ApiOperation("修改用户接口")
    @PutMapping("/modify")
    public ResponseEntity<AjaxResult> modifyUser(@RequestBody StrayAnimalsUser strayAnimalsUser) {
        log.info("开始执行修改用户接口");
        //判断是否登录用户
        if (ObjectUtils.isEmpty(UserUtils.getUserDetails()) || StringUtils.isBlank(UserUtils.getUserDetails().getKeyId())) {
            return new ResponseEntity<>(AjaxResult.error("请登录用户"), HttpStatus.OK);
        }
        strayAnimalsUser.setKeyId(UserUtils.getUserDetails().getKeyId());
        int result = certificationService.modifyUser(strayAnimalsUser);
        if (result > 0) {
            return new ResponseEntity<>(AjaxResult.success("修改成功"), HttpStatus.OK);
        }
        return new ResponseEntity<>(AjaxResult.error("修改失败"), HttpStatus.OK);
    }

    /**
     * 修改密码接口
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 返回修改结果
     */
    @ApiOperation("修改密码接口")
    @PutMapping("/modifyPassword")
    public ResponseEntity<AjaxResult> modifyPassword(String oldPassword, String newPassword) {
        log.info("开始执行修改密码接口接口");
        //判断是否登录用户
        if (ObjectUtils.isEmpty(UserUtils.getUserDetails()) || StringUtils.isBlank(UserUtils.getUserDetails().getKeyId())) {
            return new ResponseEntity<>(AjaxResult.error("请登录用户"), HttpStatus.OK);
        }
        if (certificationService.modifyPassword(UserUtils.getUserDetails().getKeyId(), oldPassword, newPassword)) {
            return new ResponseEntity<>(AjaxResult.success("修改密码成功"), HttpStatus.OK);
        }
        return new ResponseEntity<>(AjaxResult.error("旧密码错误，请重新输入"), HttpStatus.OK);
    }
}

package cn.basicPLY.animals.controller;

import cn.basicPLY.animals.entity.StrayAnimalsUser;
import cn.basicPLY.animals.service.CertificationService;
import cn.basicPLY.animals.units.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

/**
 * purpose:登录注册认证Controller层
 *
 * @author Pan Liuyang
 * 2022/4/5 22:37
 */
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
    @PostMapping("/registered")
    public ResponseEntity<AjaxResult> registeredUser(@RequestBody StrayAnimalsUser strayAnimalsUser) {
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

    @PutMapping("/modify")
    public ResponseEntity<AjaxResult> modifyUser(@RequestBody StrayAnimalsUser strayAnimalsUser) {
        ResponseEntity<AjaxResult> resultResponseEntity = certificationService.checkParameters(strayAnimalsUser);
        if (ObjectUtils.isEmpty(resultResponseEntity)) {
            int result = certificationService.modifyUser(strayAnimalsUser);
            if (result > 0) {
                return new ResponseEntity<>(AjaxResult.success("修改成功"), HttpStatus.OK);
            }
            return new ResponseEntity<>(AjaxResult.error("修改失败"), HttpStatus.BAD_REQUEST);
        }
        return resultResponseEntity;
    }
}

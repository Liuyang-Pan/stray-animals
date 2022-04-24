package cn.basicPLY.animals.controller;

import cn.basicPLY.animals.entity.StrayAnimalsAdoption;
import cn.basicPLY.animals.service.StrayAnimalsAdoptionService;
import cn.basicPLY.animals.utils.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * purpose:领养动物相关接口
 *
 * @author Pan Liuyang
 * 2022/4/23 15:52
 */
@Api(tags = "领养动物相关接口")
@Slf4j
@RestController
@RequestMapping("/adoption")
public class AdoptionController {

    @Autowired
    private StrayAnimalsAdoptionService adoptionService;

    /**
     * 发布领养信息接口
     *
     * @param strayAnimalsAdoption 领养信息
     * @return 发布成功与否信息
     */
    @ApiOperation("发布领养信息")
    @PostMapping("publish")
    public ResponseEntity<AjaxResult> publishAdoptionInfo(StrayAnimalsAdoption strayAnimalsAdoption) {
        return new ResponseEntity<>(AjaxResult.success("发布成功"), HttpStatus.OK);
    }
}

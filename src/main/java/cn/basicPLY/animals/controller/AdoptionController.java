package cn.basicPLY.animals.controller;

import cn.basicPLY.animals.entity.StrayAnimalsAdoption;
import cn.basicPLY.animals.service.StrayAnimalsAdoptionService;
import cn.basicPLY.animals.units.AjaxResult;
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
@Slf4j
@RestController
@RequestMapping("/adoption")
public class AdoptionController {

    @Autowired
    private StrayAnimalsAdoptionService adoptionService;

    @PostMapping("publish")
    public ResponseEntity<AjaxResult> publishAdoptionInfo(StrayAnimalsAdoption strayAnimalsAdoption) {
        return new ResponseEntity<>(AjaxResult.success("发布成功"), HttpStatus.OK);
    }
}

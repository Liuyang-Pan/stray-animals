package cn.basicPLY.animals.controller;

import cn.basicPLY.animals.entity.DTO.StrayAnimalsAdoptionDTO;
import cn.basicPLY.animals.entity.StrayAnimalsAdoption;
import cn.basicPLY.animals.entity.VO.StrayAnimalsAdoptionVO;
import cn.basicPLY.animals.service.StrayAnimalsAdoptionFileService;
import cn.basicPLY.animals.service.StrayAnimalsAdoptionService;
import cn.basicPLY.animals.utils.AjaxResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 领养信息Service
     */
    @Autowired
    private StrayAnimalsAdoptionService adoptionService;

    /**
     * 领养信息文件关联Service
     */
    @Autowired
    private StrayAnimalsAdoptionFileService adoptionFileService;

    /**
     * 发布领养信息接口
     *
     * @param adoptionDTO 领养信息
     * @return 发布成功与否信息
     */
    @ApiOperation("发布领养信息")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/publish")
    public ResponseEntity<AjaxResult> publishAdoptionInfo(@RequestBody StrayAnimalsAdoptionDTO adoptionDTO) {
        int result = 0;
        //创建领养信息主表对象
        StrayAnimalsAdoption strayAnimalsAdoption = new StrayAnimalsAdoption();
        //转换基础字段
        BeanUtils.copyProperties(adoptionDTO, strayAnimalsAdoption);
        //写入领养信息
        result += adoptionService.getBaseMapper().insert(strayAnimalsAdoption);

        if (ObjectUtils.isNotEmpty(adoptionDTO.getStrayAnimalsAdoptionFile())) {
            //为写入的关联文件做领养信息表关联
            adoptionDTO.getStrayAnimalsAdoptionFile().forEach(aFile -> {
                aFile.setAdoptionId(strayAnimalsAdoption.getKeyId());
            });
            //批量写入
            adoptionFileService.saveBatch(adoptionDTO.getStrayAnimalsAdoptionFile());
        }
        if (result > 0) {
            return new ResponseEntity<>(AjaxResult.success("发布成功"), HttpStatus.OK);
        }
        return new ResponseEntity<>(AjaxResult.error("发布失败"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 获取领养列表
     *
     * @return 分页领养数据
     */
    @ApiOperation("获取领养列表")
    @GetMapping("/getAdoptionList")
    public ResponseEntity<IPage<StrayAnimalsAdoptionVO>> getAdoptionList(@RequestParam(required = false) long current,
                                                                         @RequestParam(required = false) long size,
                                                                         @RequestParam(required = false) String userId,
                                                                         @RequestParam(required = false) String adoptionTitle) {
        Page<StrayAnimalsAdoptionVO> adoptionVOPage = new Page<>(current, size);
        List<StrayAnimalsAdoptionVO> strayAnimalsAdoptionVOS = adoptionService.selectStrayAnimalsAdoptionPageVO(adoptionVOPage, userId, adoptionTitle);
        adoptionVOPage.setRecords(strayAnimalsAdoptionVOS);
        return new ResponseEntity<>(adoptionVOPage, HttpStatus.OK);
    }
}

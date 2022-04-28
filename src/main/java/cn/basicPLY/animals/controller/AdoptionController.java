package cn.basicPLY.animals.controller;

import cn.basicPLY.animals.entity.DTO.StrayAnimalsAdoptionDTO;
import cn.basicPLY.animals.entity.StrayAnimalsAdoption;
import cn.basicPLY.animals.entity.VO.StrayAnimalsAdoptionVO;
import cn.basicPLY.animals.service.StrayAnimalsAdoptionFileService;
import cn.basicPLY.animals.service.StrayAnimalsAdoptionService;
import cn.basicPLY.animals.utils.AjaxResult;
import cn.basicPLY.animals.utils.UserUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
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
        if (ObjectUtils.isEmpty(UserUtils.getUserDetails()) && StringUtils.isBlank(UserUtils.getUserDetails().getKeyId())) {
            return new ResponseEntity<>(AjaxResult.error("请登录用户"), HttpStatus.BAD_REQUEST);
        }
        int result = 0;
        //创建领养信息主表对象
        StrayAnimalsAdoption strayAnimalsAdoption = new StrayAnimalsAdoption();
        //转换基础字段
        BeanUtils.copyProperties(adoptionDTO, strayAnimalsAdoption);
        //设置发布人的ID
        strayAnimalsAdoption.setForeignKeyPublisher(UserUtils.getUserDetails().getKeyId());
        strayAnimalsAdoption.setCreateBy(null != UserUtils.getUserDetails().getNickName() ? UserUtils.getUserDetails().getNickName() : "");
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
    public ResponseEntity<IPage<StrayAnimalsAdoptionVO>> getAdoptionList(Page<StrayAnimalsAdoptionVO> page,
                                                                         @RequestParam(required = false) String userId,
                                                                         @RequestParam(required = false) String adoptionTitle) {
        //分页查询
        if (ObjectUtils.isNotEmpty(UserUtils.getUserDetails())) {
            log.info("User:{}", UserUtils.getUserDetails());
        }
        List<StrayAnimalsAdoptionVO> strayAnimalsAdoptionVOS = adoptionService.selectStrayAnimalsAdoptionPageVO(page, userId, adoptionTitle);
        return new ResponseEntity<>(page.setRecords(strayAnimalsAdoptionVOS), HttpStatus.OK);
    }

    @ApiOperation("获取领养详情")
    @GetMapping("/getAdoption/{keyId}")
    public ResponseEntity<StrayAnimalsAdoptionVO> getAdoption(@PathVariable String keyId) {
        //主键为空返回错误请求
        if (StringUtils.isBlank(keyId)) {
            new ResponseEntity<>("查询的keyId为null 请检查", HttpStatus.BAD_REQUEST);
        }
        StrayAnimalsAdoptionVO strayAnimalsAdoptionVO = adoptionService.selectStrayAnimalsAdoptionInfoByKeyId(keyId);
        //查询出的数据为null返回错误信息
        if (ObjectUtils.isEmpty(strayAnimalsAdoptionVO)) {
            new ResponseEntity<>("暂无此数据 请检查keyId", HttpStatus.BAD_REQUEST);
        }
        //返回领养详情数据
        return new ResponseEntity<>(strayAnimalsAdoptionVO, HttpStatus.OK);
    }
}

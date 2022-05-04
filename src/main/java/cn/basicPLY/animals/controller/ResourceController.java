package cn.basicPLY.animals.controller;

import cn.basicPLY.animals.entity.DTO.StrayAnimalsResourceDTO;
import cn.basicPLY.animals.entity.StrayAnimalsAidStation;
import cn.basicPLY.animals.entity.StrayAnimalsResource;
import cn.basicPLY.animals.entity.VO.StrayAnimalsResourceVO;
import cn.basicPLY.animals.service.StrayAnimalsAidStationService;
import cn.basicPLY.animals.service.StrayAnimalsResourceFileService;
import cn.basicPLY.animals.service.StrayAnimalsResourceService;
import cn.basicPLY.animals.utils.AjaxResult;
import cn.basicPLY.animals.utils.UserUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * purpose:
 *
 * @author Pan Liuyang
 * 2022/5/2 14:31
 */
@Api(tags = "资源发布相关接口")
@Slf4j
@RestController
@RequestMapping("/resource")
public class ResourceController {

    /**
     * 发布资源/需求Service
     */
    @Autowired
    private StrayAnimalsResourceService resourceService;

    /**
     * 资源/需求文件关联Service
     */
    @Autowired
    private StrayAnimalsResourceFileService resourceFileService;

    /**
     * 救助站/基地Service
     */
    @Autowired
    private StrayAnimalsAidStationService aidStationService;

    @ApiOperation("发布资源/需求接口")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("publishResources")
    public ResponseEntity<AjaxResult> publishResources(@RequestBody StrayAnimalsResourceDTO resourceDTO) {
        log.info("开始执行发布资源/需求接口");
        //判断是否登录用户
        if (ObjectUtils.isEmpty(UserUtils.getUserDetails()) || StringUtils.isBlank(UserUtils.getUserDetails().getKeyId())) {
            return new ResponseEntity<>(AjaxResult.error("请登录用户"), HttpStatus.BAD_REQUEST);
        }
        int result = 0;
        //创建主表资源对象
        StrayAnimalsResource strayAnimalsResource = new StrayAnimalsResource();
        //转换基础字段
        BeanUtils.copyProperties(resourceDTO, strayAnimalsResource);
        //设置发布者
        strayAnimalsResource.setForeignKeyPublisher(UserUtils.getUserDetails().getKeyId());
        //判断是否是救助站
        QueryWrapper<StrayAnimalsAidStation> checkAidStation = new QueryWrapper<>();
        checkAidStation.eq("delete_mark", 1)
                .eq("user_id", UserUtils.getUserDetails().getKeyId());
        Long aLong = aidStationService.getBaseMapper().selectCount(checkAidStation);
        if (ObjectUtils.isNotEmpty(aLong) && aLong > 0) {
            //设置为救助站站发布信息 查询时显示靠前
            strayAnimalsResource.setAidStationMark(1);
        }
        //设置创建人
        strayAnimalsResource.setCreateBy(UserUtils.getUserDetails().getNickName());
        strayAnimalsResource.setCreateDate(new Date());
        //写入发布资源/需求信息
        result += resourceService.getBaseMapper().insert(strayAnimalsResource);

        if (ObjectUtils.isNotEmpty(resourceDTO.getStrayAnimalsResourceFiles())) {
            //为写入的关联文件做领养信息表关联
            resourceDTO.getStrayAnimalsResourceFiles().forEach(aFile -> {
                aFile.setResourceId(strayAnimalsResource.getKeyId());
            });
            //批量写入
            resourceFileService.saveBatch(resourceDTO.getStrayAnimalsResourceFiles());
        }
        if (result > 0) {
            return new ResponseEntity<>(AjaxResult.success("发布成功"), HttpStatus.OK);
        }
        return new ResponseEntity<>(AjaxResult.error("发布失败"), HttpStatus.OK);
    }

    @ApiOperation("获取资源/需求信息列表")
    @GetMapping("getResources")
    public ResponseEntity<AjaxResult> getResources(Page<StrayAnimalsResourceVO> page,
                                                   @RequestParam(required = false) String resourceType) {
        log.info("开始执行获取资源/需求信息列表接口");
        //判断是否登录用户
        if (ObjectUtils.isEmpty(UserUtils.getUserDetails()) || StringUtils.isBlank(UserUtils.getUserDetails().getKeyId())) {
            return new ResponseEntity<>(AjaxResult.error("请登录用户"), HttpStatus.OK);
        }
        List<StrayAnimalsResourceVO> strayAnimalsResourceVOList = resourceService.selectResourcePage(page, resourceType);
        return new ResponseEntity<>(AjaxResult.success(page.setRecords(strayAnimalsResourceVOList)), HttpStatus.OK);
    }

    @ApiOperation("获取资源/需求信息详情")
    @GetMapping("getResourcesByKeyId/{keyId}")
    public ResponseEntity<AjaxResult> getResourcesByKeyId(@PathVariable String keyId) {
        StrayAnimalsResourceVO strayAnimalsResourceVO = resourceService.selectResourcesByKeyId(keyId);
        if (ObjectUtils.isEmpty(strayAnimalsResourceVO)) {
            return new ResponseEntity<>(AjaxResult.error("暂无此数据 请检查KeyId"), HttpStatus.OK);
        }
        return new ResponseEntity<>(AjaxResult.success(strayAnimalsResourceVO), HttpStatus.OK);
    }
}

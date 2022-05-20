package cn.basicPLY.animals.controller;

import cn.basicPLY.animals.entity.*;
import cn.basicPLY.animals.entity.DTO.StrayAnimalsAdoptionDTO;
import cn.basicPLY.animals.entity.VO.StrayAnimalsAdoptionVO;
import cn.basicPLY.animals.enumerate.UniversalColumnEnum;
import cn.basicPLY.animals.service.StrayAnimalsAdopterService;
import cn.basicPLY.animals.service.StrayAnimalsAdoptionFileService;
import cn.basicPLY.animals.service.StrayAnimalsAdoptionService;
import cn.basicPLY.animals.service.StrayAnimalsAidStationService;
import cn.basicPLY.animals.utils.AjaxResult;
import cn.basicPLY.animals.utils.UserUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
     * 领养人信息相关Service
     */
    @Autowired
    private StrayAnimalsAdopterService adopterService;

    /**
     * 救助站/基地Service
     */
    @Autowired
    private StrayAnimalsAidStationService aidStationService;

    /**
     * 发布领养信息接口
     *
     * @param adoptionDTO 领养信息
     * @return 发布成功与否信息
     */
    @ApiOperation("发布领养/更新领养信息")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/publish")
    public ResponseEntity<AjaxResult> publishAdoptionInfo(@RequestBody StrayAnimalsAdoptionDTO adoptionDTO) {
        log.info("开始执行发布领养信息接口");
        //判断是否登录用户
        if (ObjectUtils.isEmpty(UserUtils.getUserDetails()) || StringUtils.isBlank(UserUtils.getUserDetails().getKeyId())) {
            return new ResponseEntity<>(AjaxResult.error("请登录用户"), HttpStatus.BAD_REQUEST);
        }
        //更新执行逻辑
        if ("Y".equals(adoptionDTO.getWhetherToUpdate())) {
            //更新主表
            StrayAnimalsAdoption strayAnimalsAdoption = new StrayAnimalsAdoption();
            BeanUtils.copyProperties(adoptionDTO, strayAnimalsAdoption);
            strayAnimalsAdoption.setUpdateBy(null != UserUtils.getUserDetails().getNickName() ? UserUtils.getUserDetails().getNickName() : "");
            strayAnimalsAdoption.setUpdateDate(new Date());
            int result = adoptionService.getBaseMapper().updateById(strayAnimalsAdoption);

            //先删文件关联
            QueryWrapper<StrayAnimalsAdoptionFile> fileQueryWrapper = new QueryWrapper<>();
            fileQueryWrapper.eq("adoption_id", strayAnimalsAdoption.getKeyId());
            adoptionFileService.getBaseMapper().delete(fileQueryWrapper);
            //再新增文件关联
            if (ObjectUtils.isNotEmpty(adoptionDTO.getStrayAnimalsAdoptionFile())) {
                adoptionDTO.getStrayAnimalsAdoptionFile().forEach(file -> file.setAdoptionId(strayAnimalsAdoption.getKeyId()));
                adoptionFileService.saveBatch(adoptionDTO.getStrayAnimalsAdoptionFile());
            }
            if (result > 0) {
                return new ResponseEntity<>(AjaxResult.success("更新成功"), HttpStatus.OK);
            }
            return new ResponseEntity<>(AjaxResult.error("更新失败"), HttpStatus.OK);
        }
        int result = 0;
        //创建领养信息主表对象
        StrayAnimalsAdoption strayAnimalsAdoption = new StrayAnimalsAdoption();
        //转换基础字段
        BeanUtils.copyProperties(adoptionDTO, strayAnimalsAdoption);
        //设置发布人的ID
        strayAnimalsAdoption.setForeignKeyPublisher(UserUtils.getUserDetails().getKeyId());
        //判断是否是救助站
        QueryWrapper<StrayAnimalsAidStation> checkAidStation = new QueryWrapper<>();
        checkAidStation.eq(UniversalColumnEnum.DELETE_MARK.getColumn(), 1)
                .eq("user_id", UserUtils.getUserDetails().getKeyId());
        Long aLong = aidStationService.getBaseMapper().selectCount(checkAidStation);
        if (ObjectUtils.isNotEmpty(aLong) && aLong > 0) {
            //设置为救助站站发布信息 查询时显示靠前
            strayAnimalsAdoption.setAidStationMark(1);
        }
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

    @ApiOperation("删除领养信息")
    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping("deleteAdoption")
    public ResponseEntity<AjaxResult> deleteAdoption(@RequestBody List<String> adoptionIds) {
        log.info("开始删除领养信息接口");
        AtomicBoolean flag = new AtomicBoolean(false);
        //首先删除文件关联
        UpdateWrapper<StrayAnimalsAdoptionFile> updateWrapper = new UpdateWrapper<>();
        adoptionIds.forEach(id -> {
            updateWrapper.eq("adoption_id", id);
            updateWrapper.set(UniversalColumnEnum.DELETE_MARK.getColumn(), 0);
            flag.set(adoptionFileService.update(updateWrapper));
            updateWrapper.clear();
        });
        //再删除领养申请
        UpdateWrapper<StrayAnimalsAdopter> adopterUpdateWrapper = new UpdateWrapper<>();
        adoptionIds.forEach(id -> {
            adopterUpdateWrapper.eq("adoption_id", id);
            adopterUpdateWrapper.set(UniversalColumnEnum.DELETE_MARK.getColumn(), 0);
            flag.set(adopterService.update(adopterUpdateWrapper));
            adopterUpdateWrapper.clear();
        });
        //再删除领养主表
        UpdateWrapper<StrayAnimalsAdoption> adoptionUpdateWrapper = new UpdateWrapper<>();
        adoptionIds.forEach(id -> {
            adoptionUpdateWrapper.eq("key_id", id);
            adoptionUpdateWrapper.set(UniversalColumnEnum.DELETE_MARK.getColumn(), 0);
            flag.set(adoptionService.update(adoptionUpdateWrapper));
            adoptionUpdateWrapper.clear();
        });
        if (flag.get()) {
            return ResponseEntity.ok(AjaxResult.success("删除数据成功"));
        }
        return ResponseEntity.ok(AjaxResult.error("删除数据失败"));
    }

    /**
     * 获取领养列表
     *
     * @return 分页领养数据
     */
    @ApiOperation("获取领养列表")
    @GetMapping("/getAdoptionList")
    public ResponseEntity<IPage<StrayAnimalsAdoptionVO>> getAdoptionList(Page<StrayAnimalsAdoptionVO> page,
                                                                         @RequestParam(required = false) String adoptionTitle,
                                                                         @RequestParam(required = false) String adoptionContent,
                                                                         @RequestParam(required = false) String adoptionAddress) {
        log.info("开始执行获取领养列表接口");
        //分页查询
        List<StrayAnimalsAdoptionVO> strayAnimalsAdoptionVOS = adoptionService.selectStrayAnimalsAdoptionPageVO(page, adoptionTitle, adoptionContent, adoptionAddress);
        //使电话显示为NULL
        strayAnimalsAdoptionVOS.forEach(adoption -> adoption.setAdoptionPhoneNumber(null));
        return new ResponseEntity<>(page.setRecords(strayAnimalsAdoptionVOS), HttpStatus.OK);
    }

    @ApiOperation("获取领养详情")
    @GetMapping("/getAdoption/{keyId}")
    public ResponseEntity<AjaxResult> getAdoption(@PathVariable String keyId) {
        log.info("开始执行获取领养详情接口");
        //主键为空返回错误请求
        if (StringUtils.isBlank(keyId)) {
            return new ResponseEntity<>(AjaxResult.error("查询的keyId为null 请检查"), HttpStatus.OK);
        }
        StrayAnimalsAdoptionVO strayAnimalsAdoptionVO = adoptionService.selectStrayAnimalsAdoptionInfoByKeyId(keyId);
        //查询出的数据为null返回错误信息
        if (ObjectUtils.isEmpty(strayAnimalsAdoptionVO)) {
            return new ResponseEntity<>(AjaxResult.error("暂无此数据 请检查keyId"), HttpStatus.OK);
        }
        //返回领养详情数据
        return new ResponseEntity<>(AjaxResult.success(strayAnimalsAdoptionVO), HttpStatus.OK);
    }

    @ApiOperation("申请领养")
    @PostMapping("/applyForAdoption")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<AjaxResult> applyForAdoption(@RequestBody StrayAnimalsAdopter strayAnimalsAdopter) {
        log.info("开始执行申请领养接口");
        //仅需要传入adoptionId
        //判断是否登录用户
        if (ObjectUtils.isEmpty(UserUtils.getUserDetails()) || StringUtils.isBlank(UserUtils.getUserDetails().getKeyId())) {
            return new ResponseEntity<>(AjaxResult.error("请登录用户"), HttpStatus.OK);
        }
        //判断是否已经申请
        QueryWrapper<StrayAnimalsAdopter> checkAdopter = new QueryWrapper<>();
        checkAdopter.eq(UniversalColumnEnum.DELETE_MARK.getColumn(), 1)
                .eq("adopter_id", UserUtils.getUserDetails().getKeyId())
                .eq("adoption_id", strayAnimalsAdopter.getAdoptionId());
        Long aLong = adopterService.getBaseMapper().selectCount(checkAdopter);
        if (ObjectUtils.isNotEmpty(aLong) && aLong > 0) {
            return new ResponseEntity<>(AjaxResult.error("您已申请过了"), HttpStatus.OK);
        }
        //写入领养人信息
        strayAnimalsAdopter.setAdopterId(UserUtils.getUserDetails().getKeyId());
        strayAnimalsAdopter.setCreateBy(UserUtils.getUserDetails().getNickName());
        strayAnimalsAdopter.setCreateDate(new Date());
        adopterService.getBaseMapper().insert(strayAnimalsAdopter);
        //更新领养信息表领养状态
        StrayAnimalsAdoption strayAnimalsAdoption = new StrayAnimalsAdoption();
        strayAnimalsAdoption.setKeyId(strayAnimalsAdopter.getAdoptionId());
        strayAnimalsAdoption.setAdoptionState(2);
        strayAnimalsAdoption.setUpdateBy(UserUtils.getUserDetails().getNickName());
        strayAnimalsAdoption.setUpdateDate(new Date());
        int result = adoptionService.getBaseMapper().updateById(strayAnimalsAdoption);
        if (result > 0) {
            return new ResponseEntity<>(AjaxResult.success("申请成功"), HttpStatus.OK);
        }
        return new ResponseEntity<>(AjaxResult.error("申请失败"), HttpStatus.OK);
    }

    @ApiOperation("取消领养申请")
    @PutMapping("/cancelAdoption")
    public ResponseEntity<AjaxResult> cancelAdoption(String keyId) {
        log.info("开始执行取消领养申请接口");
        //判断是否登录用户
        if (ObjectUtils.isEmpty(UserUtils.getUserDetails()) || StringUtils.isBlank(UserUtils.getUserDetails().getKeyId())) {
            return new ResponseEntity<>(AjaxResult.error("请登录用户"), HttpStatus.OK);
        }
        UpdateWrapper<StrayAnimalsAdopter> adopterUpdateWrapper = new UpdateWrapper<>();
        adopterUpdateWrapper.set(UniversalColumnEnum.DELETE_MARK.getColumn(), 0);
        adopterUpdateWrapper.eq("adoption_id", keyId);
        adopterUpdateWrapper.eq("adopter_id", UserUtils.getUserDetails().getKeyId());
        if (adopterService.update(adopterUpdateWrapper)) {
            return ResponseEntity.ok(AjaxResult.success("取消领养申请成功"));
        }
        return ResponseEntity.ok(AjaxResult.error("取消领养申请失败"));
    }

    @ApiOperation("同意申请")
    @PutMapping("/agreeToApply")
    public ResponseEntity<AjaxResult> agreeToApply(@RequestBody StrayAnimalsAdopter strayAnimalsAdopter) {
        log.info("开始执行同意申请接口");
        int result = adopterService.getBaseMapper().updateById(strayAnimalsAdopter);
        if (result > 0) {
            return new ResponseEntity<>(AjaxResult.success("同意申请成功"), HttpStatus.OK);
        }
        return new ResponseEntity<>(AjaxResult.error("同意失败"), HttpStatus.BAD_REQUEST);
    }

    @ApiOperation("已被领养")
    @PutMapping("/adopted")
    public ResponseEntity<AjaxResult> adopted(@RequestBody StrayAnimalsAdoption animalsAdoption) {
        log.info("开始执行已被领养接口");
        int result = adoptionService.getBaseMapper().updateById(animalsAdoption);
        if (result > 0) {
            return new ResponseEntity<>(AjaxResult.success("已被领养状态设置成功"), HttpStatus.OK);
        }
        return new ResponseEntity<>(AjaxResult.error("已被领养状态设置失败"), HttpStatus.BAD_REQUEST);
    }

    @ApiOperation("获取我的发布领养列表")
    @GetMapping("myPostAdoption")
    public ResponseEntity<AjaxResult> myPostAdoption(Page<StrayAnimalsAdoptionVO> page) {
        log.info("开始执行获取我的发布领养列表接口");
        //判断是否登录用户
        if (ObjectUtils.isEmpty(UserUtils.getUserDetails()) || StringUtils.isBlank(UserUtils.getUserDetails().getKeyId())) {
            return new ResponseEntity<>(AjaxResult.error("请登录用户"), HttpStatus.OK);
        }
        //分页查询
        List<StrayAnimalsAdoptionVO> strayAnimalsAdoptionVOS = adoptionService.selectMyPostAdoption(page, UserUtils.getUserDetails().getKeyId());
        return new ResponseEntity<>(AjaxResult.success(page.setRecords(strayAnimalsAdoptionVOS)), HttpStatus.OK);
    }

    @ApiOperation("获取我的领养列表")
    @GetMapping("myAdoptionList")
    public ResponseEntity<AjaxResult> myAdoptionList(Page<StrayAnimalsAdoption> page) {
        log.info("开始执行获取我的领养列表接口");
        //判断是否登录用户
        if (ObjectUtils.isEmpty(UserUtils.getUserDetails()) || StringUtils.isBlank(UserUtils.getUserDetails().getKeyId())) {
            return new ResponseEntity<>(AjaxResult.error("请登录用户"), HttpStatus.OK);
        }
        //分页查询
        List<StrayAnimalsAdoption> selectMyAdoptionList = adoptionService.selectMyAdoptionList(page, UserUtils.getUserDetails().getKeyId());
        return new ResponseEntity<>(AjaxResult.success(page.setRecords(selectMyAdoptionList)), HttpStatus.OK);
    }
}

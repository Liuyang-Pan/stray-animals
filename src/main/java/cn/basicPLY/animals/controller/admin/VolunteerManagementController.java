package cn.basicPLY.animals.controller.admin;

import cn.basicPLY.animals.entity.DTO.VolunteerManagementDTO;
import cn.basicPLY.animals.entity.StrayAnimalsVolunteer;
import cn.basicPLY.animals.enumerate.Constants;
import cn.basicPLY.animals.service.StrayAnimalsVolunteerService;
import cn.basicPLY.animals.utils.AjaxResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * purpose:志愿者管理相关接口
 *
 * @author Pan Liuyang
 * 2022/5/20 21:32
 */
@Api(tags = "志愿者管理相关接口")
@Slf4j
@RestController
@RequestMapping("/volunteer-management")
public class VolunteerManagementController {

    /**
     * 志愿者Service
     */
    @Autowired
    private StrayAnimalsVolunteerService volunteerService;

    /**
     * 查询志愿者列表接口
     *
     * @param volunteerManagement 用户实体
     * @return 修改成功与否结果
     */
    @ApiOperation("查询志愿者列表接口")
    @GetMapping("/list")
    public ResponseEntity<AjaxResult> list(VolunteerManagementDTO volunteerManagement) {
        Page<StrayAnimalsVolunteer> page = new Page<>(volunteerManagement.getCurrent(), volunteerManagement.getSize());
        LambdaQueryWrapper<StrayAnimalsVolunteer> volunteerQueryWrapper = new LambdaQueryWrapper<>();
        volunteerQueryWrapper
                .eq(StrayAnimalsVolunteer::getDeleteMark, Constants.NOT_DELETED)
                //模糊查询志愿者名称
                .like(StringUtils.isNotBlank(volunteerManagement.getVolunteerName()), StrayAnimalsVolunteer::getVolunteerName, volunteerManagement.getVolunteerName())
                //模糊查询志愿者城市
                .like(StringUtils.isNotBlank(volunteerManagement.getVolunteerCity()), StrayAnimalsVolunteer::getVolunteerCity, volunteerManagement.getVolunteerCity())
                //模糊查询志愿者期望从事工作
                .like(StringUtils.isNotBlank(volunteerManagement.getExpectedJob()), StrayAnimalsVolunteer::getExpectedJob, volunteerManagement.getExpectedJob());
        Page<StrayAnimalsVolunteer> strayAnimalsVolunteerPage = volunteerService.getBaseMapper().selectPage(page, volunteerQueryWrapper);
        return ResponseEntity.ok(AjaxResult.success(strayAnimalsVolunteerPage));
    }

    /**
     * 删除志愿者信息接口
     *
     * @param keyId 志愿者ID
     * @return 删除是否成功结果
     */
    @ApiOperation("删除志愿者信息接口")
    @DeleteMapping("/delete/{keyId}")
    public ResponseEntity<AjaxResult> delete(@PathVariable("keyId") String keyId) {
        StrayAnimalsVolunteer deleteVolunteer = new StrayAnimalsVolunteer();
        deleteVolunteer.setDeleteMark(Constants.DELETED);
        deleteVolunteer.setKeyId(keyId);
        int result = volunteerService.getBaseMapper().updateById(deleteVolunteer);
        if (result > 0) {
            return ResponseEntity.ok(AjaxResult.success("删除成功"));
        }
        return ResponseEntity.ok(AjaxResult.error("删除失败"));
    }

    /**
     * 同意志愿者申请信息接口
     *
     * @param keyId 志愿者ID
     * @return 删除是否成功结果
     */
    @ApiOperation("同意志愿者申请信息接口")
    @PutMapping("/apply/{keyId}")
    public ResponseEntity<AjaxResult> apply(@PathVariable("keyId") String keyId) {
        StrayAnimalsVolunteer applyVolunteer = new StrayAnimalsVolunteer();
        applyVolunteer.setCertificationMark(Constants.VOLUNTEER_CERTIFICATION_PASSED);
        applyVolunteer.setKeyId(keyId);
        int result = volunteerService.getBaseMapper().updateById(applyVolunteer);
        if (result > 0) {
            return ResponseEntity.ok(AjaxResult.success("认证成功"));
        }
        return ResponseEntity.ok(AjaxResult.error("认证失败"));
    }
}

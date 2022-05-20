package cn.basicPLY.animals.controller.admin;

import cn.basicPLY.animals.entity.DTO.VolunteerManagementDTO;
import cn.basicPLY.animals.entity.StrayAnimalsVolunteer;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 查询用户列表接口
     *
     * @param volunteerManagement 用户实体
     * @return 修改成功与否结果
     */
    @ApiOperation("查询用户列表接口")
    @GetMapping("/list")
    public ResponseEntity<AjaxResult> list(VolunteerManagementDTO volunteerManagement) {
        Page<StrayAnimalsVolunteer> page = new Page<>(volunteerManagement.getCurrent(), volunteerManagement.getSize());
        LambdaQueryWrapper<StrayAnimalsVolunteer> volunteerQueryWrapper = new LambdaQueryWrapper<>();
        volunteerQueryWrapper
                //模糊查询志愿者名称
                .like(StringUtils.isNotBlank(volunteerManagement.getVolunteerName()), StrayAnimalsVolunteer::getVolunteerName, volunteerManagement.getVolunteerName())
                //模糊查询志愿者城市
                .like(StringUtils.isNotBlank(volunteerManagement.getVolunteerCity()), StrayAnimalsVolunteer::getVolunteerCity, volunteerManagement.getVolunteerCity())
                //模糊查询志愿者期望从事工作
                .like(StringUtils.isNotBlank(volunteerManagement.getExpectedJob()), StrayAnimalsVolunteer::getExpectedJob, volunteerManagement.getExpectedJob());
        Page<StrayAnimalsVolunteer> strayAnimalsVolunteerPage = volunteerService.getBaseMapper().selectPage(page, volunteerQueryWrapper);
        return ResponseEntity.ok(AjaxResult.success(strayAnimalsVolunteerPage));
    }
}

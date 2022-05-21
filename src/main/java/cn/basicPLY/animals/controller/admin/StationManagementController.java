package cn.basicPLY.animals.controller.admin;

import cn.basicPLY.animals.entity.StrayAnimalsAidStation;
import cn.basicPLY.animals.enumerate.Constants;
import cn.basicPLY.animals.service.StrayAnimalsAidStationService;
import cn.basicPLY.animals.utils.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * purpose:救助基地管理相关接口
 *
 * @author Pan Liuyang
 * 2022/5/21 8:49
 */
@Api(tags = "救助基地管理相关接口")
@Slf4j
@RestController
@RequestMapping("/station-management")
public class StationManagementController {

    /**
     * 救助基地Service
     */
    @Autowired
    private StrayAnimalsAidStationService stationService;

    /**
     * 删除救助基地接口
     *
     * @param keyId 用户ID
     * @return 删除是否成功结果
     */
    @ApiOperation("删除救助基地接口")
    @DeleteMapping("/delete/{keyId}")
    public ResponseEntity<AjaxResult> delete(@PathVariable("keyId") String keyId) {
        StrayAnimalsAidStation deleteStation = new StrayAnimalsAidStation();
        deleteStation.setDeleteMark(Constants.DELETED);
        deleteStation.setKeyId(keyId);
        int result = stationService.getBaseMapper().updateById(deleteStation);
        if (result > 0) {
            return ResponseEntity.ok(AjaxResult.success("删除成功"));
        }
        return ResponseEntity.ok(AjaxResult.error("删除失败"));
    }

    /**
     * 同意救助基地申请信息接口
     *
     * @param keyId 志愿者ID
     * @return 删除是否成功结果
     */
    @ApiOperation("同意救助基地申请信息接口")
    @PutMapping("/apply/{keyId}")
    public ResponseEntity<AjaxResult> apply(@PathVariable("keyId") String keyId) {
        StrayAnimalsAidStation applyStation = new StrayAnimalsAidStation();
        applyStation.setCertificationStatus(Constants.VOLUNTEER_CERTIFICATION_PASSED);
        applyStation.setKeyId(keyId);
        int result = stationService.getBaseMapper().updateById(applyStation);
        if (result > 0) {
            return ResponseEntity.ok(AjaxResult.success("认证成功"));
        }
        return ResponseEntity.ok(AjaxResult.error("认证失败"));
    }
}

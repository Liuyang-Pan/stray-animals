package cn.basicPLY.animals.controller.admin;

import cn.basicPLY.animals.entity.DTO.UserManagementDTO;
import cn.basicPLY.animals.entity.StrayAnimalsUser;
import cn.basicPLY.animals.enumerate.Constants;
import cn.basicPLY.animals.service.StrayAnimalsUserService;
import cn.basicPLY.animals.utils.AjaxResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * purpose:用户管理相关接口
 *
 * @author Pan Liuyang
 * 2022/5/20 19:58
 */
@Api(tags = "用户管理相关接口")
@Slf4j
@RestController
@RequestMapping("/user-management")
public class UserManagementController {

    /**
     * 用户Service
     */
    @Autowired
    private StrayAnimalsUserService strayAnimalsUserService;

    /**
     * 查询用户列表接口
     *
     * @param userManagement 用户实体
     * @return 修改成功与否结果
     */
    @ApiOperation("查询用户列表接口")
    @GetMapping("/list")
    public ResponseEntity<AjaxResult> list(UserManagementDTO userManagement) {
        Page<StrayAnimalsUser> page = new Page<>(userManagement.getCurrent(), userManagement.getSize());
        LambdaQueryWrapper<StrayAnimalsUser> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper
                .eq(StrayAnimalsUser::getDeleteMark, Constants.NOT_DELETED)
                //模糊查询用户名
                .like(StringUtils.isNotBlank(userManagement.getUsername()), StrayAnimalsUser::getUsername, userManagement.getUsername())
                //模糊查询手机号
                .like(StringUtils.isNotBlank(userManagement.getPhoneNumber()), StrayAnimalsUser::getPhoneNumber, userManagement.getPhoneNumber())
                //模糊查询用户名
                .like(StringUtils.isNotBlank(userManagement.getNickName()), StrayAnimalsUser::getNickName, userManagement.getNickName());
        IPage<StrayAnimalsUser> strayAnimalsUserIPage = strayAnimalsUserService.getBaseMapper().selectPage(page, userQueryWrapper);
        return ResponseEntity.ok(AjaxResult.success(strayAnimalsUserIPage));
    }

    /**
     * 删除用户接口
     *
     * @param keyId 用户ID
     * @return 删除是否成功结果
     */
    @ApiOperation("删除用户接口")
    @DeleteMapping("/delete/{keyId}")
    public ResponseEntity<AjaxResult> delete(@PathVariable("keyId") String keyId) {
        StrayAnimalsUser deleteUser = new StrayAnimalsUser();
        deleteUser.setDeleteMark(Constants.DELETED);
        deleteUser.setKeyId(keyId);
        int result = strayAnimalsUserService.getBaseMapper().updateById(deleteUser);
        if (result > 0) {
            return ResponseEntity.ok(AjaxResult.success("删除成功"));
        }
        return ResponseEntity.ok(AjaxResult.error("删除失败"));
    }

    /**
     * 批量删除用户接口
     *
     * @param keyIdList 用户ID列表
     * @return 删除是否成功结果
     */
    @ApiOperation("批量删除用户接口")
    @DeleteMapping("/delete/list")
    public ResponseEntity<AjaxResult> delete(@RequestBody List<String> keyIdList) {
        AtomicInteger result = new AtomicInteger();
        StrayAnimalsUser deleteUser = new StrayAnimalsUser();
        deleteUser.setDeleteMark(Constants.DELETED);
        keyIdList.forEach(keyId -> {
            deleteUser.setKeyId(keyId);
            result.set(strayAnimalsUserService.getBaseMapper().updateById(deleteUser));
        });
        if (result.get() > 0) {
            return ResponseEntity.ok(AjaxResult.success("删除成功"));
        }
        return ResponseEntity.ok(AjaxResult.error("删除失败"));
    }

    /**
     * 用户禁用接口
     *
     * @param keyId 用户ID
     * @return 禁用是否成功结果
     */
    @ApiOperation("用户禁用接口")
    @PutMapping("/disabled/{keyId}")
    public ResponseEntity<AjaxResult> disabled(@PathVariable("keyId") String keyId) {
        StrayAnimalsUser disabledUser = new StrayAnimalsUser();
        disabledUser.setEnabled(Constants.DISABLED);
        disabledUser.setKeyId(keyId);
        int result = strayAnimalsUserService.getBaseMapper().updateById(disabledUser);
        if (result > 0) {
            return ResponseEntity.ok(AjaxResult.success("禁用成功"));
        }
        return ResponseEntity.ok(AjaxResult.error("禁用失败"));
    }
}

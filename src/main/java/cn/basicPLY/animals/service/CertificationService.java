package cn.basicPLY.animals.service;

import cn.basicPLY.animals.entity.StrayAnimalsUser;
import cn.basicPLY.animals.utils.AjaxResult;
import org.springframework.http.ResponseEntity;

/**
 * purpose:
 *
 * @author Pan Liuyang
 * 2022/4/23 10:26
 */
public interface CertificationService {

    /**
     * 检查注册信息是否有误
     *
     * @param strayAnimalsUser 用户信息
     * @return 检查结果
     */
    ResponseEntity<AjaxResult> checkParameters(StrayAnimalsUser strayAnimalsUser);

    /**
     * 注册用户
     *
     * @param strayAnimalsUser 用户信息
     * @return 返回新增数据成功与否结果
     */
    int registeredUser(StrayAnimalsUser strayAnimalsUser);

    /**
     * 修改用户信息
     *
     * @param strayAnimalsUser 用户信息
     * @return 返回修改数据成功与否结果
     */
    int modifyUser(StrayAnimalsUser strayAnimalsUser);
}

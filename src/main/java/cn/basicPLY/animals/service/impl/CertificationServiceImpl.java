package cn.basicPLY.animals.service.impl;

import cn.basicPLY.animals.entity.StrayAnimalsUser;
import cn.basicPLY.animals.entity.StrayAnimalsUserAuthority;
import cn.basicPLY.animals.enumerate.AuthorityEnum;
import cn.basicPLY.animals.enumerate.UserEnum;
import cn.basicPLY.animals.service.CertificationService;
import cn.basicPLY.animals.service.StrayAnimalsUserAuthorityService;
import cn.basicPLY.animals.service.StrayAnimalsUserService;
import cn.basicPLY.animals.utils.AjaxResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * purpose:
 *
 * @author Pan Liuyang
 * 2022/4/23 10:26
 */
@Service("CertificationServiceImpl")
public class CertificationServiceImpl implements CertificationService {

    /**
     * 流浪动物救助网用户Service
     */
    @Autowired
    private StrayAnimalsUserService userService;

    @Autowired
    private StrayAnimalsUserAuthorityService userAuthorityService;

    /**
     * 检查注册信息是否有误
     *
     * @param strayAnimalsUser 用户信息
     * @return 检查结果
     */
    @Override
    public ResponseEntity<AjaxResult> checkParameters(StrayAnimalsUser strayAnimalsUser) {
        //判断入参是否为空
        if (StringUtils.isBlank(strayAnimalsUser.getUsername())) {
            return new ResponseEntity<>(AjaxResult.error("用户名为空"), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(strayAnimalsUser.getPassword())) {
            return new ResponseEntity<>(AjaxResult.error("密码为空"), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(strayAnimalsUser.getPhoneNumber())) {
            return new ResponseEntity<>(AjaxResult.error("手机号为空"), HttpStatus.BAD_REQUEST);
        }

        //判断是否唯一
        QueryWrapper<StrayAnimalsUser> checkQueryWrapper = new QueryWrapper<>();

        checkQueryWrapper.eq("username", strayAnimalsUser.getUsername());
        if (ObjectUtils.isNotEmpty(userService.getBaseMapper().selectList(checkQueryWrapper))) {
            return new ResponseEntity<>(AjaxResult.error("用户名已存在"), HttpStatus.BAD_REQUEST);
        }
        checkQueryWrapper.clear();

        checkQueryWrapper.eq("phone_number", strayAnimalsUser.getPhoneNumber());
        if (ObjectUtils.isNotEmpty(userService.getBaseMapper().selectList(checkQueryWrapper))) {
            return new ResponseEntity<>(AjaxResult.error("手机号已存在"), HttpStatus.BAD_REQUEST);
        }
        checkQueryWrapper.clear();

        checkQueryWrapper.eq("email_address", strayAnimalsUser.getEmailAddress());
        if (ObjectUtils.isNotEmpty(userService.getBaseMapper().selectList(checkQueryWrapper))) {
            return new ResponseEntity<>(AjaxResult.error("邮箱地址已存在"), HttpStatus.BAD_REQUEST);
        }
        checkQueryWrapper.clear();
        return null;
    }

    /**
     * 注册用户
     *
     * @param strayAnimalsUser 用户信息
     * @return 返回新增数据成功与否结果
     */
    @Override
    public int registeredUser(StrayAnimalsUser strayAnimalsUser) {
        //创建Spring Security加密对象
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //通过Spring Security强Hash算法对密码进行加密
        strayAnimalsUser.setPassword(encoder.encode(strayAnimalsUser.getPassword()));
        //设置创建人
        strayAnimalsUser.setCreateBy(UserEnum.USER_ADMIN.getName());
        //设置创建时间
        strayAnimalsUser.setCreateDate(new Date());

        int result = 0;
        //返回写入成功信息
        result += userService.getBaseMapper().insert(strayAnimalsUser);
        //注册默认写入普通用户角色
        StrayAnimalsUserAuthority strayAnimalsUserAuthority = new StrayAnimalsUserAuthority();
        strayAnimalsUserAuthority.setAuthority(AuthorityEnum.ROLE_USER.getAuthority());
        strayAnimalsUserAuthority.setAuthorityZh(AuthorityEnum.ROLE_USER.getAuthorityZh());
        //暂时写死
        strayAnimalsUserAuthority.setAuthorityId("e4e205db-8e1a-f27e-1875-4f3ca303ddee");
        strayAnimalsUserAuthority.setUserKeyId(strayAnimalsUser.getKeyId());
        //写入创建人和时间
        strayAnimalsUserAuthority.setCreateBy(UserEnum.USER_ADMIN.getName());
        strayAnimalsUserAuthority.setCreateDate(new Date());
        result += userAuthorityService.getBaseMapper().insert(strayAnimalsUserAuthority);
        return result;
    }

    /**
     * 修改用户信息
     *
     * @param strayAnimalsUser 用户信息
     * @return 返回修改数据成功与否结果
     */
    @Override
    public int modifyUser(StrayAnimalsUser strayAnimalsUser) {
        //创建Spring Security加密对象
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //通过Spring Security强Hash算法对密码进行加密
        strayAnimalsUser.setPassword(encoder.encode(strayAnimalsUser.getPassword()));
        //设置更新人
        strayAnimalsUser.setUpdateBy(UserEnum.USER_ADMIN.getName());
        //设置更新时间
        strayAnimalsUser.setUpdateDate(new Date());
        return userService.getBaseMapper().updateById(strayAnimalsUser);
    }
}

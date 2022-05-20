package cn.basicPLY.animals.service.impl;

import cn.basicPLY.animals.entity.VO.CertificationUserDetails;
import cn.basicPLY.animals.entity.StrayAnimalsUser;
import cn.basicPLY.animals.entity.StrayAnimalsUserAuthority;
import cn.basicPLY.animals.enumerate.Constants;
import cn.basicPLY.animals.enumerate.UniversalColumnEnum;
import cn.basicPLY.animals.mapper.StrayAnimalsUserAuthorityMapper;
import cn.basicPLY.animals.mapper.StrayAnimalsUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * purpose:实现Spring Security的UserDetailsService实现自定义数据登录验证
 *
 * @author Pan Liuyang
 * 2022/4/22 20:54
 */
@Service("StrayAnimalsUserDetailsServiceImpl")
public class StrayAnimalsUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private StrayAnimalsUserMapper strayAnimalsUserMapper;

    @Autowired
    private StrayAnimalsUserAuthorityMapper strayAnimalsUserAuthorityMapper;

    /**
     * 通过用户名加载用户信息
     *
     * @param username 用户名
     * @return 用户相关信息
     * @throws UsernameNotFoundException 用户未找到相关异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //创建查询条件
        QueryWrapper<StrayAnimalsUser> strayAnimalsUserQueryWrapper = new QueryWrapper<>();
        //设置用户名获取
        strayAnimalsUserQueryWrapper.eq("username", username).eq(UniversalColumnEnum.DELETE_MARK.getColumn(), Constants.NOT_DELETED);
        //获取用户信息
        StrayAnimalsUser strayAnimalsUser = strayAnimalsUserMapper.selectOne(strayAnimalsUserQueryWrapper);
        //判断是否存在用户
        if (ObjectUtils.isEmpty(strayAnimalsUser)) {
            throw new UsernameNotFoundException("用户不存在 请注册");
        }
        if (null != strayAnimalsUser.getEnabled() && Constants.DISABLED == strayAnimalsUser.getEnabled()) {
            throw new UsernameNotFoundException("用户已禁用 请联系管理员");
        }
        //转换为认证用户信息
        CertificationUserDetails certificationUserDetails = new CertificationUserDetails();
        BeanUtils.copyProperties(strayAnimalsUser, certificationUserDetails);
        //创建查询条件
        QueryWrapper<StrayAnimalsUserAuthority> userAuthorityQueryWrapper = new QueryWrapper<>();
        //设置通过用户ID获取角色权限
        userAuthorityQueryWrapper.eq("user_key_id", strayAnimalsUser.getKeyId()).eq(UniversalColumnEnum.DELETE_MARK.getColumn(), 1);
        //注入权限参数
        certificationUserDetails.setUserAuthorities(strayAnimalsUserAuthorityMapper.selectList(userAuthorityQueryWrapper));
        //非空返回用户实体
        return certificationUserDetails;
    }
}

package cn.basicPLY.animals.entity;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * purpose:
 *
 * @author Pan Liuyang
 * 2022/4/22 23:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CertificationUserDetails extends StrayAnimalsUser implements UserDetails {
    private static final long serialVersionUID = -8285954639099850158L;

    /**
     * 用户角色权限
     */
    List<StrayAnimalsUserAuthority> userAuthorities;

    //以下实现方法为UserDetails的实现
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        //判断权限登录获取到的权限是否为空
        if (ObjectUtils.isNotEmpty(userAuthorities)) {
            userAuthorities.forEach(userAuthority -> {
                authorities.add(new SimpleGrantedAuthority(userAuthority.getAuthority()));
            });
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return super.getEnabled() == 1;
    }
}

package cn.basicPLY.animals.utils;

import cn.basicPLY.animals.entity.CertificationUserDetails;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * purpose:
 *
 * @author Pan Liuyang
 * 2022/4/24 21:49
 */
public class UserUtils {

    /**
     * 获取用户信息
     *
     * @return CertificationUserDetails
     */
    public static CertificationUserDetails getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return (CertificationUserDetails) authentication.getPrincipal();
        }
        return null;
    }
}

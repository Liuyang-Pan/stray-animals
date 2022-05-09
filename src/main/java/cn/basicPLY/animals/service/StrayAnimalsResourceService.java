package cn.basicPLY.animals.service;

import cn.basicPLY.animals.entity.StrayAnimalsResource;
import cn.basicPLY.animals.entity.VO.StrayAnimalsResourceVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【stray_animals_resource(流浪动物救助网：资源需求相关信息表)】的数据库操作Service
 * @createDate 2022-05-02 14:54:42
 */
public interface StrayAnimalsResourceService extends IService<StrayAnimalsResource> {

    /**
     * 分页获取需求/供应信息
     *
     * @param page     分页参数
     * @param isItMine 是否查询我的发布信息
     * @param keyId    当前登录用户ID
     * @return 需求/供应信息列表
     */
    List<StrayAnimalsResourceVO> selectResourcePage(Page<StrayAnimalsResourceVO> page, String resourceType, String isItMine, String keyId);

    /**
     * 根据KeyId查询资源信息详情
     *
     * @param keyId KeyId
     * @return 资源信息详情
     */
    StrayAnimalsResourceVO selectResourcesByKeyId(String keyId);
}

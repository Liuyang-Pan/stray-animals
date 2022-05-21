package cn.basicPLY.animals.service.impl;

import cn.basicPLY.animals.entity.VO.StrayAnimalsResourceVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.basicPLY.animals.entity.StrayAnimalsResource;
import cn.basicPLY.animals.service.StrayAnimalsResourceService;
import cn.basicPLY.animals.mapper.StrayAnimalsResourceMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【stray_animals_resource(流浪动物救助网：资源需求相关信息表)】的数据库操作Service实现
 * @createDate 2022-05-02 14:54:42
 */
@Service
public class StrayAnimalsResourceServiceImpl extends ServiceImpl<StrayAnimalsResourceMapper, StrayAnimalsResource>
        implements StrayAnimalsResourceService {

    /**
     * 分页获取需求/供应信息
     *
     * @param page             分页参数
     * @param resourceType     资源类型
     * @param demandSupplyType 需求供应类型(资金/物资/场地/其他)
     * @param resourceTitle    资源标题
     * @param resourceAddress  资源地址
     * @param isItMine         是否查询我的发布信息
     * @param keyId            当前登录用户ID
     * @return 需求/供应信息列表
     */
    @Override
    public List<StrayAnimalsResourceVO> selectResourcePage(Page<StrayAnimalsResourceVO> page, String resourceType, String demandSupplyType, String resourceTitle, String resourceAddress, String isItMine, String keyId) {
        return baseMapper.selectResourcePage(page, resourceType, demandSupplyType, resourceTitle, resourceAddress, isItMine, keyId);
    }

    /**
     * 根据KeyId查询资源信息详情
     *
     * @param keyId KeyId
     * @return 资源信息详情
     */
    @Override
    public StrayAnimalsResourceVO selectResourcesByKeyId(String keyId) {
        return baseMapper.selectResourcesByKeyId(keyId);
    }
}





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
     * @param page 分页参数
     * @return 需求/供应信息列表
     */
    @Override
    public List<StrayAnimalsResourceVO> selectResourcePage(Page<StrayAnimalsResourceVO> page, String resourceType) {
        return baseMapper.selectResourcePage(page, resourceType);
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





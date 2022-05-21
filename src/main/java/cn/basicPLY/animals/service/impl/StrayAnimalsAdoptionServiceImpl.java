package cn.basicPLY.animals.service.impl;

import cn.basicPLY.animals.entity.StrayAnimalsAdopter;
import cn.basicPLY.animals.entity.VO.StrayAnimalsAdopterVO;
import cn.basicPLY.animals.entity.VO.StrayAnimalsAdoptionVO;
import cn.basicPLY.animals.service.StrayAnimalsAdopterService;
import cn.basicPLY.animals.utils.UserUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.basicPLY.animals.entity.StrayAnimalsAdoption;
import cn.basicPLY.animals.service.StrayAnimalsAdoptionService;
import cn.basicPLY.animals.mapper.StrayAnimalsAdoptionMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【stray_animals_adoption(流浪动物救助网：领养信息表)】的数据库操作Service实现
 * @createDate 2022-04-28 22:09:52
 */
@Service
public class StrayAnimalsAdoptionServiceImpl extends ServiceImpl<StrayAnimalsAdoptionMapper, StrayAnimalsAdoption>
        implements StrayAnimalsAdoptionService {

    @Autowired
    private StrayAnimalsAdopterService adopterService;

    /**
     * 分页查询领养信息
     *
     * @param page            分页参数
     * @param animalType      模糊查询动物类型
     * @param animalBreed     模糊查询动物品种
     * @param adoptionAddress 模糊查询标题
     * @return 返回数据列表
     */
    @Override
    public List<StrayAnimalsAdoptionVO> selectStrayAnimalsAdoptionPageVO(Page<StrayAnimalsAdoptionVO> page, String animalType, String animalBreed, String adoptionAddress) {
        return baseMapper.selectStrayAnimalsAdoptionPageVO(page, animalType, animalBreed, adoptionAddress);
    }

    /**
     * 根据领养KeyId查询领养详情
     *
     * @param keyId keyId
     * @return 领养详情
     */
    @Override
    public StrayAnimalsAdoptionVO selectStrayAnimalsAdoptionInfoByKeyId(String keyId) {
        StrayAnimalsAdoptionVO strayAnimalsAdoptionVO = baseMapper.selectStrayAnimalsAdoptionInfoByKeyId(keyId);
        boolean isThePublisher = true;
        //当前登录用户于发布领养信息用户相等时 获取领养人相关信息
        if ((ObjectUtils.isNotEmpty(UserUtils.getUserDetails())
                && StringUtils.isNotBlank(UserUtils.getUserDetails().getKeyId()))
                && UserUtils.getUserDetails().getKeyId().equals(strayAnimalsAdoptionVO.getForeignKeyPublisher())) {
            strayAnimalsAdoptionVO.setStrayAnimalsAdopterVOS(baseMapper.selectStrayAnimalsAdopterList(keyId));
            isThePublisher = false;
        }
        //是否显示电话信息等状态
        boolean whetherToDisplayInformation = true;
        if (ObjectUtils.isNotEmpty(UserUtils.getUserDetails())
                && StringUtils.isNotBlank(UserUtils.getUserDetails().getKeyId())) {
            QueryWrapper<StrayAnimalsAdopter> adopterQueryWrapper = new QueryWrapper<>();
            adopterQueryWrapper.eq("delete_mark", 1) //是否删除标识
                    .eq("adoption_id", keyId) //领养信息关联ID
                    .eq("adopter_id", UserUtils.getUserDetails().getKeyId()) //领养人用户ID
                    .eq("adopter_state", 2); //可查看电话状态
            if (adopterService.getBaseMapper().selectCount(adopterQueryWrapper) > 0) {
                whetherToDisplayInformation = false;
            }
        }
        if (isThePublisher && whetherToDisplayInformation) {
            strayAnimalsAdoptionVO.setAdoptionPhoneNumber(null);
        }
        return strayAnimalsAdoptionVO;
    }

    @Override
    public List<StrayAnimalsAdoptionVO> selectMyPostAdoption(IPage<StrayAnimalsAdoptionVO> page, String userId) {
        return baseMapper.selectMyPostAdoption(page, userId);
    }

    @Override
    public List<StrayAnimalsAdoption> selectMyAdoptionList(Page<StrayAnimalsAdoption> page, String userId) {
        return baseMapper.selectMyAdoptionList(page, userId);
    }
}





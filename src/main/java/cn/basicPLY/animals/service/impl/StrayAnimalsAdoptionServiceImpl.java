package cn.basicPLY.animals.service.impl;

import cn.basicPLY.animals.entity.VO.StrayAnimalsAdoptionVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.basicPLY.animals.entity.StrayAnimalsAdoption;
import cn.basicPLY.animals.service.StrayAnimalsAdoptionService;
import cn.basicPLY.animals.mapper.StrayAnimalsAdoptionMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【stray_animals_adoption(流浪动物救助网：领养信息表)】的数据库操作Service实现
* @createDate 2022-04-24 21:36:24
*/
@Service
public class StrayAnimalsAdoptionServiceImpl extends ServiceImpl<StrayAnimalsAdoptionMapper, StrayAnimalsAdoption>
    implements StrayAnimalsAdoptionService{

    @Override
    public List<StrayAnimalsAdoptionVO> selectStrayAnimalsAdoptionPageVO(IPage<StrayAnimalsAdoptionVO> page, String userId, String adoptionTitle) {
        return baseMapper.selectStrayAnimalsAdoptionPageVO(page, userId, adoptionTitle);
    }
}





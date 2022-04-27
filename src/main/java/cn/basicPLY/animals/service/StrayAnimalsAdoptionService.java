package cn.basicPLY.animals.service;

import cn.basicPLY.animals.entity.StrayAnimalsAdoption;
import cn.basicPLY.animals.entity.VO.StrayAnimalsAdoptionVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【stray_animals_adoption(流浪动物救助网：领养信息表)】的数据库操作Service
* @createDate 2022-04-24 21:36:24
*/
public interface StrayAnimalsAdoptionService extends IService<StrayAnimalsAdoption> {

    List<StrayAnimalsAdoptionVO> selectStrayAnimalsAdoptionPageVO(IPage<StrayAnimalsAdoptionVO> page,
                                                                   String userId,
                                                                   String adoptionTitle);

}

package cn.basicPLY.animals.mapper;

import cn.basicPLY.animals.entity.StrayAnimalsAdoption;
import cn.basicPLY.animals.entity.VO.StrayAnimalsAdoptionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【stray_animals_adoption(流浪动物救助网：领养信息表)】的数据库操作Mapper
 * @createDate 2022-04-24 21:36:24
 * @Entity cn.basicPLY.animals.entity.StrayAnimalsAdoption
 */
public interface StrayAnimalsAdoptionMapper extends BaseMapper<StrayAnimalsAdoption> {

    /**
     * 分页查询领养信息
     *
     * @param page          分页参数
     * @param userId        用户ID
     * @param adoptionTitle 模糊查询标题
     * @return 返回数据列表
     */
    List<StrayAnimalsAdoptionVO> selectStrayAnimalsAdoptionPageVO(IPage<StrayAnimalsAdoptionVO> page,
                                                                   String userId,
                                                                   String adoptionTitle);
}




